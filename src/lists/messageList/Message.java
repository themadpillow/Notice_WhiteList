package lists.messageList;

import java.util.ArrayList;
import java.util.List;

public class Message {
	private String title;
	private List<String> texts;

	public Message(String title, List<String> texts) {
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
		this.title = title;
	}

	public List<String> getTexts() {
		return texts;
	}

	public void addText(String text) {
		texts.add(text);
	}

	private void setTexts(List<String> texts) {
		this.texts = new ArrayList<>();
		this.texts.addAll(texts);
	}
}
