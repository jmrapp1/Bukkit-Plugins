package com.jmr.classes;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class MagicClassLoader {

	public ArrayList<MagicClass> loadClasses(String file) throws Exception {
		File f = new File(file);
		if (f.exists()) {
			ArrayList<MagicClass> classes = new ArrayList<MagicClass>();
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(f);
			for (String magicClassName : yml.getKeys(false)) {
				ConfigurationSection section = yml.getConfigurationSection(magicClassName);
				classes.add(handleMagicClassSection(section));
			}
			return classes;
		} else {
			throw new Exception("File \"" + file + "\" does not exist.");
		}
	}
	
	private MagicClass handleMagicClassSection(ConfigurationSection section) throws Exception {
		String desc = "";
		List<String> spells = null;
		ArrayList<ItemStack> beginningItems = new ArrayList<ItemStack>();
		ArrayList<ItemStack> endingItems = new ArrayList<ItemStack>();
		int icon = -1;
		ItemStack chestplate = null, legs = null, boots = null;
		if (section.contains("description")) {
			desc = section.getString("description");
		}
		if (section.contains("spells")) {
			spells = section.getStringList("spells");
		}
		if (section.contains("icon")) {
			icon = section.getInt("icon");
		}
		if (section.contains("beginning-items")) {
			String[] split = section.getString("beginning-items").split(",");
			for (String s : split) {
				ItemStack is = null;
				String[] spaces = s.split(" ");
				if (spaces[0].contains(":")) {
					String[] ids = spaces[0].split(":");
					is = new ItemStack(Integer.parseInt(ids[0]),Integer.parseInt(spaces[1]), Byte.parseByte(ids[1]));
				} else {
					is = new ItemStack(Integer.parseInt(spaces[0]), Integer.parseInt(spaces[1]));
				}
				beginningItems.add(is);
			}
		}
		if (section.contains("ending-items")) {
			String[] split = section.getString("ending-items").split(",");
			for (String s : split) {
				ItemStack is = null;
				String[] spaces = s.split(" ");
				if (spaces[0].contains(":")) {
					String[] ids = spaces[0].split(":");
					is = new ItemStack(Integer.parseInt(ids[0]),Integer.parseInt(spaces[1]), Byte.parseByte(ids[1]));
				} else {
					is = new ItemStack(Integer.parseInt(spaces[0]), Integer.parseInt(spaces[1]));
				}
				endingItems.add(is);
			}
		}
		if (section.contains("chestplate")) {
			String[] split = section.getString("chestplate").trim().split(",");
			if (split.length == 2)
				chestplate = new ItemStack(Integer.parseInt(split[0]), 1, Byte.parseByte(split[1]));
			else
				chestplate = new ItemStack(Integer.parseInt(split[0]), 1);
		}
		if (section.contains("legs")) {
			String[] split = section.getString("legs").trim().split(",");
			if (split.length == 2)
				legs = new ItemStack(Integer.parseInt(split[0]), 1, Byte.parseByte(split[1]));
			else
				legs = new ItemStack(Integer.parseInt(split[0]), 1);
		}
		if (section.contains("boots")) {
			String[] split = section.getString("boots").trim().split(",");
			if (split.length == 2)
				boots = new ItemStack(Integer.parseInt(split[0]), 1, Byte.parseByte(split[1]));
			else
				boots = new ItemStack(Integer.parseInt(split[0]), 1);
		}
		return new MagicClass(section.getName(), desc, icon, spells, beginningItems, endingItems, chestplate, legs, boots);
	}

}
