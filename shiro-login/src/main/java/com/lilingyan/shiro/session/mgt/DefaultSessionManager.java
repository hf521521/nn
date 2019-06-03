package com.lilingyan.shiro.session.mgt;

import com.lilingyan.shiro.authz.AuthorizationException;
import com.lilingyan.shiro.session.InvalidSessionException;
import com.lilingyan.shiro.session.Session;
import com.lilingyan.shiro.session.UnknownSessionException;
import com.lilingyan.shiro.session.mgt.eis.MemorySessionDAO;
import com.lilingyan.shiro.session.mgt.eis.SessionDAO;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 13:22
 */
@Slf4j
public class DefaultSessionManager extends AbstractNativeSessionManager{

    private SessionFactory sessionFactory;

    protected SessionDAO sessionDAO;

    private boolean deleteInvalidSessions;

    public DefaultSessionManager() {
        this.deleteInvalidSessions = true;
        this.sessionFactory = new SimpleSessionFactory();
        this.sessionDAO = new MemorySessionDAO();
    }

    protected Session createSession(SessionContext context) throws AuthorizationException {
        return doCreateSession(context);
    }

    @Override
    protected final Session doGetSession(final SessionKey key) throws InvalidSessionException {
        log.trace("Attempting to retrieve session with key {}", key);
        Session s = retrieveSession(key);
        return s;
    }

    protected Session retrieveSession(SessionKey sessionKey) throws UnknownSessionException {
        Serializable sessionId = getSessionId(sessionKey);
        if (sessionId == null) {
            log.debug("Unable to resolve session ID from SessionKey [{}].  Returning null to indicate a " +
                    "session could not be found.", sessionKey);
            return null;
        }
        Session s = retrieveSessionFromDataSource(sessionId);
        if (s == null) {
            //session ID was provided, meaning one is expected to be found, but we couldn't find one:
            String msg = "Could not find session with ID [" + sessionId + "]";
            throw new UnknownSessionException(msg);
        }
        return s;
    }

    protected Session doCreateSession(SessionContext context) {
        /**
         * 使用工厂类创建session
         * 默认SimpleSession
         */
        Session s = newSessionInstance(context);
        create(s);
        return s;
    }

    protected Session newSessionInstance(SessionContext context) {
        return getSessionFactory().createSession(context);
    }

    protected void create(Session session) {
        if (log.isDebugEnabled()) {
            log.debug("Creating new EIS record for new session instance [" + session + "]");
        }
        //最终交由sessionDAO生成
        sessionDAO.create(session);
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    protected Session retrieveSessionFromDataSource(Serializable sessionId) throws UnknownSessionException {
        return sessionDAO.readSession(sessionId);
    }

    protected Serializable getSessionId(SessionKey sessionKey) {
        return sessionKey.getSessionId();
    }

    protected void onChange(Session session) {
        //最终任何修改都会被传递到sessionDAO
        sessionDAO.update(session);
    }

}
