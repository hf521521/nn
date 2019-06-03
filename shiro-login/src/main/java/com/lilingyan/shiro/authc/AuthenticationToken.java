package com.lilingyan.shiro.authc;

/**
 * @Author: lilingyan
 * @Date 2019/6/3 11:21
 */
public interface AuthenticationToken {

    Object getPrincipal();

    Object getCredentials();

}
