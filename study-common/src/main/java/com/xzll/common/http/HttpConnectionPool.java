package com.xzll.common.http;


import cn.hutool.json.JSONUtil;
import com.xzll.common.base.XzllBaseException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;


/**
 * @Author: hzz
 * @Date: 2023/2/28 10:39:52
 * @Description: http 连接池,池化思想，提高性能；
 * 注意： 因为使用了keep-alive因子，可能有的第三方服务不支持
 */
public class HttpConnectionPool {


	private volatile boolean isInit = false;

	private String poolName;

	private final static Logger logger = LoggerFactory.getLogger(HttpConnectionPool.class);

	public static final int MAX_TOTAL_POOL = 10000;
	public static final int MAX_CON_PER_ROUTE = 6000;
	public static final int SOCKET_TIMEOUT = 6000;
	public static final int CONNECTION_REQUEST_TIMEOUT = 1500;
	public static final int CONNECT_TIMEOUT = 1500;

	private int maxTotalPool = MAX_TOTAL_POOL;
	private int maxConPerRoute = MAX_CON_PER_ROUTE;
	private int socketTimeout = SOCKET_TIMEOUT;
	private int connectionRequestTimeout = CONNECTION_REQUEST_TIMEOUT;
	private int connectTimeout = CONNECT_TIMEOUT;

	private RequestConfig defRequestConfig;

	HttpConnectionPool(String poolName){
		this.poolName = poolName;
	}


	private PoolingHttpClientConnectionManager poolConnManager;

	public void init() {
		if(true == isInit){
			return;
		}

		try {
			init(null, (String) null);
		} catch (Exception e) {
			logger.error("初始化http连接池异常, poolName={}", poolName, e);
		}
	}

	public void init(int maxTotalPool, int maxConPerRoute, int socketTimeout, int connectionRequestTimeout, int connectTimeout) {
		if(true == isInit){
			return;
		}

		this.maxTotalPool = maxTotalPool;
		this.maxConPerRoute = maxConPerRoute;
		this.socketTimeout = socketTimeout;
		this.connectionRequestTimeout = connectionRequestTimeout;
		this.connectTimeout = connectTimeout;

		try {
			init(null, (String) null);
		} catch (Exception e) {
			logger.error("初始化http连接池异常, poolName={}", poolName, e);
		}
	}

	public void init(String certPasswd, String certPathWithFileName) throws IOException {
		if(true == isInit){
			return;
		}
		try {
			if( StringUtils.isEmpty(certPasswd) || StringUtils.isEmpty(certPathWithFileName) ){
				initCommon(null, null);
			}else{
				FileInputStream fileInputStream = null;
				if(null != certPathWithFileName){
					fileInputStream = new FileInputStream(new File(certPathWithFileName));// 加载本地的证书进行https加密传输
				}
				initCommon(certPasswd, fileInputStream);
			}
		} catch (Exception e) {
			logger.error("初始化http连接池异常, poolName={}", poolName, e);
		}


	}

	public void init(String certPasswd, byte[] bytes) {
		if(true == isInit){
			return;
		}

		try {
			if(null == bytes || bytes.length <= 0 || StringUtils.isEmpty(certPasswd)){
				initCommon(null, null);
			}else{
				// 读取二进制数据并转换成对象
				InputStream bis = new ByteArrayInputStream(bytes);
				initCommon(certPasswd,bis);
			}

		} catch (Exception e) {
			logger.error("初始化http连接池异常, poolName={}", poolName, e);
		}

	}


	public synchronized void initCommon(String certPasswd, InputStream fileInputStream) throws IOException {

		if(true == isInit){
			return;
		}

		defRequestConfig = RequestConfig.custom() //
				.setConnectionRequestTimeout(connectionRequestTimeout) //
				.setConnectTimeout(connectTimeout) //
				.setSocketTimeout(socketTimeout) //
				.build();

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
			logger.error("初始化http连接池异常, poolName={}", poolName, e);
		} finally {
			if (null != fileInputStream) {
				fileInputStream.close();
			}
		}

