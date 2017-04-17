package com.nisovin.magicspells.spells.buff;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.nisovin.magicspells.spelleffects.EffectPosition;
import com.nisovin.magicspells.spells.BuffSpell;
import com.nisovin.magicspells.util.MagicConfig;

public class StrengthSpell extends BuffSpell {

	private int strength;
	private int boostDuration;
	
	private HashMap<Player,Integer> buffed;
	
	public StrengthSpell(MagicConfig config, String spellName) {
		super(config, spellName);
		
		strength = getConfigInt("effect-strength", 3);
		boostDuration = getConfigInt("boost-duration", 300);
		
		buffed = new HashMap<Player,Integer>();
	}

	@Override
	public boolean castBuff(final Player player, float power, String[] args) {
		buffed.put(player, Math.round(strength*power));
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, boostDuration, buffed.get(player)), true);
		addUseAndChargeCost(player);
		playSpellEffects(EffectPosition.CASTER, player);
		return true;
	}

	@Override
	public void turnOffBuff(Player player) {
		if (buffed.remove(player) != null) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 1, 0), true);
			player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		}
	}
	
	@Override
	protected void turnOff() {
		for (Player p : buffed.keySet()) {
			p.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
		}
		buffed.clear();
	}

	@Override
	public boolean isActive(Player player) {
		return buffed.containsKey(player);
	}

}
