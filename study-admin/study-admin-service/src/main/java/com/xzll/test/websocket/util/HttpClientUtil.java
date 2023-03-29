package com.xzll.test.websocket.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: hzz
 * @Date: 2021/11/22 10:45:09
 * @Description:
 */
public class HttpClientUtil {


	private Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private class MyX509TrustManager implements X509TrustManager {

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// TODO Auto-generated method stub

		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// TODO Auto-generated method stub

		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}



	}

	public String postRequest(String url,String params) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {

			// 创建SSLContext对象，并使用我们指定的信任管理器初始化     
			TrustManager[] tm = {new MyX509TrustManager()};
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			// 打开和URL之间的连接
			URL realUrl = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
			conn.setSSLSocketFactory(ssf);


			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("content-Type", "application/json");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());

			// 发送请求参数
			out.print(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader( new InputStreamReader(conn.getInputStream()));

			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}

		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();

		}

		//使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return result;

	}

	/**
	 * [HTTPS]
	 * @return
	 */
	public String postForm (String url,String params) throws Exception {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {

			// 创建SSLContext对象，并使用我们指定的信任管理器初始化     
			TrustManager[] tm = {new MyX509TrustManager()};
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, tm, new java.security.SecureRandom());

			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			// 打开和URL之间的连接
			URL realUrl = new URL(url);
			HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
			conn.setSSLSocketFactory(ssf);

			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);

			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());

			// 发送请求参数
			out.print(params);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader( new InputStreamReader(conn.getInputStream()));

			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}

		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			e.printStackTrace();

		}

		//使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return result;

	}

	/**
	 * 发送HttpPost请求，参数为map
	 *
	 * @param url 请求地址
	 * @param map 请求参数
	 * @return 返回字符串
	 */
	public String sendPost(String url, Map<String, String> map) {
		// 设置参数
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		for (String key : map.keySet()) {
			formparams.add(new BasicNameValuePair(key, map.get(key)));
		}
		// 取得HttpPost对象
		HttpPost httpPost = new HttpPost(url);
		// 防止被当成攻击添加的
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36");
		// 参数放入Entity
		httpPost.setEntity(new UrlEncodedFormEntity(formparams, Consts.UTF_8));
		CloseableHttpResponse response = null;
		String result = null;
		try {
			CloseableHttpClient HTTPCLIENT = HttpClients.createDefault();
			// 执行post请求
			response = HTTPCLIENT.execute(httpPost);
			// 得到entity
			HttpEntity entity = response.getEntity();
			// 得到字符串
			result = EntityUtils.toString(entity);
		} catch (IOException e) {

		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {

				}
			}
		}
		return result;
	}


	public class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	public String getRequest(String url, Map<String, Object> params) {

		// 拼接参数
		String paramStr = "";
		for (String key : params.keySet()) {
			String keyParam = "" + params.get(key);

			if (keyParam.contains("+")) {
				keyParam = keyParam.replace("+", "%2B");
			}

			if (keyParam.contains(" ")) {
				keyParam = keyParam.replace(" ", "");
			}
			paramStr = paramStr + "&" + key + "=" + keyParam;
		}
		paramStr = paramStr.substring(1);
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + paramStr;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			Map<String, List<String>> map = connection.getHeaderFields();
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			logger.error("发送GET请求出现异常！url" + url,  e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}


	/**
	 * 发送网络get请求
	 *
	 * @param url
	 * @return
	 */
	public String getNetRequest(String url, Map<String, String> params) {
		// 构造HttpClient实例
		HttpClient client = new HttpClient();
		String paramStr = "";
		for (String key : params.keySet()) {
			paramStr = paramStr + "&" + key + "=" + params.get(key);
		}
		paramStr = paramStr.substring(1);
		System.err.println(url + "?" + paramStr);
		// 创建GET方法的实例
		GetMethod method = new GetMethod(url + "?" + paramStr);
		// 接收返回结果
		String result = null;
		try {
			// 执行HTTP GET方法请求
			client.executeMethod(method);
			// 返回处理结果
			result = method.getResponseBodyAsString();
		} catch (HttpException e) {
			// 发生致命的异常，可能是协议不对或者返回的内容有问题
			logger.error("请检查输入的URL! sendData:"+paramStr,e);
		} catch (IOException e) {
			// 发生网络异常
			logger.error("发生网络异常! sendData:"+paramStr,e);
		} finally {
			// 释放链接
			method.releaseConnection();
			// 关闭HttpClient实例
			if (client != null) {
				((SimpleHttpConnectionManager) client.getHttpConnectionManager()).shutdown();
				client = null;
			}
		}
		return result;
	}

	/**
	 * 发送网络post请求
	 * @param url
	 * @param jsonObject
	 * @return
	 */
	public String postNetRequest(String url , JSONObject jsonObject){

		List<String> ketSet = new ArrayList<String>(jsonObject.keySet());
		PostMethod postMethod = new PostMethod(url);
		postMethod.getParams().setParameter( HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
		for(String key:ketSet){
			postMethod.addParameter(key, jsonObject.getString(key));
		}

		// 接收返回结果
		String result = null;
		HttpClient httpclient = new HttpClient();
		try {
			int httpcode = httpclient.executeMethod(postMethod);
			result = postMethod.getResponseBodyAsString();
		}catch (HttpException e) {
			logger.error("请检查输入的URL! sendData:"+jsonObject,e);
		} catch (IOException e) {
			// 发生网络异常
			logger.error("发生网络异常! sendData:"+jsonObject,e);
		} finally {
			// 释放链接
			postMethod.releaseConnection();
			// 关闭HttpClient实例
			if (httpclient != null) {
				((SimpleHttpConnectionManager) httpclient.getHttpConnectionManager()).shutdown();
				httpclient = null;
			}
		}


		return result;
	}



	/**
	 * jsonp跨域请求数据响应<br/>
	 * 方法名：responsejsonpData<br/>
	 *
	 * @param request
	 * @param response
	 * @param map      void<br/>
	 * @throws <br/>
	 * @author：Mryang<br/>
	 * @createTime：2016年7月31日-下午11:17:31 <br/>
	 * @tel: 15198268054<br                                                                                                                                                                                                                                                               />
	 * @since 1.0.0
	 */
	public void responsejsonpData(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-Type", "text/html;Charset=utf-8");
		try {
			PrintWriter writer = response.getWriter();
			String params = request.getParameter("callback");
			String json = JSONObject.toJSONString(map);
			writer.print(params + "(" + json + ")");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
