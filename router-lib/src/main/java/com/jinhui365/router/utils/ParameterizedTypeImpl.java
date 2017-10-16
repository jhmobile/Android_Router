package com.jinhui365.router.utils;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 * Name:ParameterizedTypeImpl
 * Author:jmtian
 * Commemt:gson解析反射类
 * Date: 2017/8/24 16:06
 */

public class ParameterizedTypeImpl implements ParameterizedType {
    private final Class raw;
    private final Type[] args;

    public ParameterizedTypeImpl(Class raw, Type[] args) {
        this.raw = raw;
        this.args = args != null?args:new Type[0];
    }

    public Type[] getActualTypeArguments() {
        return this.args;
    }

    public Type getRawType() {
        return this.raw;
    }

    public Type getOwnerType() {
        return null;
    }
}
