package com.danstoncube.plugin.drzoid.Boutique;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldGuardOperator
{

	public static WorldGuardPlugin worldguard = null;
	
	
	public static void setupWorldGuard() 
	{
		Boutique boutique = Boutique.getInstance();
		
		Plugin worldguard = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		 
	    // WorldGuard may not be loaded
	    if (worldguard == null || !(worldguard instanceof WorldGuardPlugin)) 
	    {
	    	worldguard = null; 
	    	
	    }
	    else
	    {
	    	boutique.log.info(boutique.logPrefix + "Utilisation de Worldguard !");
	    }
	}
	
	
	public static boolean canBuild(Player p, Block b)
	{
		if(worldguard == null)
			return true;
		
		return worldguard.canBuild(p, b.getLocation());
	}
}
