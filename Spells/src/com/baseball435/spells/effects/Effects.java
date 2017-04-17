package com.baseball435.spells.effects;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import com.baseball435.spells.Main;

import java.lang.reflect.Field;

public class Effects {

	public static void createFirework(Player player, Location loc, boolean trail, boolean flicker, Color color, Color fadeColor, Type type) {
		Firework fw = (Firework) player.getWorld().spawn(loc, Firework.class);
        FireworkEffect effect = FireworkEffect.builder().trail(trail).flicker(flicker).withFade(fadeColor).withColor(color).with(type).build();
        FireworkMeta fwm = fw.getFireworkMeta();
        fwm.clearEffects();
        fwm.addEffect(effect);
        Field f;
        try {
            f = fwm.getClass().getDeclaredField("power");
            f.setAccessible(true);
            f.set(fwm, -2);
        } catch (Exception e) {
        	Main.LOG.warning("Couldn't create firework: " + e.getMessage());
        }
        fw.setFireworkMeta(fwm);
	}
	
}
