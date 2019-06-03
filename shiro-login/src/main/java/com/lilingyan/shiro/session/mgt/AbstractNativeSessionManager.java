package com.lilingyan.shiro.session.mgt;

import com.lilingyan.shiro.authz.AuthorizationException;
import com.lilingyan.shiro.session.InvalidSessionException;
import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.session.SessionException;
import com.lilingyan.shiro.session.UnknownSessionException;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:21
 */
public abstract class AbstractNativeSessionManager extends AbstractSessionManager implements NativeSessionManager{

    public Session start(SessionContext context) {
        /**.
         * 创建了一个SimpleSession
         */
        Session session = createSession(context);
        onStart(session, context);
        /**
         * 创建对外暴露的Session
         * 其实就是代理了一下
         *
         * 主要为了让session在普通的操作下完成更多的功能
         */
        return createExposedSession(session, context);
    }

    protected abstract Session createSession(SessionContext context) throws AuthorizationException;

    protected void onStart(Session session, SessionContext context) {
    }

    public Session getSession(SessionKey key) throws SessionException {
        Session session = lookupSession(key);
        return session != null ? createExposedSession(session, key) : null;
    }

    private Session lookupSession(SessionKey key) throws SessionException {
        if (key == null) {
            throw new NullPointerException("SessionKey argument cannot be null.");
        }
        return doGetSession(key);
    }

    protected abstract Session doGetSession(SessionKey key) throws InvalidSessionException;

    protected Session createExposedSession(Session session, SessionContext context) {
        //代理Session
        return new DelegatingSession(this, new DefaultSessionKey(session.getId()));
    }

    protected Session createExposedSession(Session session, SessionKey key) {
        return new DelegatingSession(this, new DefaultSessionKey(session.getId()));
    }

    public Object getAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException {
        return lookupRequiredSession(sessionKey).getAttribute(attributeKey);
    }

    public void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException {
        if (value == null) {
            removeAttribute(sessionKey, attributeKey);
        } else {
            Session s = lookupRequiredSession(sessionKey);
            s.setAttribute(attributeKey, value);
            onChange(s);
        }
    }

    public Object removeAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException {
        Session s = lookupRequiredSession(sessionKey);
        Object removed = s.removeAttribute(attributeKey);
        if (removed != null) {
            onChange(s);
        }
        return removed;
    }

    private Session lookupRequiredSession(SessionKey key) throws SessionException {
        Session session = lookupSession(key);
        if (session == null) {
            String msg = "Unable to locate required Session instance based on SessionKey [" + key + "].";
            throw new UnknownSessionException(msg);
        }
        return session;
    }

    protected void onChange(Session s) {
    }

}
