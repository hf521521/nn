package com.lilingyan.shiro.session.mgt.eis;

import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.session.UnknownSessionException;
import com.lilingyan.shiro.util.CollectionUtils;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:32
 */
public class MemorySessionDAO extends AbstractSessionDAO {

    private ConcurrentMap<Serializable, Session> sessions;

    public MemorySessionDAO() {
        this.sessions = new ConcurrentHashMap<Serializable, Session>();
    }

    protected Serializable doCreate(Session session) {
        /**
         * sessionId最终在SessionDAO中生成
         * 默认使用java的uuid
         */
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId);
        storeSession(sessionId, session);
        return sessionId;
    }

    protected Session doReadSession(Serializable sessionId) {
        return sessions.get(sessionId);
    }

    public void update(Session session) throws UnknownSessionException {
        storeSession(session.getId(), session);
    }

    protected Session storeSession(Serializable id, Session session) {
        if (id == null) {
            throw new NullPointerException("id argument cannot be null.");
        }
        return sessions.putIfAbsent(id, session);
    }

    public void delete(Session session) {
        if (session == null) {
            throw new NullPointerException("session argument cannot be null.");
        }
        Serializable id = session.getId();
        if (id != null) {
            sessions.remove(id);
        }
    }

    public Collection<Session> getActiveSessions() {
        Collection<Session> values = sessions.values();
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptySet();
        } else {
            return Collections.unmodifiableCollection(values);
        }
    }

}
