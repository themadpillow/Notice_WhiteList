package nwl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import lists.messageList.MessageList;
import lists.playerList.ListNames;
import lists.playerList.NWList;
import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin {
	public static final String Prefix = ChatColor.GOLD + "§l[NWL] " + ChatColor.RESET;
	private static JavaPlugin plugin;

	private static NWList whiteList;
	private static NWList blackList;
	private static NWList noobList;
	private static MessageList messageList;

	@Override
	public void onEnable() {
		setPlugin(this);
		if (!getPlugin().getDataFolder().exists()) {
			getPlugin().getDataFolder().mkdirs();
		}

		Bukkit.getScheduler().runTaskLater(this, () -> {
			Bukkit.getConsoleSender().sendMessage(Prefix + ChatColor.GREEN + "Listの取得・更新を行っています");
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.isOp()) {
					player.sendMessage(Prefix + ChatColor.GREEN + "Listの取得・更新を行っています");
				}
			}
		}, 0L);

		setWhiteList(new NWList(ListNames.WhiteList));
		setBlackList(new NWList(ListNames.BlackList));
		setNoobList(new NWList(ListNames.NoobList));
		setMessageList(new MessageList());

		Bukkit.getScheduler().runTaskLater(this, () -> {
			Bukkit.getConsoleSender().sendMessage(Prefix + ChatColor.GREEN + "Listの取得・更新が完了しました");
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (player.isOp()) {
					player.sendMessage(Prefix + ChatColor.GREEN + "Listの取得・更新が完了しました");
				}
			}
		}, 0L);

		Bukkit.getPluginManager().registerEvents(new Events(), this);
		getCommand("nwl").setExecutor(new Commands());
	}

	@Override
	public void onDisable() {
		whiteList.saveToFile();
		blackList.saveToFile();
		noobList.saveToFile();
		messageList.saveToFile();
	}

	public static JavaPlugin getPlugin() {
		return plugin;
	}

	private void setPlugin(JavaPlugin plugin) {
		Main.plugin = plugin;
	}

	public static void reloadConfigs() {
		whiteList.reload();
		blackList.reload();
		noobList.reload();
		messageList.reload();
	}

	public static NWList getWhiteList() {
		return whiteList;
	}

	private void setWhiteList(NWList whiteList) {
		this.whiteList = whiteList;
	}

	public static NWList getBlackList() {
		return blackList;
	}

	private void setBlackList(NWList blackList) {
		this.blackList = blackList;
	}

	public static MessageList getMessageList() {
		return messageList;
	}

	private void setMessageList(MessageList messageList) {
		this.messageList = messageList;
	}

	public static NWList getNoobList() {
		return noobList;
	}

	private void setNoobList(NWList noobList) {
		this.noobList = noobList;
	}
}
