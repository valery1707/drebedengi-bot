package name.valery1707.drebedengi.domain;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class GetRecordList extends BaseRequest {
	private LinkedHashMap<String, Object> params = new LinkedHashMap<>();
	private Set<Integer> idList = new LinkedHashSet<>();

	@Override
	public void write(StringBuilder str) {
		super.write(str);
		append(str, "params", params);
		append(str, "idList", idList);
	}

	public GetRecordList withParam(String name, Object value) {
		params.put(name, value);
		return this;
	}

	public GetRecordList withId(Integer id) {
		idList.add(id);
		return this;
	}
}
