package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.authc.AuthenticationException;
import com.lilingyan.shiro.authc.AuthenticationInfo;
import com.lilingyan.shiro.authc.AuthenticationToken;
import com.lilingyan.shiro.session.InvalidSessionException;
import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.session.mgt.DefaultSessionKey;
import com.lilingyan.shiro.session.mgt.SessionKey;
import com.lilingyan.shiro.subject.PrincipalCollection;
import com.lilingyan.shiro.subject.Subject;
import com.lilingyan.shiro.subject.SubjectContext;
import com.lilingyan.shiro.subject.support.DefaultSubjectContext;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:46
 */
@Slf4j
public class DefaultSecurityManager extends SessionsSecurityManager{

    protected SubjectDAO subjectDAO;
    protected SubjectFactory subjectFactory;

    public DefaultSecurityManager() {
        super();
        this.subjectFactory = new DefaultSubjectFactory();
        this.subjectDAO = new DefaultSubjectDAO();
    }

    public Subject login(Subject subject, AuthenticationToken token) throws AuthenticationException {
        AuthenticationInfo info;
        try {
            /**
             * 取获校验token的信息
             * 如果有返回就认为登陆成功
             * 抛出任何AuthenticationException子类错误 就认为登陆失败
             */
            info = authenticate(token);
        } catch (AuthenticationException ae) {
            try {
//                onFailedLogin(token, ae, subject);
            } catch (Exception e) {
                if (log.isInfoEnabled()) {
                    log.info("onFailedLogin method threw an " +
                            "exception.  Logging and propagating original AuthenticationException.", e);
                }
            }
            throw ae; //propagate
        }

        /**
         * 走到这里
         * 证明已经登陆成功
         *
         * 下一步就是创建Subject
         */
        Subject loggedIn = createSubject(token, info, subject);

//        onSuccessfulLogin(token, info, loggedIn);

        return loggedIn;
    }

    /**
     * 登陆成功后创建Subject
     * 如果已经有subject
     * 则把现在的认证信息与原来的Subject绑定
     * 如果没有Subject  则创建一个 并执行绑定操作
     * @param token
     * @param info
     * @param existing
     * @return
     */
    protected Subject createSubject(AuthenticationToken token, AuthenticationInfo info, Subject existing) {
        SubjectContext context = createSubjectContext();
        context.setAuthenticated(true);             //增加了登陆成功的标志
        /**
         * 同时保存了登陆的token和realm认证后返回的信息
         */
        context.setAuthenticationToken(token);      //token
        context.setAuthenticationInfo(info);
        if (existing != null) {
            context.setSubject(existing);
        }
        return createSubject(context);
    }

    public Subject createSubject(SubjectContext subjectContext) {
        //复制了一遍subjectContext
        SubjectContext context = copy(subjectContext);
        //确保存在SecurityManager
        context = ensureSecurityManager(context);
        //解析Session
        context = resolveSession(context);
        //解析登陆信息(用于RememberMe)
//        context = resolvePrincipals(context);
        Subject subject = doCreateSubject(context);
        /**
         * 保存subject
         * web情况下 会保存在session中
         */
        save(subject);
        return subject;
    }

    protected Subject doCreateSubject(SubjectContext context) {
        //使用Subject工厂统一创建Subject
        return getSubjectFactory().createSubject(context);
    }

    protected SubjectContext resolvePrincipals(SubjectContext context) {
        return context;
    }

    protected SubjectContext resolveSession(SubjectContext context) {
        if (context.resolveSession() != null) {
            log.debug("Context already contains a session.  Returning.");
            return context;
        }
        try {
            Session session = resolveContextSession(context);
            if (session != null) {
                context.setSession(session);
            }
        } catch (InvalidSessionException e) {
            log.debug("Resolved SubjectContext context session is invalid.  Ignoring and creating an anonymous " +
                    "(session-less) Subject instance.", e);
        }
        return context;
    }

    protected Session resolveContextSession(SubjectContext context) throws InvalidSessionException {
        SessionKey key = getSessionKey(context);
        if (key != null) {
            return getSession(key);
        }
        return null;
    }

    protected SessionKey getSessionKey(SubjectContext context) {
        //获取当前subject中的SessionId
        Serializable sessionId = context.getSessionId();
        if (sessionId != null) {
            return new DefaultSessionKey(sessionId);
        }
        return null;
    }

    protected SubjectContext createSubjectContext() {
        return new DefaultSubjectContext();
    }

    public void logout(Subject subject) {
    }

    protected SubjectContext ensureSecurityManager(SubjectContext context) {
        if (context.resolveSecurityManager() != null) {
            log.trace("Context already contains a SecurityManager instance.  Returning.");
            return context;
        }
        log.trace("No SecurityManager found in context.  Adding self reference.");
        context.setSecurityManager(this);
        return context;
    }

    protected SubjectContext copy(SubjectContext subjectContext) {
        return new DefaultSubjectContext(subjectContext);
    }

    public SubjectFactory getSubjectFactory() {
        return subjectFactory;
    }

    private static boolean isEmpty(PrincipalCollection pc) {
        return pc == null || pc.isEmpty();
    }

    /**
     * 具体保存逻辑
     * 由subjectDAO的实现类完成
     * @param subject
     */
    protected void save(Subject subject) {
        this.subjectDAO.save(subject);
    }

}
