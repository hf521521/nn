package com.lilingyan.shiro.session.mgt;

import com.lilingyan.shiro.session.Session;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:23
 */
public interface SessionFactory {

    Session createSession(SessionContext initData);

}
