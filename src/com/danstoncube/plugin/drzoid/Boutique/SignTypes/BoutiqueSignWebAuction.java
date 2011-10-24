package com.danstoncube.plugin.drzoid.Boutique.SignTypes;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class BoutiqueSignWebAuction extends BoutiqueSign
{
	public BoutiqueSignWebAuction(Sign sign, Player owner)
	{
		super(sign, owner, BoutiqueSignWebAuction.getTypeStr());
		// TODO Auto-generated constructor stub
	}

	public static String getTypeStr()
	{
		// TODO Auto-generated method stub
		return "webauction";
	}

	
	
}
