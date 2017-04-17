package com.baseball435.spells.spell.wizard;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.baseball435.spells.PlayerInfo;
import com.baseball435.spells.PlayerManager;
import com.baseball435.spells.abstr.AbstractDistanceSpell;
import com.baseball435.spells.effects.Effects;

public class ManaLeechSpell extends AbstractDistanceSpell {

	public ManaLeechSpell() {
		super("Mana_Leech", 300, null, "Wizard", 369, 0, 7000, 25, 25);
	}

	@Override
	public void onUse(Player player, Entity target) {
		if (checkPlayerOnTeam(player, target))
			return;
		if (player.getLocation().distance(target.getLocation()) <= maxDistance) {
			if (target instanceof Player) {
				Player pTarget = (Player) target;
				PlayerInfo ti = PlayerManager.getPlayerInfo(pTarget);
				Effects.createFirework(player, target.getLocation(), true, false, Color.BLUE, Color.AQUA, Type.STAR);
				ti.addMana((int)-damage);
				if (ti.getMana() < 0)
					ti.setMana(0);
				PlayerInfo pi = PlayerManager.getPlayerInfo(player);
				pi.addMana((int) damage);
				if (pi.getMana() > 100)
					pi.setMana(100);
				PlayerManager.getPlayerInfo(player).usedSpell(this);
				player.sendMessage(ChatColor.BLUE + "You stole " + (int) damage + " mana!");
				PlayerManager.resetScoreboard(player);
			}
		}
	}
	
}
