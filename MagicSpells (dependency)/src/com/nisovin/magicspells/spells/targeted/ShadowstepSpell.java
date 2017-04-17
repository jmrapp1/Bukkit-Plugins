package com.nisovin.magicspells.spells.targeted;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.jmr.minigames.Minigame;
import com.jmr.minigames.MinigameManager;
import com.nisovin.magicspells.MagicSpells;
import com.nisovin.magicspells.spells.TargetedEntitySpell;
import com.nisovin.magicspells.spells.TargetedSpell;
import com.nisovin.magicspells.util.BlockUtils;
import com.nisovin.magicspells.util.MagicConfig;
import com.nisovin.magicspells.util.TargetInfo;

public class ShadowstepSpell extends TargetedSpell implements TargetedEntitySpell {

	private String strNoLandingSpot;
	private int duration;
	private boolean returnTp;
	
	public ShadowstepSpell(MagicConfig config, String spellName) {
		super(config, spellName);
		
		strNoLandingSpot = getConfigString("str-no-landing-spot", "Cannot shadowstep there.");
		duration = getConfigInt("duration", 5);
		returnTp = getConfigBoolean("return", false);
	}

	@Override
	public PostCastAction castSpell(Player player, SpellCastState state, float power, String[] args) {
		if (state == SpellCastState.NORMAL) {
			TargetInfo<LivingEntity> target = getTargetedEntity(player, power);
			if (target == null) {
				// fail
				return noTarget(player);
			}
			
			boolean done = shadowstep(player, target.getTarget());
			if (!done) {
				return noTarget(player, strNoLandingSpot);
			} else {
				sendMessages(player, target.getTarget());
				return PostCastAction.NO_MESSAGES;
			}
			
		}
		return PostCastAction.HANDLE_NORMALLY;
	}
	
	private boolean shadowstep(Player player, LivingEntity target) {
		// get landing location
		Location targetLoc = target.getLocation();
		Vector facing = targetLoc.getDirection().setY(0).multiply(-1);
		Location loc = targetLoc.toVector().add(facing).toLocation(targetLoc.getWorld());
		loc.setPitch(0);
		loc.setYaw(targetLoc.getYaw());
		
		// check if clear
		Block b = loc.getBlock();
		if (!BlockUtils.isPathable(b.getType()) || !BlockUtils.isPathable(b.getRelative(BlockFace.UP))) {
			// fail - no landing spot
			return false;
		}
		
		if (returnTp) {
			ShadowStepRunnable run = new ShadowStepRunnable(player, duration, player.getLocation());
			int task = Bukkit.getScheduler().scheduleAsyncRepeatingTask(MagicSpells.plugin, run, 0, 20L);
			run.setId(task);
		}
		
		// ok
		playSpellEffects(player.getLocation(), loc);
		player.teleport(loc);
		
		return true;
	}

	@Override
	public boolean castAtEntity(Player caster, LivingEntity target, float power) {
		if (!validTargetList.canTarget(caster, target)) {
			return false;
		} else {
			return shadowstep(caster, target);
		}
	}

	@Override
	public boolean castAtEntity(LivingEntity target, float power) {
		return false;
	}

}

class ShadowStepRunnable implements Runnable {
	
	private int id;
	private Player player;
	private int duration;
	private Location back;
	
	public ShadowStepRunnable(Player player, int duration, Location back) {
		this.player = player;
		this.duration = duration;
		this.back = back;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Override
	public void run() {
		duration -= 1;
		if (duration <= 0) {
			Minigame m = MinigameManager.getInstance().getMinigamesIn(player);
			if (m != null) {
				player.teleport(back);
				player.sendMessage(ChatColor.DARK_AQUA + "You were teleported back.");
				cancelTask();
			}
		}
	}
	
	public void cancelTask() {
		Bukkit.getScheduler().cancelTask(id);
	}

}
