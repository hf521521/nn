package com.lilingyan.shiro.session;

import java.io.Serializable;

/**
 * 代理session
 * 让暴露方法可控
 * @Author: lilingyan
 * @Date 2019/6/3 13:51
 */
public class ProxiedSession implements Session{

    protected final Session delegate;

    public ProxiedSession(Session target) {
        if (target == null) {
            throw new IllegalArgumentException("Target session to proxy cannot be null.");
        }
        delegate = target;
    }

    public Serializable getId() {
        return delegate.getId();
    }

    @Override
    public Object getAttribute(Object key) throws InvalidSessionException {
        return delegate.getAttribute(key);
    }

    @Override
    public void setAttribute(Object key, Object value) throws InvalidSessionException {
        delegate.setAttribute(key, value);
    }

    @Override
    public Object removeAttribute(Object key) throws InvalidSessionException {
        return delegate.removeAttribute(key);
    }

}
