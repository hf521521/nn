package com.lilingyan.shiro.session.mgt.eis;

import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.session.UnknownSessionException;

import java.io.Serializable;
import java.util.Collection;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:24
 */
public interface SessionDAO {

    Serializable create(Session session);

    Session readSession(Serializable sessionId) throws UnknownSessionException;

    void update(Session session) throws UnknownSessionException;

    void delete(Session session);

    Collection<Session> getActiveSessions();

}
