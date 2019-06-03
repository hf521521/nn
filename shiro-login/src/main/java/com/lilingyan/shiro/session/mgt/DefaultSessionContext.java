package com.lilingyan.shiro.session.mgt;

import com.lilingyan.shiro.util.MapContext;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 14:15
 */
public class DefaultSessionContext extends MapContext implements SessionContext{

    private static final String SESSION_ID = DefaultSessionContext.class.getName() + ".SESSION_ID";

    public Serializable getSessionId() {
        return getTypedValue(SESSION_ID, Serializable.class);
    }

    public void setSessionId(Serializable sessionId) {
        nullSafePut(SESSION_ID, sessionId);
    }

}
