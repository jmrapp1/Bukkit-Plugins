package com.jmr.menu;

import org.bukkit.entity.Player;

/**
 * MenuCloseBehaviour
 *
 * What to do when a menu is closed by a player. Useful for menus which MUST
 * have an answer before closing
 *
 * @author XHawk87
 */
public interface MenuCloseBehaviour {

    /**
     * Called when a player closes a menu
     * 
     * @param player The player closing the menu
     */
    public void onClose(Player player);
}