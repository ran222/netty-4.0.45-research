package yuan.research.nio.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: yuanwei
 * Date: 2015.9.25 0025 11:40
 * To change this template use File | Settings | File Templates.
 *
 */
public class CharsetUtil{
	public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");
	public static final Charset US_ASCII = Charset.forName("US-ASCII");
	public static final Charset UTF_16 = Charset.forName("UTF-16");
	public static final Charset UTF_8 = Charset.forName("UTF-8");
	public static final Charset GBK = Charset.forName("GBK");

	public static byte[] getBytes(final String string, final Charset charset) {
		if (string == null) {
			return null;
		}
		return string.getBytes(charset);
	}
	public static byte[] getBytes(final String string, final String charsetName) {
		if (string == null) {
			return null;
		}
		try {
			return string.getBytes(charsetName);
		} catch (final UnsupportedEncodingException e) {
			return null;
		}
	}

	public static byte[] getBytesIso8859_1(final String string) {
		return getBytes(string, ISO_8859_1);
	}

	public static byte[] getBytesUsAscii(final String string) {
		return getBytes(string, US_ASCII);
	}

	public static byte[] getBytesUtf16(final String string) {
		return getBytes(string, UTF_16);
	}

	public static byte[] getBytesUtf8(final String string) {
		return getBytes(string, UTF_8);
	}

	private static String newString(final byte[] bytes, final Charset charset) {
		return bytes == null ? null : new String(bytes, charset);
	}

	public static String newStringIso8859_1(final byte[] bytes) {
		return new String(bytes, ISO_8859_1);
	}

	public static String newStringUsAscii(final byte[] bytes) {
		return new String(bytes, US_ASCII);
	}

	public static String newStringUtf16(final byte[] bytes) {
		return new String(bytes, UTF_16);
	}


	public static String newStringUtf8(final byte[] bytes) {
		return newString(bytes, UTF_8);
	}


	public static void main(String[] args) throws Exception{
		String src="订单";
		byte[] src_bytes_default=src.getBytes();
		byte[] src_bytes_utf8=src.getBytes(UTF_8);
		byte[] src_bytes_gbk=src.getBytes(GBK);
		byte[] bytes_utf8={-24, -82, -94, -27, -115, -107};
		byte[] bytes_ISO_8859_1={63, 63};
		byte[] bytes_US_ASCII={63, 63};
		byte[] bytes_GBK={-74, -87, -75, -91};
		System.out.println(Arrays.toString("订单".getBytes()));
		System.out.println(Arrays.toString("订单".getBytes("UTF-8")));
		System.out.println("是 utf8:"+Arrays.equals(src_bytes_default, bytes_utf8));
		System.out.println("是 gbk:"+Arrays.equals(src_bytes_default,bytes_GBK));
		System.out.println("是 ISO_8859_1:"+Arrays.equals(src_bytes_default,bytes_ISO_8859_1));
		System.out.println("是 US_ASCII:"+Arrays.equals(src_bytes_default,bytes_US_ASCII));



		System.out.println("utf8:"+Arrays.toString(src.getBytes(UTF_8)));
		System.out.println("ISO_8859_1:"+Arrays.toString(src.getBytes(ISO_8859_1)));
		System.out.println("US_ASCII:"+Arrays.toString(src.getBytes(US_ASCII)));
		System.out.println("GBK:"+Arrays.toString(src.getBytes(GBK)));
	}
}
