package com.lilingyan.shiro.session.mgt;

import com.lilingyan.shiro.session.InvalidSessionException;
import com.lilingyan.shiro.session.Session;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:27
 */
public class SimpleSession implements Session {

    private transient Serializable id;

    @Override
    public Serializable getId() {
        return this.id;
    }

    @Override
    public Object getAttribute(Object key) throws InvalidSessionException {
        return null;
    }

    @Override
    public void setAttribute(Object key, Object value) throws InvalidSessionException {

    }

    @Override
    public Object removeAttribute(Object key) throws InvalidSessionException {
        return null;
    }

    public void setId(Serializable id) {
        this.id = id;
    }

}
