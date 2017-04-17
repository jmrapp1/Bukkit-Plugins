package com.jmr.pmanager;

import java.util.HashMap;

import net.minecraft.server.v1_8_R3.Tuple;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.jmr.classes.MagicClass;
import com.jmr.classes.MagicClassManager;

public class PlayerManager {

	private static final PlayerManager instance = new PlayerManager();

	private HashMap<Player, PlayerInformation> playerInfo = new HashMap<Player, PlayerInformation>();
	
	private PlayerManager() {
	
	}
	
	public void addPlayer(Player player, PlayerInformation info) {
		playerInfo.put(player, info);
	}
	
	public void removePlayer(Player player) {
		playerInfo.remove(player);
	}
	
	public void savePlayer(Player player) {
		Database.getInstance().savePlayer(playerInfo.get(player));
	}
	
	public void loadPlayer(Player player) {
		PlayerInformation info = Database.getInstance().getPlayerInformation(player.getUniqueId().toString());
		info.setName(player.getName());
		addPlayer(player, info);
		savePlayer(player); //reupdate username if changed or new player
	}
	
	public PlayerInformation getInfo(Player player) {
		return playerInfo.get(player);
	}
	
	public void switchMagicClass(Player player) {
		int currentIndex = 0;
		if (player.hasMetadata("magicClass")) {
			MagicClass mc = MagicClassManager.getInstance().getMagicClass((String)player.getMetadata("magicClass").get(0).value());
			for (int i = 0; i < MagicClassManager.getInstance().getMagicClasses().size(); i++) {
				MagicClass clazz = MagicClassManager.getInstance().getMagicClasses().get(i);
				if (mc == clazz) {
					currentIndex = i;
					break;
				}
			}
		}
		MagicClass nextClass = (currentIndex == MagicClassManager.getInstance().getMagicClasses().size() - 1 ? MagicClassManager.getInstance().getMagicClasses().get(0) : MagicClassManager.getInstance().getMagicClasses().get(currentIndex + 1));
		player.setMetadata("magicClass", new FixedMetadataValue(Plugin.pluginInstance, nextClass.getName()));
		player.sendMessage(ChatColor.AQUA + "You changed your magic class to the " + nextClass.getName() + ".");
	}
	
	public void backupInventory(Player player) {
		player.setMetadata("backupArmor", new FixedMetadataValue(Plugin.pluginInstance, player.getInventory().getArmorContents()));
		player.getInventory().setArmorContents(null);
		player.setMetadata("backupInv", new FixedMetadataValue(Plugin.pluginInstance, player.getInventory().getContents()));
	}
	
	public Tuple<Player, Integer> findPlayerAndCount(String name) {
		int count = 0;
		Player found = null;
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (p.getName().contains(name)) {
				count++;
				found = p;
			}
		}
		return new Tuple(found, count);
	}
	
	public void reloadBackupInventory(Player player) {
		if (player.hasMetadata("backupInv")) {
			player.getInventory().clear();
			player.getInventory().setContents((ItemStack[])player.getMetadata("backupInv").get(0).value());
			player.removeMetadata("backupInv", Plugin.pluginInstance);
		}
		if (player.hasMetadata("backupArmor")) {
			player.getInventory().setArmorContents((ItemStack[])player.getMetadata("backupArmor").get(0).value());
			player.removeMetadata("backupArmor", Plugin.pluginInstance);
		}
	}
	
	public static PlayerManager getInstance() {
		return instance;
	}
	
}
