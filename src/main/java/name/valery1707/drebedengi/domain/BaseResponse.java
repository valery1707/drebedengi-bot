package name.valery1707.drebedengi.domain;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.substringBetween;

public abstract class BaseResponse {
	private static final String SOAP_RES_PREFIX = "<SOAP-ENV:Body>";
	private static final String SOAP_RES_SUFFIX = "</SOAP-ENV:Body>";

	private boolean success;
	private Integer failCode;
	private String failMessage;

	public BaseResponse(String soap) {
		soap = substringBetween(soap, SOAP_RES_PREFIX, SOAP_RES_SUFFIX);
		String operation = StringUtils.uncapitalize(this.getClass().getSimpleName());
		if (soap.contains("<SOAP-ENV:Fault>")) {
			setSuccess(false);
			setFailCode(readTag(soap, "faultCode".toLowerCase(), null, Integer::valueOf));
			setFailMessage(readTag(soap, "faultString".toLowerCase(), null, s -> s));
		} else if (soap.contains("<ns1:" + operation + ">")) {
			setSuccess(true);
			readTags(soap);
		} else {
			setSuccess(false);
			setFailCode(-1);
			setFailMessage(soap);
		}
	}

	protected abstract void readTags(String soap);

	protected String readTagString(String soap, String tagName) {
		return readTag(soap, tagName, "xsd:string", StringUtils::trimToNull);
	}

	protected Integer readTagInteger(String soap, String tagName) {
		return readTag(soap, tagName, "xsd:integer", Integer::valueOf);
	}

	protected Boolean readTagBoolean(String soap, String tagName) {
		return readTag(soap, tagName, "xsd:boolean", "TRUE"::equalsIgnoreCase);
	}

	protected <K, V> Map<K, V> readTagMap(String soap, String tagName, Function<String, K> keyMapper, Function<String, V> valueMapper) {
		return readTag(soap, tagName, "ns2:Map", value -> {
			LinkedHashMap<K, V> map = new LinkedHashMap<>();
			TagIterator iterator = iterate(value, "item");
			while (iterator.hasNext()) {
				String next = iterator.next();
				map.put(keyMapper.apply(next), valueMapper.apply(next));
			}
			return map;
		});
	}

	protected <R> R readTag(String soap, String tagName, String type, Function<String, R> mapper) {
		TagIterator iterator = iterate(soap, tagName);
		if (!iterator.hasNext()) {
			return null;
		}
		return mapper.apply(iterator.next());
	}

	@NotNull
	protected BaseResponse.TagIterator iterate(String soap, String tagName) {
		return new TagIterator(soap, tagName);
	}

	private static final String NIL = "xsi:nil=\"true\"/>";
	private static final int NIL_LEN = NIL.length();

	private static class TagIterator implements Iterator<String> {
		private final String soap;
		private final Matcher matcher;

		public TagIterator(String soap, String tagName) {
			this.soap = soap;
			Pattern pattern = Pattern.compile("</?" + Pattern.quote(tagName));
			matcher = pattern.matcher(soap);
		}

		@Override
		public boolean hasNext() {
			return matcher.find();
		}

		@Override
		public String next() {
			int start = matcher.start();
			int end = matcher.end();
			int nestedLevel = 0;
			while (matcher.find()) {
				String group = matcher.group();
				if (group.startsWith("</")) {
					if (nestedLevel <= 0) {
						end = matcher.end();
						break;
					} else {
						nestedLevel--;
					}
				} else {
					if (matcher.end() + NIL_LEN > soap.length() || !NIL.equals(soap.substring(matcher.end() + 1, matcher.end() + NIL_LEN + 1))) {
						nestedLevel++;
					}
				}
			}
			String value = soap.substring(start, end + 1);
			value = value.substring(value.indexOf('>') + 1, value.lastIndexOf("<"));
			return value.trim();
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getFailMessage() {
		return failMessage;
	}

	public Integer getFailCode() {
		return failCode;
	}

	public void setFailCode(Integer failCode) {
		this.failCode = failCode;
	}

	public void setFailMessage(String failMessage) {
		this.failMessage = failMessage;
	}
}
