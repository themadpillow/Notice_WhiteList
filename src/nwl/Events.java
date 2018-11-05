package nwl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import net.md_5.bungee.api.ChatColor;

public class Events implements Listener {
	@EventHandler
	public void login(PlayerLoginEvent e) {
		if (!Main.getWhiteList().contains(e.getPlayer())) {
			if (Main.getBlackList().contains(e.getPlayer())) {
				for (Player player : Bukkit.getOnlinePlayers()) {
					if (player.isOp()) {
						player.sendMessage(
								Main.Prefix + ChatColor.RED + "警告:" + e.getPlayer().getName() + "はBlackListに登録されています");
					}
				}
			} else if (!Main.getNoobList().contains(e.getPlayer())) {
				Main.getNoobList().add(e.getPlayer());
			}
		}
	}
}
