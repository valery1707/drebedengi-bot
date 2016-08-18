package name.valery1707.drebedengi.domain;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		if (value == null || value.isEmpty()) {
			return;
		}
		AtomicBoolean isFirst = new AtomicBoolean(true);
		append(str, name, "ns2:Map", b -> value.entrySet().forEach(
				e -> {
					if (isFirst.compareAndSet(true, false)) {
						b.append("\n");
					}
					b.append("<item>");
					append(b, "key", e.getKey());
					append(b, "value", e.getValue());
					b.append("</item>\n");
				}
		));
	}

	protected <T> void append(StringBuilder str, String name, Collection<T> list) {
		if (list == null || list.isEmpty() || list.stream().allMatch(Objects::isNull)) {
			return;
		}
		int size = list.size();
		T first = list.stream().filter(Objects::nonNull).findFirst().get();
		String type;
		Function<T, ?> mapper;
		if (first instanceof String) {
			type = "xsd:string";
			mapper = o -> (String) o;
		} else if (first instanceof Boolean) {
			type = "xsd:boolean";
			mapper = o -> (Boolean) o;
		} else if (first instanceof Integer) {
			type = "xsd:int";
			mapper = o -> (Integer) o;
		} else {
			type = "xsd:string";
			mapper = Object::toString;
		}
		String arrayType = String.format("SOAP-ENC:arrayType=\"%s[%d]\"", type, size);
		append(str, name, new String[]{arrayType, "xsi:type=\"SOAP-ENC:Array\""}, s -> {
			s.append("\n");
			list.stream()
					.filter(Objects::nonNull)
					.forEachOrdered(item -> append(s, "item", mapper.apply(item)));
		});
	}

	protected void append(StringBuilder str, String name, String type, Consumer<StringBuilder> valueWriter) {
		append(str, name, new String[]{"xsi:type=\"" + type + "\""}, valueWriter);
	}

	protected void append(StringBuilder str, String name, String[] attrs, Consumer<StringBuilder> valueWriter) {
		str.append("<").append(name);
		str.append(Stream.of(attrs).collect(Collectors.joining(" ", " ", "")));
		str.append(">");
		valueWriter.accept(str);
		str.append("</").append(name).append(">\n");
	}
}
