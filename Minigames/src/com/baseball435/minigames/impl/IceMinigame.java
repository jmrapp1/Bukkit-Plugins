package com.baseball435.minigames.impl;

import org.bukkit.DyeColor;
import org.bukkit.Location;

import com.baseball435.minigames.Main;
import com.baseball435.minigames.minigame.Minigame;

public class IceMinigame extends TDMMinigame {

	public IceMinigame() {
		super("Cold War", 120, 5, new Location(Main.getWorld(), -118, 71, -23), new Location(Main.getWorld(), -532, 81, 158), new Location(Main.getWorld(), -477, 126, 75), new Location(Main.getWorld(), -505, 88, 78),  new Location(Main.getWorld(), -505, 88, 155), 10); // 5 minutes
		team1.setColors("Blue", DyeColor.LIGHT_BLUE);
		team2.setColors("Green", DyeColor.LIME);
	}

	@Override
	public Minigame getCopy() {
		return new IceMinigame();
	}
	
}
