package einblick;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

public abstract class AusgabeInEinerDatei implements Ausgabeform {
	private Writer writer;
	private String filename;
	private TextBlockSpeicher speicher;
	private String targetDir;

	private STGroupFile group;

	public AusgabeInEinerDatei(String filename, TextBlockSpeicher speicher,String targetDir,
			String groupName) {
		this.filename = filename;
		this.speicher = speicher;
		this.targetDir = targetDir;
		this.group = new STGroupFile(groupName, '$', '$');

	}

	public String getFilename() {
		return filename;
	}

	protected void createFile() {
		try {
			String fileName = changeDateiname(this.filename);
			Path parent = Path.of(fileName).getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}
			this.writer = new BufferedWriter(new FileWriter(fileName, StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw new EinblickException(e);
		}
	}

	private String changeDateiname(String filename) {
		return targetDir + filename;
	}

	@Override
	public void print(String text) {
		try {
			writer.write(text);
		} catch (IOException e) {
			throw new EinblickException(e);
		}
	}

	@Override
	public void applyTemplate(List<String> parameter) {
		String templateName = parameter.get(0);
		String name = parameter.get(1);
		String title = parameter.get(2);
		applyTemplate(templateName, name, title);
	}

	@Override
	public void referenz(String name) {
	}

	protected void close() {
		try {
			writer.close();
		} catch (IOException e) {
			throw new EinblickException(e);
		}
	}

	public void perform() {
		createFile();
		this.perform(getFilename());
		close();
	}

	protected void perform(String name) {
		this.perform(getTextBlock(name));
	}

	public TextBlock getTextBlock(String name) {
		return speicher.get(name);
	}

	protected void applyTemplate(String templateName, String name, String title) {
		ST t = group.getInstanceOf(templateName);
		t.add("name", name);
		t.add("title", title);
		this.print(t.render());
	}

}