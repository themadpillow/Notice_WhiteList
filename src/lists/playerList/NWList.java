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

import nwl.GetLatestMCID;
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
		if (!file.exists()) {
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

		UUID uuid = UUID.fromString(strings[1]);
		String name = new GetLatestMCID(uuid).get();
		if (name == null) {
			name = strings[0];
		}

		ListPlayer addPlayer = new ListPlayer(
				name,
				uuid,
				LocalDateTime.parse(strings[2]),
				mark);

		list.add(addPlayer);
	}

	public boolean add(Player player) {
		ListPlayer addPlayer = new ListPlayer(player);
		return add(addPlayer);
	}

	public boolean add(ListPlayer listPlayer) {
		if (contains(listPlayer)) {
			return false;
		}
		listPlayer.setDate(LocalDateTime.now());
		list.add(listPlayer);
		addToFile(listPlayer);

		return true;
	}

	public boolean remove(UUID UUID) {
		ListPlayer removePlayer;
		if ((removePlayer = getListPlayer(UUID)) != null) {
			list.remove(removePlayer);
			saveToFile();
			return true;
		}
		return false;
	}

	public boolean remove(Player player) {
		return remove(player.getUniqueId());
	}

	public boolean remove(ListPlayer listPlayer) {
		return remove(listPlayer.getUUID());
	}

	public boolean contains(UUID UUID) {
		for (ListPlayer listPlayer : list) {
			if (listPlayer.getUUID().equals(UUID)) {
				return true;
			}
		}
		return false;
	}

	public boolean contains(Player player) {
		return contains(player.getUniqueId());
	}

	public boolean contains(ListPlayer listPlayer) {
		return contains(listPlayer.getUUID());
	}

	public boolean setMark(UUID UUID) {
		ListPlayer markPlayer;
		if ((markPlayer = getListPlayer(UUID)) != null) {
			markPlayer.setMark(true);
			saveToFile();
			return true;
		}
		return false;
	}

	public boolean setMark(Player player) {
		return setMark(player.getUniqueId());
	}

	public boolean setMark(ListPlayer listPlayer) {
		return setMark(listPlayer.getUUID());
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
		pWriter.close();
	}

	public void saveToFile() {
		//TODO
		FileWriter fWriter = null;
		try {
			fWriter = new FileWriter(file);
		} catch (IOException e) {
			throw new RuntimeException("ファイルへの書き込みに失敗しました");
		}
		BufferedWriter bWriter = new BufferedWriter(fWriter);
		PrintWriter pWriter = new PrintWriter(bWriter);
		for (ListPlayer listPlayer : list) {
			writeToFile(listPlayer, pWriter);
		}

		pWriter.close();
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
	}

	public List<ListPlayer> getList() {
		return list;
	}

	public ListPlayer getListPlayer(UUID UUID) {
		for (ListPlayer listPlayer : list) {
			if (listPlayer.getUUID().equals(UUID)) {
				return listPlayer;
			}
		}
		return null;
	}

	public ListPlayer getListPlayer(Player player) {
		return getListPlayer(player.getUniqueId());
	}

	public ListPlayer getListPlayer(String MCID) {
		for (ListPlayer listPlayer : list) {
			if (listPlayer.getName().equalsIgnoreCase(MCID)) {
				return listPlayer;
			}
		}
		return null;
	}
}
