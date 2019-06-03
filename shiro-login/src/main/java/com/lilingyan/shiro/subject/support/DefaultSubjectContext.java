package com.lilingyan.shiro.subject.support;

import com.lilingyan.shiro.SecurityUtils;
import com.lilingyan.shiro.UnavailableSecurityManagerException;
import com.lilingyan.shiro.authc.AuthenticationInfo;
import com.lilingyan.shiro.authc.AuthenticationToken;
import com.lilingyan.shiro.mgt.SecurityManager;
import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.subject.PrincipalCollection;
import com.lilingyan.shiro.subject.Subject;
import com.lilingyan.shiro.subject.SubjectContext;
import com.lilingyan.shiro.util.MapContext;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 14:02
 */
@Slf4j
public class DefaultSubjectContext extends MapContext implements SubjectContext {

    public static final String PRINCIPALS_SESSION_KEY = DefaultSubjectContext.class.getName() + "_PRINCIPALS_SESSION_KEY";
    private static final String SECURITY_MANAGER = DefaultSubjectContext.class.getName() + ".SECURITY_MANAGER";
    public static final String AUTHENTICATED_SESSION_KEY = DefaultSubjectContext.class.getName() + "_AUTHENTICATED_SESSION_KEY";
    private static final String AUTHENTICATED = DefaultSubjectContext.class.getName() + ".AUTHENTICATED";
    private static final String AUTHENTICATION_TOKEN = DefaultSubjectContext.class.getName() + ".AUTHENTICATION_TOKEN";
    private static final String AUTHENTICATION_INFO = DefaultSubjectContext.class.getName() + ".AUTHENTICATION_INFO";
    private static final String SUBJECT = DefaultSubjectContext.class.getName() + ".SUBJECT";
    private static final String SESSION_ID = DefaultSubjectContext.class.getName() + ".SESSION_ID";
    private static final String SESSION = DefaultSubjectContext.class.getName() + ".SESSION";
    private static final String PRINCIPALS = DefaultSubjectContext.class.getName() + ".PRINCIPALS";
    public static final String SESSION_CREATION_ENABLED = DefaultSubjectContext.class.getName() + ".SESSION_CREATION_ENABLED";

    public DefaultSubjectContext() {
        super();
    }

    public DefaultSubjectContext(SubjectContext ctx) {
        super(ctx);
    }

    @Override
    public SecurityManager resolveSecurityManager() {
        SecurityManager securityManager = getSecurityManager();
        if (securityManager == null) {
            if (log.isDebugEnabled()) {
                log.debug("No SecurityManager available in subject context map.  " +
                        "Falling back to SecurityUtils.getSecurityManager() lookup.");
            }
            try {
                securityManager = SecurityUtils.getSecurityManager();
            } catch (UnavailableSecurityManagerException e) {
                if (log.isDebugEnabled()) {
                    log.debug("No SecurityManager available via SecurityUtils.  Heuristics exhausted."+e);
                }
            }
        }
        return securityManager;
    }

    @Override
    public PrincipalCollection resolvePrincipals() {
        PrincipalCollection principals = getPrincipals();

        /**
         * 从realm认证后的凭证对象中
         * 获取principals
         */
        if (isEmpty(principals)) {
            AuthenticationInfo info = getAuthenticationInfo();
            if (info != null) {
                principals = info.getPrincipals();
            }
        }

        if (isEmpty(principals)) {
            Subject subject = getSubject();
            if (subject != null) {
                principals = subject.getPrincipals();
            }
        }

        if (isEmpty(principals)) {
            //try the session:
            Session session = resolveSession();
            if (session != null) {
                principals = (PrincipalCollection) session.getAttribute(PRINCIPALS_SESSION_KEY);
            }
        }

        return principals;
    }

    @Override
    public Session resolveSession() {
        Session session = getSession();
        if (session == null) {
            /**
             * 如果已经绑定过了上次的subject
             */
            Subject existingSubject = getSubject();
            if (existingSubject != null) {
                session = existingSubject.getSession(false);
            }
        }
        return session;
    }

    @Override
    public boolean isSessionCreationEnabled() {
        Boolean val = getTypedValue(SESSION_CREATION_ENABLED, Boolean.class);
        return val == null || val;
    }


    public SecurityManager getSecurityManager() {
        return getTypedValue(SECURITY_MANAGER, SecurityManager.class);
    }

    /**
     * 获取当前subject上下文绑定的Session
     * @return
     */
    public Session getSession() {
        return getTypedValue(SESSION, Session.class);
    }

    public PrincipalCollection getPrincipals() {
        return getTypedValue(PRINCIPALS, PrincipalCollection.class);
    }

    public AuthenticationInfo getAuthenticationInfo() {
        return getTypedValue(AUTHENTICATION_INFO, AuthenticationInfo.class);
    }

    public Subject getSubject() {
        return getTypedValue(SUBJECT, Subject.class);
    }

    @Override
    public boolean resolveAuthenticated() {
        return false;
    }

    @Override
    public void setSecurityManager(SecurityManager securityManager) {
        nullSafePut(SECURITY_MANAGER, securityManager);
    }

    @Override
    public void setAuthenticated(boolean authc) {
        put(AUTHENTICATED, authc);
    }

    @Override
    public void setAuthenticationToken(AuthenticationToken token) {
        nullSafePut(AUTHENTICATION_TOKEN, token);
    }

    @Override
    public void setAuthenticationInfo(AuthenticationInfo info) {
        nullSafePut(AUTHENTICATION_INFO, info);
    }

    @Override
    public void setSubject(Subject subject) {
        nullSafePut(SUBJECT, subject);
    }

    public void setSession(Session session) {
        nullSafePut(SESSION, session);
    }

    @Override
    public void setSessionId(Serializable sessionId) {
        nullSafePut(SESSION_ID, sessionId);
    }

    @Override
    public void setPrincipals(PrincipalCollection principals) {
        if (!isEmpty(principals)) {
            put(PRINCIPALS, principals);
        }
    }

    @Override
    public void setSessionCreationEnabled(boolean enabled) {
        nullSafePut(SESSION_CREATION_ENABLED, enabled);
    }

    @Override
    public Serializable getSessionId() {
        return getTypedValue(SESSION_ID, Serializable.class);
    }

    private static boolean isEmpty(PrincipalCollection pc) {
        return pc == null || pc.isEmpty();
    }
}
