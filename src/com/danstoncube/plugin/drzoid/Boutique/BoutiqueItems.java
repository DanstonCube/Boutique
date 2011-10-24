package com.danstoncube.plugin.drzoid.Boutique;

import java.util.HashMap;

import org.bukkit.Bukkit;



public class BoutiqueItems
{
	
	private static HashMap<String,BoutiqueItem> _items = new HashMap<String,BoutiqueItem>();
	

	public static Boolean isValidItem(Integer itemId, Integer itemDamage)
	{			
		if(itemDamage == null)
			itemDamage = 0;
		
		String key = itemId + ":" + itemDamage;
		
		
		Bukkit.getServer().getLogger().info("isValiditems, key= " + key);
		
		return BoutiqueItems._items.containsKey(key);
	}
	
	
	public static String getItemName(Integer itemId, Integer itemDamage)
	{
		String key = itemId + ":" + itemDamage;
		
		if(BoutiqueItems._items.containsKey(key))
			return BoutiqueItems._items.get(key).itemName;

		return "";
	}
	
	public static String getItemShortname(Integer itemId, Integer itemDamage)
	{
		String key = itemId + ":" + itemDamage;
		
		if(BoutiqueItems._items.containsKey(key))
			return BoutiqueItems._items.get(key).itemShortname;

		return "";
	}


	public static void put(BoutiqueItem bi)
	{
		Bukkit.getServer().getLogger().info("AJOUT " + bi.itemId +":" + bi.itemDamage);
		
		
		String key = bi.itemId + ":" + bi.itemDamage;
		BoutiqueItems._items.put(key, bi);
	}
	
}
