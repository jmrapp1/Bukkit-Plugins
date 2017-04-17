package com.nisovin.magicspells.spells.instant;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.spelleffects.EffectPosition;
import com.nisovin.magicspells.spells.InstantSpell;
import com.nisovin.magicspells.util.MagicConfig;

public class AreaDamageSpell extends InstantSpell {
	
	private int radius, damage;
	
	public AreaDamageSpell(MagicConfig config, String spellName) {
		super(config, spellName);
		
		radius = getConfigInt("range", 15);
		damage = getConfigInt("damage", 5);
	}

	@Override
	public PostCastAction castSpell(Player player, SpellCastState state, float power, String[] args) {
		if (state == SpellCastState.NORMAL) {
			int range = Math.round(this.radius*power);
			List<Entity> entities = player.getNearbyEntities(range, range, range);
			for (Entity entity : entities) {
				if (entity instanceof Player && validTargetList.canTarget(player, (LivingEntity)entity)) {
					if (MagicSpells.notOnSameTeam(player, (Player) entity)) {
						((LivingEntity)entity).damage(damage);
						playSpellEffects(EffectPosition.TARGET, entity);
					}
				}
			}
		}
		return PostCastAction.HANDLE_NORMALLY;
	}	
	
}