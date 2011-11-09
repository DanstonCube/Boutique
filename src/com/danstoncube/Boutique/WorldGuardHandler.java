package com.danstoncube.Boutique;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldGuardHandler
{

	public static WorldGuardPlugin worldguard = null;
	
	
	public static void setupWorldGuard() 
	{
		Boutique boutique = Boutique.getInstance();
		
		Plugin testworldguard = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		 
	    // WorldGuard may not be loaded
	    if (testworldguard == null || !(testworldguard instanceof WorldGuardPlugin)) 
	    {
	    	worldguard = null; 
	    	
	    }
	    else
	    {
	    	worldguard = (WorldGuardPlugin) testworldguard;
	    	boutique.log.info(boutique.logPrefix + Messages.getString("WorldGuard.HOOKED"));
	    }
	}
	
	
	public static boolean canBuild(Player p, Block b)
	{
		if(worldguard == null)
			return true;
		
		return worldguard.canBuild(p, b.getLocation());
	}
}
