package name.valery1707.drebedengi.domain;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class GetRecordListResponseTest {
	@Test
	public void testRead() throws Exception {
		String soap = IOUtils.toString(this.getClass().getResourceAsStream("/drebedengi.xml.sample/getRecordList_response.xml"), StandardCharsets.UTF_8);
		assertThat(soap).isNotEmpty();
		GetRecordListResponse response = new GetRecordListResponse(soap);
		assertThat(response).isNotNull();
		assertThat(response.getValue())
				.hasSize(82);
		assertThat(response.getValue().get(53133)).isNotNull();
		assertThat(response.getValue().get(53133)).isNotEmpty()
				.containsEntry("id", "53133")
				.containsEntry("group_id", null)
		;
	}
}
