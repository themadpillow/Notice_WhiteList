package nwl;

import java.util.Map;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import lists.messageList.Message;
import lists.playerList.ListPlayer;
import net.md_5.bungee.api.ChatColor;

public class Commands implements CommandExecutor {
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (cmd.getName().equalsIgnoreCase("nwl")) {
			if (args.length == 0
					|| args[0].equalsIgnoreCase("help")) {
				sendAllHelp(sender);
			} else if (args[0].equalsIgnoreCase("whitelist")) {
				whiteListCommand(sender, args);
			} else if (args[0].equalsIgnoreCase("blacklist")) {
				blackListCommand(sender, args);
			} else if (args[0].equalsIgnoreCase("notice")) {
				noticeCommand(sender, args);
			} else if (args[0].equalsIgnoreCase("reload")) {
				Main.reloadConfigs();
				sender.sendMessage(Main.Prefix + "config及びListDataを読み込みました");
			} else {
				sendAllHelp(sender);
			}

			return true;
		}

		return false;
	}

	private void whiteListCommand(CommandSender sender, String[] args) {
		if (args.length < 3 || args[1].equalsIgnoreCase("help")) {
			sendWhiteListHelp(sender);
			return;
		}

		ListPlayer listPlayer;
		if ((listPlayer = Main.getNoobList().getListPlayer(args[2])) == null
				&& (listPlayer = Main.getWhiteList().getListPlayer(args[2])) == null
				&& (listPlayer = Main.getBlackList().getListPlayer(args[2])) == null) {
			sender.sendMessage(Main.Prefix + "指定されたPlayerは見つかりませんでした");
			return;
		}

		if (args[1].equalsIgnoreCase("add")) {
			if (Main.getWhiteList().add(listPlayer)) {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "をWhiteListに追加しました");
				if (Main.getNoobList().contains(listPlayer)) {
					Main.getNoobList().remove(listPlayer);
				}
			} else {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "は既にWhiteListに追加済みです");
			}
		} else if (args[1].equalsIgnoreCase("remove")) {
			if (Main.getWhiteList().remove(listPlayer)) {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "をWhiteListから削除しました");
			} else {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "はWhiteListに存在しません");
			}
		} else if (args[1].equalsIgnoreCase("mark")) {
			if (Main.getWhiteList().setMark(listPlayer)) {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "に★を付与しました");
			} else {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "はWhiteListに存在しません");
			}
		} else if (args[1].equalsIgnoreCase("list")) {
			if (Main.getWhiteList().contains(listPlayer)) {
				sender.sendMessage(ChatColor.GOLD + "=========================================");
				sender.sendMessage(ChatColor.GOLD + "MCID: " + ChatColor.BLUE + listPlayer.getName());
				sender.sendMessage(ChatColor.GOLD + "UUID: " + ChatColor.BLUE + listPlayer.getUUID().toString());
				sender.sendMessage(ChatColor.GOLD + "Mark: " + ChatColor.BLUE + listPlayer.isMark());
				sender.sendMessage(ChatColor.GOLD + "Time: " + ChatColor.BLUE + listPlayer.getDate().toString());
				sender.sendMessage(ChatColor.GOLD + "=========================================");
			} else {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "はWhiteListに存在しません");
			}
		}
	}

	private void blackListCommand(CommandSender sender, String[] args) {
		if (args.length < 3 || args[1].equalsIgnoreCase("help")) {
			sendBlackListHelp(sender);
			return;
		}

		ListPlayer listPlayer;
		if ((listPlayer = Main.getNoobList().getListPlayer(args[2])) == null
				&& (listPlayer = Main.getWhiteList().getListPlayer(args[2])) == null
				&& (listPlayer = Main.getBlackList().getListPlayer(args[2])) == null) {
			sender.sendMessage(Main.Prefix + "指定されたPlayerは見つかりませんでした");
			return;
		}

		if (args[1].equalsIgnoreCase("add")) {
			if (Main.getBlackList().add(listPlayer)) {
				if (Main.getNoobList().contains(listPlayer)) {
					Main.getNoobList().remove(listPlayer);
				}
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "をBlackListに追加しました");
			} else {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "は既にBlackListに追加済みです");
			}
		} else if (args[1].equalsIgnoreCase("remove")) {
			if (Main.getBlackList().remove(listPlayer)) {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "をBlackListから削除しました");
			} else {
				sender.sendMessage(Main.Prefix + listPlayer.getName() + "はBlackListに存在しません");
			}
		}
	}

	private void noticeCommand(CommandSender sender, String[] args) {
		if (args.length < 2 || args[1].equalsIgnoreCase("help")) {
			sendNoticeHelp(sender);
			return;
		}

		if (args[1].equalsIgnoreCase("create")) {
			if (args.length != 4
					|| !args[2].matches("[0-9]*")) {
				sendNoticeHelp(sender);
				return;
			}

			int number = Integer.parseInt(args[2]);
			String title = args[3];
			if (Main.getMessageList().create(number, title)) {
				sender.sendMessage(Main.Prefix + number + "番 Title:" + title + " を作成しました");
			} else {
				sender.sendMessage(Main.Prefix + number + "番は既に使用されています");
				sender.sendMessage(Main.Prefix + number + "番: " + Main.getMessageList().getMessage(number));
			}
		} else if (args[1].equalsIgnoreCase("add")) {
			if (args.length != 4
					|| !args[2].matches("[0-9]*")) {
				sendNoticeHelp(sender);
				return;
			}

			int number = Integer.parseInt(args[2]);
			String text = args[3];
			if (Main.getMessageList().add(number, text)) {
				sender.sendMessage(Main.Prefix + number + "番に追記しました");
			} else {
				sender.sendMessage(Main.Prefix + number + "番は存在しません");
			}
		} else if (args[1].equalsIgnoreCase("remove")) {
			if (args.length != 3
					|| !args[2].matches("[0-9]*")) {
				sendNoticeHelp(sender);
				return;
			}

			int number = Integer.parseInt(args[2]);
			if (Main.getMessageList().remove(number)) {
				sender.sendMessage(Main.Prefix + number + "番を削除しました");
			} else {
				sender.sendMessage(Main.Prefix + number + "番は存在しません");
			}
		} else if (args[1].equalsIgnoreCase("list")) {
			Map<Integer, Message> messageMap = Main.getMessageList().getMessageMap();
			Set<Integer> keys = messageMap.keySet();
			sender.sendMessage(ChatColor.GOLD + "=========================================");
			for (int key : keys) {
				sender.sendMessage(ChatColor.GREEN + (key + ": " + messageMap.get(key).getTitle()));
			}
			sender.sendMessage(ChatColor.GOLD + "=========================================");
		} else if (args[1].equalsIgnoreCase("send")) {
			if (args.length != 3
					|| !args[2].matches("[0-9]*")) {
				sendNoticeHelp(sender);
				return;
			}

			int number = Integer.parseInt(args[2]);
			Message message;
			if ((message = Main.getMessageList().getMessage(number)) != null) {
				String[] texts = message.getTexts().toArray(new String[0]);
				for (ListPlayer listPlayer : Main.getWhiteList().getList()) {
					if (!listPlayer.isMark()) {
						listPlayer.sendMessage(message.getTitle());
						listPlayer.sendMessage(texts);
					}
				}
				sender.sendMessage(Main.Prefix + number + "番:" + message.getTitle() + "を送信しました");
			} else {
				sender.sendMessage(Main.Prefix + "指定されたお知らせ番号は存在しません");
			}
		}
	}

	private void sendAllHelp(CommandSender sender) {
		sendWhiteListHelp(sender);
		sendBlackListHelp(sender);
		sendNoticeHelp(sender);
		sender.sendMessage(ChatColor.RED + "/nwl reload"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "config及びListの再読み込み");
	}

	private void sendWhiteListHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "/nwl whitelist add <MCID>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "NWL-WhiteListにUUIDを保存");
		sender.sendMessage(ChatColor.RED + "/nwl whitelist remove <MCID>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "NWL-WhiteListからUUIDを削除");
		sender.sendMessage(ChatColor.RED + "/nwl whitelist mark <MCID>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "NWL-WhiteListのUUIDに★を付与");
		sender.sendMessage(ChatColor.RED + "/nwl whiteist list <MCID>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "指定したプレイヤーのUUID・マークの有無・書き込み日付の表示");
	}

	private void sendBlackListHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "/nwl blacklist add <MCID>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "NWL-BlackListにUUIDを保存");
		sender.sendMessage(ChatColor.RED + "/nwl blacklist remove <MCID>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "NWL-BlackListからUUIDを削除");
	}

	private void sendNoticeHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "/nwl notice create <お知らせ番号> <題名>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "お知らせを作成");
		sender.sendMessage(ChatColor.RED + "/nwl notice add <お知らせ番号> <本文>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "指定したお知らせに本文追加");
		sender.sendMessage(ChatColor.RED + "/nwl notice remove <お知らせ番号>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "お知らせ削除");
		sender.sendMessage(ChatColor.RED + "/nwl notice list"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "お知らせリストを表示");
		sender.sendMessage(ChatColor.RED + "/nwl notice send <お知らせ番号>"
				+ ChatColor.YELLOW + " => "
				+ ChatColor.GREEN + "WhiteListのプレイヤーに指定したお知らせを送る");
	}
}
