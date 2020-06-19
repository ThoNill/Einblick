package einblick;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesLoader {
	private ParserData parserData;
	private AusgabeData ausgabeData;

	public PropertiesLoader() {
		parserData = new ParserData();
		ausgabeData = new AusgabeData();
	}

	public void readProperties(String filename) throws IOException {
		File file = Security.checkFilename(filename);
		if (file.exists()) {
			Properties properties = ausDateiLaden(file);
			propertiesSetzen(properties);
		} else {
			throw new EinblickException("Die Datei " + filename + " ist nicht vorhanden");
		}
	}

	private Properties ausDateiLaden(File file) throws IOException {
		Properties properties = new Properties();
		FileInputStream inStream = new FileInputStream(file);
		properties.load(inStream);
		inStream.close();
		return properties;
	}

	private void propertiesSetzen(Properties properties) {
		if (properties.containsKey("escape")) {
			parserData.escape = properties.getProperty("escape").charAt(0);
		}
		if (properties.containsKey("trenner")) {
			parserData.trenner = properties.getProperty("trenner").charAt(0);
		}
		if (properties.containsKey("stop")) {
			parserData.stop = properties.getProperty("stop").charAt(0);
		}
		if (properties.containsKey("startTextBlock")) {
			parserData.startTextBlock = properties.getProperty("startTextBlock");
		}
		if (properties.containsKey("startTextAusgabe")) {
			parserData.startTextAusgabe = properties.getProperty("startTextAusgabe");
		}
		if (properties.containsKey("referenz")) {
			parserData.referenz = properties.getProperty("referenz");
		}
		if (properties.containsKey("template")) {
			parserData.template = properties.getProperty("template");
		}
		if (properties.containsKey("startVersteckt")) {
			parserData.startVersteckt = properties.getProperty("startVersteckt");
		}
		if (properties.containsKey("dokuType")) {
			ausgabeData.dokuType = properties.getProperty("dokuType");
		}
		if (properties.containsKey("dokuSTGroup")) {
			ausgabeData.dokuSTGroup = properties.getProperty("dokuSTGroup");
		}
		if (properties.containsKey("codeSTGroup")) {
			ausgabeData.codeSTGroup = properties.getProperty("codeSTGroup");
		}
		if (properties.containsKey("target")) {
			ausgabeData.target = properties.getProperty("target");
		}
	}

	public ParserData getParserData() {
		return parserData;
	}

	public AusgabeData getAusgabeData() {
		return ausgabeData;
	}

}
