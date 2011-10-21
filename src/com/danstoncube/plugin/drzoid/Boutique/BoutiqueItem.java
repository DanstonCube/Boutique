package com.danstoncube.plugin.drzoid.Boutique;

public class BoutiqueItem
{
	
	public String itemName;
	public Integer itemId;
	public Integer itemDamage;
	public String itemShortname;
	
	public BoutiqueItem(Integer id, Integer damage)
	{
		// TODO Auto-generated constructor stub
		this.itemId = id;
		this.itemDamage = damage;
		this.itemName = BoutiqueItems.getItemName(id, damage);
		this.itemShortname = BoutiqueItems.getItemShortname(id, damage);
	}
	
	
	
	
	
	
}

