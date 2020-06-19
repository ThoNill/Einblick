package einblick;

class ParserData {
	char escape;
	char trenner;
	char stop;
	String startTextBlock;
	String startTextAusgabe;
	String referenz;
	String template;
	String startVersteckt;

	ParserData() {
		this('\\', ',', ')', "ßblk", "ßout", "ßref", "ßtemp", "ßhide");
	}

	ParserData(char escape, char trenner, char stop, String startTextBlock, String startTextAusgabe,
			String referenz, String template, String startVersteckt) {
		this.escape = escape;
		this.trenner = trenner;
		this.stop = stop;
		this.startTextBlock = startTextBlock;
		this.startTextAusgabe = startTextAusgabe;
		this.referenz = referenz;
		this.template = template;
		this.startVersteckt = startVersteckt;
	}
}