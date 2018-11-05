package lists.playerList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import nwl.Main;

public class NWList {
	File file;
	private String fileName;
	private List<ListPlayer> list;

	public NWList(ListNames listName) {
		fileName = listName.getFileName();
		file = new File(Main.getPlugin().getDataFolder().getPath() + "\\" + fileName);
		reload();
	}

	public void reload() {
		fileExistsCheck();
		list = new ArrayList<>();
		loadFile(fileName);
	}
	
	private void fileExistsCheck() {
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadFile(String fileName) {
		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = in.readLine()) != null) {
				add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void add(String line) throws ParseException {
		String[] strings = line.split(" ");
		boolean mark = false;
		if (strings[0].startsWith("★")) {
			strings[0] = strings[0].substring(1);
			mark = true;
		}
		ListPlayer addPlayer = new ListPlayer(
				strings[0],
				UUID.fromString(strings[1]),
				LocalDateTime.parse(strings[2]),
				mark);

		list.add(addPlayer);
	}

	public boolean add(Player player) {
		ListPlayer addPlayer = new ListPlayer(player);

		if (contains(player)) {
			return false;
		}
		list.add(addPlayer);
		addToFile(addPlayer);

		return true;

	}

	public boolean remove(Player player) {
		ListPlayer removePlayer;
		if ((removePlayer = getListPlayer(player)) != null) {
			list.remove(removePlayer);
			saveToFile();
			return true;
		}
		return false;
	}

	public boolean contains(Player player) {
		for (ListPlayer listPlayer : list) {
			if (listPlayer.getUUID().equals(player.getUniqueId())) {
				return true;
			}
		}
		return false;
	}

	public boolean setMark(Player player) {
		ListPlayer markPlayer;
		if ((markPlayer = getListPlayer(player)) != null) {
			markPlayer.setMark(true);
			saveToFile();
			return true;
		}
		return false;
	}

	public void addToFile(ListPlayer listPlayer) {
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(file, true);
		} catch (IOException e) {
			throw new RuntimeException("ファイルへの追記に失敗しました");
		}
		PrintWriter pWriter = new PrintWriter(new BufferedWriter(fWriter));

		writeToFile(listPlayer, pWriter);
	}

	public void saveToFile() {
		//TODO
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new RuntimeException("ファイルへの書き込みに失敗しました");
		}
		PrintWriter pWriter = new PrintWriter(new BufferedWriter(fWriter));
		for (ListPlayer listPlayer : list) {
			writeToFile(listPlayer, pWriter);
		}
	}

	private void writeToFile(ListPlayer listPlayer, PrintWriter pWriter) {
		StringBuilder write;
		if (listPlayer.isMark()) {
			write = new StringBuilder("★");
		} else {
			write = new StringBuilder();
		}
		write.append(listPlayer.getName());
		write.append(" ");
		write.append(listPlayer.getUUID());
		write.append(" ");
		write.append(listPlayer.getDate());

		pWriter.println(write);

		pWriter.close();
	}

	public List<ListPlayer> getList() {
		return list;
	}

	public ListPlayer getListPlayer(Player player) {
		for (ListPlayer listPlayer : list) {
			if (listPlayer.getUUID().equals(player.getUniqueId())) {
				return listPlayer;
			}
		}
		return null;
	}
}
