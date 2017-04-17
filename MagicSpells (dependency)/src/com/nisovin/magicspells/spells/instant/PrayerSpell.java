package com.nisovin.magicspells.spells.instant;

import org.bukkit.Bukkit;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;

import com.nisovin.magicspells.spelleffects.EffectPosition;
import com.nisovin.magicspells.spells.InstantSpell;
import com.nisovin.magicspells.util.MagicConfig;

public class PrayerSpell extends InstantSpell {
	
	private double amountHealed;
	private String strAtFullHealth;
	private boolean checkPlugins;

	public PrayerSpell(MagicConfig config, String spellName) {
		super(config, spellName);
		
		amountHealed = getConfigInt("amount-healed", 10);
		strAtFullHealth = getConfigString("str-at-full-health", "You are already at full health.");
		checkPlugins = getConfigBoolean("check-plugins", true);
	}

	@Override
	public PostCastAction castSpell(Player player, SpellCastState state, float power, String[] args) {
		if (state == SpellCastState.NORMAL) {
			Damageable d = (Damageable) player;
			if (d.getHealth() >= d.getMaxHealth() && amountHealed > 0) {
				sendMessage(player, strAtFullHealth);
				return PostCastAction.ALREADY_HANDLED;
			} else if (player.isValid()) {
				double health = d.getHealth();
				if (health > 0) {
					double amt = amountHealed * power;
					if (checkPlugins && amt > 0) {
						EntityRegainHealthEvent evt = new EntityRegainHealthEvent(player, amt, RegainReason.CUSTOM);
						Bukkit.getPluginManager().callEvent(evt);
						if (evt.isCancelled()) {
							return PostCastAction.ALREADY_HANDLED;
						}
						amt = evt.getAmount();
					}
					health += amt;
					if (health > d.getMaxHealth()) {
						health = d.getMaxHealth();
					} else if (health < 0) {
						health = 0;
					}
					player.setHealth(health);
					playSpellEffects(EffectPosition.CASTER, player);
				}
			}
		}
		return PostCastAction.HANDLE_NORMALLY;
	}

}
