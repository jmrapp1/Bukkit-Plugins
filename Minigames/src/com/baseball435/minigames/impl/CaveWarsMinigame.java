package com.baseball435.minigames.impl;

import org.bukkit.DyeColor;
import org.bukkit.Location;

import com.baseball435.minigames.Main;
import com.baseball435.minigames.minigame.Minigame;

public class CaveWarsMinigame extends TDMMinigame {

	public CaveWarsMinigame() {
		super("Cave Wars", 120, 5, new Location(Main.getWorld(), -118, 71, -23), new Location(Main.getWorld(), 2109, 8, 2185), new Location(Main.getWorld(), 2070, 33, 2127), new Location(Main.getWorld(), 2091, 19, 2183),  new Location(Main.getWorld(), 2082, 19, 2135), 10); // 5 minutes
		team1.setColors("Yellow", DyeColor.YELLOW);
		team2.setColors("Green", DyeColor.LIME);
	}

	@Override
	public Minigame getCopy() {
		return new CaveWarsMinigame();
	}

}
