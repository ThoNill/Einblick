/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package einblick;

import org.junit.Test;

import einblick.AusgabeData;
import einblick.Main;
import einblick.Parser;
import einblick.PropertiesLoader;
import einblick.TextBlock;
import einblick.TextBlockSpeicher;
import einblick.TextMusterung;
import einblick.VerarbeitungsArt;

import static einblick.LogHelper.*;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryTest {
	private final static Logger LOGGER = Logger.getLogger(LibraryTest.class.getName());

	@Test
	public void trenner() {
		nächstePosition(0, ", das ist");
		nächstePosition(1, " , das ist");
		nächstePosition(3, " \\,, das ist");
		nächstePosition(7, " das ist");
		nächstePosition(5, " das ) ist");
		nächstePosition(7, " das ()) ist");
		nächstePosition(7, " das {}) ist");
		nächstePosition(" das ( , )  ist".length() - 1, " das ( , )  ist");
		try {
			nächstePosition(-1, " das [ , )  ist");
			fail("Exception erwartet");
		} catch (Exception ex) {
			// ok Exception wird erwartet
		}
		try {
			nächstePosition(-1, " das ] , )  ist");
			fail("Exception erwartet");
		} catch (Exception ex) {
			// ok Exception wird erwartet
		}

	}

	public void nächstePosition(int pos, String text) {
		Parser parser = new Parser();
		assertEquals(pos, parser.posNächsterTrenner(text.toCharArray(), 0, true));
	}

	@Test
	public void parser() {
		checkParser(", das ist", "", "", VerarbeitungsArt.TEXT, ", das ist");
		checkParser(", das ist\n", "", "", VerarbeitungsArt.TEXT, ", das ist\n");
		checkParser(" ßblk(eins,zwei) ", " ", " ", VerarbeitungsArt.TEXT_BLOCK, "eins", "zwei");
		checkParser(" ßblk(eins,zwei)   ", " ", "   ", VerarbeitungsArt.TEXT_BLOCK, "eins", "zwei");
		checkParser("ßblk(eins,zwei)", "", "", VerarbeitungsArt.TEXT_BLOCK, "eins", "zwei");
		checkParser("ßblk(eins,zwei,drei,vier)", "", "", VerarbeitungsArt.TEXT_BLOCK, "eins", "zwei", "drei,vier");
		checkParser("ßblk(eins)", "", "", VerarbeitungsArt.TEXT_BLOCK, "eins");
		checkParser("ßblk()", "", "", VerarbeitungsArt.TEXT_BLOCK);
	}

	private void checkParser(String text, String prefix, String postfix, VerarbeitungsArt art, String... params) {
		Parser Parser = new Parser();
		TextMusterung expected = new TextMusterung(art, Arrays.asList(params), prefix, postfix);
		List<TextMusterung> textTeilListe = Parser.createTextMusterungList(text);

		finer(LOGGER,"expected: {0} ",expected);
		finer(LOGGER,"real:  {0} ", textTeilListe.get(0));
		assertTrue(expected.equals(textTeilListe.get(0)));
	}

	@Test
	public void multipleTextTeil() {
		checkParser(", das ist", 1);
		checkParser(", das ist\n", 1);
		checkParser(" ßblk(eins,zwei) ", 1);
		checkParser(" ßblk(eins,zwei)   ", " ", "   ", VerarbeitungsArt.TEXT_BLOCK, "eins", "zwei");
		checkParser("ßblk(eins,zwei)", 1);
		checkParser("ßblk(eins,zwei) ßblk(eins1,zwei1) ßblk(eins,zwei) e", 3, "", " ", " ", " e");
		checkParser("ßblk(eins,zwei,drei,vier)", 1);
		checkParser("ßblk(eins)", 1);
		checkParser("ßblk()", 1);
	}

	private void checkParser(String text, int count) {
		Parser Parser = new Parser();
		List<TextMusterung> textTeilListe = Parser.createTextMusterungList(text);
		assertEquals(count, textTeilListe.size());
	}

	private void checkParser(String text, int count, String prefix, String... postfix) {
		Parser parser = new Parser();
		List<TextMusterung> textTeilListe = parser.createTextMusterungList(text);
		assertEquals(count, textTeilListe.size());
		assertEquals(prefix, textTeilListe.get(0).getPrefix());
		int pos = 0;
		for (TextMusterung cf : textTeilListe) {
			if (pos > 0) {
				assertEquals("", cf.getPrefix());
			}
			assertEquals(postfix[pos], cf.getPostfix());
			pos++;
		}
	}

	@Test
	public void testTextBlockSpeicher1() {
		TextBlock block = new TextBlock("anonym", "anonym",VerarbeitungsArt.TEXT_BLOCK);
		TextBlockSpeicher speicher = new TextBlockSpeicher("target/",new AusgabeData().codeSTGroup);
		block.addTextTeil(createDefinition("test1", "title1"));
		block.addTextTeil(createTextTeil("Test1"));
		block.addTextTeil(createTextTeil("Test2"));
		block.addTextTeil(createTextTeil(""));
		block.addTextTeil(createTextTeil(""));
		block.addTextTeil(createTextTeil(""));

		block.addTextTeil(createTextTeil("Test2"));
		block.addTextTeil(createDefinition("test2", "title2"));
		block.addTextTeil(createTextTeil("Test2.1"));
		speicher.accept(block);
		TextBlock block1 = speicher.get("test1");
		assertNotNull(block1);
		assertEquals(5, block1.size());
		assertEquals(1, speicher.get("test2").size());

	}

	@Test
	public void testTextBlockSpeicher2() {
		TextBlock block = new TextBlock("anonym", "anonym",VerarbeitungsArt.TEXT_BLOCK);
		TextBlockSpeicher speicher = new TextBlockSpeicher("target/",new AusgabeData().codeSTGroup);
		block.addTextTeil(createDefinition("test1", "title1"));
		block.addTextTeil(createTextTeil("Test1"));
		block.addTextTeil(createTextTeil("Test2"));
		block.addTextTeil(createTextTeil(""));
		block.addTextTeil(createTextTeil(""));
		block.addTextTeil(createTextTeil(""));

		block.addTextTeil(createTextTeil("Test2"));
		block.addTextTeil(createAusgabe("target.txt", "title2"));
		block.addTextTeil(createTextTeil("Test2.1"));
		speicher.accept(block);
		TextBlock block1 = speicher.get("test1");
		assertNotNull(block1);
		assertEquals(5, block1.size());
		assertEquals(1, speicher.get("target.txt").size());
		speicher.performWrites();
	}

	@Test
	public void testTextBlockSpeicher3() {
		TextBlock block = new TextBlock("anonym", "anonym",VerarbeitungsArt.TEXT_BLOCK);
		TextBlockSpeicher speicher = new TextBlockSpeicher("target/",new AusgabeData().codeSTGroup);
		block.addTextTeil(createDefinition("insert", "Text der eingefügt wird"));
		block.addTextTeil(createTextTeil("Test1"));
		block.addTextTeil(createTextTeil("Test2"));
		block.addTextTeil(createTextTeil(""));
		block.addTextTeil(createTextTeil(""));
		block.addTextTeil(createTextTeil(""));

		block.addTextTeil(createAusgabe("targetInsert.txt", "title2"));
		block.addTextTeil(createTextTeil("vor"));
		block.addTextTeil(createReferenz("insert"));
		block.addTextTeil(createTextTeil("nach"));
		speicher.accept(block);
		TextBlock insertBlock = speicher.get("insert");
		assertNotNull(insertBlock);
		assertEquals(5, insertBlock.size());
		assertEquals(3, speicher.get("targetInsert.txt").size());
		speicher.performWrites();
	}

	private TextMusterung createTextTeil(String text) {
		List<String> parameter = new LinkedList<String>();
		parameter.add(text);
		return new TextMusterung(VerarbeitungsArt.TEXT, parameter, "", "");
	}

	private TextMusterung createDefinition(String name, String title) {
		List<String> parameter = new LinkedList<String>();
		parameter.add(name);
		parameter.add(title);
		return new TextMusterung(VerarbeitungsArt.TEXT_BLOCK, parameter, "", "");
	}

	private TextMusterung createAusgabe(String dateiName, String title) {
		List<String> parameter = new LinkedList<String>();
		parameter.add(dateiName);
		parameter.add(title);
		return new TextMusterung(VerarbeitungsArt.TEXT_AUSGABE, parameter, "", "");
	}

	private TextMusterung createReferenz(String name) {
		List<String> parameter = new LinkedList<String>();
		parameter.add(name);
		return new TextMusterung(VerarbeitungsArt.REFERENZ, parameter, "", "");
	}

	@Test
	public void ladeProperties() {
		try {
			new PropertiesLoader().readProperties("./src/test/resources/test.properties");
		} catch (IOException e) {
			fail("Fehler beim Einlesen der Properties");
		}
	}
	
	@Test
	public void ladeLeereProperties() {
		try {
			new PropertiesLoader().readProperties("./src/test/resources/testLeere.properties");
		} catch (IOException e) {
			fail("Fehler beim Einlesen der Properties");
		}
	}

	@Test
	public void ladeFehlendeProperties() {
		try {
			new PropertiesLoader().readProperties("fehlt.properties");
			fail("Ausnahme erwartet, aber es gab keine");
		} catch (Exception e) {
		}
	}

	@Test
	public void main() {
		try {
			Main.main(new String[] {"./src/test/resources/test.w","target/main/"});
			File dokuFile = new File("target/main/src/test/resources/test.md");
			assertTrue(dokuFile.exists());
			File progFile = new File("target/main/ergebnis.txt");
			assertTrue(progFile.exists());

		} catch (Exception e) {
			fail("Ausnahme aufgetreten");
		}
	}

}