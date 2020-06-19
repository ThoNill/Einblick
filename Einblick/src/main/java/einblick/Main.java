package einblick;

import java.io.IOException;
import java.util.logging.Logger;

public class Main {
	private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {

		if (args.length == 0) {
			System.err.println(" java " + Main.class.getName() + " <Dateiname> <Zielverzeichnis> <PropertyDatei>");
			System.exit(-1);
		}

		if (args.length > 3) {
			System.err.println("zuviele Argumente");
			System.exit(-2);
		}

		if (args.length <= 3) {
			starten(args);
		}

	}

	private static void starten(String[] args) {
		String fileName = args[0];
		try {
			Reader reader = new Reader();
			parameterSetzen(args, reader);
			reader.readAndWriteCodeAndDoku(fileName);
		} catch (IOException e) {
			LOGGER.severe(e.getMessage());
			System.exit(-3);
		}
	}

	private static void parameterSetzen(String[] args, Reader reader) throws IOException {
		if (args.length == 2) {
			Security.checkFilename(args[1]);
			reader.setTarget(args[1]);
		}
		if (args.length == 3) {
			Security.checkFilename(args[2]);
			reader.readProperties(args[2]);
		}
	}
}
