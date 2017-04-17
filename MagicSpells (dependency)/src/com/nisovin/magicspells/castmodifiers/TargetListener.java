package com.nisovin.magicspells.castmodifiers;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.events.SpellTargetEvent;
import com.nisovin.magicspells.events.SpellTargetLocationEvent;

public class TargetListener implements Listener {
	
	@EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
	public void onSpellTarget(SpellTargetEvent event) {
		ModifierSet m = event.getSpell().getTargetModifiers();
		if (m != null) {
			m.apply(event);
		}
		LivingEntity target = event.getTarget();
		if (target instanceof Player) {
			if (((Player)target).hasPermission("magicspells.notarget")) {
				event.setCancelled(true);
			} else if (MagicSpells.getMagicZoneManager() != null && MagicSpells.getMagicZoneManager().willFizzle(target.getLocation(), event.getSpell())) {
				event.setCancelled(true);
			}
		}
	}
	
	@EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
	public void onSpellTarget(SpellTargetLocationEvent event) {
		if (MagicSpells.getMagicZoneManager() != null && MagicSpells.getMagicZoneManager().inNoDamageZone(event.getTargetLocation())) {	
			event.setCancelled(true);
		}
		ModifierSet m = event.getSpell().getTargetModifiers();
		if (m != null) {
			m.apply(event);
		}
	}
	
}
