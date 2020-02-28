package org.yogurtcat.server.common.utils.lang;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * 文本相似度计算<br>
 * 工具类参考：hutool
 * 
 * @author s
 **/
public class TextSimilarity {

	/**
	 * 计算相似度
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @return 相似度
	 */
	public static double similar(String str1, String str2) {
		String newStr1, newStr2;
		if (str1.length() < str2.length()) {
			newStr1 = removeSign(str2);
			newStr2 = removeSign(str1);
		} else {
			newStr1 = removeSign(str1);
			newStr2 = removeSign(str2);
		}
		// 用较大的字符串长度作为分母，相似子串作为分子计算出字串相似度
		int temp = Math.max(newStr1.length(), newStr2.length());
		int temp2 = longestCommonSubstring(newStr1, newStr2).length();
		return div(new BigDecimal(temp2), new BigDecimal(temp), 10, RoundingMode.HALF_UP).doubleValue();
	}

	/**
	 * 计算相似度百分比
	 * 
	 * @param str1 字符串1
	 * @param str2 字符串2
	 * @param scale 保留小数
	 * @return 百分比
	 */
	public static String similar(String str1, String str2, int scale) {
		return formatPercent(similar(str1, str2), scale);
	}


	// --------------------------------------------------------------------------------------------------- Private method start
	
	/**
	 * 提供(相对)精确的除法运算,当发生除不尽的情况时,由scale指定精确度
	 * 
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 精确度，如果为负值，取绝对值
	 * @param roundingMode 保留小数的模式 {@link RoundingMode}
	 * @return 两个参数的商
	 */
	public static BigDecimal div(BigDecimal v1, BigDecimal v2, int scale, RoundingMode roundingMode) {
		if (null == v1) {
			return BigDecimal.ZERO;
		}
		if (scale < 0) {
			scale = -scale;
		}
		return v1.divide(v2, scale, roundingMode);
	}
	
	/**
	 * 格式化百分比，小数采用四舍五入方式
	 * 
	 * @param number 值
	 * @param scale 保留小数位数
	 * @return 百分比
	 */
	public static String formatPercent(double number, int scale) {
		final NumberFormat format = NumberFormat.getPercentInstance();
		format.setMaximumFractionDigits(scale);
		return format.format(number);
	}
	
	
	/**
	 * 将字符串的所有数据依次写成一行，去除无意义字符串
	 * 
	 * @param str 字符串
	 * @return 处理后的字符串
	 */
	private static String removeSign(String str) {
		StringBuilder sb = new StringBuilder(str.length());
		// 遍历字符串str,如果是汉字数字或字母，则追加到ab上面
		int length = str.length();
		for (int i = 0; i < length; i++) {
			sb.append(charReg(str.charAt(i)));
		}
		return sb.toString();
	}

	/**
	 * 判断字符是否为汉字，数字和字母， 因为对符号进行相似度比较没有实际意义，故符号不加入考虑范围。
	 * 
	 * @param charValue 字符
	 * @return 是否为汉字，数字和字母
	 */
	private static boolean charReg(char charValue) {
		return (charValue >= 0x4E00 && charValue <= 0XFFF) || 
				(charValue >= 'a' && charValue <= 'z') || 
				(charValue >= 'A' && charValue <= 'Z') || 
				(charValue >= '0' && charValue <= '9');
	}

	/**
	 * 求公共子串，采用动态规划算法。 其不要求所求得的字符在所给的字符串中是连续的。
	 * 
	 * @param strA 字符串1
	 * @param strB 字符串2
	 * @return 公共子串
	 */
	private static String longestCommonSubstring(String str1, String str2) {
		char[] charsStr1 = str1.toCharArray();
		char[] charsStr2 = str2.toCharArray();
		int m = charsStr1.length;
		int n = charsStr2.length;

		// 初始化矩阵数据,matrix[0][0]的值为0， 如果字符数组chars_strA和chars_strB的对应位相同，则matrix[i][j]的值为左上角的值加1， 否则，matrix[i][j]的值等于左上方最近两个位置的较大值， 矩阵中其余各点的值为0.
		int[][] matrix = new int[m + 1][n + 1];
		for (int i = 1; i <= m; i++) {
			for (int j = 1; j <= n; j++) {
				if (charsStr1[i - 1] == charsStr2[j - 1]) {
					matrix[i][j] = matrix[i - 1][j - 1] + 1;
				} else {
					matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
				}
			}
		}

		// 矩阵中，如果matrix[m][n]的值不等于matrix[m-1][n]的值也不等于matrix[m][n-1]的值， 则matrix[m][n]对应的字符为相似字符元，并将其存入result数组中。
		char[] result = new char[matrix[m][n]];
		int currentIndex = result.length - 1;
		while (matrix[m][n] != 0) {
			if (matrix[m][n] == matrix[m][n - 1]) {
				n--;
			} else if (matrix[m][n] == matrix[m - 1][n]) {
				m--;
			} else {
				result[currentIndex] = charsStr1[m - 1];
				currentIndex--;
				n--;
				m--;
			}
		}
		return new String(result);
	}
	// --------------------------------------------------------------------------------------------------- Private method end
}
