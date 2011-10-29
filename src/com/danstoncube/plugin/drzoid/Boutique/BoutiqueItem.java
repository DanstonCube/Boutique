package com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.Bukkit;

public class BoutiqueItem
{
	
	public String itemName;
	public Integer itemId;
	public Integer itemDamage;
	public String itemShortname;
	public int itemStack = 64;
	
	public BoutiqueItem(Integer id, Integer damage)
	{
		Bukkit.getServer().getLogger().info("DBG ITEM: " + id + ":" + damage);

		
		if(damage == null)
			damage = 0;
		
		if(id == null)
			id = 0; 
		
		this.itemId = id;
		this.itemDamage = damage;
		this.itemName = BoutiqueItems.getItemName(id, damage);
		this.itemShortname = BoutiqueItems.getItemShortname(id, damage);
	}

	public BoutiqueItem()
	{
		// TODO Auto-generated constructor stub
	}
	
	
	public Boolean parseString(String object)
	{
		this.itemId = 0;
		this.itemDamage = 0;
		this.itemStack = 64;
		this.itemName = "Objet inconnu";
		this.itemShortname = "INCONNU";
		
		
		
		//ID:DAMAGE:SHORNAME:NAME:STACKTO
		String[] item = object.split(":");  
		
		if(item.length < 5) 
			return false;
		
		
		//StackTo renseigné ?
		if(item.length >= 5)
		{
			try
			{
				this.itemStack = Integer.parseInt(item[4]);
			}
			catch(NumberFormatException e)
			{
				
			}
		}
		
		
		//Name renseigné ?
		if(item.length >= 4)
		{
			this.itemName = item[3];
		}
		
		//Shortname renseigné ?
		if(item.length >= 3)
		{
			if(!item[2].isEmpty())
				this.itemShortname = item[2];			
		}
		
		//Damage renseigné ?
		if(item.length >= 2)
		{
			try
			{
				this.itemDamage = Integer.parseInt(item[1]);
			}
			catch(NumberFormatException e)
			{
			}
		}
		
		//Id renseigné ?
		if(item.length >= 1)
		{
			try
			{
				this.itemId = Integer.parseInt(item[0]);
			}
			catch(NumberFormatException e)
			{
				return false;
			}
		}
				
		
		
		if(this.itemId == 0)
			return false;
				
		if(this.itemName.isEmpty())
			this.itemName = this.itemShortname;
		
		
		return true;
	}
	
	
	
	
	
}

