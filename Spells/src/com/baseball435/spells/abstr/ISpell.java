package com.baseball435.spells.abstr;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public interface ISpell {

	void onUse(Player player, Entity target);
	
}
