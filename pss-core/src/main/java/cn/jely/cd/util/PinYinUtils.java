/*
 * 捷利商业进销存管理系统
 * @(#)CnToSpell.java
 * Copyright (c) 2002-2012 Jely Corporation
 * @date: 2013-3-30
 */
package cn.jely.cd.util;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang.StringUtils;

/**
 * 汉字转化为全拼,码表利用输入法的码表导出，汉字信息非常全，平时见到的汉字 转拼音的API只能处理几千个一级汉字，很多字都没有法查到
 * 该API除可以处理99％以上的汉字， 而且可以返回多音字的读音
 * 
 * @ClassName:CnToSpell
 * @author 周义礼
 * @version 2013-3-30 下午12:34:44
 */
public class PinYinUtils {
	private static final String PRE_FIX_UTF = "&#x";
	private static final String POS_FIX_UTF = ";";

	/**
	 * 取得字符串的中文拼音,有多音字则以逗号分开其组合</br> 如:"曾经" 结果为:cj,zj.
	 * 
	 * @Title:getPinYinShengMu
	 * @param org
	 * @return String
	 */
	public static String getPinYinShengMu(String org) {
		HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
		hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		hanyuPinyinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		if (StringUtils.isBlank(org))
			return null;
		String deletewhitespace = org.replaceAll("\\s*", "");// 用正则去掉空字符
		char[] srcchars = deletewhitespace.toCharArray();
		StringBuilder pyBuilder = new StringBuilder();
		String[][] tmp = new String[deletewhitespace.length()][];
		for (int i = 0; i < srcchars.length; i++) {
			char c = srcchars[i];
			try {
				String[] zhuyinArray = PinyinHelper.toHanyuPinyinStringArray(c, hanyuPinyinOutputFormat);
				// String[] zhuyinArray = PinYin.zhuYin(c);
				Set<String> zhuyinSet = new HashSet<String>();
				if (zhuyinArray != null && zhuyinArray.length > 0) {
					for (String zhuyin : zhuyinArray) {
						if (zhuyin.length() > 0) {
							zhuyinSet.add(zhuyin.substring(0, 1));
						}
					}
					tmp[i] = zhuyinSet.toArray(new String[zhuyinSet.size()]);
				} else {
					tmp[i] = new String[] { String.valueOf(c) };
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
		}
		String[] pinyinArray = Exchange(tmp);
		for (int i = 0; i < pinyinArray.length; i++) {
			if (i > 0) {
				pyBuilder.append(",");
			}
			pyBuilder.append(pinyinArray[i]);
		}
		String result = pyBuilder.toString().replaceAll("[^a-zA-Z0-9\\,]", "");// 将不是字母,数字及,号的全部替换为空串
		return result;
	}

	public static String[] Exchange(String[][] strJaggedArray) {
		String[][] temp = DoExchange(strJaggedArray);
		return temp[0];
	}

	/**
	 * 递归
	 * 
	 * @author wyh
	 * @param strJaggedArray
	 * @return
	 */
	private static String[][] DoExchange(String[][] strJaggedArray) {
		int len = strJaggedArray.length;
		if (len >= 2) {
			int len1 = strJaggedArray[0].length;
			int len2 = strJaggedArray[1].length;
			int newlen = len1 * len2;
			String[] temp = new String[newlen];
			int Index = 0;
			for (int i = 0; i < len1; i++) {
				for (int j = 0; j < len2; j++) {
					temp[Index] = strJaggedArray[0][i] + strJaggedArray[1][j];
					Index++;
				}
			}
			String[][] newArray = new String[len - 1][];
			for (int i = 2; i < len; i++) {
				newArray[i - 1] = strJaggedArray[i];
			}
			newArray[0] = temp;
			return DoExchange(newArray);
		} else {
			return strJaggedArray;
		}
	}

	/*------------------其它工具-------------------------*/
	/**
	 * Translate charset encoding to unicode
	 * 
	 * @param sTemp
	 *            charset encoding is gb2312
	 * @return charset encoding is unicode
	 */
	public static String XmlFormalize(String sTemp) {
		StringBuffer sb = new StringBuffer();

		if (sTemp == null || sTemp.equals("")) {
			return "";
		}
		String s = PinYinUtils.TranEncodeTOGB(sTemp);
		for (int i = 0; i < s.length(); i++) {
			char cChar = s.charAt(i);
			if (PinYinUtils.isGB2312(cChar)) {
				sb.append(PRE_FIX_UTF);
				sb.append(Integer.toHexString(cChar));
				sb.append(POS_FIX_UTF);
			} else {
				switch ((int) cChar) {
				case 32:
					sb.append("&#32;");
					break;
				case 34:
					sb.append("&quot;");
					break;
				case 38:
					sb.append("&amp;");
					break;
				case 60:
					sb.append("&lt;");
					break;
				case 62:
					sb.append("&gt;");
					break;
				default:
					sb.append(cChar);
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 将字符串编码格式转成GB2312
	 * 
	 * @param str
	 * @return
	 */
	public static String TranEncodeTOGB(String str) {
		try {
			String strEncode = PinYinUtils.getEncoding(str);
			String temp = new String(str.getBytes(strEncode), "GB2312");
			return temp;
		} catch (java.io.IOException ex) {

			return null;
		}
	}

	/**
	 * 判断输入字符是否为gb2312的编码格式
	 * 
	 * @param c
	 *            输入字符
	 * @return 如果是gb2312返回真，否则返回假
	 */
	public static boolean isGB2312(char c) {
		Character ch = new Character(c);
		String sCh = ch.toString();
		try {
			byte[] bb = sCh.getBytes("gb2312");
			if (bb.length > 1) {
				return true;
			}
		} catch (java.io.UnsupportedEncodingException ex) {
			return false;
		}
		return false;
	}

	/**
	 * 判断字符串的编码
	 * 
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) {
		String encode = "UTF-8";
		if (testEncoding(str, encode)) {
			return encode;
		}
		encode = "UTF-16BE";
		if (testEncoding(str, encode)) {
			return encode;
		}
		encode = "GBK";
		if (testEncoding(str, encode)) {
			return encode;
		}
		encode = "GB2312";
		if (testEncoding(str, encode)) {
			return encode;
		}
		encode = "ISO-8859-1";
		if (testEncoding(str, encode)) {
			return encode;
		}
		return "";
	}

	/**
	 * @Title:testEncoding
	 * @param str
	 */
	private static boolean testEncoding(String str, String encode) {
		try {
			if (str == null || encode == null) {
				return false;
			}
			return str.equals(new String(str.getBytes(encode), encode));
		} catch (Exception exception3) {
			return false;
		}
	}

	public static void main(String[] args) {
		Date start = new Date();
		System.out.println(start.getTime());
		String str = null;
		str = "逯闫 乐";
		str = "数据字典单";
		System.out.println(getPinYinShengMu(str));
		// System.out.println(TranEncodeTOGB(str));
		// System.out.println(Charset.defaultCharset().displayName());
		Date end = new Date();
		System.out.println(end.getTime());
	}
}