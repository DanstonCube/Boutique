package com.danstoncube.plugin.drzoid.Boutique.SignTypes;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class BoutiqueSignServer extends BoutiqueSign
{
	
	public BoutiqueSignServer(Sign sign, Player owner, BoutiqueSign type)
	{
		super(sign, owner, type);
		// TODO Auto-generated constructor stub
	}

	public static String getTypeStr()
	{
		// TODO Auto-generated method stub
		return "server";
	}

}
