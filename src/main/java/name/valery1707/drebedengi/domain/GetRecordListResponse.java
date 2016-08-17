package name.valery1707.drebedengi.domain;

import java.util.Map;

public class GetRecordListResponse extends BaseResponse {
	private Map<Integer, Map<String, String>> value;

	public GetRecordListResponse(String soap) {
		super(soap);
	}

	@Override
	protected void readTags(String soap) {
		setValue(readTagMap(soap, "getRecordListReturn",
				key -> readTagInteger(key, "key"),
				value -> readTagMap(value, "value",
						k2 -> readTagString(k2, "key"),
						v2 -> readTagString(v2, "value")
				)
		));
	}

	public Map<Integer, Map<String, String>> getValue() {
		return value;
	}

	public void setValue(Map<Integer, Map<String, String>> value) {
		this.value = value;
	}
}
