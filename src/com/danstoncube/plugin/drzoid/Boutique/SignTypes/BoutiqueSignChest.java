package com.danstoncube.plugin.drzoid.Boutique.SignTypes;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class BoutiqueSignChest extends BoutiqueSign
{

	public BoutiqueSignChest(Sign sign, Player owner, BoutiqueSign type)
	{
		super(sign, owner, type);
		// TODO Auto-generated constructor stub
	}


	
	
	public static String getTypeStr()
	{
		// TODO Auto-generated method stub
		return "chest";
	}
	
	

}
