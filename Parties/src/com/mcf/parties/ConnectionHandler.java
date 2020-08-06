package com.mcf.parties;

import java.util.concurrent.TimeUnit;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ConnectionHandler implements Listener {

	@EventHandler
	public void onChat(ChatEvent e ) {
		if(e.getSender() instanceof ProxiedPlayer) {
			ProxiedPlayer player = (ProxiedPlayer) e.getSender();
			if(PartyHandler.getChattingPlayers().contains(player)) {
				String message = e.getMessage();
				if(message.indexOf('/') == 0) return;
				e.setCancelled(true);
				PartyHandler.sendPartyMessage(player, message);
			}
		}
	}
	
	@EventHandler
	public void onDisconnect(PlayerDisconnectEvent e) {
		if(PartyHandler.isInParty(e.getPlayer())) {
			Instance.get().getProxy().getScheduler().schedule(Instance.get(), new OnlineScheduledTask(e.getPlayer()), 60 * 5, TimeUnit.SECONDS);
		}
	}
	
}
