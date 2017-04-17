package com.baseball435.spells;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

import code.husky.mysql.MySQL;

public class DatabaseManager {

	private static MySQL mysql = new MySQL(Main.PLUGIN, "localhost", "3306", "spells", "root", "");
	
	public static void loadPlayerSpells(Player player) {
		try {
			Statement statement = mysql.openConnection().createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM ownedspells WHERE username = '" + player.getName() + "';");
			res.next();
			if (res.getString("username") != null) {
				String ownedSpells = res.getString("owned");
				for (String s : ownedSpells.split(" "))
					PlayerManager.getPlayerInfo(player).addSpell(s);
			}
		} catch (SQLException e) {
		}	
	}
	
	private static boolean playerSpellsRowExists(Player player) {
		try {
			Statement statement = mysql.openConnection().createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM ownedspells WHERE username = '" + player.getName() + "';");
			res.next();
			return res.getString("username") != null;
		} catch (SQLException e) {
		}	
		return false;
	}
	
	private static boolean playerClassRowExists(Player player) {
		try {
			Statement statement = mysql.openConnection().createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM classes WHERE username = '" + player.getName() + "';");
			res.next();
			return res.getString("username") != null;
		} catch (SQLException e) {
		}	
		return false;
	}
	
	public static String loadPlayerClass(Player player) {
		try {
			Statement statement = mysql.openConnection().createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM classes WHERE username = '" + player.getName() + "';");
			res.next();
			if (res.getString("username") != null) {
				return res.getString("class");
			}
		} catch (SQLException e) {
		}	
		return ClassManager.classNames[0];
	}
	
	public static void savePlayerSpells(Player player) {
		try {
			String all = "";
			for (String s : PlayerManager.getPlayerInfo(player).getOwnedSpells())
				all += s + " ";
			Statement statement = mysql.openConnection().createStatement();
			if (playerSpellsRowExists(player))
				statement.executeUpdate("UPDATE ownedspells SET `owned`='" + all + "' WHERE username='" + player.getName() + "';");
			else
				statement.executeUpdate("INSERT INTO ownedspells (`username`, `owned`) VALUES ('" + player.getName() + "', '" + all + "');");
		} catch (SQLException e) {
		}
	}
	
	public static void savePlayerClass(Player player) {
		try {
			String playerClass = PlayerManager.getPlayerInfo(player).getPlayerClass();
			Statement statement = mysql.openConnection().createStatement();
			if (playerClassRowExists(player))
				statement.executeUpdate("UPDATE classes SET `class`='" + playerClass + "' WHERE username='" + player.getName() + "';");
			else
				statement.executeUpdate("INSERT INTO classes (`username`, `class`) VALUES ('" + player.getName() + "', '" + playerClass + "');");
		} catch (SQLException e) {
		}
	}
	
}
