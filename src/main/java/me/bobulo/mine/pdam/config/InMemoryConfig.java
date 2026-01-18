package me.bobulo.mine.pdam.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryConfig extends AbstractBaseConfig {

    private final Map<String, Object> data = new ConcurrentHashMap<>();

    @Override
    public Object getRaw(String key) {
        return data.get(key);
    }

    @Override
    public void setValue(String key, Object value) {
        if (value == null) {
            data.remove(key);
            return;
        }

        data.put(key, value);
    }

}
