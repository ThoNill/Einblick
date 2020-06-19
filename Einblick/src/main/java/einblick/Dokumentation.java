package einblick;

public class Dokumentation extends AusgabeInEinerDatei {

	private boolean ausgeben = true;
	private BlockendeCheck blockendeCheck = new BlockendeCheck();

	public Dokumentation(String name, TextBlockSpeicher speicher, String targetDir,
			String dokuSTGroup) {
		super(name, speicher, targetDir, dokuSTGroup);
		this.ausgeben = true;
	}

	@Override
	public void print(String text) {
		if (ausgeben) {
			super.print(text);
		} else {
			blockendeCheck.ifBlockende(text,() -> ausgeben = true);
		}
	}

	@Override
	public void perform(TextBlock block) {
		createFile();
		block.stream().forEach(c -> this.perform(c));
		close();
	}

	@Override
	public void startBlock(String name, String title) {
		applyTemplate("startBlock", name, title);
	}

	@Override
	public void open(String name, String title) {
		applyTemplate("open", name, title);
	}

	@Override
	public void hidden(String name) {
		this.ausgeben = false;
	}

	@Override
	public void referenz(String name) {
		if (ausgeben) {
			TextBlock block = getTextBlock(name);
			if (!VerarbeitungsArt.VERSTECKT.equals(block.getArt())) {
				applyTemplate("reference", name, block.getTitle());
			}
		}
	}

}
