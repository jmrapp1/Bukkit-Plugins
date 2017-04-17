package com.baseball435.spells.classes;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.baseball435.minigames.Main;
import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractArrowBlockSpell;
import com.baseball435.spells.abstr.AbstractArrowSpell;
import com.baseball435.spells.abstr.AbstractBlockSpell;
import com.baseball435.spells.abstr.AbstractDamageSpell;
import com.baseball435.spells.abstr.AbstractDistanceSpell;
import com.baseball435.spells.abstr.AbstractHitSpell;
import com.baseball435.spells.abstr.AbstractNoTargetSpell;
import com.baseball435.spells.abstr.AbstractSpell;

public class PlayerClass {

	private final ArrayList<AbstractSpell> spells = new ArrayList<AbstractSpell>();
	private final ArrayList<ItemStack> spawnItems = new ArrayList<ItemStack>();
	private final String name;
	
	public PlayerClass(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addSpell(AbstractSpell spell) {
		spells.add(spell);
	}
	
	public AbstractSpell getSpell(String name) {
		for (AbstractSpell spell : spells)
			if (spell.getName().equalsIgnoreCase(name))
				return spell;
		return null;
	}
	
	private AbstractSpell getSpell(int itemId) {
		for (AbstractSpell spell : spells)
			if (spell.getItemId() == itemId)
				return spell;
		return null;
	}
	
	public void addSpawnItems(Player player) {
		for (ItemStack is : spawnItems)
			player.getInventory().addItem(is);
	}
	
	public boolean handleEntityInteraction(Player player, Entity entity, int itemId) {
		AbstractSpell spell = getSpell(itemId);
		if (spell != null && (spell instanceof AbstractDamageSpell || spell instanceof AbstractDistanceSpell || spell instanceof AbstractHitSpell) && PlayerManager.getPlayerInfo(player).ownsSpell(spell.getName()))
			return handleSpell(spell, player, entity, itemId);
		return false;
	}
	
	public boolean handleInteraction(Player player, int itemId) {
		AbstractSpell spell = getSpell(itemId);
		if (spell != null && (spell instanceof AbstractNoTargetSpell || spell instanceof AbstractArrowSpell) && PlayerManager.getPlayerInfo(player).ownsSpell(spell.getName()))
			return handleSpell(spell, player, null, itemId);
		return false;
	}
	
	public boolean handleBlockInteraction(Player player, Block block, int itemId) {
		AbstractSpell spell = getSpell(itemId);
		if (spell != null && spell instanceof AbstractBlockSpell && PlayerManager.getPlayerInfo(player).ownsSpell(spell.getName()))
			return handleBlockSpell(spell, player, block, itemId);
		return false;
	}
	
	private boolean handleBlockSpell(AbstractSpell spell, Player player, Block block, int itemId) {
		if (PlayerManager.getMana(player) != -1 && PlayerManager.getMana(player) >= spell.getManaCost()) {
			if (PlayerManager.getPlayerInfo(player).canUseSpell(spell)) {
				((AbstractBlockSpell)spell).onUse(player, block);
			}
		} else {
			player.sendMessage(ChatColor.AQUA + "You don't have enough mana!");
		}
		return true;
	}
	
	public void handleArrowFire(Player player, int itemId, Arrow arrow) {
		AbstractSpell spell = getSpell(itemId);
		if (spell != null && spell instanceof AbstractArrowSpell && PlayerManager.getPlayerInfo(player).ownsSpell(spell.getName())) {
			arrow.setMetadata("spell", new FixedMetadataValue(Main.getBukkitPlugin(), spell.getName()));
			((AbstractArrowSpell) spell).afterUse(player, arrow);
		}
	}
	
	public void handleArrowHit(Player shooter, Entity target, Arrow arrow) {
		if (arrow.hasMetadata("spell")) {
			String spellName = arrow.getMetadata("spell").get(0).asString();
			AbstractSpell spell = getSpell(spellName);
			if (spell != null && spell instanceof AbstractArrowSpell && PlayerManager.getPlayerInfo(shooter).ownsSpell(spell.getName())) {
				((AbstractArrowSpell) spell).onHit(shooter, target, arrow);
			}
		}
	}
	
	public void handleArrowBlockHit(Player shooter, Block block, Arrow arrow) {
		if (arrow.hasMetadata("spell")) {
			String spellName = arrow.getMetadata("spell").get(0).asString();
			AbstractSpell spell = getSpell(spellName);
			if (spell != null && spell instanceof AbstractArrowBlockSpell && PlayerManager.getPlayerInfo(shooter).ownsSpell(spell.getName())) {
				((AbstractArrowBlockSpell) spell).onHit(shooter, block, arrow);
			}
		}
	}
	
	private boolean handleSpell(AbstractSpell spell, Player player, Entity target, int itemId) {
		if (PlayerManager.getMana(player) != -1 && PlayerManager.getMana(player) >= spell.getManaCost()) {
			if (PlayerManager.getPlayerInfo(player).canUseSpell(spell)) {
				spell.onUse(player, target);
			}
		} else {
			player.sendMessage(ChatColor.AQUA + "You don't have enough mana!");
		}
		return true;
	}
	
}
