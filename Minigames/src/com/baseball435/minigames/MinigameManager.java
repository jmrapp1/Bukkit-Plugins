package com.baseball435.minigames;

import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.baseball435.minigames.impl.CastleWarsMinigame;
import com.baseball435.minigames.impl.CaveWarsMinigame;
import com.baseball435.minigames.impl.IceMinigame;
import com.baseball435.minigames.minigame.Minigame;

public class MinigameManager {

	public static String[] minigameNames = new String[] { "Castle Wars", "Cave Wars", "Cold War" };
	private static Minigame[] registeredMinigames = new Minigame[] { new CastleWarsMinigame(), new CaveWarsMinigame(), new IceMinigame() };
	public static CopyOnWriteArrayList<Minigame> currentMinigames = new CopyOnWriteArrayList<Minigame>();
	
	public static void startMinigame(Minigame minigame) {
		minigame.onStart();
		currentMinigames.add(minigame);
	}
	
	public static void updateMinigames() {
		for (Minigame mg : currentMinigames)
			mg.onTick();
	}
	
	public static void endMinigame(Minigame minigame) {
		currentMinigames.remove(minigame);
		minigame.onEnd();
	}
	
	public static boolean minigameExists(String name) {
		for (String s : minigameNames)
			if (s.equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	private static Minigame getRunningMinigame(String name) {
		for (Minigame mg : currentMinigames)
			if (mg.getName().equalsIgnoreCase(name))
				return mg;
		return null;
	}
	
	public static void handleBlockInside(Location loc, Material material) {
		for (Minigame mg : currentMinigames) {
			if (mg.blockInside(loc)) {
				mg.addDestroyedBlock(loc, material);
				break;
			}
		}
	}
	
	public static void handleJoinMinigame(Player player, String name) {
		if (minigameStarted(name)) {
			Minigame mg = getRunningMinigame(name);
			mg.addPlayer(player);
		} else {
			Minigame mg = getRegisteredMinigame(name).getCopy();
			startMinigame(mg);
			mg.addPlayer(player);
		}
	}
	
	private static Minigame getRegisteredMinigame(String name) {
		for (Minigame mg : registeredMinigames)
			if (mg.getName().equalsIgnoreCase(name))
				return mg;
		return null;
	}
	
	private static boolean minigameStarted(String name) {
		for (Minigame mg : currentMinigames)
			if (mg.getName().equalsIgnoreCase(name))
				return true;
		return false;
	}
	
}
