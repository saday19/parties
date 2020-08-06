package com.mcf.parties;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class OnlineScheduledTask implements Runnable {
	
	private ProxiedPlayer player;
	
	public OnlineScheduledTask(ProxiedPlayer player) {
		this.player = player;
	}
	
	@Override
	public void run() {
		if(PartyHandler.isInParty(player)) {
			if(!player.isConnected()) {
				if(PartyHandler.isLeader(player)) {
					PartyHandler.disbandParty(player);
				} else {
					PartyHandler.leaveParty(player);
				}
			}
		}
	}

}
