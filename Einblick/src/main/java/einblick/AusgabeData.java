package einblick;

class AusgabeData {
	String target;
	String dokuType;
	String dokuSTGroup;
	String codeSTGroup;

	AusgabeData() {
		this("target/", ".md", "weave.stg", "tangle.stg");
	}

	AusgabeData(String target, String dokuType, String dokuSTGroup, String codeSTGroup) {
		super();
		this.target = target;
		this.dokuType = dokuType;
		this.dokuSTGroup = dokuSTGroup;
		this.codeSTGroup = codeSTGroup;
	}
}
