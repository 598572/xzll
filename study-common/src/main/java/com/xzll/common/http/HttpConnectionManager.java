package com.xzll.common.http;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: hzz
 * @Date: 2023/2/28 15:28:36
 * @Description:  连接管理器
 */
public class HttpConnectionManager {

	private static Map<String, HttpConnectionPool> httpManager = new ConcurrentHashMap<String, HttpConnectionPool>();

	public static HttpConnectionPool getConnectionPool(String poolName){
		HttpConnectionPool rtv = httpManager.get(poolName);
		if(null == rtv){
			synchronized(HttpConnectionManager.class){
				rtv = addHttpConnectionPool(poolName);
				rtv.init();
			}

		}
		return rtv;
	}

	public static HttpConnectionPool getConnectionPool(String poolName, int maxTotalPool, int maxConPerRoute){
		HttpConnectionPool rtv = httpManager.get(poolName);
		if(null == rtv){
			synchronized(HttpConnectionManager.class){
				rtv = addHttpConnectionPool(poolName);
				rtv.init(maxTotalPool, maxConPerRoute, HttpConnectionPool.SOCKET_TIMEOUT, HttpConnectionPool.CONNECTION_REQUEST_TIMEOUT, HttpConnectionPool.CONNECT_TIMEOUT);
			}

		}

		return rtv;
	}

	public static HttpConnectionPool getConnectionPool(String poolName, int maxTotalPool, int maxConPerRoute, int socketTimeout, int connectionRequestTimeout, int connectTimeout){
		HttpConnectionPool rtv = httpManager.get(poolName);
		if(null == rtv){
			synchronized(HttpConnectionManager.class){
				rtv = addHttpConnectionPool(poolName);
				rtv.init(maxTotalPool, maxConPerRoute, socketTimeout, connectionRequestTimeout, connectTimeout);
			}

		}

		return rtv;
	}

	public static HttpConnectionPool getConnectionPool(String poolName, int socketTimeout){
		HttpConnectionPool rtv = httpManager.get(poolName);
		if(null == rtv){
			synchronized(HttpConnectionManager.class){
				rtv = addHttpConnectionPool(poolName);
				rtv.init(HttpConnectionPool.MAX_TOTAL_POOL, HttpConnectionPool.MAX_CON_PER_ROUTE, socketTimeout, HttpConnectionPool.CONNECTION_REQUEST_TIMEOUT, HttpConnectionPool.CONNECT_TIMEOUT);
			}

		}

		return rtv;
	}

	public static HttpConnectionPool getConnectionPool(String poolName, int socketTimeout, int connectionRequestTimeout, int connectTimeout){
		HttpConnectionPool rtv = httpManager.get(poolName);
		if(null == rtv){
			synchronized(HttpConnectionManager.class){
				rtv = addHttpConnectionPool(poolName);
				rtv.init(HttpConnectionPool.MAX_TOTAL_POOL, HttpConnectionPool.MAX_CON_PER_ROUTE, socketTimeout, connectionRequestTimeout, connectTimeout);
			}

		}

		return rtv;
	}

	public static HttpConnectionPool getConnectionPool(String poolName, String certPasswd, String certPathWithFileName){
		HttpConnectionPool rtv = httpManager.get(poolName);
		if(null == rtv){
			synchronized(HttpConnectionManager.class){
				rtv = addHttpConnectionPool(poolName);
				try {
					rtv.init(certPasswd, certPathWithFileName);
				} catch (Exception e) {
				}
			}

		}


		return rtv;
	}

	public static HttpConnectionPool getConnectionPool(String poolName, String certPasswd, byte[] bytes){
		HttpConnectionPool rtv = httpManager.get(poolName);
		if(null == rtv){
			synchronized(HttpConnectionManager.class){
				rtv = addHttpConnectionPool(poolName);
				try {
					rtv.init(certPasswd, bytes);
				} catch (Exception e) {
				}
			}

		}


		return rtv;
	}


	public static HttpConnectionPool getConnectionPool(String poolName, String certPasswd, InputStream fileInputStream){
		HttpConnectionPool rtv = httpManager.get(poolName);
		if(null == rtv){
			synchronized(HttpConnectionManager.class){
				rtv = addHttpConnectionPool(poolName);
				try {
					rtv.initCommon(certPasswd, fileInputStream);
				} catch (Exception e) {
				}
			}

		}


		return rtv;
	}


	private static synchronized HttpConnectionPool addHttpConnectionPool(String poolName){
		HttpConnectionPool rtv = httpManager.get(poolName);
		if(null != rtv){
			return rtv;
		}

		rtv = new HttpConnectionPool(poolName);

		httpManager.put(poolName, rtv);

		return rtv;
	}
}
