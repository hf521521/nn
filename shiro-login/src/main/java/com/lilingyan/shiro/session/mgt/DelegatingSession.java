package com.lilingyan.shiro.session.mgt;

import com.lilingyan.shiro.session.InvalidSessionException;
import com.lilingyan.shiro.session.Session;

import java.io.Serializable;

/**
 * 代理的session
 * 他会把对所有对session的操作
 * 提交给sessionManager去处理
 * @Author: lilingyan
 * @Date 2019/6/3 13:37
 */
public class DelegatingSession implements Session {

    private final SessionKey key;
    private final transient NativeSessionManager sessionManager;

    public DelegatingSession(NativeSessionManager sessionManager, SessionKey key) {
        if (sessionManager == null) {
            throw new IllegalArgumentException("sessionManager argument cannot be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("sessionKey argument cannot be null.");
        }
        if (key.getSessionId() == null) {
            String msg = "The " + DelegatingSession.class.getName() + " implementation requires that the " +
                    "SessionKey argument returns a non-null sessionId to support the " +
                    "Session.getId() invocations.";
            throw new IllegalArgumentException(msg);
        }
        this.sessionManager = sessionManager;
        this.key = key;
    }

    public Serializable getId() {
        return key.getSessionId();
    }

    @Override
    public Object getAttribute(Object attributeKey) throws InvalidSessionException {
        return sessionManager.getAttribute(this.key, attributeKey);
    }

    @Override
    public void setAttribute(Object attributeKey, Object value) throws InvalidSessionException {
        if (value == null) {
            removeAttribute(attributeKey);
        } else {
            //被代理了
            sessionManager.setAttribute(this.key, attributeKey, value);
        }
    }

    @Override
    public Object removeAttribute(Object attributeKey) throws InvalidSessionException {
        return sessionManager.removeAttribute(this.key, attributeKey);
    }

}
