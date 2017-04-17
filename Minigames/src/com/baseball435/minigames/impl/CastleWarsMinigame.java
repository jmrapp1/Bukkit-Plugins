package com.baseball435.minigames.impl;

import org.bukkit.Location;

import com.baseball435.minigames.Main;
import com.baseball435.minigames.minigame.Minigame;

public class CastleWarsMinigame extends TDMMinigame {

	public CastleWarsMinigame() {
		super("Castle Wars", 120, 25, new Location(Main.getWorld(), -118, 71, -23), new Location(Main.getWorld(), 2874, 72, 2644), new Location(Main.getWorld(), 2752, 126, 2582), new Location(Main.getWorld(), 2755, 95, 2613),  new Location(Main.getWorld(), 2870, 95, 2613), 10); // 5 minutes
	}

	@Override
	public Minigame getCopy() {
		return new CastleWarsMinigame();
	}
	
}
