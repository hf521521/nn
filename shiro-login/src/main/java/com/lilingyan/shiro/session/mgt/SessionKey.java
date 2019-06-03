package com.lilingyan.shiro.session.mgt;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:26
 */
public interface SessionKey {

    Serializable getSessionId();

}
