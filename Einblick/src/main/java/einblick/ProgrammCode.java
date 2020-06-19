package einblick;

public class ProgrammCode extends AusgabeInEinerDatei {

	public ProgrammCode(String name, TextBlockSpeicher speicher,
			String targetDir,
			String sourceSTGroup) {
		super(name, speicher,targetDir, sourceSTGroup);
	}

	@Override
	public void referenz(String name) {
		perform(name);
	}

	@Override
	public void hidden(String name) {
		perform(name);
	}

	@Override
	public void startBlock(String name, String title) {
		// No action needed 
	}

	@Override
	public void open(String name, String title) {
		// No action needed 
	}
}
