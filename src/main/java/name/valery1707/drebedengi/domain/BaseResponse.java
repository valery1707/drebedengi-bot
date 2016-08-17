package name.valery1707.drebedengi.domain;

import org.apache.commons.lang3.StringUtils;

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
		return readTag(soap, tagName, "xsd:string", s -> s);
	}

	protected Integer readTagInteger(String soap, String tagName) {
		return readTag(soap, tagName, "xsd:integer", Integer::valueOf);
	}

	protected Boolean readTagBoolean(String soap, String tagName) {
		return readTag(soap, tagName, "xsd:boolean", "TRUE"::equalsIgnoreCase);
	}

	protected <R> R readTag(String soap, String tagName, String type, Function<String, R> mapper) {
		Pattern pattern = Pattern.compile("</?" + Pattern.quote(tagName));
		Matcher matcher = pattern.matcher(soap);
		if (!matcher.find()) {
			return null;
		}
		int start = matcher.start();
		int end = matcher.end();
		int nestedLevel = 0;
		while (matcher.find()) {
			if (matcher.group().startsWith("</")) {
				if (nestedLevel <= 0) {
					end = matcher.end();
				} else {
					nestedLevel--;
				}
			} else {
				nestedLevel++;
			}
		}
		String value = soap.substring(start, end+1);
		value = value.substring(value.indexOf('>')+1, value.lastIndexOf("<"));
		return mapper.apply(value.trim());
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
