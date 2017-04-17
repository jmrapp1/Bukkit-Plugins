package com.jmr.menu;

import java.util.HashMap;

import com.jmr.menu.menus.MagicClassesMenu;
import com.jmr.menu.menus.SpellShopMenu;

public class MenuManager {

	private static final MenuManager instance = new MenuManager();
	
	private final HashMap<String, IGameMenu> menus = new HashMap<String, IGameMenu>();
	
	private MenuManager() {
		menus.put("magicClasses", new MagicClassesMenu());
		menus.put("spellShop", new SpellShopMenu());
	}
	
	public IGameMenu getMenu(String name) {
		return menus.get(name);
	}
	
	public void addMenu(String name, IGameMenu menu) {
		menus.put(name, menu);
	}
	
	public static MenuManager getInstance() {
		return instance;
	}
	
}
