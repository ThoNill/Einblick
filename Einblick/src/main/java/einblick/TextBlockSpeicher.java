package einblick;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import javax.annotation.processing.Generated;

public class TextBlockSpeicher extends HashMap<String, TextBlock> implements Consumer<TextMusterung> {

	private static final long serialVersionUID = 2358643385100984743L;

	private TextBlock aktuellerBlock;
	private BlockendeCheck blockendeCheck = new BlockendeCheck();

	private List<ProgrammCode> writerBlocks = new LinkedList<>();
	private String targetDir;
	private String sourceSTGroup;

	public TextBlockSpeicher(String targetDir, String sourceSTGroup) {
		super();
		this.targetDir = targetDir;
		this.sourceSTGroup = sourceSTGroup;
	}

	public void accept(TextBlock gesamtBlock) {
		gesamtBlock.stream().forEach(this);
	}

	@Override
	public void accept(TextMusterung c) {
		switch (c.getArt()) {
		case TEXT:
			addTextTeil(c);
			aktuellerBlockZurücksetzen(c.getName());
			return;
		case TEXT_BLOCK:
		case TEXT_AUSGABE:
			activate(c.getArt(), c.getName(), c.getTitle());
			return;
		case VERSTECKT:
			activate(c.getArt(), c.getName(), "Hidden");
			return;
		case REFERENZ:
		case TEMPLATE:
			addTextTeil(c);
			return;
		default:
			throw new EinblickException("Not possible");
		}
	}

	private void addTextTeil(TextMusterung textTeil) {
		if (aktuellerBlock != null) {
			aktuellerBlock.add(textTeil);
		}
	}

	private void aktuellerBlockZurücksetzen(String text) {
		if (aktuellerBlock != null) {
			blockendeCheck.ifBlockende(text,() -> aktuellerBlock=null);
		}
	}

	private TextBlock activate(VerarbeitungsArt art, String name, String title) {
		TextBlock alterBlock = get(name);
		if (alterBlock == null) {
			TextBlock block = new TextBlock(name, title, art);
			put(name, block);
			aktuellerBlock = block;
			if (VerarbeitungsArt.TEXT_AUSGABE.equals(art)) {
				ProgrammCode programm = new ProgrammCode(name, this,targetDir, sourceSTGroup);
				writerBlocks.add(programm);
			}
		} else {
			aktuellerBlock = alterBlock;
		}
		return aktuellerBlock;
	}

	public void performWrites() {
		writerBlocks.stream().forEach(prog -> prog.perform());
	}

	@Override
	@Generated(value = { "eclipse" })
	@SuppressWarnings("all") 
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((aktuellerBlock == null) ? 0 : aktuellerBlock.hashCode());
		result = prime * result + ((blockendeCheck == null) ? 0 : blockendeCheck.hashCode());
		result = prime * result + ((sourceSTGroup == null) ? 0 : sourceSTGroup.hashCode());
		result = prime * result + ((targetDir == null) ? 0 : targetDir.hashCode());
		result = prime * result + ((writerBlocks == null) ? 0 : writerBlocks.hashCode());
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
		TextBlockSpeicher other = (TextBlockSpeicher) obj;
		if (aktuellerBlock == null) {
			if (other.aktuellerBlock != null)
				return false;
		} else if (!aktuellerBlock.equals(other.aktuellerBlock))
			return false;
		if (blockendeCheck == null) {
			if (other.blockendeCheck != null)
				return false;
		} else if (!blockendeCheck.equals(other.blockendeCheck))
			return false;
		if (sourceSTGroup == null) {
			if (other.sourceSTGroup != null)
				return false;
		} else if (!sourceSTGroup.equals(other.sourceSTGroup))
			return false;
		if (targetDir == null) {
			if (other.targetDir != null)
				return false;
		} else if (!targetDir.equals(other.targetDir))
			return false;
		if (writerBlocks == null) {
			if (other.writerBlocks != null)
				return false;
		} else if (!writerBlocks.equals(other.writerBlocks))
			return false;
		return true;
	}

}
