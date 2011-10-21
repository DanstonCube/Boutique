package com.danstoncube.plugin.drzoid.Boutique;

public class WebItemsOperator
{

	private Boutique plugin;

	
	WebItemsOperator(Boutique b)
	{
		this.plugin = b;
	}
	
	public Boolean containEnough(String p, Integer itemid, Integer itemdamage, Integer quantity)
	{
		return plugin.db.wa_HasEnoughItem(p, itemid, itemdamage, quantity);
	}

	public boolean removeFromWebStock(String p, int itemid, int itemdamage, int quantity)
	{
		// TODO Auto-generated method stub
		return plugin.db.wa_RemoveFromStock(p, itemid, itemdamage, quantity);
	}

	public boolean addToWebStock(String p, int itemid, int itemdamage, int quantity)
	{
		// TODO Auto-generated method stub
		return plugin.db.wa_AddToStock(p, itemid, itemdamage, quantity);
	}

	public int contains(String p, int giveType, int giveDamage)
	{
		// TODO Auto-generated method stub
		return plugin.db.wa_CountItem(p, giveType, giveDamage);
	}
	
	
	
	
	
	
	
	
}
