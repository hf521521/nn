package com.lilingyan.shiro.session;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:24
 */
public interface Session {

    Serializable getId();

    Object getAttribute(Object key) throws InvalidSessionException;

    void setAttribute(Object key, Object value) throws InvalidSessionException;

    Object removeAttribute(Object key) throws InvalidSessionException;

}
