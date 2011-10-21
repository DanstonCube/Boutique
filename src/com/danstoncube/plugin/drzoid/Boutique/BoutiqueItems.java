package com.danstoncube.plugin.drzoid.Boutique;

import java.util.HashMap;



public class BoutiqueItems
{
	
	private static HashMap<String,BoutiqueItem> _items = new HashMap<String,BoutiqueItem>();
	
	
	public static void loadItems()
	{
		//Configuration itemsConfig = new Configuration(new File(""));
		//itemsConfig.getStringList("", new List<String>());
		
		
	}
	
	
	public static Boolean isValidItem(Integer itemId, Integer itemDamage)
	{	
		return _items.containsKey(itemId + ":" + itemDamage);
	}
	
	
	public static String getItemName(Integer itemId, Integer itemDamage)
	{
		return _items.get(itemId + ":" + itemDamage).itemName;
	}
	
	public static String getItemShortname(Integer itemId, Integer itemDamage)
	{
		return _items.get(itemId + ":" + itemDamage).itemShortname;
	}
	
}
