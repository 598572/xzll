package com.xzll.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetUtils {
	private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);

    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");

    private static final Pattern IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");

    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");

    private static Properties outAddrPro;

	public static String getMachineHostName(){
		String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {

		}

		return hostname;
	}
	public static void main(String[] args) {
		System.out.println(getMachineHostName());
	}
	public static String getMachineIpAddr(){
		String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {

		}

		return hostname;
	}


	/**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * 参考文章： http://developer.51cto.com/art/201111/305181.htm
     *
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     *
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     *
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }


	public static boolean checkIfInnerIp(String ip) {
		//匹配127.0.0.1
		// 127\\.0\\.0\\.1

		//匹配10.0.0.0 - 10.255.255.255的网段
        //^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3}

        //匹配172.16.0.0 - 172.31.255.255的网段
        //172\\.([1][6-9]|[2]\\d|3[01])(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}

        //匹配192.168.0.0 - 192.168.255.255的网段
        //192\\.168(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}

        String pattern = "((127\\.0\\.0\\.1)|(192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))"
                + "(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|"
                + "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})";

        Pattern reg = Pattern.compile(pattern);
        Matcher match = reg.matcher(ip);

		return match.find();
	}



    public static boolean isIPv4Address(final String input) {
        return IPV4_PATTERN.matcher(input).matches();
    }

    private static boolean isIPv6StdAddress(final String input) {
        return IPV6_STD_PATTERN.matcher(input).matches();
    }

    private static boolean isIPv6HexCompressedAddress(final String input) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(input).matches();
    }

    public static boolean isIPv6Address(final String input) {
        return isIPv6StdAddress(input) || isIPv6HexCompressedAddress(input);
    }

    public static String getOneInnerIp(){

    	try{
    		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
    		while (netInterfaces.hasMoreElements()) {
    			NetworkInterface ni = netInterfaces.nextElement();
    			Enumeration<InetAddress> addresses = ni.getInetAddresses();

    			while (addresses.hasMoreElements()) {
    				InetAddress inetAddress = addresses.nextElement();

    				// 如果是环路地址
    				if (inetAddress.isLoopbackAddress()) {
    					continue;
    				}

    				String ip = inetAddress.getHostAddress();

    				if (NetUtils.isIPv4Address(ip)) {
    					if (NetUtils.checkIfInnerIp(ip)) {
    						return ip;
    					}
    				}
    			}
    		}
    	}catch(Exception e){

    	}

		return null;
    }

    /**
     * 获取外网ip
     * @return
     */
    public static String getOutIpAddr(){
    	String outAddr = null;


    	return outAddr;
    }
}
