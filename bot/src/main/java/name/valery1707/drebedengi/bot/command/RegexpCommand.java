package name.valery1707.drebedengi.bot.command;

import java.util.regex.Pattern;

public abstract class RegexpCommand implements ICommand {
	private final Pattern pattern;

	public RegexpCommand(Pattern pattern) {
		this.pattern = pattern;
	}

	public RegexpCommand(String cmd) {
		this(Pattern.compile("^\\s*/" + Pattern.quote(cmd) + "\\b"));
	}

	public Pattern pattern() {
		return pattern;
	}

	@Override
	public boolean canHandle(String message) {
		return false;
	}
}
