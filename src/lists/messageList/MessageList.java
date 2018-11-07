package lists.messageList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nwl.Main;

public class MessageList {
	private Map<Integer, Message> messageMap;
	private File file;

	public MessageList() {
		messageMap = new HashMap<>();
		file = new File(Main.getPlugin().getDataFolder().getPath() + "\\message.yml");
		reload();
	}

	public void reload() {
		fileExistsCheck();
		loadFile();
	}

	private void fileExistsCheck() {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadFile() {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			String title = null;
			int number = 0;
			List<String> texts = new ArrayList<>();

			String line;
			while ((line = in.readLine()) != null) {
				if (!line.matches(" .*")) {
					if (!texts.isEmpty()) {
						Message message = new Message(title, texts);
						messageMap.put(number, message);
						texts.clear();
					}
					title = line.replaceAll(":", "");
				} else if (line.startsWith(" Number:")) {
					number = Integer.parseInt(line.substring(9));
				} else if (line.startsWith("  ")) {
					texts.add(line.substring(2));
				} else if (line.startsWith("\n")) {
					Message message = new Message(title, texts);
					messageMap.put(number, message);
					texts.clear();
				}
			}
			if (!texts.isEmpty()) {
				Message message = new Message(title, texts);
				messageMap.put(number, message);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean create(int number, String title) {
		if (messageMap.containsKey(number)) {
			return false;
		}

		Message message = new Message(title);
		messageMap.put(number, message);
		addToFile(number, message);
		return true;
	}

	public boolean add(int number, String text) {
		Message message;
		if ((message = messageMap.get(number)) != null) {
			message.addText(text);
			saveToFile();
			return true;
		}
		return false;
	}

	public boolean remove(int number) {
		boolean remove = messageMap.remove(number) != null;
		saveToFile();
		return remove;
	}

	public Message getMessage(int number) {
		return messageMap.get(number);
	}

	public Map<Integer, Message> getMessageMap() {
		return messageMap;
	}

	private void addToFile(int number, Message message) {
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new RuntimeException("ファイルへの書き込みに失敗しました");
		}
		PrintWriter pWriter = new PrintWriter(new BufferedWriter(fWriter));

		writeToFile(number, messageMap.get(number), pWriter);
	}

	private void saveToFile() {
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new RuntimeException("ファイルへの書き込みに失敗しました");
		}
		PrintWriter pWriter = new PrintWriter(new BufferedWriter(fWriter));

		for (int key : messageMap.keySet()) {
			writeToFile(key, messageMap.get(key), pWriter);
		}
	}

	private void writeToFile(int number, Message message, PrintWriter pWriter) {
		pWriter.println(message.getTitle().replaceAll("§", "\\$") + ":");
		pWriter.println(" " + "Number: " + number);
		pWriter.println(" " + "Messages:");
		for (String text : message.getTexts()) {
			pWriter.println("  " + text.replaceAll("§", "\\$"));
		}
		pWriter.println();
		pWriter.close();
	}
}
