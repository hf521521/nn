package com.lilingyan.shiro.session.mgt;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:39
 */
public class DefaultSessionKey implements SessionKey{

    private Serializable sessionId;

    public DefaultSessionKey(Serializable sessionId) {
        this.sessionId = sessionId;
    }

    public Serializable getSessionId() {
        return this.sessionId;
    }

}