		isInit = true;
	}


	/**
	 *
	 * @param url 请求的路径，路径已经拼接了参数
	 * @param responsecharset 响应内容的编码，默认为utf-8
	 * @return
	 * @throws Exception
	 */
	public String callByGet(String url, String responsecharset) throws Exception{
		checkInit();

		if(null == responsecharset){
			responsecharset = "utf-8";
		}

		HttpGet httpGet = getHttpGet(url);

		HttpResponse response = getConnection().execute(httpGet);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);

			throw new XzllBaseException(error);
		}

		String result = EntityUtils.toString(response.getEntity(), responsecharset);

		return result;
	}

	/**
	 *
	 * @param url 请求的路径
	 * @param params 请求的参数
	 * @param responsecharset 响应内容的编码，默认为utf-8
	 * @return
	 * @throws Exception
	 */
	public String callByGet(String url, Map<String, String> params , String responsecharset) throws Exception{
		return callByGet(url, params, null, responsecharset);
	}

	/**
	 *
	 * @param url 请求的路径
	 * @param params 请求的参数
	 * @param header 请求头
	 * @param responsecharset 响应内容的编码，默认为utf-8
	 * @return
	 * @throws Exception
	 */
	public String callByGet(String url, Map<String, String> params , Map<String, String> header, String responsecharset) throws Exception{
		checkInit();

		if(null == responsecharset){
			responsecharset = "utf-8";
		}

		// 拼接参数
		String paramStr = null;
		for (String key : params.keySet()) {
			if(null == paramStr){
				paramStr = key + "=" + params.get(key);
			}else{
				paramStr = paramStr + "&" + key + "=" + params.get(key);
			}

		}

		String queryUrl = url + "?" + paramStr;

		HttpGet httpGet = getHttpGet(queryUrl);

		addHeader(httpGet, header);
		HttpResponse response = getConnection().execute(httpGet);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);

			throw new XzllBaseException(error);
		}

		String result = EntityUtils.toString(response.getEntity(), responsecharset);

		return result;
	}

	/**
	 *
	 * @param url 请求的路径
	 * @param data 请求的参数
	 * @param responseCharset 响应内容的编码，默认为utf-8
	 * @return
	 * @throws Exception
	 */
	public String callByGet(String url, String data , String responseCharset) throws Exception{
		checkInit();

		if(null == responseCharset){
			responseCharset = "utf-8";
		}

		String queryUrl = url + "?" + data;

		HttpGet httpGet = getHttpGet(queryUrl);


		HttpResponse response = getConnection().execute(httpGet);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);

			throw new XzllBaseException(error);
		}

		String result = EntityUtils.toString(response.getEntity(), responseCharset);

		return result;
	}

	/**
	 *
	 * @param url 请求的路径
	 * @param data 请求的参数
	 * @param type 默认为json，可以指定请求的数据格式
	 * @param requestCharset 请求数据的内容编码
	 * @param responseCharset 响应内容的编码
	 * @return
	 * @throws Exception
	 */
	public String callByPost(String url, String data, HttpRequestContentType type, String requestCharset, String responseCharset) throws IOException {
		String statusCode = "200";
		try {
			checkInit();

			if (null == responseCharset) {
				responseCharset = "utf-8";
			}

			if (null == requestCharset) {
				requestCharset = "utf-8";
			}

			if (null == type) {
				type = HttpRequestContentType.JSON;
			}

			HttpPost httpPost = getHttpPost(url);


			httpPost.setEntity(new StringEntity(data, requestCharset));

			addContentType(httpPost, type, requestCharset);

			HttpResponse response = getConnection().execute(httpPost);
			statusCode = String.valueOf(response.getStatusLine().getStatusCode());
			if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
				String error = "http请求失败    url:" + url + " statusCode=" + response.getStatusLine().getStatusCode();
				logger.error(error);

				throw new XzllBaseException(error);
			}

			String result = EntityUtils.toString(response.getEntity(), responseCharset);

			return result;
		} catch (ClientProtocolException e) {
			statusCode = "503";
			throw e;
		} catch (IOException e) {
			statusCode = "503";
			throw e;
		} finally {
			URI uri = URI.create(url);
		}
	}




	/**
	 *
	 * @param url 请求的路径
	 * @param data 请求的参数
	 * @param header 请求头
	 * @param type 默认为json，可以指定请求的数据格式
	 * @param requestCharset 请求数据的内容编码
	 * @param responseCharset 响应内容的编码
	 * @return
	 * @throws Exception
	 */
	public String callByPost(String url, String data, Map<String, String> header, HttpRequestContentType type, String requestCharset, String responseCharset) throws Exception{
		checkInit();

		if(null == responseCharset){
			responseCharset = "utf-8";
		}

		if(null == requestCharset){
			requestCharset = "utf-8";
		}

		if(null == type){
			type = HttpRequestContentType.JSON;
		}

		HttpPost httpPost = getHttpPost(url);

		httpPost.setEntity(new StringEntity(data, requestCharset));

		addContentType(httpPost, type, requestCharset);

		addHeader(httpPost, header);

		HttpResponse response = getConnection().execute(httpPost);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);

			throw new XzllBaseException(error);
		}

		String result = EntityUtils.toString(response.getEntity(), responseCharset);

		return result;
	}

	/**
	 *
	 * @param url 请求路径
	 * @param params 参数
	 * @param file 文件
	 * @param requestCharset 请求数据的内容编码
	 * @param responseCharset 响应内容的编码
	 * @return
	 * @throws Exception
	 */
	public String callByPostFile(String url, Map<String,String> params, File file, String requestCharset, String responseCharset) throws Exception{
		checkInit();
		if(null == responseCharset){
			responseCharset = "utf-8";
		}
		if(null == requestCharset){
			requestCharset = "utf-8";
		}
		HttpPost post = new HttpPost(url);

		//构造待上传数据,加入builder
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.RFC6532);
		builder.setCharset(Charset.forName(requestCharset));
		if(file != null ){
			builder.addPart("file", new FileBody(file, ContentType.DEFAULT_BINARY));
		}

		if(params != null ){
			for(Map.Entry<String,String> param:params.entrySet()){
				builder.addPart(param.getKey(),new StringBody(param.getValue(),ContentType.create(HttpRequestContentType.MULTIPART.getDesc(), Charset.forName(requestCharset))));
			}
		}

		HttpEntity entity = builder.build();
		post.setEntity(entity);
		HttpResponse response = getConnection().execute(post);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);
			throw new XzllBaseException(error);
		}
		String result = EntityUtils.toString(response.getEntity(), responseCharset);
		return result;
	}

	public String callByPostFile(String url, Map<String,String> params, byte[] fileContent, String fileName, String requestCharset, String responseCharset) throws Exception{
		checkInit();
		if(null == responseCharset){
			responseCharset = "utf-8";
		}
		if(null == requestCharset){
			requestCharset = "utf-8";
		}
		HttpPost post = new HttpPost(url);

		//构造待上传数据,加入builder
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.RFC6532);
		builder.setCharset(Charset.forName(requestCharset));
		if(fileContent != null ){
			builder.addBinaryBody("file", fileContent, ContentType.APPLICATION_OCTET_STREAM, fileName);
		}

		if(params != null ){
			for(Map.Entry<String,String> param:params.entrySet()){
				builder.addPart(param.getKey(),new StringBody(param.getValue(),ContentType.create(HttpRequestContentType.MULTIPART.getDesc(), Charset.forName(requestCharset))));
			}
		}

		HttpEntity entity = builder.build();
		post.setEntity(entity);
		HttpResponse response = getConnection().execute(post);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);
			throw new XzllBaseException(error);
		}
		String result = EntityUtils.toString(response.getEntity(), responseCharset);
		return result;
	}

	/**
	 *
	 * @param url 请求的路径
	 * @param data 请求的参数
	 * @param type 默认为json，可以指定请求的数据格式
	 * @param requestCharset 请求数据的内容编码
	 * @param responseCharset 响应内容的编码
	 * @return
	 * @throws Exception
	 */
	public String callByPostObject(String url, Map<String, Object> data, HttpRequestContentType type, String requestCharset, String responseCharset) throws Exception{
		return callByPostObject(url, data, null, type, requestCharset, responseCharset);
	}

	/**
	 *
	 * @param url 请求的路径
	 * @param data 请求的参数
	 * @param header 请求头
	 * @param type 默认为json，可以指定请求的数据格式
	 * @param requestCharset 请求数据的内容编码
	 * @param responseCharset 响应内容的编码
	 * @return
	 * @throws Exception
	 */
	public String callByPostObject(String url, Map<String, Object> data, Map<String, String> header, HttpRequestContentType type, String requestCharset, String responseCharset) throws Exception{
		if(null == data){
			return null;
		}

		if( data.getClass().isAssignableFrom(Object.class) ){
			return null;
		}

		checkInit();

		if(null == responseCharset){
			responseCharset = "utf-8";
		}

		if(null == requestCharset){
			requestCharset = "utf-8";
		}

		if(null == type){
			type = HttpRequestContentType.JSON;
		}

		String dataStr = null;
		if( HttpRequestContentType.JSON == type ){
			dataStr = JSONUtil.toJsonStr(data);
		}else if( HttpRequestContentType.FORM == type ){
			dataStr = "";
			for(String key : data.keySet()){
				dataStr += key + "=" + data.get(key) + "&";
			}

			dataStr = dataStr.substring(0, dataStr.length() - 1);
		}

		HttpPost httpPost = getHttpPost(url);

		httpPost.setEntity(new StringEntity(dataStr, requestCharset));

		addContentType(httpPost, type, requestCharset);

		addHeader(httpPost, header);

		HttpResponse response = getConnection().execute(httpPost);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);

			throw new XzllBaseException(error);
		}

		String result = EntityUtils.toString(response.getEntity(), responseCharset);

		return result;
	}

	/**
	 *
	 * @param url 请求的路径
	 * @param data 请求的参数
	 * @param type 默认为json，可以指定请求的数据格式
	 * @param requestCharset 请求数据的内容编码
	 * @param responseCharset 响应内容的编码
	 * @return
	 * @throws Exception
	 */
	public String callByPost(String url, Map<String, String> data, HttpRequestContentType type, String requestCharset, String responseCharset) throws Exception{
		if(null == data){
			return null;
		}

		if( data.getClass().isAssignableFrom(Object.class) ){
			return null;
		}

		checkInit();

		if(null == responseCharset){
			responseCharset = "utf-8";
		}

		if(null == requestCharset){
			requestCharset = "utf-8";
		}

		if(null == type){
			type = HttpRequestContentType.JSON;
		}

		String dataStr = null;
		if( HttpRequestContentType.JSON == type ){
			dataStr = JSONUtil.toJsonStr(data);
		}else if( HttpRequestContentType.FORM == type ){
			dataStr = "";
			for(String key : data.keySet()){
				dataStr += key + "=" + data.get(key) + "&";
			}

			dataStr = dataStr.substring(0, dataStr.length() - 1);
		}

		HttpPost httpPost = getHttpPost(url);

		httpPost.setEntity(new StringEntity(dataStr, requestCharset));

		addContentType(httpPost, type, requestCharset);

		HttpResponse response = getConnection().execute(httpPost);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);

			throw new XzllBaseException(error);
		}

		String result = EntityUtils.toString(response.getEntity(), responseCharset);

		return result;
	}


	/**
	 *
	 * @param url 请求的路径
	 * @param data 请求的参数
	 * @param type 默认为json，可以指定请求的数据格式
	 * @param requestCharset 请求数据的内容编码
	 * @param responseCharset 响应内容的编码
	 * @return
	 * @throws Exception
	 */
	public String callByPost(String url, Map<String, String> data, Map<String, String> header, HttpRequestContentType type, String requestCharset, String responseCharset) throws Exception{
		if(null == data){
			return null;
		}

		if( data.getClass().isAssignableFrom(Object.class) ){
			return null;
		}

		checkInit();

		if(null == responseCharset){
			responseCharset = "utf-8";
		}

		if(null == requestCharset){
			requestCharset = "utf-8";
		}

		if(null == type){
			type = HttpRequestContentType.JSON;
		}

		String dataStr = null;
		if( HttpRequestContentType.JSON == type ){
			dataStr = JSONUtil.toJsonStr(data);
		}else if( HttpRequestContentType.FORM == type ){
			dataStr = "";
			for(String key : data.keySet()){
				dataStr += key + "&" + data.get(key);
			}

			dataStr = dataStr.substring(0, dataStr.length() - 1);
		}

		HttpPost httpPost = getHttpPost(url);

		httpPost.setEntity(new StringEntity(dataStr, requestCharset));

		addContentType(httpPost, type, requestCharset);

		addHeader(httpPost, header);

		HttpResponse response = getConnection().execute(httpPost);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);

			throw new XzllBaseException(error);
		}

		String result = EntityUtils.toString(response.getEntity(), responseCharset);

		return result;
	}

	/**
	 *
	 * @param url 请求的路径
	 * @param type 默认为json，可以指定请求的数据格式
	 * @param requestCharset 请求数据的内容编码
	 * @param responseCharset 响应内容的编码
	 * @return
	 * @throws Exception
	 */
	public String callByDelete(String url, Map<String, String> header, HttpRequestContentType type, String requestCharset, String responseCharset) throws Exception{

		checkInit();

		if(null == responseCharset){
			responseCharset = "utf-8";
		}

		if(null == requestCharset){
			requestCharset = "utf-8";
		}

		if(null == type){
			type = HttpRequestContentType.JSON;
		}

		HttpDelete httpDelete = getHttpDelete(url);

		addContentType(httpDelete, type, requestCharset);

		addHeader(httpDelete, header);

		HttpResponse response = getConnection().execute(httpDelete);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败    url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);

			throw new XzllBaseException(error);
		}

		String result = EntityUtils.toString(response.getEntity(), responseCharset);

		return result;
	}

	public String callByPut(String url, Map<String, String> header, String dataStr, HttpRequestContentType type, String requestCharset, String responseCharset) throws Exception{
		checkInit();

		if(null == responseCharset){
			responseCharset = "utf-8";
		}

		if(null == requestCharset){
			requestCharset = "utf-8";
		}

		if(null == type){
			type = HttpRequestContentType.JSON;
		}

		HttpPut httpPut = getHttpPut(url);

		addContentType(httpPut, type, requestCharset);

		addHeader(httpPut, header);

		httpPut.setEntity(new StringEntity(dataStr, requestCharset));

		HttpResponse response = getConnection().execute(httpPut);
		if (HttpStatus.SC_OK != response.getStatusLine().getStatusCode()) {
			String error = "http请求失败,url:" + url  + " statusCode=" + response.getStatusLine().getStatusCode();
			logger.error(error);
			throw new XzllBaseException(error);
		}

		String result = EntityUtils.toString(response.getEntity(), responseCharset);

		return result;
	}

	private void checkInit(){
		if(false == isInit){
			throw new RuntimeException("connectionPool还没有调用init方法");
		}
	}

	@Deprecated
	private CloseableHttpClient getConnection(String certPasswd,  byte[] bytes) {
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

	private CloseableHttpClient getConnection() {
		return getConnection(defRequestConfig);
	}

	private CloseableHttpClient getConnection(RequestConfig requestConfig) {
		if (null == requestConfig) {
			requestConfig = defRequestConfig;
		}
		//为http工具 添加拦截器和默认的请求参数
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(poolConnManager)
				.addInterceptorFirst(new HttpClientTraceIdInterceptor())
				.setDefaultRequestConfig(requestConfig).build();

		return httpClient;
	}

	private HttpPost getHttpPost(String url) {
		HttpPost pmethod = new HttpPost(url); // 设置响应头信息

		// 设置响应头信息
		addCommonHeader(pmethod);

		return pmethod;
	}

	private HttpPut getHttpPut(String url){
		HttpPut pmethod = new HttpPut(url);

		// 设置响应头信息
		addCommonHeader(pmethod);

		return pmethod;
	}

	private HttpDelete getHttpDelete(String url){
		HttpDelete pmethod = new HttpDelete(url);

		// 设置响应头信息
		addCommonHeader(pmethod);

		return pmethod;
	}

	private HttpGet getHttpGet(String url) {
		HttpGet pmethod = new HttpGet(url);

		// 设置响应头信息
		addCommonHeader(pmethod);

		return pmethod;
	}

	private void addContentType(HttpRequestBase method, HttpRequestContentType type, String requestCharset){
		method.addHeader("Content-Type", type.getDesc() + ";charset=" + requestCharset);
	}

	private void addCommonHeader(HttpRequestBase method){
		method.addHeader("Connection", "keep-alive");
		method.addHeader("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");//模拟某种浏览器，免得被特殊网关拦截
	}

	private void addHeader(HttpRequestBase method, Map<String, String> header){
		if(null == header){
			return;
		}

		for(String key : header.keySet()){
			String value = header.get(key);

			method.addHeader(key, value);
		}
	}

	public static void main(String[] args) throws Exception {

		String url = "https://baidu.com";

		HttpConnectionPool httpClientManager = new HttpConnectionPool("test");

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

			result = httpClientManager.callByGet(queryUrl, "utf-8");

			System.out.println("微信openId获取结果  result:" + result);
		} catch (IOException e) {

			logger.error("http请求失败    url:" + queryUrl, e);

			throw new XzllBaseException("http请求失败    url:"+queryUrl+",exception: "+e.getMessage());
		}

	}

}
