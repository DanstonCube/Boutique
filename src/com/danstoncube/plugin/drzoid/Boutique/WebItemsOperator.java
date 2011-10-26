package com.danstoncube.plugin.drzoid.Boutique;

public class WebItemsOperator
{
	public static String notEnoughErr = "La boutique web n'a plus assez d'objets en stock !";

	Boutique plugin  = null;
	WebItemsOperator(Boutique plugin)
	{
		this.plugin = plugin;
	}
	 
	public static Boolean containEnough(String p, Integer itemid, Integer itemdamage, Integer quantity)
	{
		return Boutique.getInstance().db.wa_HasEnoughItem(p, itemid, itemdamage, quantity);
	}

	public static boolean removeFromWebStock(String p, int itemid, int itemdamage, int quantity)
	{
		// TODO Auto-generated method stub
		return Boutique.getInstance().db.wa_RemoveFromStock(p, itemid, itemdamage, quantity);
	}

	public static boolean addToWebStock(String p, int itemid, int itemdamage, int quantity)
	{
		// TODO Auto-generated method stub
		return Boutique.getInstance().db.wa_AddToStock(p, itemid, itemdamage, quantity);
	}

	public static int contains(String p, int giveType, int giveDamage)
	{
		// TODO Auto-generated method stub
		return Boutique.getInstance().db.wa_CountItem(p, giveType, giveDamage);
	}
	
	
	
	
	
	
	
	
}
