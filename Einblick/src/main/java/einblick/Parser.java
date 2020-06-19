package einblick;

import static einblick.LogHelper.finer;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Deque;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser implements Function<String, List<TextMusterung>> {
	private static final Logger LOGGER = Logger.getLogger(Parser.class.getName());

	private ParserData data;

	private PatternDefinition patternTextBlock;
	private PatternDefinition patternTextAusgabe;
	private PatternDefinition patternReferenz;
	private PatternDefinition patternTemplate;
	private PatternDefinition patternVersteckt;

	Deque<Character> klammern = new ArrayDeque<>();

	public Parser() {
		this(new ParserData());
	}

	public Parser(ParserData data) {
		this.data = data;
		createPatterns();
	}

	private void createPatterns() {
		patternTextBlock = createPattern(data.startTextBlock, VerarbeitungsArt.TEXT_BLOCK, 2);
		patternTextAusgabe = createPattern(data.startTextAusgabe, VerarbeitungsArt.TEXT_AUSGABE, 2);
		patternReferenz = createPattern(data.referenz, VerarbeitungsArt.REFERENZ, 2);
		patternTemplate = createPattern(data.template, VerarbeitungsArt.TEMPLATE, 3);
		patternVersteckt = createPattern(data.startVersteckt, VerarbeitungsArt.VERSTECKT, 1);
	}

	@Override
	public List<TextMusterung> apply(String text) {
		return createTextMusterungList(text + "\n");
	}

	List<TextMusterung> createTextMusterungList(String text) {
		Optional<PatternDefinition> oPattern = searchPattern(text);
		if (oPattern.isPresent()) {
			PatternDefinition pattern = oPattern.get();
			return createPatternDataList(pattern, text);
		} else {
			return Arrays.asList(new TextMusterung(VerarbeitungsArt.TEXT, Arrays.asList(text), "", ""));
		}
	}

	private Optional<PatternDefinition> searchPattern(String text) {
		if (patternTextBlock.matches(text))
			return Optional.of(patternTextBlock);
		if (patternTextAusgabe.matches(text))
			return Optional.of(patternTextAusgabe);
		if (patternReferenz.matches(text))
			return Optional.of(patternReferenz);
		if (patternTemplate.matches(text))
			return Optional.of(patternTemplate);
		if (patternVersteckt.matches(text))
			return Optional.of(patternVersteckt);
		return Optional.empty();
	}

	private List<TextMusterung> createPatternDataList(PatternDefinition pattern, String text) {
		Matcher match = pattern.getPattern().matcher(text);
		List<TextMusterung> list = new LinkedList<>();
		boolean findPattern = match.find();
		int bearbeitetePatterns = 0;
		int startPattern = match.start();
		int endPattern = match.end() + 1;
		while (findPattern) {
			int endOfParameters = endeDerParameter(text.toCharArray(), endPattern);

			finer(LOGGER,"text={0}",text);
			finer(LOGGER,"startPattern={0} ",startPattern);
			finer(LOGGER,"endPattern={0} ", endPattern);
			finer(LOGGER,"endParameter={0} ",endOfParameters);

			char[] cText = text.substring(endPattern, endOfParameters).toCharArray();
			List<String> params = sammleParameter(cText, pattern.getParameterCount());

			String prefix = prefixBestimmen(text, startPattern, bearbeitetePatterns);
			String postfix = text.substring(endOfParameters);

			findPattern = match.find();
			if (findPattern) {
				startPattern = match.start();
				endPattern = match.end() + 1;
				postfix = text.substring(endOfParameters, startPattern);
			}

			list.add(new TextMusterung(pattern.getArt(), params, prefix, postfix));
			bearbeitetePatterns++;
		}
		if (list.isEmpty()) {
			throw new EinblickException("Wrong call, this Pattern must match");
		}
		return list;
	}

	private int endeDerParameter(char[] text, int startPos) {
		return posNächsterTrenner(text, startPos, false) + 1;
	}

	private List<String> sammleParameter(char[] text, int paramCount) {
		List<String> liste = new ArrayList<>(10);
		int startPos = 0;
		if (paramCount > 0) {
			int pc = paramCount;
			while (pc > 0) {
				startPos = addEinzelnenParameter(text, paramCount, liste, startPos, pc);
				pc--;
			}
			if (isTrenner(text[startPos - 1])) {
				addEinzelnenParameter(text, paramCount, liste, startPos, pc);
			}
		}
		return liste;
	}

	private String prefixBestimmen(String text, int startPattern, int bearbeitetePatterns) {
		return (bearbeitetePatterns > 0) ? "" : text.substring(0, startPattern);
	}

	private int addEinzelnenParameter(char[] text, int paramCount, List<String> liste, int startPos, int pc) {
		int endPos = posNächsterTrenner(text, startPos, paramCount == -1 || pc > 0);
		if (startPos < endPos) {
			String parameter = new String(text, startPos, endPos - startPos);
			liste.add(parameter);
		}
		return endPos + 1;
	}

	private PatternDefinition createPattern(String regex, VerarbeitungsArt art, int paramCount) {
		Security.checkRegexp(regex);
		return new PatternDefinition(paramCount, art, Pattern.compile(regex));
	}

	public int posNächsterTrenner(char[] text, int pos, boolean beachteTrenner) {
		finer(LOGGER,"Rext={0} ",new String(text));
		for (int p = pos; p < text.length; p++) {
			char c = text[p];
			finer(LOGGER,"c={0} pos={1}",c,p);
			if (isEscape(c)) {
				return posNächsterTrenner(text, p+2, beachteTrenner);
			}
			if (isParameterEnde(beachteTrenner, c)) {
				return p;
			}
			if (startKlammer(c)) {
				klammern.push(c);
			}
			if (endeKlammer(c)) {
				checkKlammerung(text, c);
			}
		}
		return text.length - 1;
	}

	private boolean isParameterEnde(boolean beachteTrenner, char c) {
		return klammern.isEmpty() && (isStop(c) || (beachteTrenner && isTrenner(c)));
	}

	private char startZeichen(char c) {
		switch (c) {
		case ')':
			return '(';
		case '}':
			return '{';
		case ']':
			return '[';
		default:
			return c;
		}
	}

	private boolean startKlammer(char c) {
		return c == '(' || c == '{' || c == '[';
	}

	private boolean endeKlammer(char c) {
		return c == ')' || c == '}' || c == ']';
	}

	private boolean isStop(char c) {
		return (c == data.stop );
	}

	private boolean isEscape(char c) {
		return c == data.escape;
	}

	private boolean isTrenner(char c) {
		return c == data.trenner;
	}
	
	private void checkKlammerung(char[] text, char c) {
		if (klammern.isEmpty()) {
			throw new EinblickException("Ende Klammer ohne Start " + new String(text));
		}
		if (startZeichen(c) != klammern.pop()) {
			throw new EinblickException("Falsche Klammerung " + new String(text));
		}
	}
}
