package einblick;

import java.util.LinkedList;

import javax.annotation.processing.Generated;

public class TextBlock extends LinkedList<TextMusterung> {

	private static final long serialVersionUID = 4496105378474092485L;
	private String name;
	private String title;
	private VerarbeitungsArt art;

	public TextBlock(String name, String title, VerarbeitungsArt art) {
		super();
		this.name = name;
		this.title = title;
		this.art = art;
	}

	public String getName() {
		return name;
	}

	public String getTitle() {
		return title;
	}

	public void addTextTeil(TextMusterung textTeil) {
		add(textTeil);
	}

	public VerarbeitungsArt getArt() {
		return art;
	}

	@Override
	@Generated(value = { "eclipse" })
	@SuppressWarnings("all") 
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((art == null) ? 0 : art.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	@Generated(value = { "eclipse" })
	@SuppressWarnings("all") 
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextBlock other = (TextBlock) obj;
		if (art != other.art)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
