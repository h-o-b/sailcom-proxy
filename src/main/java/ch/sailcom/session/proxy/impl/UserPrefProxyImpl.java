package ch.sailcom.session.proxy.impl;

import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.sailcom.session.domain.User;
import ch.sailcom.session.domain.UserPref;
import ch.sailcom.session.proxy.UserPrefProxy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@SessionScoped
public class UserPrefProxyImpl implements UserPrefProxy, Serializable {

	private static final long serialVersionUID = -7964478087782131052L;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserPrefProxyImpl.class);

	private static final String REDIS_URL = System.getenv("REDIS_URL");
	private static JedisPool jedisPool;
	private static ObjectMapper mapper = new ObjectMapper();

	private static JedisPool getPool() throws URISyntaxException {
		URI redisURI = new URI(REDIS_URL);
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(10);
		poolConfig.setMaxIdle(5);
		poolConfig.setMinIdle(1);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setTestWhileIdle(true);
		JedisPool pool = new JedisPool(poolConfig, redisURI);
		return pool;
	}

	@PostConstruct
	void init() {
		if (jedisPool != null) {
			return;
		}
		try {
			jedisPool = getPool();
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	private String getRedisKey(User user) {
		return "user:" + user.id + ":pref";
	}

	@Override
	public synchronized UserPref getUserPref(User user) {
		LOGGER.debug("getUserPref({})", user.id);
		try (Jedis jedis = jedisPool.getResource()) {
			String userPrefJson = jedis.get(getRedisKey(user));
			LOGGER.debug("getUserPref({}): {}", userPrefJson);
			if (userPrefJson == null) {
				UserPref userPref = new UserPref();
				userPref.favoriteShips = new HashSet<Integer>();
				userPref.ratedShips = new HashMap<Integer, Integer>();
				userPrefJson = mapper.writeValueAsString(userPref);
				jedis.set(getRedisKey(user), userPrefJson);
			}
			LOGGER.debug("getUserPref({}): {}", userPrefJson);
			return mapper.readValue(userPrefJson, UserPref.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setUserPref(User user, UserPref userPref) {
		LOGGER.debug("setUserPref({}, {})", user.id, userPref);
		try (Jedis jedis = jedisPool.getResource()) {
			String userPrefJson = mapper.writeValueAsString(userPref);
			jedis.set(getRedisKey(user), userPrefJson);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

}
