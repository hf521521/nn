package com.lilingyan.shiro.session.mgt;

import com.lilingyan.shiro.session.InvalidSessionException;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:20
 */
public interface NativeSessionManager extends SessionManager{

    Object getAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException;

    void setAttribute(SessionKey sessionKey, Object attributeKey, Object value) throws InvalidSessionException;

    Object removeAttribute(SessionKey sessionKey, Object attributeKey) throws InvalidSessionException;

}
