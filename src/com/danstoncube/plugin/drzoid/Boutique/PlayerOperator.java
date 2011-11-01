package com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class PlayerOperator 
{
	
	public static String playerStockErr = "Il te faut plus de cet objet dans la main quand tu fais un clic-droit sur le panneau.";

	@SuppressWarnings("deprecation")
	public static void givePlayerItem(int amount, int type, int damage, Player p) 
	{
		//TODO: get working with player.getInventory().addItem(item);
		//Catch to stop creation of non-existent items.
		
		if( Material.getMaterial(type) == null )
		    return; 
		
	    int amt = 0;
		int maxItemStack = ChestOperator.getItemMaxStack(type,damage);
		
		ItemStack item = null;
		
		for(int amtToDrop = 0; amtToDrop < amount;){
			if ((amtToDrop + maxItemStack) <= amount)
				amt = maxItemStack;
			else
				amt = amount - amtToDrop;
			
			item = new ItemStack(type,amt,(short)damage);
			
			//p.getWorld().dropItemNaturally(p.getLocation(), item);
			
			int test = p.getInventory().firstEmpty();
			if(test >= 0)
			{
				p.getInventory().addItem(item);
				try
				{
					p.updateInventory();
				}
				catch(Exception e)
				{
					
				}
			}
			else
			{
				//test inventaire plein
				String plugName = "" + ChatColor.BLUE + "[Boutique] " + ChatColor.WHITE;
				p.sendMessage(plugName + ChatColor.RED + "Attention, inventaire PLEIN -> items deposés au sol !!!" );
				p.getWorld().dropItemNaturally(p.getLocation(), item);
			}
			
			
			//p.getInventory().notify();
				
			amtToDrop += amt;
		}
	}
	
	public static boolean playerHasEnough(int amount, int type, int damage, Player p) 
	{
		if (p.getItemInHand().getTypeId() == type)
			return (p.getItemInHand().getAmount() >= amount && p.getItemInHand().getDurability() == (short)damage);
		return false;
	}
	
	public static void removeFromPlayer(int amount, int type, int damage, Player p) 
	{
		ItemStack item = p.getItemInHand();

		if (item.getTypeId() == type && item.getDurability() == (short)damage)
			if((item.getAmount()-amount) < 1)
				p.setItemInHand(null);
			else
				item.setAmount(item.getAmount() - amount);
	}
}
