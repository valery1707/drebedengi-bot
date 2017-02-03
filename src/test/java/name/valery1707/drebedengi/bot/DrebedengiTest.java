package name.valery1707.drebedengi.bot;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.LinkedHashMap;
import javaslang.collection.List;
import javaslang.control.Try;
import name.valery1707.drebedengi.domain.GetAccessStatus;
import name.valery1707.drebedengi.domain.GetAccessStatusResponse;
import name.valery1707.drebedengi.domain.GetRecordList;
import name.valery1707.drebedengi.domain.GetRecordListResponse;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class DrebedengiTest {

	private DrebedengiConf conf;
	private Drebedengi drebedengi;

	@Before
	public void setUp() throws Exception {
		conf = new DrebedengiConf();
		conf.setApi(new DrebedengiConf.Api());
		conf.getApi().setUrl(new URL("http://www.drebedengi.ru/soap/"));
		conf.getApi().setKey("demo_api");
		drebedengi = new Drebedengi(conf);
		drebedengi.init();
	}

	@Test
	public void testGetAccessStatus_ok() throws Exception {
		Try<GetAccessStatusResponse> response = drebedengi.request(new GetAccessStatus(), GetAccessStatusResponse::new);
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.get()).isNotNull();
		assertThat(response.get().isSuccess()).isTrue();
		assertThat(response.get().getFailCode()).isNull();
		assertThat(response.get().getFailMessage()).isNullOrEmpty();
		assertThat(response.get().getValue()).isEqualTo(1);
	}

	@Test
	public void testGetAccessStatus_fail() throws Exception {
		//todo Add test for pass
		conf.getApi().setKey("----");
		Try<GetAccessStatusResponse> response = drebedengi.request(new GetAccessStatus(), GetAccessStatusResponse::new);
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.get()).isNotNull();
		assertThat(response.get().isSuccess()).isFalse();
		assertThat(response.get().getFailCode()).isEqualTo(50);
		assertThat(response.get().getFailMessage()).isNotEmpty().contains(conf.getApi().getKey());
		assertThat(response.get().getValue()).isNull();
	}

	@Test
	public void testRecordList() throws Exception {
		Try<GetRecordListResponse> response = drebedengi.request(
				new GetRecordList()
						.withParam("is_report", false)// Data not for report, but for export
						.withParam("is_show_duty", true)// Include duty records
						.withParam("r_period", 8)// Show last 20 record (for each operation type, if not one, see 'r_what')
						.withParam("r_how", 1)// Show by detail, not grouped
						.withParam("r_what", 6)// Show all operations (waste, income, moves and currency changes)
						.withParam("r_currency", 0)// Show in original currency
						.withParam("r_is_place", 0)// All places
						.withParam("r_is_tag", 0),// All tags
				GetRecordListResponse::new);
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.get()).isNotNull();
		assertThat(response.get().getFailMessage()).isNullOrEmpty();
		assertThat(response.get().getFailCode()).isNull();
		assertThat(response.get().isSuccess()).isTrue();
		assertThat(response.get().getValue()).hasSize(25);
	}

	@Test
	public void testRecordList_filterById() throws Exception {
		Try<GetRecordListResponse> response = drebedengi.request(
				new GetRecordList()
						.withParam("is_report", false)// Data not for report, but for export
						.withParam("is_show_duty", true)// Include duty records
						.withParam("r_period", 8)// Show last 20 record (for each operation type, if not one, see 'r_what')
						.withParam("r_how", 1)// Show by detail, not grouped
						.withParam("r_what", 6)// Show all operations (waste, income, moves and currency changes)
						.withParam("r_currency", 0)// Show in original currency
						.withParam("r_is_place", 0)// All places
						.withParam("r_is_tag", 0)// All tags
						.withId(53133)
						.withId(53131)
				, GetRecordListResponse::new);
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.get()).isNotNull();
		assertThat(response.get().getFailMessage()).isNullOrEmpty();
		assertThat(response.get().getFailCode()).isNull();
		assertThat(response.get().isSuccess()).isTrue();
		assertThat(response.get().getValue())
				.hasSize(2)
				.containsOnlyKeys(53133, 53131);
	}

	@Test
	public void testRecordList_filterByPlaces() throws Exception {
		List<Integer> places = List.of(40040, 41439);
		Try<GetRecordListResponse> response = drebedengi.request(
				new GetRecordList()
						.withParam("is_report", false)// Data not for report, but for export
						.withParam("is_show_duty", true)// Include duty records
						.withParam("r_period", 8)// Show last 20 record (for each operation type, if not one, see 'r_what')
						.withParam("r_how", 1)// Show by detail, not grouped
						.withParam("r_what", 3)// waste
						.withParam("r_currency", 0)// Show in original currency
						.withParam("r_is_place", 1)// Include only selected
						.withParam("r_is_tag", 0)// All tags
						.withParam("r_place", places)
				, GetRecordListResponse::new);
		assertThat(response.isSuccess()).isTrue();
		assertThat(response.get()).isNotNull();
		assertThat(response.get().getFailMessage()).isNullOrEmpty();
		assertThat(response.get().getFailCode()).isNull();
		assertThat(response.get().isSuccess()).isTrue();
		LinkedHashMap<Integer, Map<String, String>> value = LinkedHashMap.ofAll(response.get().getValue());
		assertThat(value.map((id, entry) -> Tuple.of(id, entry.get("place_id"))).toList().map(Tuple2::_2).map(Integer::valueOf)).containsOnlyElementsOf(places);
		assertThat(response.get().getValue())
				.hasSize(20);
	}
}
