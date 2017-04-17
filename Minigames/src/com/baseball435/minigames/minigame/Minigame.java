package com.baseball435.minigames.minigame;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import com.baseball435.minigames.Main;
import com.baseball435.minigames.MinigameManager;
import com.baseball435.spells.ClassManager;
import com.baseball435.spells.PlayerInfo;
import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractSpell;

public abstract class Minigame {

	protected final String name;
	protected final long timeLimit;
	protected final int scoreObjective;
	protected final Location quitLocation, area1, area2;
	protected final HashMap<Location, Material> manipulatedBlocks = new HashMap<Location, Material>();
	private int tick;
	
	public Minigame(String name, long timeLimit, int scoreObjective, Location quitLocation, Location area1, Location area2) {
		this.name = name;
		this.timeLimit = timeLimit;
		this.scoreObjective = scoreObjective;
		this.quitLocation = quitLocation;
		this.area1 = area1;
		this.area2 = area2;
	}
	
	public abstract void onStart();
	
	public abstract void onTick();
	
	public abstract void onEnd();
	
	public abstract void addPlayer(Player player);
	
	public abstract void removePlayer(Player player, boolean checkNoPlayers);
	
	public abstract Minigame getCopy();
	
	public abstract void onPlayerRespawn(Player player);
	
	public abstract void checkNoPlayers();
	
	public abstract void onAddPlayer(Player player);
	
	public abstract void checkScoreObjective();
	
	public abstract void onRemovePlayer(Player player);
	
	@SuppressWarnings("deprecation")
	protected void giveSpellItems(Player player, boolean saveItems) {
		PlayerInventory pi = player.getInventory();
		if (saveItems) {
			com.baseball435.minigames.PlayerManager.getPlayerInfo(player).saveInventory(pi.getContents());
			com.baseball435.minigames.PlayerManager.getPlayerInfo(player).saveArmor(pi.getArmorContents());
		}
		pi.setContents(new ItemStack[pi.getSize()]);
		pi.setArmorContents(new ItemStack[4]);
		
		PlayerInfo pinfo = PlayerManager.getPlayerInfo(player);
		for (String s : pinfo.getOwnedSpells()) {
			if (ClassManager.classExists(pinfo.getPlayerClass()) && ClassManager.classContainsSpell(pinfo.getPlayerClass(), s)) {
				AbstractSpell spell = ClassManager.getSpell(s);
				if (spell != null) {
					int itemId = spell.getItemId();
					ItemStack i = new ItemStack(itemId, 1);
					ItemMeta im = i.getItemMeta();
					ArrayList<String> lore = new ArrayList<String>();
					lore.add("Cooldown: " + (spell.getCoolDownTime() / 1000) + " seconds");
					lore.add("Mana Cost: " + spell.getManaCost());
					im.setDisplayName(ChatColor.AQUA + spell.getName().replace("_", " "));
					im.setLore(lore);
					i.setItemMeta(im);
					pi.addItem(i);
				}
			}
		}
		ClassManager.addClassItems(player);
		player.updateInventory();
	}
	
	@SuppressWarnings("deprecation")
	protected void giveSavedInventory(Player player) {
		player.getInventory().clear();
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.getInventory().setContents(com.baseball435.minigames.PlayerManager.getPlayerInfo(player).getSavedInventory());
		player.getInventory().setArmorContents(com.baseball435.minigames.PlayerManager.getPlayerInfo(player).getSavedArmor());
		player.updateInventory();
	}
	
	protected void checkEnd() {
		tick++;
		if (tick >= timeLimit)
			MinigameManager.endMinigame(this);
		checkScoreObjective();
	}
	
	public String getName() {
		return name;
	}
	
	public long getTimeLimit() {
		return timeLimit;
	}
	
	public int getScoreObjective() {
		return scoreObjective;
	}
	
	public Location getQuitLocation() {
		return quitLocation;
	}
	
	public Location getCenter() {
		int xDif = Math.abs(area1.getBlockX() - area2.getBlockX());
		int zDif = Math.abs(area1.getBlockZ() - area2.getBlockZ());
		Location min = null;
		if (area1.getBlockX() <= area2.getBlockX() && area1.getBlockZ() <= area2.getBlockZ())
			min = area1;
		else
			min = area2;
		return new Location(Main.getWorld(), min.getBlockX() + xDif, min.getBlockY(), min.getBlockZ() + zDif);
	}
	
	public void addDestroyedBlock(Location loc, Material material) {
		Material mat = (Material) manipulatedBlocks.get(loc);
		if (mat != null) {
			if (mat == Material.AIR)
				return;
			else if (mat != Material.AIR && material == Material.AIR)
				return;
		}
		manipulatedBlocks.put(loc, material);
	}
	
	public boolean blockInside(Location loc) {
		int minX, minZ, width, length;
		minX = Math.min(area1.getBlockX(), area2.getBlockX());
		minZ = Math.min(area1.getBlockZ(), area2.getBlockZ());
		width = Math.abs(area1.getBlockX() - area2.getBlockX());
		length = Math.abs(area1.getBlockZ() - area2.getBlockZ());
		Rectangle r = new Rectangle(minX, minZ, width, length);
		return r.intersects(loc.getX(), loc.getZ(), 1, 1);
	}
	
	@SuppressWarnings("rawtypes")
	protected void reloadDestroyedBlocks() {
		Iterator it = manipulatedBlocks.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry)it.next();
			Material material = (Material) pairs.getValue();
			Location pos = (Location) pairs.getKey();
			Main.getWorld().getBlockAt(pos).setType(material);
		}
		manipulatedBlocks.clear();
	}
	
}
