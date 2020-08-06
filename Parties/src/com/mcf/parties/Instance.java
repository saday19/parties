package com.mcf.parties;

import net.md_5.bungee.api.plugin.Plugin;

public class Instance extends Plugin {

	private static Instance plugin;
	
	public  void onEnable() {
		plugin = this;
		getProxy().getPluginManager().registerCommand(this, new Party());
		getProxy().getPluginManager().registerListener(this, new ConnectionHandler());
	}
	
	public static Instance get() {
		return plugin;
	}
	
}
