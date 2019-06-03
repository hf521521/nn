package com.lilingyan.shiro.mgt;

import com.lilingyan.shiro.realm.Realm;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:29
 */
public abstract class RealmSecurityManager implements SecurityManager{

    private Collection<Realm> realms;

    public void setRealm(Realm realm) {
        if (realm == null) {
            throw new IllegalArgumentException("Realm argument cannot be null");
        }
        Collection<Realm> realms = new ArrayList<Realm>(1);
        realms.add(realm);
        setRealms(realms);
    }

    public void setRealms(Collection<Realm> realms) {
        if (realms == null) {
            throw new IllegalArgumentException("Realms collection argument cannot be null.");
        }
        if (realms.isEmpty()) {
            throw new IllegalArgumentException("Realms collection argument cannot be empty.");
        }
        this.realms = realms;
        afterRealmsSet();
    }

    protected void afterRealmsSet() {
    }

}
