package com.lilingyan.shiro.subject.support;

import com.lilingyan.shiro.authc.AuthenticationException;
import com.lilingyan.shiro.authc.AuthenticationToken;
import com.lilingyan.shiro.mgt.SecurityManager;
import com.lilingyan.shiro.session.InvalidSessionException;
import com.lilingyan.shiro.session.ProxiedSession;
import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.session.mgt.DefaultSessionContext;
import com.lilingyan.shiro.session.mgt.SessionContext;
import com.lilingyan.shiro.subject.PrincipalCollection;
import com.lilingyan.shiro.subject.Subject;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:50
 */
@Slf4j
public class DelegatingSubject implements Subject {

    protected PrincipalCollection principals;
    protected boolean authenticated;
    protected Session session;
    protected transient SecurityManager securityManager;
    protected boolean sessionCreationEnabled;

    public DelegatingSubject(PrincipalCollection principals, boolean authenticated,
                             Session session, boolean sessionCreationEnabled, SecurityManager securityManager) {
        if (securityManager == null) {
            throw new IllegalArgumentException("SecurityManager argument cannot be null.");
        }
        this.securityManager = securityManager;
        this.principals = principals;
        this.authenticated = authenticated;
        if (session != null) {
            //包装一下session
            this.session = decorate(session);
        }
        this.sessionCreationEnabled = sessionCreationEnabled;
    }

    protected Session decorate(Session session) {
        if (session == null) {
            throw new IllegalArgumentException("session cannot be null");
        }
        return new StoppingAwareProxiedSession(session, this);
    }

    public void login(AuthenticationToken token) throws AuthenticationException {
        //让securityManager取执行登陆(附带了subjet(一般还没登陆)和登陆信息)
        Subject subject = securityManager.login(this, token);

        PrincipalCollection principals;

        if (subject instanceof DelegatingSubject) {
            DelegatingSubject delegating = (DelegatingSubject) subject;
            //we have to do this in case there are assumed identities - we don't want to lose the 'real' principals:
            principals = delegating.principals;
        } else {
            principals = subject.getPrincipals();
        }

        if (principals == null || principals.isEmpty()) {
            String msg = "Principals returned from securityManager.login( token ) returned a null or " +
                    "empty value.  This value must be non null and populated with one or more elements.";
            throw new IllegalStateException(msg);
        }
        this.principals = principals;
        this.authenticated = true;
        Session session = subject.getSession(false);
        if (session != null) {
            this.session = decorate(session);
        } else {
            this.session = null;
        }
    }

    @Override
    public Session getSession() {
        return getSession(true);
    }

    /**
     * 如果没有session
     * 则创建
     * @param create
     * @return
     */
    @Override
    public Session getSession(boolean create) {
        if (log.isTraceEnabled()) {
            log.trace("attempting to get session; create = " + create +
                    "; session is null = " + (this.session == null) +
                    "; session has id = " + (this.session != null && session.getId() != null));
        }

        if (this.session == null && create) {

            //added in 1.2:
            if (!isSessionCreationEnabled()) {      //没开启sessionCreationEnabled  会报错
                String msg = "Session creation has been disabled for the current subject.  This exception indicates " +
                        "that there is either a programming error (using a session when it should never be " +
                        "used) or that Shiro's configuration needs to be adjusted to allow Sessions to be created " +
                        "for the current Subject.  See the " + DisabledSessionException.class.getName() + " JavaDoc " +
                        "for more.";
                throw new DisabledSessionException(msg);
            }

            /**
             * 创建的是DefaultSessionContext
             * 默认没什么内容
             */
            SessionContext sessionContext = createSessionContext();
            /**
             * 最终交由securityManager来生成Session
             */
            Session session = this.securityManager.start(sessionContext);
            /**
             * 最终让用户操作的多次包装的Session
             */
            this.session = decorate(session);
        }
        return this.session;
    }

    protected SessionContext createSessionContext() {
        SessionContext sessionContext = new DefaultSessionContext();
        return sessionContext;
    }

    @Override
    public PrincipalCollection getPrincipals() {
        return this.principals;
    }

    protected boolean isSessionCreationEnabled() {
        return this.sessionCreationEnabled;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    /**
     * 再一次代理了Session
     * 这里关联了subject
     * 加了 在失效Session同时，把subject中的Session置空 的行为
     */
    private class StoppingAwareProxiedSession extends ProxiedSession {

        private final DelegatingSubject owner;

        private StoppingAwareProxiedSession(Session target, DelegatingSubject owningSubject) {
            super(target);
            owner = owningSubject;
        }

        public void stop() throws InvalidSessionException {
//            super.stop();
            owner.sessionStopped();
        }

    }

    private void sessionStopped() {
        this.session = null;
    }

}
