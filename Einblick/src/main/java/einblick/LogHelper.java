package einblick;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LogHelper {

	/* wird nicht gebraucht*/
	private LogHelper() {
		super();
	}

	public static void finer(Logger logger, String format, Object... args) {
		logger.log(Level.FINER, format, args);
	}

}
