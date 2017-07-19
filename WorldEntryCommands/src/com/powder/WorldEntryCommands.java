package com.powder;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class WorldEntryCommands extends JavaPlugin implements Listener {

	public void onEnable() {
		this.saveDefaultConfig();
		Bukkit.getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getTo().getWorld() != event.getFrom().getWorld()) {
			String leavecmd = "";
			String leavepath = event.getFrom().getWorld().getName() + "." + "leavecommand";
			if (this.getConfig().contains(leavepath))
				leavecmd = this.getConfig().getString(leavepath);
			String joincmd = "";
			String joinpath = event.getTo().getWorld().getName() + "." + "joincommand";
			if (this.getConfig().contains(joinpath))
				joincmd = this.getConfig().getString(joinpath);
			if (!leavecmd.isEmpty())
				for (String s: leavecmd.split("\\|"))
					event.getPlayer().performCommand(s);
			if (!joincmd.isEmpty())
				for (String s: joincmd.split("\\|"))
					event.getPlayer().performCommand(s);
		}
	}

}
