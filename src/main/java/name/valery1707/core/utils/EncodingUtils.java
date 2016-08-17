package name.valery1707.core.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class EncodingUtils {
	private static final char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static final byte[] attr_char = {'!', '#', '$', '&', '+', '-', '.', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '^', '_', '`', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '|', '~'};

	/**
	 * @param src Source string
	 * @return String encoded in RFC 5987
	 * @see <http href="http://greenbytes.de/tech/tc2231/#attwithfn2231utf8">Browser tests</http>
	 */
	public static String rfc5987Encode(final String src) {
		final byte[] bytes = src.getBytes(StandardCharsets.UTF_8);
		final int len = bytes.length;
		final StringBuilder sb = new StringBuilder(len << 1);
		for (final byte b : bytes) {
			if (Arrays.binarySearch(attr_char, b) >= 0) {
				sb.append((char) b);
			} else {
				sb.append('%');
				sb.append(digits[0x0f & (b >>> 4)]);
				sb.append(digits[b & 0x0f]);
			}
		}
		return sb.toString();
	}

	public static String safeEncode(String src) {
		final byte[] bytes = src.getBytes(StandardCharsets.UTF_8);
		final int len = bytes.length;
		final StringBuilder sb = new StringBuilder(len << 1);
		boolean underscore = false;
		for (final byte b : bytes) {
			if (Arrays.binarySearch(attr_char, b) >= 0) {
				sb.append((char) b);
				underscore = false;
			} else if (!underscore) {
				sb.append('_');
				underscore = true;
			}
		}
		return sb.toString();
	}
}
