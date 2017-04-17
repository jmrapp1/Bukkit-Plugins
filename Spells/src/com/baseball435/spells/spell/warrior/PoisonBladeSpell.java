package com.baseball435.spells.spell.warrior;

import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.baseball435.minigames.Main;
import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractHitSpell;
import com.baseball435.spells.effects.Effects;

public class PoisonBladeSpell extends AbstractHitSpell {

	public PoisonBladeSpell() {
		super("Poison_Blade", 450, null, "Warrior", 272, 25, 5000, 2);
	}

	@Override
	public void onUse(Player player, Entity target) {
		if (target instanceof Player) {
			((Player)target).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 100, 1, true));
			((Player)target).setMetadata("spell_damage",new FixedMetadataValue(Main.getBukkitPlugin(), "potion"));
			((Player)target).setMetadata("spell_killer",new FixedMetadataValue(Main.getBukkitPlugin(), player));
			Effects.createFirework(player, player.getLocation(), true, false, Color.LIME, Color.RED, Type.BALL);
			removeMana(player);
			PlayerManager.getPlayerInfo(player).usedSpell(this);
		}
	}

}
