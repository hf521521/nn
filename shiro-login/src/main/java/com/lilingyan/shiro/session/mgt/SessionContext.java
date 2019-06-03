package com.lilingyan.shiro.session.mgt;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:25
 */
public interface SessionContext extends Map<String, Object> {

    Serializable getSessionId();

    void setSessionId(Serializable sessionId);

}
