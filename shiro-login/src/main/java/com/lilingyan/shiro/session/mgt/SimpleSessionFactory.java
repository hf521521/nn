package com.lilingyan.shiro.session.mgt;

import com.lilingyan.shiro.session.Session;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:26
 */
public class SimpleSessionFactory implements SessionFactory{

    public Session createSession(SessionContext initData) {
        /**
         * 这里生成sessionId 可以吗？
         */
        return new SimpleSession();
    }

}
