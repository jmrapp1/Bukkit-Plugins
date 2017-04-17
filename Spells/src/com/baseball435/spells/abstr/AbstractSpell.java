package com.baseball435.spells.abstr;

import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.DatabaseManager;
import com.baseball435.spells.Main;
import com.baseball435.spells.PlayerInfo;
import com.baseball435.spells.PlayerManager;

public abstract class AbstractSpell implements ISpell {

	protected final AbstractSpell parentSpell;
	protected final String className, name;
	protected final int itemId, manaCost, coolDownTime, price;
	
	public AbstractSpell(String name, int price, AbstractSpell parentSpell, String className, int itemId, int manaCost, int coolDownTime) {
		this.name = name;
		this.price = price;
		this.parentSpell = parentSpell;
		this.className = className;
		this.itemId = itemId;
		this.manaCost = manaCost;
		this.coolDownTime = coolDownTime;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}
	
	public String getClassName() {
		return className;
	}
	
	public int getItemId() {
		return itemId;
	}
	
	public int getManaCost() {
		return manaCost;
	}
	
	protected void removeMana(Player player) {
		PlayerManager.subtractMana(player, manaCost);
		PlayerManager.resetScoreboard(player);
	}
	
	public int getCoolDownTime() {
		return coolDownTime;
	}
	
	protected boolean checkPlayerOnTeam(Player player, Entity target) {
		if (com.baseball435.minigames.PlayerManager.getPlayerInfo(player).inMinigame() && target instanceof Player) {
			if (com.baseball435.minigames.PlayerManager.getPlayerInfo((Player) target).inMinigame()) {
				if (com.baseball435.minigames.PlayerManager.getPlayerInfo(player).getTeam() == com.baseball435.minigames.PlayerManager.getPlayerInfo((Player) target).getTeam()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public void purchaseSpell(Player player) {
		PlayerInfo pc = PlayerManager.getPlayerInfo(player);
		if (pc.ownsSpell(name)) {
			player.sendMessage(ChatColor.RED + "You already own this spell!");
			return;
		}
		
		if (parentSpell != null) {
			if (!pc.ownsSpell(parentSpell.getName())) {
				player.sendMessage(ChatColor.RED + "You need to purchase the spell \"" + parentSpell.getName() + "\" before this!");
				return;
			}
		}
		double balance = Main.ECON.getBalance(player.getName());
		if (balance >= price) {
			EconomyResponse er = Main.ECON.withdrawPlayer(player.getName(), price);
			if (er.transactionSuccess()) {
				player.sendMessage(ChatColor.AQUA + "You purchased the spell \"" + name.replace("_", " ") + "\" for " + price + " " + Main.ECON.currencyNamePlural());
				pc.addSpell(name);
				DatabaseManager.savePlayerSpells(player);
			} else {
				player.sendMessage(ChatColor.RED + "Error occurred while purchasing the spell \"" + name + "\". Please alert an administrator.");
			}
		} else {
			player.sendMessage(ChatColor.RED + "You don't have enough money to purchase this spell!");
		}
	}
	
}
