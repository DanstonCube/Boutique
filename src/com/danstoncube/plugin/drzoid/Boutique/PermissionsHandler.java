package com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import com.nijikokun.bukkit.Permissions.Permissions;




public class PermissionsHandler 
{
	public static final String permissionErr = "Tu n'as pas le droit de faire çà.";
	public static final String permissionBreakErr = "Tu ne peux pas détruire çà.";
	
	public static boolean permissionsEnabled = true;
	
	public static Plugin permissions = null;
	
	
	public static void setupPermissions(Boutique plugin) 
	{
		Plugin test = plugin.getServer().getPluginManager().getPlugin("Permissions");
		if (test != null) 
		{
			((Permissions)test).getHandler();
			plugin.log.info("["+plugin.displayname+"] utilise Permissions");
			permissionsEnabled = true;
			return;
		}
	}
	
	private static boolean checkNode(Player p, String node)
	{
		if(permissions != null)
		{
			return Permissions.Security.permission(p, node);
		}
		
		return p.hasPermission(node);
	}
	
	public static boolean canSetGlobalSign(Player p)
	{
		//Checks to see if the player can set signs that get and give global values (aka not tied to an account)
		if (!permissionsEnabled)
			return p.isOp();
		else
			return checkNode(p, "signtrader.MakeGlobalSign") || checkNode(p, "boutique.MakeGlobalSign");
	}
	
	public static  boolean canSetPersonalSign(Player p)
	{
		//Checks to see if the player has permission to make trading signs
		if (permissionsEnabled) 
		{
			return checkNode(p, "signtrader.MakePersonalSign") || checkNode(p, "boutique.MakePersonalSign");
		}
		return true;
	}
	
	public static boolean isSignOwner(Sign s, String name)
	{
		String location = s.getX() + ":" + s.getY() + ":" + s.getZ() + ":" + s.getWorld().getName();
		if (Boutique.signLocs.get(location) == null)
			return true;
		if (name.compareToIgnoreCase(Boutique.signLocs.get(location)) == 0)
			return true;
		
		return false;
	}
	
	public static boolean canUseSign(Player p)
	{
		//Checks to see if the player can use signs.
		if (permissionsEnabled)
			return checkNode(p, "signtrader.Use")|| checkNode(p, "boutique.Use");
		
		return true;
	}

	public static boolean canSetOwner(Player p) {
		if (permissionsEnabled)
			return checkNode(p, "signtrader.admin.SetOwner")|| checkNode(p, "boutique.admin.SetOwner");
		return p.isOp();
	}
	
}