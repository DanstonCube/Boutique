package com.danstoncube.plugin.drzoid.Boutique.SignTypes;

import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class BoutiqueSignChest extends BoutiqueSign
{

	public BoutiqueSignChest(Sign sign, Player owner, BoutiqueSign type)
	{
		super(sign, owner, type);
		// TODO Auto-generated constructor stub
	}

	private Chest _chest = null;
	
	
	public static String getTypeStr()
	{
		// TODO Auto-generated method stub
		return "chest";
	}

	public Chest getChest()
	{
		// TODO Auto-generated method stub
		return this._chest;
	} 
	
	public void setChest(Chest chest)
	{
		this._chest = chest;
	}

}
