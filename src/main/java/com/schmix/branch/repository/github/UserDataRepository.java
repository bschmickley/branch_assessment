package com.schmix.branch.repository.github;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.schmix.branch.models.github.UserData;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class UserDataRepository {
    private final Cache<String, UserData> cache;

    public UserDataRepository() {
        cache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(15, TimeUnit.MINUTES)
                .build();
    }

    public UserData get(String key) {
        return cache.getIfPresent(key);
    }

    public void save(String key, UserData data) {
        cache.put(key, data);
    }
}
