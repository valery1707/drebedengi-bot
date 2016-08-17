package name.valery1707.drebedengi.domain;

import java.util.LinkedHashMap;

public class GetRecordList extends BaseRequest {
	private LinkedHashMap<String, Object> params = new LinkedHashMap<>();
	//todo private Set<Integer> idList;

	@Override
	public void write(StringBuilder str) {
		super.write(str);
		append(str, "params", params);
	}

	public GetRecordList withParam(String name, Object value) {
		params.put(name, value);
		return this;
	}
}
