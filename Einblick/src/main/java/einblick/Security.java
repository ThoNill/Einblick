package einblick;

import java.io.File;
import java.nio.file.Path;

public class Security {

	private Security() {
		super();
	}

	public static void checkRegexp(String regex) {
		if (regex.contains("(") || regex.contains(".") || regex.contains("*") || regex.contains("+")) {
			throw new EinblickException("Die reguläre Expression " + regex + " ist nicht erlaubt");
		}
	}

	public static File checkFilename(String filename) {
		if (filename.contains("..")) {
			throw new EinblickException("Der Dateiname " + filename + " ist nicht erlaubt");
		}
		Path normalizedPath = new File(filename).toPath().normalize();
		return normalizedPath.toFile();
	}

}
