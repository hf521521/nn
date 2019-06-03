package com.lilingyan.shiro.session.mgt;

import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.session.SessionException;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:24
 */
public interface SessionManager {

    Session start(SessionContext context);

    Session getSession(SessionKey key) throws SessionException;

}
