package einblick;

import static einblick.LogHelper.finer;

import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PatternDefinition {
	private static final Logger LOGGER = Logger.getLogger(PatternDefinition.class.getName());

	private int parameterCount;
	private VerarbeitungsArt art;
	private Pattern pattern;

	public PatternDefinition(int parameterCount, VerarbeitungsArt art, Pattern pattern) {
		super();
		this.parameterCount = parameterCount;
		this.art = art;
		this.pattern = pattern;
	}

	public int getParameterCount() {
		return parameterCount;
	}

	public VerarbeitungsArt getArt() {
		return art;
	}

	public Pattern getPattern() {
		return pattern;
	}

	public boolean matches(String text) {
		finer(LOGGER,"text={0}",text);
		finer(LOGGER,"pattern={0}", pattern.pattern());
		return pattern.matcher(text).find();
	}
}
