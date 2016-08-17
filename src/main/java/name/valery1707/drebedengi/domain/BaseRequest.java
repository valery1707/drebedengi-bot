package name.valery1707.drebedengi.domain;

import java.util.Map;
import java.util.function.Consumer;

public class BaseRequest {
	private String apiId;
	private String login;
	private String pass;

	public String getApiId() {
		return apiId;
	}

	public void setApiId(String apiId) {
		this.apiId = apiId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public void write(StringBuilder str) {
		append(str, "apiId", getApiId());
		append(str, "login", getLogin());
		append(str, "pass", getPass());
	}

	protected void append(StringBuilder str, String name, String value) {
		append(str, name, "xsd:string", b -> b.append(value));
	}

	protected void append(StringBuilder str, String name, Boolean value) {
		append(str, name, "xsd:boolean", b -> b.append(value.toString().toLowerCase()));
	}

	protected void append(StringBuilder str, String name, Integer value) {
		append(str, name, "xsd:int", b -> b.append(value.toString()));
	}

	protected void append(StringBuilder str, String name, Object value) {
		if (value instanceof String) {
			append(str, name, (String) value);
		} else if (value instanceof Boolean) {
			append(str, name, (Boolean) value);
		} else if (value instanceof Integer) {
			append(str, name, (Integer) value);
		} else {
			append(str, name, "xsd:anyType", b -> b.append(value.toString()));
		}
	}

	protected void append(StringBuilder str, String name, Map<String, Object> value) {
		if (value.isEmpty()) {
			return;
		}
		append(str, name, "ns2:Map", b -> value.entrySet().forEach(
				e -> {
					b.append("<item>");
					append(b, "key", e.getKey());
					append(b, "value", e.getValue());
					b.append("</item>\n");
				}
		));
	}

	protected void append(StringBuilder str, String name, String type, Consumer<StringBuilder> valueWriter) {
		str.append("<").append(name).append(" xsi:type=\"").append(type).append("\">");
		valueWriter.accept(str);
		str.append("</").append(name).append(">\n");
	}
}
