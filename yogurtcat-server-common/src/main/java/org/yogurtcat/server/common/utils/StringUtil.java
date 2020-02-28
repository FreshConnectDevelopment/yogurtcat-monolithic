package org.yogurtcat.server.common.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.yogurtcat.server.common.utils.lang.TextSimilarity;

/**
 * 字符串工具类 用作apache common lang3包StringUtils的补充
 * 
 * @author s
 *
 */
public class StringUtil {

	private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");
	private static final String UNKNOWN = "unknown";

	/**
	 * 计算两个字符串的相似度
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return 相似度
	 */
	public static double similar(String str1, String str2) {
		return TextSimilarity.similar(str1, str2);
	}

	/**
	 * 计算连个字符串的相似度百分比
	 * 
	 * @param str1  字符串1
	 * @param str2  字符串2
	 * @param scale
	 * @return 相似度百分比
	 */
	public static String similar(String str1, String str2, int scale) {
		return TextSimilarity.similar(str1, str2, scale);
	}

	/**
	 * 
	 * @param logMessage
	 * @param obj
	 * @return
	 * @throws IntrospectionException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public static String translate(String logMessage, Object obj)
			throws IntrospectionException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Matcher matcher = PATTERN.matcher(logMessage);
		while (matcher.find()) {
			String key = matcher.group();
			String keyclone = key.substring(1, key.length() - 1).trim();
			// obj是基本类型或基本类型的封装类型    代表仅此一个参数
			if (isType(obj)) {
				logMessage = logMessage.replace(key, obj.toString());
				return logMessage;
			}
			PropertyDescriptor pd = new PropertyDescriptor(keyclone, obj.getClass());
			if (pd != null) {
				Method getMethod = pd.getReadMethod();
				if (getMethod != null) {
					Object value = getMethod.invoke(obj);
					if (value != null) {
						logMessage = logMessage.replace(key, value.toString());
					}
				}
			}
		}
		return logMessage;
	}

	/**
	 * 获取ip地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	/**判断一个对象是否是基本类型或基本类型的封装类型
	 * @param obj
	 * @return boolean
	 * */
	private static boolean isType(Object obj) {
		try {
			if ( (obj instanceof String) || ((Class<?>)obj.getClass().getField("TYPE").get(null)).isPrimitive()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

}
