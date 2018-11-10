package lists.playerList;

import java.time.LocalDateTime;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ListPlayer {
	private String name;
	private UUID uuid;
	private LocalDateTime date;
	private boolean mark;

	public ListPlayer(String name, UUID uuid, LocalDateTime localDateTime, boolean mark) {
		this.setName(name);
		this.setUUID(uuid);
		this.setDate(localDateTime);
		this.setMark(mark);
	}

	public ListPlayer(Player p) {
		this(p.getName(), p.getUniqueId(),
				LocalDateTime.now(), false);
	}

	public void sendMessage(String... messages) {
		Player player;
		if ((player = Bukkit.getServer().getPlayer(uuid)) != null) {
			player.sendMessage(messages);
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
}
