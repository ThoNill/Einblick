package einblick;

import java.util.List;

import javax.annotation.processing.Generated;

public class TextMusterung {

	private VerarbeitungsArt art;
	private List<String> parameter;
	private String prefix;
	private String postfix;

	public TextMusterung(VerarbeitungsArt art, List<String> parameter, String prefix, String postfix) {
		super();
		this.art = art;
		this.parameter = parameter;
		this.prefix = prefix;
		this.postfix = postfix;
	}

	public VerarbeitungsArt getArt() {
		return art;
	}

	public List<String> getParameter() {
		return parameter;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getPostfix() {
		return postfix;
	}

	public String getName() {
		if (parameter.isEmpty()) {
			throw new EinblickException("No name");
		}
		return parameter.get(0);
	}

	public String getTitle() {
		if (parameter.isEmpty()) {
			throw new EinblickException("No title");
		}
		if (parameter.size() == 1)
			return parameter.get(0);
		if (parameter.size() == 2)
			return parameter.get(1);
		return "";
	}

	@Override
	@Generated(value = { "eclipse" })
	@SuppressWarnings("all") 
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((art == null) ? 0 : art.hashCode());
		result = prime * result + ((parameter == null) ? 0 : parameter.hashCode());
		result = prime * result + ((postfix == null) ? 0 : postfix.hashCode());
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		return result;
	}

	@Override
	@Generated(value = { "eclipse" })
	@SuppressWarnings("all") 
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TextMusterung other = (TextMusterung) obj;
		if (art == null) {
			if (other.art != null)
				return false;
		} else if (!art.equals(other.art)) {
			return false;
		}
		if (parameter == null) {
			if (other.parameter != null)
				return false;
		} else if (!parameter.equals(other.parameter)) {
			return false;
		}
		if (postfix == null) {
			if (other.postfix != null) {
				return false;
			}
		} else if (!postfix.equals(other.postfix)) {
			return false;
		}
		if (prefix == null) {
			if (other.prefix != null) {
				return false;
			}
		} else if (!prefix.equals(other.prefix)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "TextTeil [art=" + art + ", parameter=" + parameter + ", prefix=" + prefix + ", postfix=" + postfix
				+ "]";
	}

}
