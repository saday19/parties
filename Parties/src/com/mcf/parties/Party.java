package com.mcf.parties;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Party extends Command {

	public Party() {
		super("party");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		if(sender instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) sender;
			if(args.length == 1) {
				if(args[0].equalsIgnoreCase("create")) {
					PartyHandler.create(player);
					return;
				} else if(args[0].equalsIgnoreCase("disband")) {
					PartyHandler.disbandParty(player);
					return;
				} else if(args[0].equalsIgnoreCase("chat")) {
					PartyHandler.toggleChat(player);
					return;
				} else if(args[0].equalsIgnoreCase("warp")) {
					PartyHandler.warp(player);
					return;
				} else if(args[0].equalsIgnoreCase("leave")) {
					PartyHandler.leaveParty(player);
					return;
				} else if(args[0].equalsIgnoreCase("accept")) {
					PartyHandler.attemptInviteAccept(player);
					return;
				} else if(args[0].equalsIgnoreCase("view")) {
					PartyHandler.view(player);
					return;
				}
			} else if(args.length == 2) {
				ProxiedPlayer receiver = Instance.get().getProxy().getPlayer(args[1]);
				if(args[0].equalsIgnoreCase("promote")) {
					PartyHandler.promote(player, receiver);
					return;
				} else if(args[0].equalsIgnoreCase("invite")) {
					PartyHandler.attemptInvite(player, receiver);
					return;
				} else if(args[0].equalsIgnoreCase("remove")) {
					PartyHandler.remove(player, receiver);
					return;
				}
			}
			sendHelp(sender);
		}
	}
	
	private void sendHelp(CommandSender sender) {
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&a&l         Party Commands")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party create &7- Creates a party.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party disband &7- Disbands a player's party.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party promote <name> &7- Makes the player the party leader.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party invite <name> &7- Invites a player to your party.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party remove <name> &7- Removes a player from your party.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party accept &7- Accepts your most recent invite.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party leave &7- Leave your current party.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party chat &7- Toggles party chat.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party warp &7- Warp all party members to your server.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "&7 - &a/party view &7- View the members in your party.")));
		sender.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', "")));
	}
	
}
