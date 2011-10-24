package com.danstoncube.plugin.drzoid.Boutique;

public class BoutiqueItem
{
	
	public String itemName;
	public Integer itemId;
	public Integer itemDamage;
	public String itemShortname;
	public int itemStack = 64;
	
	public BoutiqueItem(Integer id, Integer damage)
	{
		// TODO Auto-generated constructor stub
		
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
		this.itemName = "";
		this.itemShortname = "";
		
		String[] strings = object.split(";");
		
		if(strings.length < 3 ) 
			return false;
			
		String[] item = strings[0].split(":");  
		
		if(item.length == 1)
		{
			this.itemId = Integer.parseInt(item[0]);
		}
		else if(item.length == 2)
		{
			this.itemId = Integer.parseInt(item[0]);
			this.itemDamage = Integer.parseInt(item[1]);
		}
		else if(item.length == 3)
		{
			this.itemId = Integer.parseInt(item[0]);
			this.itemDamage = Integer.parseInt(item[1]);
		}
		
		if(strings.length == 3)
		{
			this.itemShortname = strings[1];
			this.itemName = strings[2];
		}
		else if(strings.length == 2)
		{
			this.itemShortname = strings[1];
			this.itemName = strings[1];
		}
		
		
		return true;
	}
	
	
	
	
	
}

