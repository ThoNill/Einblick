package einblick;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class Reader implements Consumer<List<TextMusterung>> {
	private static final String IST_KEINE_W_DATEI = " ist keine .w Datei";
	private static final String DIE_DATEI = "Die Datei ";
	private ParserData parserData;
	private AusgabeData ausgabeData;
	private Parser parser;

	private TextBlockSpeicher speicher;
	private TextBlock dokumentationBlock;
	private Dokumentation dokumentation;

	public Reader() {
		parserData = new ParserData();
		ausgabeData = new AusgabeData();
		parser = new Parser(parserData);
	}

	public void readProperties(String filename) throws IOException {
		PropertiesLoader loader = new PropertiesLoader();
		loader.readProperties(filename);
		parserData = loader.getParserData();
		ausgabeData = loader.getAusgabeData();
		parser = new Parser(parserData);

	}

	public void readAndWriteCodeAndDoku(String fileName) {
		File file = checkFilename(fileName);
		try {
			read(file.toString());
			speicher.accept(dokumentationBlock);
			writeCode();
			writeDoku();
		} catch (IOException e) {
			throw new EinblickException(DIE_DATEI + fileName + " konnte nicht verarbeitet werden");
		}
	}

	public void setTarget(String target) {
		this.ausgabeData.target = target;
	}

	private void read(String filename) throws IOException {
		speicher = new TextBlockSpeicher(ausgabeData.target, ausgabeData.dokuSTGroup);
		String dokufilename = filename.replaceAll("\\.w$", ausgabeData.dokuType);
		dokumentationBlock = new TextBlock(dokufilename, "main", VerarbeitungsArt.TEXT_AUSGABE);
		dokumentation = new Dokumentation(dokufilename, speicher, ausgabeData.target, ausgabeData.dokuSTGroup);
		Files.lines(Path.of(filename)).map(parser).forEach(this);
	}

	private File checkFilename(String filename) {
		File file = Security.checkFilename(filename);
		if (!file.exists()) {
			throw new IllegalArgumentException(DIE_DATEI + filename + " existiert nicht");
		}
		if (!file.isFile()) {
			throw new IllegalArgumentException("Der Pfad " + filename + " ist keine Datei");
		}
		if (!file.getPath().endsWith(".w")) {
			throw new IllegalArgumentException(DIE_DATEI + filename + IST_KEINE_W_DATEI);
		}
		return file;
	}

	private void writeCode() {
		speicher.performWrites();
	}

	private void writeDoku() {
		dokumentation.perform(dokumentationBlock);
	}

	@Override
	public void accept(List<TextMusterung> list) {
		list.forEach(textTeil -> accept(textTeil));
	}

	private void accept(TextMusterung textTeil) {
		if (!VerarbeitungsArt.TEXT.equals(textTeil.getArt())) {
			addSimpleTextTeil(textTeil.getPrefix());
			dokumentationBlock.add(textTeil);
			addSimpleTextTeil(textTeil.getPostfix());
		} else {
			dokumentationBlock.add(textTeil);
		}

	}

	private void addSimpleTextTeil(String text) {
		dokumentationBlock.addTextTeil(new TextMusterung(VerarbeitungsArt.TEXT, Arrays.asList(text), "", ""));
	}

}
