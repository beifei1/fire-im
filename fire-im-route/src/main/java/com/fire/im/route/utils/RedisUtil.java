package com.fire.im.route.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.Set;

/**
 * @Author: wangzc
 * @Date: 2020/8/21 9:39
 */
@Slf4j
@Component
@ConditionalOnBean(RedisTemplate.class)
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @SneakyThrows
    public boolean expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
        return true;
    }

    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }


    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((List<String>)CollectionUtils.arrayToList(key));
            }
        }
    }

    public Set<String> keys(String prefix) {
        return redisTemplate.keys(prefix);
    }


    public List<String> multiGet(Set<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    //============================String=============================

    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }


    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public boolean set(String key, String value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }

    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    //================================Map=================================

    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }


    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }


    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }


    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    public java.util.Set<String> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error("{}", key, e);
            return null;
        }
    }

    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public long sSet(String key, String... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            log.error("{}", key, e);
            return 0;
        }
    }


    public long sSetAndTime(String key, long time, String... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            log.error("{}", key, e);
            return 0;
        }
    }

    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error("{}", key, e);
            return 0;
        }
    }

    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            log.error("{}", key, e);
            return 0;
        }
    }
    //===============================list=================================

    public java.util.List<String> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range((String) key, start, end);
        } catch (Exception e) {
            log.error("{}", key, e);
            return null;
        }
    }


    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error("{}", key, e);
            return 0;
        }
    }


    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error("{}", key, e);
            return null;
        }
    }


    public boolean lSet(String key, String value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public boolean lPush(String key, String value) {

        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public boolean lPush(String key, String... value) {

        try {
            redisTemplate.opsForList().leftPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }

    public Serializable rPop(String key) {

        try {
            return redisTemplate.opsForList().rightPop(key);
        } catch (Exception e) {
            log.error("{}", key, e);
            return StringUtils.EMPTY;
        }
    }

    public boolean lSet(String key, String value, long time) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public boolean lSet(String key, java.util.List<String> value) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public boolean lSet(String key, java.util.List<String> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public boolean lUpdateIndex(String key, long index, String value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error("{}", key, e);
            return false;
        }
    }


    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            log.error("{}", key, e);
            return 0;
        }
    }

}
