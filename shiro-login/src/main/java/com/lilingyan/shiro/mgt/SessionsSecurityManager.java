package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.authz.AuthorizationException;
import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.session.SessionException;
import com.lilingyan.shiro.session.mgt.DefaultSessionManager;
import com.lilingyan.shiro.session.mgt.SessionContext;
import com.lilingyan.shiro.session.mgt.SessionKey;
import com.lilingyan.shiro.session.mgt.SessionManager;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:17
 */
public abstract class SessionsSecurityManager extends AuthenticatingSecurityManager {

    private SessionManager sessionManager;

    public SessionsSecurityManager() {
        super();
        this.sessionManager = new DefaultSessionManager();
    }

    public Session start(SessionContext context) throws AuthorizationException {
        return this.sessionManager.start(context);
    }

    public Session getSession(SessionKey key) throws SessionException {
        return this.sessionManager.getSession(key);
    }

}
