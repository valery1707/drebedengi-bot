package name.valery1707.drebedengi.domain;

public class GetAccessStatusResponse extends BaseResponse {
	private Integer value;

	public GetAccessStatusResponse(String soap) {
		super(soap);
	}

	@Override
	protected void readTags(String soap) {
		setValue(readTagInteger(soap, "getAccessStatusReturn"));
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
}
