package nwl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import lists.playerList.ListPlayer;
import net.md_5.bungee.api.ChatColor;

public class Events implements Listener {
	@EventHandler
	public void login(PlayerLoginEvent e) {
		if (Main.getBlackList().contains(e.getPlayer())) {
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.isOp()) {
					player.sendMessage(
							Main.Prefix
									+ ChatColor.RED
									+ "警告:"
									+ e.getPlayer().getName()
									+ "はBlackListに登録されています");
				}
			}
		} else {
			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
				ListPlayer joinPlayer;
				if ((joinPlayer = Main.getWhiteList().getListPlayer(e.getPlayer())) == null
						&& (joinPlayer = Main.getNoobList().getListPlayer(e.getPlayer())) == null) {
					Main.getNoobList().add(e.getPlayer());
				} else {
					if (!joinPlayer.getIps().contains(e.getPlayer().getAddress().getAddress().getHostName())) {
						joinPlayer.addIp(e.getPlayer().getAddress().getAddress().getHostName());
					}
				}
			}, 0L);
		}
	}
}
