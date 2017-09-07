package com.socool.soft.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

import com.socool.soft.exception.ErrorCode;
import com.socool.soft.exception.SystemException;
import com.socool.soft.service.IRedisService;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Service
public class RedisServiceImpl implements IRedisService {

	@Autowired
	private ShardedJedisPool pool;
	
	private ShardedJedis getRedisClient() throws SystemException {
        ShardedJedis shardJedis = null;
        try {
            shardJedis = pool.getResource();
        } catch (Exception e) {
            if (shardJedis != null) {
                shardJedis.close();
            }
            throw new SystemException(ErrorCode.REDIS_ERROR);
        }
        return shardJedis;
    }
	
//	public int exists(String key) throws SystemException {
//		ShardedJedis jedis = getRedisClient();
//		
//		try {
//			if(jedis.exists(SerializeUtil.serialize(key))) {
//				return 1;
//			}
//		} catch(Exception e) {
//			throw new SystemException(ErrorCode.REDIS_ERROR);
//		} finally {
//			jedis.close();
//		}
//		return 0;
//	}
	
//	@Override
//	public int setObject(String key, Object value) throws SystemException {
//		ShardedJedis jedis = getRedisClient();
//		
//		try {
//			jedis.set(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
//		} catch(Exception e) {
//			throw new SystemException(ErrorCode.REDIS_ERROR);
//		} finally {
//			jedis.close();
//		}
//		return 1;
//	}
	
	@Override
	public int set(String key, String value) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			jedis.set(key, value);
		} catch(Exception e) {
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
		return 1;
	}
	
	@Override
	public int hsetObject(String key, String field, Object value) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			jedis.hset(key.getBytes(), SerializationUtils.serialize(field), SerializationUtils.serialize(value));
		} catch(Exception e) {
			e.printStackTrace();
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
		return 1;
	}
	
	@Override
	public int setex(String key, int expireTime, String value) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			jedis.setex(key, expireTime, value);
		} catch(Exception e) {
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
		return 1;
	}
	
//	@Override
//	public Object getObject(String key) throws SystemException {
//		ShardedJedis jedis = getRedisClient();
//		
//		try {
//			byte[] bytes = jedis.get(SerializeUtil.serialize(key));
//			if(bytes != null) {
//				return SerializeUtil.unserialize(bytes);
//			}
//		} catch(Exception e) {
//			throw new SystemException(ErrorCode.REDIS_ERROR);
//		} finally {
//			jedis.close();
//		}
//		return null;
//	}
	
	@Override
	public String get(String key) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			return jedis.get(key);
		} catch(Exception e) {
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
	}
	
	@Override
	public Object hgetObject(String key, String field) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			byte[] bytes =  jedis.hget(key.getBytes(), SerializationUtils.serialize(field));
			if(bytes != null) {
				return SerializationUtils.deserialize(bytes);
			}
		} catch(Exception e) {
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
		return null;
	}
	
//	@Override
//	public long delObject(String key) throws SystemException {
//		ShardedJedis jedis = getRedisClient();
//		
//		try {
//			return jedis.del(SerializeUtil.serialize(key));
//		} catch(Exception e) {
//			throw new SystemException(ErrorCode.REDIS_ERROR);
//		} finally {
//			jedis.close();
//		}
//	}
	
	@Override
	public long del(String key) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			return jedis.del(key);
		} catch(Exception e) {
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
	}
	
	@Override
	public long hdelObject(String key, String field) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			return jedis.hdel(key.getBytes(), SerializationUtils.serialize(field));
		} catch(Exception e) {
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
	}

	@Override
	public long sadd(String key, String value) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			return jedis.sadd(key, value);
		} catch(Exception e) {
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
	}

	@Override
	public long srem(String key, String value) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			return jedis.srem(key, value);
		} catch(Exception e) {
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
	}

	@Override
	public boolean sismember(String key, String value) throws SystemException {
		ShardedJedis jedis = getRedisClient();
		
		try {
			return jedis.sismember(key, value);
		} catch(Exception e) {
			throw new SystemException(ErrorCode.REDIS_ERROR);
		} finally {
			jedis.close();
		}
	}

//	public int lpush(String key, Object value) throws SystemException {
//		ShardedJedis jedis = getRedisClient();
//		
//		try {
//			jedis.lpush(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
//		} catch(Exception e) {
//			throw new SystemException(ErrorCode.REDIS_ERROR);
//		} finally {
//			jedis.close();
//		}
//		return 1;
//	}
	
//	public List<Object> lrange(String key) throws SystemException {
//		ShardedJedis jedis = getRedisClient();
//		
//		try {
//			List<byte[]> bytesList = jedis.lrange(SerializeUtil.serialize(key), 0, -1);
//			if(bytesList != null) {
//				List<Object> objs = new ArrayList<Object>();
//				for(byte[] bytes : bytesList) {
//					objs.add(SerializeUtil.unserialize(bytes));
//				}
//				return objs;
//			}
//		} catch(Exception e) {
//			throw new SystemException(ErrorCode.REDIS_ERROR);
//		} finally {
//			jedis.close();
//		}
//		return null;
//	}
}
