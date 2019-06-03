package com.lilingyan.shiro.session.mgt.eis;

import com.lilingyan.shiro.session.Session;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:31
 */
public interface SessionIdGenerator {

    Serializable generateId(Session session);

}
