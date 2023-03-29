package com.xzll.test.websocket.util;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URISyntaxException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class HttpConnectionManager {

	private final static Logger logger = LoggerFactory.getLogger(HttpConnectionManager.class);

	private static final int maxTotalPool = 500;
	private static final int maxConPerRoute = 100;
	private static final int socketTimeout = 5000;
	private static final int connectionRequestTimeout = 3000;
	private static final int connectTimeout = 1000;

	private static final RequestConfig defRequestConfig;
	static {
		defRequestConfig = RequestConfig.custom() //
				.setConnectionRequestTimeout(connectionRequestTimeout) //
				.setConnectTimeout(connectTimeout) //
				.setSocketTimeout(socketTimeout) //
                .build();
    }

	private PoolingHttpClientConnectionManager poolConnManager;

	public HttpConnectionManager init() {
		try {
			init(null, (String) null);
		} catch (Exception e) {
		}
		return this;
	}

	public void init(String certPasswd, String certPathWithFileName) throws IOException {
		try {
			FileInputStream fileInputStream = null;
			if(null != certPathWithFileName){
				fileInputStream = new FileInputStream(new File(certPathWithFileName));// 加载本地的证书进行https加密传输
			}
			initCommon(certPasswd, fileInputStream);
		} catch (Exception e) {
			logger.error("http连接池初始化失败", e);
		}


	}

	public void init(String certPasswd, byte[] bytes) {

		try {
			// 读取二进制数据并转换成对象
			InputStream bis = new ByteArrayInputStream(bytes);
			initCommon(certPasswd,bis);
		} catch (Exception e) {
			logger.error("http连接池初始化失败", e);
		}

	}


	public void initCommon(String certPasswd, InputStream fileInputStream) throws IOException {

		try {
			SSLContext sslcontext = null;

			RegistryBuilder<ConnectionSocketFactory> connectionSocketFactory = RegistryBuilder.<ConnectionSocketFactory> create();


			if (null != certPasswd && null != fileInputStream) {

				KeyStore keyStore = KeyStore.getInstance("PKCS12");

				keyStore.load(fileInputStream, certPasswd.toCharArray());// 设置证书密码

				sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPasswd.toCharArray()).build();
			}else{
				X509TrustManager tm = new X509TrustManager() {
					@Override
					public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					}

					@Override
					public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					}

					@Override
					public X509Certificate[] getAcceptedIssuers() {
						return null;
					}
				};
				sslcontext = SSLContext.getInstance("TLS");
				sslcontext.init(null, new TrustManager[] { tm }, null);
			}

			connectionSocketFactory.register("https", new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE));
			connectionSocketFactory.register("http", PlainConnectionSocketFactory.getSocketFactory());

			poolConnManager = new PoolingHttpClientConnectionManager(connectionSocketFactory.build());
			poolConnManager.setMaxTotal(maxTotalPool);
			poolConnManager.setDefaultMaxPerRoute(maxConPerRoute);

			SocketConfig socketConfig = SocketConfig.custom().setSoTimeout(socketTimeout).build();
			poolConnManager.setDefaultSocketConfig(socketConfig);

			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					try {
						poolConnManager.closeExpiredConnections();
						poolConnManager.closeIdleConnections(20, TimeUnit.SECONDS);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}, 0, 5000);

		} catch (Exception e) {
			logger.error("http连接池初始化失败", e);
		} finally {
			if (null != fileInputStream) {
				fileInputStream.close();
			}
		}

	}

	public CloseableHttpClient getConnection(String certPasswd,  byte[] bytes) {
		CloseableHttpClient httpclient = null;
		try {
			// 读取二进制数据并转换成对象
			InputStream bis = new ByteArrayInputStream(bytes);

			KeyStore keyStore = KeyStore.getInstance("PKCS12");

			keyStore.load(bis, certPasswd.toCharArray());// 设置证书密码

			SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, certPasswd.toCharArray()).build();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);

			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (Exception e) {
			logger.error("http连接池初始化失败", e);
		}

		return httpclient;
	}

	public CloseableHttpClient getConnection() {
		return getConnection(defRequestConfig);
	}

	public CloseableHttpClient getConnection(RequestConfig requestConfig) {
		if (null == requestConfig) {
			requestConfig = defRequestConfig;
		}

		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolConnManager)
				.setDefaultRequestConfig(requestConfig).build();

		return httpClient;
	}

	public HttpPost getPostMethod(String url) {
		HttpPost pmethod = new HttpPost(url); // 设置响应头信息
		pmethod.addHeader("Connection", "keep-alive");
		pmethod.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

		return pmethod;
	}

	public HttpPut getHttpPut(String url){
		HttpPut pmethod = new HttpPut(url);

		// 设置响应头信息
		pmethod.addHeader("Connection", "keep-alive");

		return pmethod;
	}

	public HttpDelete getHttpDelete(String url){
		HttpDelete pmethod = new HttpDelete(url);

		// 设置响应头信息
		pmethod.addHeader("Connection", "keep-alive");

		return pmethod;
	}

	public HttpGet getGetMethod(String url) {
		HttpGet pmethod = new HttpGet(url);

		// 设置响应头信息
		pmethod.addHeader("Connection", "keep-alive");

		return pmethod;
	}

	public static void main(String[] args) throws HttpException, IOException, URISyntaxException {

		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=wx1a1a7760d96d2568&code=001aOUL30sd6AC17PML30tRdM30aOULz&grant_type=authorization_code&secret=f984bdab1f0ede9556fb2305fe6c603a";

		HttpConnectionManager httpClientManager = new HttpConnectionManager();

		Map<String, String> params = new HashMap();

		String result = null;
		String queryUrl = null;

		try {
			// 拼接参数
			String paramStr = null;
			for (String key : params.keySet()) {
				if(null == paramStr){
					paramStr = key + "=" + params.get(key);
				}else{
					paramStr = paramStr + "&" + key + "=" + params.get(key);
				}

			}

			queryUrl = url + "?" + paramStr;

			HttpGet httpGet = httpClientManager.getGetMethod(queryUrl);
			HttpResponse response = httpClientManager.getConnection().execute(httpGet);
			if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
				logger.error("http请求失败    url:" + queryUrl  + " statusCode=" + response.getStatusLine().getStatusCode());
//				throw new BusinessException();
			}

			result = EntityUtils.toString(response.getEntity());

			System.out.println("微信openId获取结果  result:" + result);
		} catch (IOException e) {

			logger.error("http请求失败    url:" + queryUrl, e);

//			throw new BusinessException();
		}

	}

}
