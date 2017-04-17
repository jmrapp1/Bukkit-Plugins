package com.baseball435.spells;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.abstr.AbstractSpell;
import com.baseball435.spells.classes.PlayerClass;
import com.baseball435.spells.spell.HealingSpell;
import com.baseball435.spells.spell.archer.BlinkArrowSpell;
import com.baseball435.spells.spell.archer.DartArrowSpell;
import com.baseball435.spells.spell.archer.ExplodingArrowSpell;
import com.baseball435.spells.spell.archer.KnockbackArrowSpell;
import com.baseball435.spells.spell.archer.PoisonArrowSpell;
import com.baseball435.spells.spell.warrior.DefensiveStanceSpell;
import com.baseball435.spells.spell.warrior.DisarmSpell;
import com.baseball435.spells.spell.warrior.FatalBlowSpell;
import com.baseball435.spells.spell.warrior.PoisonBladeSpell;
import com.baseball435.spells.spell.warrior.ShoutSpell;
import com.baseball435.spells.spell.wizard.FireballSpell;
import com.baseball435.spells.spell.wizard.IgniteSpell;
import com.baseball435.spells.spell.wizard.LightningBoltSpell;
import com.baseball435.spells.spell.wizard.ManaLeechSpell;
import com.baseball435.spells.spell.wizard.TeleportSpell;
import com.baseball435.spells.spell.wizard.TombSpell;

public class ClassManager {
	
	public static String[] classNames = new String[] { "Wizard", "Spy", "Warrior", "Archer", "Healer"};
	private static ArrayList<PlayerClass> classes = new ArrayList<PlayerClass>();
	
	public static void registerClasses() {
		classes.add(new PlayerClass("Wizard"));
		classes.add(new PlayerClass("Spy"));
		classes.add(new PlayerClass("Warrior"));
		classes.add(new PlayerClass("Archer"));
		classes.add(new PlayerClass("Healer"));
	}
	
	public static void registerSpells() {
		
		//Wizard
		registerSpell(new FireballSpell());
		registerSpell(new TeleportSpell());
		registerSpell(new HealingSpell());
		registerSpell(new LightningBoltSpell());
		registerSpell(new ManaLeechSpell());
		registerSpell(new TombSpell());
		registerSpell(new IgniteSpell());
		
		//Archer
		registerSpell(new ExplodingArrowSpell());
		registerSpell(new BlinkArrowSpell());
		registerSpell(new DartArrowSpell());
		registerSpell(new KnockbackArrowSpell());
		registerSpell(new PoisonArrowSpell());
		
		//Warrior
		registerSpell(new ShoutSpell());
		registerSpell(new DisarmSpell());
		registerSpell(new FatalBlowSpell());
		registerSpell(new PoisonBladeSpell());
		registerSpell(new DefensiveStanceSpell());
		
	}
	
	private static void registerSpell(AbstractSpell spell) {
		String className = spell.getClassName();
		PlayerClass pc = getPlayerClass(className);
		if (pc != null) {
			pc.addSpell(spell);
			Main.LOG.info(spell.getName() + " was registered.");
		}
	}
	
	public static void purchaseSpell(Player player, String name) {
		if (getSpell(name) != null)
			getSpell(name).purchaseSpell(player);
	}
	
	public static AbstractSpell getSpell(String name) {
		for (PlayerClass pc : classes) {
			AbstractSpell spell = pc.getSpell(name);
			if (spell != null) {
				return spell;
			}
		}
		return null;
	}
	
	private static PlayerClass getPlayerClass(String name) {
		for (PlayerClass pc : classes)
			if (pc.getName().equalsIgnoreCase(name))
				return pc;
		return null;
	}
	
	public static void addClassItems(Player player) {
		PlayerClass pc = getPlayerClass(PlayerManager.getPlayerInfo(player).getPlayerClass());
		if (pc != null) {
			pc.addSpawnItems(player);
		}
	}
	
	public static boolean classContainsSpell(String playerClass, String spell) {
		PlayerClass pc = getPlayerClass(playerClass);
		return pc.getSpell(spell) != null;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean handleEntityInteraction(Player player, Entity entity) { //returns if spell found
		PlayerClass pc = getPlayerClass(PlayerManager.getPlayerClass(player));
		if (pc != null) {
			return pc.handleEntityInteraction(player, entity, player.getItemInHand().getType().getId());
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean handleInteraction(Player player) { //returns if spell found
		PlayerClass pc = getPlayerClass(PlayerManager.getPlayerClass(player));
		if (pc != null) {
			return pc.handleInteraction(player, player.getItemInHand().getType().getId());
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static boolean handleBlockInteraction(Player player) {
		PlayerClass pc = getPlayerClass(PlayerManager.getPlayerClass(player));
		if (pc != null) {
			return pc.handleBlockInteraction(player, player.getTargetBlock(null, 25), player.getItemInHand().getType().getId());
		}
		return false;
	}
	
	public static void handleArrowFired(Player player, int itemId, Arrow arrow) {
		PlayerClass pc = getPlayerClass(PlayerManager.getPlayerClass(player));
		if (pc != null) {
			pc.handleArrowFire(player, itemId, arrow);
		}
	}
	
	public static void handleArrowHit(Player shooter, Entity target, Arrow arrow) {
		PlayerClass pc = getPlayerClass(PlayerManager.getPlayerClass(shooter));
		if (pc != null) {
			pc.handleArrowHit(shooter, target, arrow);
		}
	}
	
	public static void handleArrowBlockHit(Player shooter, Block block, Arrow arrow) {
		PlayerClass pc = getPlayerClass(PlayerManager.getPlayerClass(shooter));
		if (pc != null) {
			pc.handleArrowBlockHit(shooter, block, arrow);
		}
	}
	
	public static void handleEntityHitEntity(Player player, Entity target, int itemId) {
		PlayerClass pc = getPlayerClass(PlayerManager.getPlayerClass(player));
		if (pc != null && target != null) {
			pc.handleEntityInteraction(player, target, itemId);
		}
	}
	
	public static boolean classExists(String name) {
		for (String s : classNames)
			if (s.equalsIgnoreCase(name))
				return true;
		return false;
	}
	
}
