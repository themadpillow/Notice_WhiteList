package lists.messageList;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private String title;
	private List<String> texts;

	public Message(String title, List<String> texts) {
		this.texts = new ArrayList<>();
		this.setTitle(title);
		this.setTexts(texts);
	}

	public Message(String title) {
		this(title, new ArrayList<>());
	}

	public String getTitle() {
		return title;
	}

	private void setTitle(String title) {
		this.title = title.replaceAll("\\$", "§");
		;
	}

	public List<String> getTexts() {
		return texts;
	}

	public void addText(String text) {
		texts.add(text.replaceAll("\\$", "§"));
	}

	private void setTexts(List<String> texts) {
		for (String text : texts) {
			addText(text);
		}
	}
}
