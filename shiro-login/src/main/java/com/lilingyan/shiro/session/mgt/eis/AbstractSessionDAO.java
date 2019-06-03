package com.lilingyan.shiro.session.mgt.eis;

import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.session.UnknownSessionException;
import com.lilingyan.shiro.session.mgt.SimpleSession;
import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:28
 */
public abstract class AbstractSessionDAO implements SessionDAO{

    private SessionIdGenerator sessionIdGenerator;

    /**
     * 默认sessionid是java生成的uuid
     */
    public AbstractSessionDAO() {
        this.sessionIdGenerator = new JavaUuidSessionIdGenerator();
    }

    protected Serializable generateSessionId(Session session) {
        if (this.sessionIdGenerator == null) {
            String msg = "sessionIdGenerator attribute has not been configured.";
            throw new IllegalStateException(msg);
        }
        return this.sessionIdGenerator.generateId(session);
    }

    protected void assignSessionId(Session session, Serializable sessionId) {
        ((SimpleSession) session).setId(sessionId);
    }

    @Override
    public Serializable create(Session session) {
        Serializable sessionId = doCreate(session);
        return sessionId;
    }

    protected abstract Serializable doCreate(Session session);

    @Override
    public Session readSession(Serializable sessionId) throws UnknownSessionException {
        Session s = doReadSession(sessionId);
        if (s == null) {
            throw new UnknownSessionException("There is no session with id [" + sessionId + "]");
        }
        return s;
    }

    protected abstract Session doReadSession(Serializable sessionId);

}
