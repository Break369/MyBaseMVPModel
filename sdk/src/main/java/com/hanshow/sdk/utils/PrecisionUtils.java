package com.hanshow.sdk.utils;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PrecisionUtils extends NumberUtils
{
	private static final int DEF_DIV_SCALE = 10;

	public static double add(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.add(b2).doubleValue();
	}

	public static double sub(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.subtract(b2).doubleValue();
	}

	public static double mul(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		Log.e("输出",b1.multiply(b2).toString());
		return b1.multiply(b2).doubleValue();
	}
	public static String mulString(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		Log.e("输出",b1.multiply(b2).toString());
		return b1.multiply(b2).toString();
	}

	public static double div(double v1, double v2)
	{
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));

		return b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	// 取余数
	public static double mod(double v1, double v2)
	{
		if (v2 == 0)
			return v1;

		double a = div(v1, v2);
		int b = (int) a;
		double c = mul(b, v2);
		return sub(Math.abs(v1), Math.abs(c));
	}

	// 比较小数
	public static int doubleCompare(double f1, double f2, int dec)
	{
		int i;
		float f = 1.00F;

		for (i = 0; i <= dec; i++)
		{
			f /= 10.00;
		}

		f1 = doubleConvert(f1, dec, 1);
		f2 = doubleConvert(f2, dec, 1);

		if (Math.abs(f1 - f2) <= f)
		{
			return 0;
		}

		if (f1 > f2)
		{
			return 1;
		}

		return -1;
	}

	public static int getDoubleSign(double f)
	{
		return (doubleCompare(f, 0, 2) >= 0) ? 1 : (-1);
	}

	public static int getDoubleScale(double f)
	{
		int i = 0;
		double d;

		do
		{
			d = f * Math.pow(10.0, i);
			if (d == (long) d)
				return i;
			i++;
		} while (true);
	}

	public static String doubleToString(double value)
	{
		return doubleToString(value, 2, 1, false, 0);
	}

	public static String doubleToString(double value, int dec, int flag)
	{
		return doubleToString(value, dec, flag, false, 0);
	}

	public static String doubleToString(double value, int dec, int flag, boolean subdec)
	{
		return doubleToString(value, dec, flag, subdec, 0);
	}

	public static String getXMLItem(String input, String point)
	{
		int len = point.length();
		int start = input.indexOf("<" + point + ">");
		int end = input.indexOf("</" + point + ">");
		if (start >= 0 && end >= 0)
		{
			return input.substring(start + len + 2, end);
		}
		return null;
	}

	public static String doubleToString(double value, int dec, int flag, boolean subdec, int rightwidth)
	{
		StringBuffer sb = new StringBuffer("0");

		if (dec > 0)
			sb.append(".");
		for (int i = 0; i < dec; i++)
			sb.append("0");

		DecimalFormat df = new DecimalFormat(sb.toString());

		String s = df.format(doubleConvert(value, dec, flag));

		// 去掉位部0
		if (subdec)
		{
			while (s.charAt(s.length() - 1) == '0' || s.charAt(s.length() - 1) == '.')
			{
				if (s.charAt(s.length() - 1) == '.')
				{
					s = s.substring(0, s.length() - 1);
					break;
				}
				else
				{
					s = s.substring(0, s.length() - 1);
				}
			}
		}

		if (rightwidth > 0)
		{
			StringUtils.leftPad(s, rightwidth, ' ');
		}

		return s;
	}

	// 浮点运算1 = 0.999999,需要进位到两位小数再取整
	// 浮点运算299/300 = 0.996666,进位取整=1,还需再乘分母用金额比较，如果大倍数要减1
	public static int integerDiv(double f1, double f2)
	{
		double fz = Math.abs(f1);
		double fm = Math.abs(f2);
		if (fm == 0)
			fm = 1;

		int num = (int) doubleConvert(fz / fm);
		if (doubleCompare(fm * num, fz, 2) > 0)
			num--;
		if (num < 0)
			num = 0;

		return num;
	}

	public static void main(String[] args)
	{
		//System.out.println(doubleConvert(10.99999, 3, 0));
		
		System.out.println(hex2short("067B"));
	}

	public static double doubleConvert(double f)
	{
		return doubleConvert(f, 2, 1);
	}

	// 转换数,f:表示传入值,dec:表示精确位数,flag:表示0就是截断,1表示四舍五入
	public static double doubleConvert(double f, int dec, int flag, boolean r)
	{
		try
		{
			if (flag == 1)
			{
				double d = f;
				// 先计算数字f 精度后2位的精度
				if (!r)
					d = doubleConvert(d, dec + 2, 1, true);
				d = mul(d, Math.pow(10.0, dec));
				return (Math.round(d)) / (Math.pow(10.0, dec));
			}
			else
			{
				double d = Math.abs(f);
				if (!r)
					d = doubleConvert(d, dec + 2, 1, true);
				d = mul(d, Math.pow(10.0, dec));
				return (Math.floor(d)) / (Math.pow(10.0, dec) * (f < 0 ? -1 : 1));
			}
		}
		catch (Exception ex)
		{
			return 0;
		}
	}

	// 转换数,f:表示传入值,dec:表示精确位数,flag:表示0就是截断,1表示四舍五入
	public static double doubleConvert(double f, int dec, int flag)
	{
		return doubleConvert(f, dec, flag, false);
	}

	public static String getRegisterCodeSeqno(String regcode)
	{
		if (regcode == null || regcode.length() < 29)
			return "";

		return regcode.substring(24, 29);
	}

	static final String gs_key_para = "Bvp01CeFM234H5ljZhiG6rstDu789abnxIfPgQcEdOqyTwNJmWXUYKLkozARSV";

	// 浮点转化为中文
	public static String getFloatConverChinese(double value)
	{
		String strnum = doubleToString(value);
		String strCheck = null;
		String strFen = null;
		String strDW = null;
		String strNum = null;
		String strBig = null;
		String strNow = null;
		String strBear = "";

		if ((strnum == null) || strnum.trim().equals(""))
		{
			return "零";
		}

		double d = 0;

		try
		{
			d = Double.parseDouble(strnum);
		}
		catch (Exception ex)
		{
			return "数据" + value + "非法！";
		}

		strCheck = value + ".";

		int dot = strCheck.indexOf(".");

		if (dot > 12)
		{
			return "数据" + value + "过大，无法处理！";
		}

		try
		{
			int i = 0;
			strBig = "";
			strDW = "";
			strNum = "";

			long intFen = (long) mul(d, 100);

			if (strnum.charAt(0) == '-')
			{
				strBear = "负";
				strFen = String.valueOf(intFen).substring(1);
			}
			else
			{
				strFen = String.valueOf(intFen);
			}

			int lenIntFen = strFen.length();

			while (lenIntFen != 0)
			{
				i++;

				switch (i)
				{
					case 1:
						strDW = "分";

						break;

					case 2:
						strDW = "角";

						break;

					case 3:
						strDW = "元";

						break;

					case 4:
						strDW = "拾";

						break;

					case 5:
						strDW = "佰";

						break;

					case 6:
						strDW = "仟";

						break;

					case 7:
						strDW = "万";

						break;

					case 8:
						strDW = "拾";

						break;

					case 9:
						strDW = "佰";

						break;

					case 10:
						strDW = "仟";

						break;

					case 11:
						strDW = "亿";

						break;

					case 12:
						strDW = "拾";

						break;

					case 13:
						strDW = "佰";

						break;

					case 14:
						strDW = "仟";

						break;
				}

				switch (strFen.charAt(lenIntFen - 1))
				{
					case '1':
						strNum = "壹";

						break;

					case '2':
						strNum = "贰";

						break;

					case '3':
						strNum = "叁";

						break;

					case '4':
						strNum = "肆";

						break;

					case '5':
						strNum = "伍";

						break;

					case '6':
						strNum = "陆";

						break;

					case '7':
						strNum = "柒";

						break;

					case '8':
						strNum = "捌";

						break;

					case '9':
						strNum = "玖";

						break;

					case '0':
						strNum = "零";

						break;
				}

				strNow = strBig;

				if ((i == 1) && (strFen.charAt(lenIntFen - 1) == '0'))
				{
					strBig = "整";
				}

				else if ((i == 2) && (strFen.charAt(lenIntFen - 1) == '0'))
				{
					if (!strBig.equals("整"))
					{
						strBig = "零" + strBig;
					}
				}
				else if ((i == 3) && (strFen.charAt(lenIntFen - 1) == '0'))
				{
					strBig = "元" + strBig;
				}
				else if ((i < 7) && (i > 3) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) != '零') && (strNow.charAt(0) != '元'))
				{
					strBig = "零" + strBig;
				}
				else if ((i < 7) && (i > 3) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) == '零'))
				{
				}

				else if ((i < 7) && (i > 3) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) == '元'))
				{
				}

				else if ((i == 7) && (strFen.charAt(lenIntFen - 1) == '0'))
				{
					strBig = "万" + strBig;
				}

				else if ((i < 11) && (i > 7) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) != '零') && (strNow.charAt(0) != '万'))
				{
					strBig = "零" + strBig;
				}
				else if ((i < 11) && (i > 7) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) == '万'))
				{
				}
				else if ((i < 11) && (i > 7) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) == '零'))
				{
				}
				else if ((i < 11) && (i > 8) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) == '万') && (strNow.charAt(2) == '仟'))
				{
					strBig = strNum + strDW + "万零" + strBig.substring(1, strBig.length());
				}

				else if (i == 11)
				{
					// 亿位为零且万全为零存在仟位时，去掉万补为零
					if ((strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) == '万') && (strNow.charAt(2) == '仟'))
					{
						strBig = "亿" + "零" + strBig.substring(1, strBig.length());
					}

					// 亿位为零且万全为零不存在仟位时，去掉万
					else if ((strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) == '万') && (strNow.charAt(2) != '仟'))
					{
						strBig = "亿" + strBig.substring(1, strBig.length());
					}

					// 亿位不为零且万全为零存在仟位时，去掉万补为零
					else if ((strNow.charAt(0) == '万') && (strNow.charAt(2) == '仟'))
					{
						strBig = strNum + strDW + "零" + strBig.substring(1, strBig.length());
					}

					// 亿位不为零且万全为零不存在仟位时，去掉万
					else if ((strNow.charAt(0) == '万') && (strNow.charAt(2) != '仟'))
					{
						strBig = strNum + strDW + strBig.substring(1, strBig.length());
					}

					// 其他正常情况
					else
					{
						strBig = strNum + strDW + strBig;
					}
				}

				else if ((i < 15) && (i > 11) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) != '零') && (strNow.charAt(0) != '亿'))
				{
					strBig = "零" + strBig;
				}

				else if ((i < 15) && (i > 11) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) == '亿'))
				{
				}

				else if ((i < 15) && (i > 11) && (strFen.charAt(lenIntFen - 1) == '0') && (strNow.charAt(0) == '零'))
				{
				}

				else if ((i < 15) && (i > 11) && (strFen.charAt(lenIntFen - 1) != '0') && (strNow.charAt(0) == '零') && (strNow.charAt(1) == '亿') && (strNow.charAt(3) != '仟'))
				{
					strBig = strNum + strDW + strBig.substring(1, strBig.length());
				}
				else if ((i < 15) && (i > 11) && (strFen.charAt(lenIntFen - 1) != '0') && (strNow.charAt(0) == '零') && (strNow.charAt(1) == '亿') && (strNow.charAt(3) == '仟'))
				{
					strBig = strNum + strDW + "亿零" + strBig.substring(2, strBig.length());
				}
				else
				{
					strBig = strNum + strDW + strBig;
				}

				strFen = strFen.substring(0, lenIntFen - 1);
				lenIntFen--;
			}

			if (strBear.equals("负"))
			{
				strBig = strBear + strBig;
			}

			return strBig;
		}
		catch (Exception exx)
		{
			exx.printStackTrace();

			return "";
		}
	}

	public static String getRandom()
	{
		String crcstr = String.valueOf(Math.round(Math.random() * 1000));

		if (crcstr.length() > 3)
		{
			return crcstr.substring(0, 3);
		}
		else
		{
			return StringUtils.leftPad(crcstr, 3, '0');
		}

	}

	public static double getGoodsPrice(double price, double precision)
	{
		int round = 0;

		if (precision == 0)
			round = 2;
		else
			round = getDoubleScale(precision);

		return doubleConvert(price, round, 1);
	}

	public static double getPrecisionByRound(double total, char mode)
	{
		double result = total;
		// 收银截断方式，0-精确到分、1-四舍五入到角、2-截断到角、3-四舍五入到元、4-截断到元、5-进位到角、6-进位到元
		// 7-5舍6入到角
		switch (mode)
		{
			case '0':
				result = PrecisionUtils.doubleConvert(total, 2, 1);

				break;

			case '1':
				result = PrecisionUtils.doubleConvert(total, 1, 1);

				break;

			case '2':
				result = PrecisionUtils.doubleConvert(total, 1, 0);

				break;

			case '3':
				result = PrecisionUtils.doubleConvert(total, 0, 1);

				break;

			case '4':
				result = PrecisionUtils.doubleConvert(total, 0, 0);

				break;
			case '5':
				result = PrecisionUtils.doubleConvert(total + 0.09, 1, 0);

				break;
			case '6':
				result = PrecisionUtils.doubleConvert(total + 0.9, 0, 0);

				break;

			case '7':
				result = PrecisionUtils.doubleConvert(total - 0.01, 1, 1);

				break;
		}

		return result;
	}

	// 删除数字已外的字符
	public static String getFilterNumberNoStr(String str)
	{
		String str1 = "";

		for (int i = 0; i < str.length(); i++)
		{
			if (NumberUtils.isNumber(String.valueOf(str.charAt(i))))
			{
				str1 = str1 + str.charAt(i);
			}
		}
		return str1;
	}

	static byte toByte(char c)
	{
		final String key = "0123456789ABCDEF";
		return (byte) key.indexOf(c);
	}

	public static short hex2short(String hex)
	{
		byte[] bytes = hex2Byte(hex);
		return byte2Short(bytes);
	}

	public static short byte2Short(byte[] bytes)
	{
		if (bytes.length > 1)
			return (short) (bytes[0] << 8 | bytes[1]);
		return (short) (bytes[0] << 8);
	}

	public static byte[] hex2Byte(String hexstr)
	{
		int len = hexstr.length() / 2;
		byte[] result = new byte[len];
		char[] chr = hexstr.toCharArray();
		for (int i = 0; i < len; i++)
		{
			int pos = i * 2;
			result[i] = (byte) ((toByte(chr[pos]) << 4) | toByte(chr[pos + 1]));
		}
		return result;
	}
}
