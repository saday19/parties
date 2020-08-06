package com.mcf.parties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PartyHandler {

	private static HashMap<String, List<ProxiedPlayer>> parties = new HashMap<String, List<ProxiedPlayer>>();
	
	private static HashMap<ProxiedPlayer, String> invites = new HashMap<ProxiedPlayer, String>();
	
	private static ArrayList<ProxiedPlayer> chatting = new ArrayList<ProxiedPlayer>();
	
	
	private static TextComponent not_leader = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou must be the party leader to use this command!"));
	private static TextComponent not_in_party = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou need to be in a party to use this command!"));
	
	
	public static ArrayList<ProxiedPlayer> getChattingPlayers() {
		return chatting;
	}
	
	public static void attemptInvite(ProxiedPlayer player, ProxiedPlayer receiver) {
		if(isLeader(player)) {
			String party = getPartyNameForProxiedPlayer(player);
			if(receiver != null) {
				invites.put(receiver, party);
				TextComponent message1 = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&a" + player.getName() + " has invited you to their party!"));
				TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7Click &e&lHERE&7 to join the party."));
				message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept"));
				receiver.sendMessage(message1);
				receiver.sendMessage(message);
				player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aInvite sent!")));
			} else {
				player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cTarget is not online!")));
			}
		} else 
			player.sendMessage(not_leader);
	}
	
	public static void attemptInviteAccept(ProxiedPlayer player) {
		if(invites.containsKey(player)) {
			String party = invites.get(player);
			ArrayList<ProxiedPlayer> list = (ArrayList<ProxiedPlayer>) getPartyMembers(party);
			list.add(player);
			invites.remove(player);
			for(ProxiedPlayer member : list) {
				member.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&a" + player.getName() + " has joined the party!")));
			}
		} else  
			player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou have no pending invites!")));
	}
	
	public static String getPartyNameForProxiedPlayer(ProxiedPlayer player) {
		for(String leader : parties.keySet()) {
			if(parties.get(leader).contains(player)) return leader;
		}
		return null;
	}
	
	public static boolean isInParty(ProxiedPlayer player) {
		if(parties.get(getPartyNameForProxiedPlayer(player)) != null) {
			return true;
		}
		for(String key : parties.keySet()) {
			if(parties.get(key).contains(player)) return true;
		}
		return false;
	}
	
	public static void create(ProxiedPlayer player) {
		if(!isInParty(player)) {
			List<ProxiedPlayer> list = new ArrayList<ProxiedPlayer>();
			list.add(player);
			parties.put(player.getName(), list);
			player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aYou successfully created a party.")));
		} else {
			player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou must leave your party if you want to create another!")));
		}
	}
	
	public static void detatchFromParty(ProxiedPlayer player) {
		String party = getPartyNameForProxiedPlayer(player);
		List<ProxiedPlayer> list = parties.get(party);
		list.remove(player);
		parties.put(party, list);
	}
	
	public static void leaveParty(ProxiedPlayer player) {
		if(getPartyNameForProxiedPlayer(player).equals(player.getName())) {
			player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou must promote someone in your party to leave.")));
			return;
		}
		String party = getPartyNameForProxiedPlayer(player);
		detatchFromParty(player);
		player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aYou have successfully left the party.")));
		for(ProxiedPlayer member : getPartyMembers(party)) {
			member.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&c" + player.getName() + " has left the party.")));
		}
	}
	
	public static boolean isLeader(ProxiedPlayer player) {
		if(parties.keySet().contains(player.getName())) return true;
		return false;
	}
	
	public static void promote(ProxiedPlayer promoter, ProxiedPlayer promotee) {
		if(isLeader(promoter)) {
			List<ProxiedPlayer> list = parties.get(promoter.getName());
			String name = promotee.getName();
			parties.remove(promoter.getName());
			parties.put(name, list);
			promoter.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aYou have transferred party leadership to " + promotee.getName() + ".")));
			promotee.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aYou are now the party leader.")));
		} else {
			promoter.sendMessage(not_in_party);
		}
	}
	
	public static List<ProxiedPlayer> getPartyMembers(String name) {
		return parties.get(name);
	}
	
	public static void disbandParty(ProxiedPlayer player) {
		if(isLeader(player)) {
			String party = getPartyNameForProxiedPlayer(player);
			for(ProxiedPlayer member : getPartyMembers(party)) {
				member.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYour party has been disbanded!")));
			}
			parties.remove(party);
		} else {
			player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou must be the party leader to use this command!")));
		}
	}
	
	public static void toggleChat(ProxiedPlayer player) {
		if(isInParty(player)) {
			if(chatting.contains(player)) {
				chatting.remove(player);
				player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cParty chat disabled!")));
			} else {
				chatting.add(player);
				player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aParty chat enabled!")));
			}
		} else {
			player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou must be in a party to use this!")));
		}
	}
	
	public static void sendPartyMessage(ProxiedPlayer player, String message) {
		if(isInParty(player)) {
			if(chatting.contains(player)) {
				for(ProxiedPlayer member : getPartyMembers(getPartyNameForProxiedPlayer(player))) {
					member.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7[&aParty&7] &f" + player.getName() + ":&a " + message)));
				}
			}
		} else {
			player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou must be in a party to use this!")));
		}
	}

	public static void remove(ProxiedPlayer player, ProxiedPlayer receiver) {
		if(isLeader(player)) {
			if(receiver != null) {
				detatchFromParty(receiver);
				receiver.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou have been removed from the party!")));
				for(ProxiedPlayer member : getPartyMembers(getPartyNameForProxiedPlayer(player))) {
					member.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&c" + receiver.getName() + " has been removed from the party!")));
				}
			} else {
				player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cTarget not found!")));
			}
		} else {
			player.sendMessage(not_leader);
		}
	}
	
	public static void warp(ProxiedPlayer player) {
		ServerInfo info = player.getServer().getInfo();
		String server = info.getName();
		if(server.contains("village")) {
			player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&cYou cannot warp your party to this server!")));
			return;
		}
		for(ProxiedPlayer member : getPartyMembers(getPartyNameForProxiedPlayer(player))) {
			if(member != player) {
				if(member.isConnected()) {
					member.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7" + player.getName() + " has summoned you to " + server + ".")));
					member.connect(info);
				}
			}
		}
		player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7Warping party members to " + server + ".")));
	}
	
	public static void view(ProxiedPlayer player) {
		if(isInParty(player)) {
			String name = getPartyNameForProxiedPlayer(player);
			String message = "&a" + name + "'s party: &7";
			for(ProxiedPlayer member : getPartyMembers(name)) {
				message = message + member.getName() + ", ";
			}
			message = message.substring(0, message.length() - 2);
			player.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', message)));
		}
	}
	
}
