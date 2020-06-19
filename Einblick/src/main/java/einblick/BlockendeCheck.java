package einblick;

import static einblick.LogHelper.finer;

import java.util.logging.Logger;

public class BlockendeCheck {
	private static final Logger LOGGER = Logger.getLogger(BlockendeCheck.class.getName());

	private int blankCount;
	private boolean blockEnde = false;

	public BlockendeCheck() {
		super();
	}

	void ifBlockende(String text, Runnable run) {
		blockendePrüfen(text);
		finer(LOGGER, "text= {0} {1}", text, isBlockEnde());
		if (isBlockEnde()) {
			run.run();
			clear();
		}
	}

	private void blockendePrüfen(String text) {
		if (text.isBlank()) {
			finer(LOGGER, "Leer! {0}", blankCount);
			blankCount++;
		} else {
			finer(LOGGER, " {0}", text);
			blankCount = 0;
		}
		if (blankCount >= 3 || nurEinPunktInDerZeile(text)) {
			blockEnde = true;
		}
	}

	private boolean nurEinPunktInDerZeile(String text) {
		return text.strip().length() == 1 && text.charAt(0) == '.';
	}

	public boolean isBlockEnde() {
		return blockEnde;
	}

	public void clear() {
		blankCount = 0;
		blockEnde = false;
	}
}