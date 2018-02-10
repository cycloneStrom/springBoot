package org.poem.common.utils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicDouble;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by poem on 2016/6/18.
 * redis noSql数据库的配置信息
 */
@Component
public final class RedisUtils {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisUtils.class);

    /**
     *
     */
    public static final String SEPARATOR = ":";

    /**
     * redis 配置管理
     */
    private static RedisTemplate<String, Object> redisTemplate;


    /**
     * redis序列号管理
     */
    private static JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer();


    public RedisUtils (){

    }

    static RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public RedisUtils(RedisTemplate<String, Object> redisTemplate){
        if(RedisUtils.redisTemplate == null){
            RedisUtils.redisTemplate = redisTemplate;
        }
    }

    /**
     * 根据Key获取值
     * @param keys
     * @return
     */
    private static Collection<String> keys(Collection<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return null;
        }
        ArrayList<String> _keys = new ArrayList<>(keys.size());
        for (String key : keys) {
            String cKey = key(key);
            if (StringUtils.isNotEmpty(cKey)) {
                _keys.add(cKey);
            }
        }
        return _keys;
    }

    /**
     * 根据key获取值
     * @param key
     * @return
     */
    private static String key(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        String schema = SchoolContextUtils.currentSchoolSchema();
        if (StringUtils.isEmpty(schema)) {
            return key;
        }
        String schemaKey = schema + SEPARATOR + key;
        LOGGER.debug("redis use key:" + schemaKey);
        return schemaKey;
    }

    /**
     * @param key
     * @param <V>
     * @return
     * @see org.springframework.data.redis.core.ValueOperations#get(java.lang.Object)
     */
    public static <V> V get(String key) {
        return (V) redisTemplate.opsForValue().get(key(key));
    }

    /**
     * @param key
     * @param value
     * @param <V>
     * @return
     * @see org.springframework.data.redis.core.ValueOperations#getAndSet(Object, Object)
     */
    public static <V> V getAndSet(String key, Object value) {
        return (V) redisTemplate.opsForValue().getAndSet(key(key), value);
    }

    /**
     * @param key
     * @param value
     * @see org.springframework.data.redis.core.ValueOperations#set(Object, Object)
     */
    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key(key), value);
    }

    /**
     * @param key
     * @return
     * @see org.springframework.data.redis.core.ListOperations#size(Object)
     */
    public static Long listSize(String key) {
        return redisTemplate.opsForList().size(key(key));
    }

    /**
     * @param key
     * @param start
     * @param end
     * @see org.springframework.data.redis.core.ListOperations#trim(Object, long, long)
     */
    public static void listTrim(String key, long start, long end) {
        redisTemplate.opsForList().trim(key(key), start, end);
    }

    /**
     * @param key
     * @param <V>
     * @return
     * @see org.springframework.data.redis.core.ListOperations#leftPop(Object)
     */
    public static <V> V listLeftPop(String key) {
        return (V) redisTemplate.opsForList().leftPop(key(key));
    }

    /**
     * @param key
     * @param value
     * @return
     * @see org.springframework.data.redis.core.ListOperations#leftPush(Object, Object)
     */
    public static Long listLeftPush(String key, Object value) {
        return redisTemplate.opsForList().leftPush(key(key), value);
    }

    /**
     * @param key
     * @param values
     * @return
     * @see org.springframework.data.redis.core.ListOperations#leftPushAll(Object, Object...)
     */
    public static Long listLeftPushAll(String key, Object... values) {
        return redisTemplate.opsForList().leftPushAll(key(key), values);
    }

    /**
     * @param key
     * @param value
     * @return
     * @see org.springframework.data.redis.core.ListOperations#rightPush(Object, Object)
     */
    public static Long listRightPush(String key, Object value) {
        return redisTemplate.opsForList().rightPush(key(key), value);
    }

    /**
     * @param key
     * @param <V>
     * @return
     * @see org.springframework.data.redis.core.ListOperations#rightPop(Object)
     */
    public static <V> V listRightPop(String key) {
        return (V) redisTemplate.opsForList().rightPop(key(key));
    }

    public static Object queueRightPop(String key) {
        Object obj = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                byte[] bytes = connection.rPop(redisTemplate.getStringSerializer().serialize(key(key)));
                return jdkSerializer.deserialize(bytes);
            }
        });
        return obj;
    }

    /**
     * Set {@code key} to hold the string {@code value} until {@code timeout}.
     *
     * @param key
     * @param value
     * @param timeout
     * @param unit
     * @see org.springframework.data.redis.core.ValueOperations#set(Object, Object, long, java.util.concurrent.TimeUnit)
     * @see //redis.io/commands/set
     */
    public static void set(String key, Object value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key(key), value, timeout, unit);
    }

    /**
     * @param keys
     * @param <V>
     * @return
     * @see org.springframework.data.redis.core.ValueOperations#multiGet(java.util.Collection<String>)
     */
    public static <V> List<V> multiGet(Collection<String> keys) {
        return (List<V>) redisTemplate.opsForValue().multiGet(keys(keys));
    }

    /**
     * @param key
     * @param delta
     * @return
     * @see org.springframework.data.redis.core.ValueOperations#increment(Object, long)
     */
    public static Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key(key), delta);
    }

    /**
     * @param key
     * @param delta
     * @return
     * @see org.springframework.data.redis.core.ValueOperations#increment(Object, double)
     */
    public static Double increment(String key, double delta) {
        return redisTemplate.opsForValue().increment(key(key), delta);
    }

    /**
     * @param key
     * @return
     * @see org.springframework.data.redis.core.ValueOperations#size(Object)
     */
    public static Long size(String key) {
        return redisTemplate.opsForValue().size(key(key));
    }

    /**
     * @param key
     * @see org.springframework.data.redis.core.RedisTemplate#delete(Object)
     */
    public static void delete(String key) {
        redisTemplate.delete(key(key));
    }

    /**
     * @param keys
     * @see org.springframework.data.redis.core.RedisTemplate#delete(java.util.Collection<String>)
     */
    public static void delete(Collection<String> keys) {
        redisTemplate.delete(keys(keys));
    }

    /**
     * @param key
     * @param hashKeys
     * @see org.springframework.data.redis.core.HashOperations#delete(Object, java.lang.Object...)
     */
    public static void hashDelete(String key, Object... hashKeys) {
        redisTemplate.opsForHash().delete(key(key), hashKeys);
    }

    /**
     * @param key
     * @param hashKey
     * @param <V>
     * @return
     * @see org.springframework.data.redis.core.HashOperations#get(Object, java.lang.Object)
     */
    public static <V> V hashGet(String key, Object hashKey) {
        return (V) redisTemplate.opsForHash().get(key(key), hashKey);
    }

    /**
     * @param key
     * @param hashKey
     * @param hashValue
     * @see org.springframework.data.redis.core.HashOperations#put(Object, Object, Object)
     */
    public static void hashPut(String key, Object hashKey, Object hashValue) {
        redisTemplate.opsForHash().put(key(key), hashKey, hashValue);
    }

    /**
     * @param key
     * @param start
     * @param end
     * @param <V>
     * @return
     * @see org.springframework.data.redis.core.ListOperations#range(java.lang.Object, long, long)
     */
    public static <V> V listRange(String key, long start, long end) {
        return (V) redisTemplate.opsForList().range(key(key), start, end);
    }

    /**
     * @param key
     * @return
     * @see org.springframework.data.redis.core.RedisTemplate#getExpire(Object)
     */
    public static Long getExpire(String key) {
        return redisTemplate.getExpire(key(key));
    }

    /**
     * @param key
     * @param date
     * @return
     * @see org.springframework.data.redis.core.RedisTemplate#expireAt(Object, java.util.Date)
     */
    public static Boolean expireAt(String key, final Date date) {
        return redisTemplate.expireAt(key(key), date);
    }

    /**
     * @param key
     * @param timeout
     * @param unit
     * @return
     * @see org.springframework.data.redis.core.RedisTemplate#expire(Object, long, java.util.concurrent.TimeUnit)
     */
    public static Boolean expire(String key, final long timeout, final TimeUnit unit) {
        return redisTemplate.expire(key(key), timeout, unit);
    }

    /**
     * @param key
     * @return
     * @see org.springframework.data.redis.core.RedisTemplate#hasKey(Object)
     */
    public static Boolean hasKey(String key) {
        return redisTemplate.hasKey(key(key));
    }

    /**
     * Counter for Long
     *
     * @param key
     * @param timeout
     * @param unit
     * @param initialValue
     * @return
     */
    public static RedisAtomicLong initCounter(String key, final Long timeout, final TimeUnit unit, Long initialValue) {
        RedisAtomicLong inr = new RedisAtomicLong(key(key), redisTemplate.getConnectionFactory(), initialValue);
        if (timeout != null && unit != null) {
            inr.expire(timeout, unit);
        }
        return inr;
    }

    /**
     * Counter for Long
     *
     * @param key
     * @return
     */
    public static RedisAtomicLong getCounter(final String key) {
        Object value = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Object result = connection.get(redisTemplate.getStringSerializer().serialize(key(key)));
                if (result == null) {
                    return new Long(0);
                }
                GenericToStringSerializer serializer = new GenericToStringSerializer<Long>(Long.class);
                byte[] bytes = (byte[]) result;
                if (bytes.length == 0) {
                    return new Long(0);
                }
                return serializer.deserialize(bytes);
            }
        });
        Long initValue = value == null ? 0l : (Long) value;
        RedisAtomicLong inr = new RedisAtomicLong(key(key), redisTemplate.getConnectionFactory(), initValue);
        return inr;
    }

    /**
     * 方便操作double类型数据
     * 默认反序列化是JDK，这里是GenericToStringSerializer
     *
     * @param key
     * @param initValue
     * @return
     */
    public static RedisAtomicDouble getAtomicDouble(final String key, Double initValue) {
        if (initValue == null) {
            Object value = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    Object result = connection.get(redisTemplate.getStringSerializer().serialize(key(key)));
                    if (result == null) {
                        return new Double(0);
                    }
                    GenericToStringSerializer serializer = new GenericToStringSerializer<Double>(Double.class);
                    byte[] bytes = (byte[]) result;
                    if (bytes.length == 0) {
                        return new Double(0);
                    }
                    return serializer.deserialize(bytes);
                }
            });
            initValue = value == null ? 0d : (Double) value;
        }
        RedisAtomicDouble inr = new RedisAtomicDouble(key(key), redisTemplate.getConnectionFactory(), initValue);
        return inr;
    }

    /**
     * 获取double类型数据
     * 默认反序列化是JDK，这里是GenericToStringSerializer
     *
     * @param key
     * @return
     */
    public static Double getDoubleValue(final String key) {
        Object value = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Object result = connection.get(redisTemplate.getStringSerializer().serialize(key(key)));
                if (result == null) {
                    return null;
                }
                GenericToStringSerializer serializer = new GenericToStringSerializer<Double>(Double.class);
                byte[] bytes = (byte[]) result;
                if (bytes.length == 0) {
                    return null;
                }
                return serializer.deserialize(bytes);
            }
        });
        return value == null ? null : (Double) value;
    }

    /**
     * 获取double类型数据
     * 默认反序列化是JDK，这里是GenericToStringSerializer
     *
     * @param key
     * @return
     */
    public static <T extends Number> T getCounterValue(String key, Class<T> type) {
        Object value = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                Object result = connection.get(redisTemplate.getStringSerializer().serialize(key(key)));
                if (result == null) {
                    return null;
                }
                GenericToStringSerializer serializer = new GenericToStringSerializer<T>(type);
                byte[] bytes = (byte[]) result;
                if (bytes.length == 0) {
                    return null;
                }
                return (T) serializer.deserialize(bytes);
            }
        });
        return value == null ? null : (T) value;
    }
}
