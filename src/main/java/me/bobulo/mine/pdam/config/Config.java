package me.bobulo.mine.pdam.config;

import java.lang.reflect.Type;

public interface Config {

    String getString(String key);

    boolean getBoolean(String key);

    int getInt(String key);

    long getLong(String key);

    float getFloat(String key);

    double getDouble(String key);

    byte getByte(String key);

    short getShort(String key);

    <V> V getValue(String key, Class<V> valueType);

    <V> V getValue(String key, Type valueType);

    void setValue(String key, Object value);

}
