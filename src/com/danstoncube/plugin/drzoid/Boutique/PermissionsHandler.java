package com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


import com.nijikokun.bukkit.Permissions.Permissions;




public class PermissionsHandler 
{
	public static final String permissionErr = Messages.getString("Perm.NOPERM"); //$NON-NLS-1$
	public static final String permissionBreakErr = Messages.getString("Perm.NOBREAK"); //$NON-NLS-1$
	
	public static boolean permissionsEnabled = true;
	
	public static Plugin permissions = null;
	
	
	public static void setupPermissions() 
	{
		Boutique boutique = Boutique.getInstance();
		Plugin test = boutique.getServer().getPluginManager().getPlugin("Permissions"); //$NON-NLS-1$
		if (test != null) 
		{
			((Permissions)test).getHandler();
			boutique.log.info(boutique.logPrefix + Messages.getString("Perm.HOOKED")); //$NON-NLS-1$
			permissionsEnabled = true;
			permissions = test;
			return;
		}
	}
	
	private static boolean checkNode(Player p, String node)
	{
		
		if(permissions != null)
		{
			//Boutique.getInstance().log.info("Permissions: hasPerm: " +  Permissions.Security.has(p, node));
			return Permissions.Security.has(p, node);
		}
		
		//Boutique.getInstance().log.info("Bukkit: hasPerm: " +  Permissions.Security.has(p, node));
		return p.hasPermission(node);
	}
	
	public static boolean canSetGlobalSign(Player p)
	{
		//Checks to see if the player can set signs that get and give global values (aka not tied to an account)
		if (!permissionsEnabled)
			return p.isOp();
		else
			return checkNode(p, "signtrader.MakeGlobalSign") || checkNode(p, "boutique.MakeGlobalSign"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	public static  boolean canSetPersonalSign(Player p)
	{
		//Checks to see if the player has permission to make trading signs
		if (permissionsEnabled) 
		{
			return checkNode(p, "signtrader.MakePersonalSign") || checkNode(p, "boutique.MakePersonalSign"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return true;
	}
	
	/*
	public static boolean isSignOwner(Sign s, String name)
	{
		String location = s.getX() + ":" + s.getY() + ":" + s.getZ() + ":" + s.getWorld().getName();
		if (Boutique.signLocs.get(location) == null)
			return true;
		if (name.compareToIgnoreCase(Boutique.signLocs.get(location)) == 0)
			return true;
		
		return false;
	}
	*/
	
	public static boolean canUseSign(Player p)
	{
		//Checks to see if the player can use signs.
		if (permissionsEnabled)
			return checkNode(p, "signtrader.Use")|| checkNode(p, "boutique.Use"); //$NON-NLS-1$ //$NON-NLS-2$
		
		return true;
	}

	public static boolean canSetOwner(Player p) {
		if (permissionsEnabled)
			return checkNode(p, "signtrader.admin.SetOwner")|| checkNode(p, "boutique.admin.SetOwner"); //$NON-NLS-1$ //$NON-NLS-2$
		return p.isOp();
	}

	public static boolean canSetWebAuctionSign(Player p)
	{		
		if (!permissionsEnabled)
			return p.isOp();
		else
			return checkNode(p, "signtrader.MakeWebAuctionSign") || checkNode(p, "boutique.MakeWebAuctionSign"); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
}