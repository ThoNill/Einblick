package einblick;

import java.util.List;

public interface Ausgabeform {

	default void perform(TextBlock block) {
		block.stream().forEach(c -> this.perform(c));
	}

	default void perform(TextMusterung c) {
		performDefault(c);
	}

	default void performDefault(TextMusterung c) {
		switch (c.getArt()) {
		case TEXT:
			print(c.getName());
			return;
		case TEXT_BLOCK:
			startBlock(c.getName(), c.getTitle());
			return;
		case TEXT_AUSGABE:
			open(c.getName(), c.getTitle());
			return;
		case VERSTECKT:
			hidden(c.getName());
			return;
		case REFERENZ:
			referenz(c.getName());
			return;
		case TEMPLATE:
			applyTemplate(c.getParameter());
			return;
		default:
			throw new EinblickException("Not possible");
		}
	}

	void print(String text);

	void startBlock(String name, String title);

	void open(String name, String title);

	void applyTemplate(List<String> parameter);

	void referenz(String name);

	void hidden(String name);

}
