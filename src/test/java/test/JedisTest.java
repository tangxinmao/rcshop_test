package test;

import com.socool.soft.util.AESUtil;
import com.socool.soft.util.SerializeUtil;

import redis.clients.jedis.Jedis;

public class JedisTest {

	public static void main(String[] args) throws Exception {
//		Jedis jedis = new Jedis("127.0.0.1", 6379);
//		String key = "test3";
//		Integer value = 100;
//		jedis.set(SerializeUtil.serialize(key), SerializeUtil.serialize(value));
//		jedis.incr(SerializeUtil.serialize(key));
//		System.out.println(SerializeUtil.unserialize(jedis.get(SerializeUtil.serialize(key))));
//		System.out.println(jedis.hget("abc", "xyz"));
//		jedis.close();
		System.out.println(AESUtil.Encrypt("{\"county\":\"\",\"cityId\":14,\"address\":\"四川\",\"name\":\"等等\",\"province\":\"DKI Jakarta\",\"memberId\":\"10041\",\"city\":\"Jakarta Barat\",\"mobile\":\"123456\"}"));
	}
}
