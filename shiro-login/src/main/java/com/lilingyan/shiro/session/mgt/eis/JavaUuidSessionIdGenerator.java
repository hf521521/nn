package com.lilingyan.shiro.session.mgt.eis;

import com.lilingyan.shiro.session.Session;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:31
 */
public class JavaUuidSessionIdGenerator implements SessionIdGenerator{

    public Serializable generateId(Session session) {
        return UUID.randomUUID().toString();
    }

}
