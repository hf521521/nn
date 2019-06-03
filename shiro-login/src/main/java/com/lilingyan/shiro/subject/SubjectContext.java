package com.lilingyan.shiro.subject;

import com.lilingyan.shiro.authc.AuthenticationInfo;
import com.lilingyan.shiro.authc.AuthenticationToken;
import com.lilingyan.shiro.mgt.SecurityManager;
import com.lilingyan.shiro.session.Session;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:28
 */
public interface SubjectContext extends Map<String, Object> {

    SecurityManager resolveSecurityManager();
    Session resolveSession();
    boolean isSessionCreationEnabled();
    PrincipalCollection resolvePrincipals();
    boolean resolveAuthenticated();

    void setSecurityManager(SecurityManager securityManager);
    void setAuthenticated(boolean authc);
    void setAuthenticationToken(AuthenticationToken token);
    void setAuthenticationInfo(AuthenticationInfo info);
    void setSubject(Subject subject);
    void setSession(Session session);
    void setSessionId(Serializable sessionId);
    void setPrincipals(PrincipalCollection principals);
    void setSessionCreationEnabled(boolean enabled);

    Serializable getSessionId();

}
