package com.powder;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

public class JsonRules extends JavaPlugin {
	public static List<String> RULES = new ArrayList<String>();
	private void reloadRules() {
		if (!this.getDataFolder().exists())
			this.getDataFolder().mkdir();
		File rfile = new File(this.getDataFolder(), "rules.txt");
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(rfile));
		} catch (FileNotFoundException e) {
			try {
				Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(rfile)));
				writer.write("{\"text\":\"\"}");
				writer.flush();
				writer.close();
			} catch (IOException e1) {
				Bukkit.getLogger().severe("Error: Exception occurered in JsonRules");
			}
			return;
		}
		try {
			RULES = new ArrayList<String>();
			String line = br.readLine();

		    while (line != null) {
		        RULES.add(line);
		        line = br.readLine();
		    }
		    br.close();
		} catch (IOException e) {
			Bukkit.getLogger().severe("Error: Exception occurered in JsonRules");
		}
		
		
	}

	@Override
	public void onEnable() {
		reloadRules();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
		if (command.getName().equalsIgnoreCase("rules")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				PacketContainer chat = new PacketContainer(PacketType.Play.Server.CHAT);
				try {
					for (String s: RULES) {
						chat.getChatComponents().write(0, WrappedChatComponent.fromJson(s));
						ProtocolLibrary.getProtocolManager().sendServerPacket(player, chat);
					}
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (RuntimeException e) {
					player.sendMessage("\u00a7dIncorrect JSON: " + e.getCause().getMessage());
				}
				return true;
			}
		} else if (command.getName().equalsIgnoreCase("reloadrules")) {
			sender.sendMessage("\u00a7aRules have been reloaded.");
			reloadRules();
			return true;
		}
		return false;
	}
}
