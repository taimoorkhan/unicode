package utils;

import java.io.UnsupportedEncodingException;
import java.util.Random;

public class UnicodeUtils {

	private static final int CONTINUATION_BYTE_MARKER = 0x80; // 10xxxxxx
	private static final int SIX_BIT_MASK = 0x3F; // 00111111

	private static final int MIN_SUPPLEMENTARY_CODE_POINT = 0x10000;
	private static final int MAX_SUPPLEMENTARY_CODE_POINT = 0x10FFFF;
	private static final int MAX_FOUR_BYTES_CODE_POINT_DIFF = MAX_SUPPLEMENTARY_CODE_POINT
			- MIN_SUPPLEMENTARY_CODE_POINT;

	private static Random random = new Random(System.currentTimeMillis());

	public static byte[] encodeUTF8(int codePoint) {
		if (codePoint <= 127) {
			// MSB is 0 - single byte
			return new byte[] { (byte) codePoint };
		}

		if (codePoint > 0x10FFFF) {
			throw new IllegalArgumentException("Invalid code point: " + codePoint);
		}

		byte[] bytes = new byte[4];
		int byteCount = 0;
		int leadingByteMask = 1 << 5; // 00011111

		while (true) {
			// Extract the first (= low order, right most) 6 bits from the code
			// point and create a
			// continuation byte with them.
			byte curByte = (byte) ((codePoint & SIX_BIT_MASK) | CONTINUATION_BYTE_MARKER);
			bytes[byteCount] = curByte;

			// Remove the 6 bits we just encoded.
			// NOTE: Use ">>>" (shift zeros into the left most position)
			codePoint = codePoint >>> 6;
			byteCount++;
			if (codePoint <= leadingByteMask) {
				// Remaining bits fit into the leading byte

				// Calculate most significant bits:
				// 1. A "1" for each byte used (including the leading byte)
				// 2. Followed by a "0"
				int msbs;
				switch (byteCount) { // number of continuation bytes
				case 1:
					msbs = 0xC0; // 110xxxxx
					break;
				case 2:
					msbs = 0xE0; // 1110xxxx
					break;
				case 3:
					msbs = 0xF0; // 11110xxx
					break;

				default:
					// Continuation bytes are limited to 3 (see
					// "invalid code point" exception above).
					throw new IllegalStateException();
				}

				curByte = (byte) (msbs | codePoint);
				bytes[byteCount] = curByte;

				byteCount++;
				break;
			} else {
				// We need another continuation byte
				leadingByteMask = leadingByteMask >>> 1;
			}
		}

		// NOTE: Bytes are in reversed order. Make it correct.
		switch (byteCount) {
		case 2:
			return new byte[] { bytes[1], bytes[0] };
		case 3:
			return new byte[] { bytes[2], bytes[1], bytes[0] };
		case 4:
			return new byte[] { bytes[3], bytes[2], bytes[1], bytes[0] };
		default:
			// Byte count is limited to 4 (see "invalid code point" exception
			// above).
			throw new IllegalStateException();
		}
	}

	public static int getRandomFourByteCodePoint() {
		int nextRand = random.nextInt(MAX_FOUR_BYTES_CODE_POINT_DIFF);
		return MIN_SUPPLEMENTARY_CODE_POINT + nextRand;
	}

	public static String getRandomFourByteUtf8CodePoint()
			throws UnsupportedEncodingException {
		int nextCodePoint = UnicodeUtils.getRandomFourByteCodePoint();
		byte[] bytes = encodeUTF8(nextCodePoint);
		return new String(bytes, "UTF-8");
	}

	public static String randomSupplementaryCodepoints(int length) throws UnsupportedEncodingException {
		if(length <= 0 ) throw new IllegalArgumentException("Lenght has to be a positive number");
		StringBuilder sb = new StringBuilder();
		for(int i = 0 ; i < length ; i++){
			sb.append(getRandomFourByteUtf8CodePoint());
		}
		
		return sb.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		for (int i = 0; i < 50; i++) {
			String cp = UnicodeUtils.getRandomFourByteUtf8CodePoint();
//			System.out.println("Lenght = " + cp.length()
//					+ ", codepoint count = "
//					+ cp.codePointCount(0, cp.length()) + ", byte count = "
//					+ cp.getBytes().length);
//			
			System.out.println(randomSupplementaryCodepoints(20));
		}
	}
}
