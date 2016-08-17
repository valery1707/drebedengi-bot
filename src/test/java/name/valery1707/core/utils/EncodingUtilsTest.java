package name.valery1707.core.utils;


import org.junit.Test;

import static name.valery1707.core.utils.EncodingUtils.rfc5987Encode;
import static name.valery1707.core.utils.EncodingUtils.safeEncode;
import static org.assertj.core.api.Assertions.assertThat;

public class EncodingUtilsTest {

	@Test
	public void testRFC5987() throws Exception {
		//http://greenbytes.de/tech/tc2231/#attwithfn2231utf8
		assertThat(rfc5987Encode(
				"foo-ä-€.html")).isEqualTo(
				"foo-%c3%a4-%e2%82%ac.html");
	}

	@Test
	public void testSafe() throws Exception {
		assertThat(safeEncode(
				"foo-ä-€.html")).isEqualTo(
				"foo-_-_.html");
		assertThat(safeEncode(
				"ТОО КФ ДУДАР Прил_3_Логистическая специф\\x09=?koi8-r?B?ycvBw8nRXzAyLjA2LjE2ICgyKS54bHN4?=.xlsx")).isEqualTo(
				"__3__x09_koi8-r_B_ycvBw8nRXzAyLjA2LjE2ICgyKS54bHN4_.xlsx");
	}
}
