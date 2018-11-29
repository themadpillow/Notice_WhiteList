package lists.playerList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ListPlayer {
	private String name;
	private UUID uuid;
	private LocalDateTime date;
	private boolean mark;
	private List<String> ips = new ArrayList<>();

	public ListPlayer(String name, UUID uuid, LocalDateTime localDateTime, boolean mark, String... ips) {
		this.setName(name);
		this.setUUID(uuid);
		this.setDate(localDateTime);
		this.setMark(mark);
		if (ips != null) {
			this.ips.addAll(Arrays.asList(ips));
		}
	}

	public ListPlayer(Player p) {
		this(p.getName(), p.getUniqueId(),
				LocalDateTime.now(), false, p.getAddress().getAddress().getHostName());
	}

	public void sendMessage(String... messages) {
		Player player;
		if ((player = Bukkit.getServer().getPlayer(uuid)) != null) {
			player.sendMessage(messages);
		}
	}
	
	public void playSound(Sound sound, float volume) {
		Player player;
		if ((player = Bukkit.getServer().getPlayer(uuid)) != null) {
			player.playSound(player.getLocation(), sound, volume, volume);
		}
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public UUID getUUID() {
		return uuid;
	}

	private void setUUID(UUID uuid) {
		this.uuid = uuid;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime localDateTime) {
		this.date = localDateTime;
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public List<String> getIps() {
		return ips;
	}

	public void addIp(String ip) {
		ips.add(ip);
	}
}
