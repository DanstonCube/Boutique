package com.danstoncube.plugin.drzoid.Boutique;


import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

import org.bukkit.block.Sign;
//import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;




public class SignManager 
{
	private Boutique plugin;
	
	private String activeErr = "Ce panneau est inactif.";
	
	private String blacklistErr = "Cet objet ne peut être acheté ni vendu.";
	
	private String similarTypeErr = "Même objet, on ne peut les échanger.";
	
	private String formatErr = "Le panneau a un format incorrect.";
	
	private String plugName;
	
	private String globalStr = "global";
	
	private String personalStr = "personal";
	
	private int maxDist = 30; // max x or z dist away (to keep from putting signs far away in a different chunk.)

	
	private HashMap<String,BoutiqueSignInfo> boutique_signs; 
	
	
	
	public SignManager (Boutique bt) 
	{
		this.plugin = bt;
		plugName = "" + ChatColor.BLUE + "[" + bt.displayname + "] " + ChatColor.WHITE;
	}

	public void LoadSigns()
	{	
		//boutique_signs.put("",new BoutiqueSignInfo("","","","",""));
	}
	
	
	public void setSign(Sign s, Player p) 
	{
		signSetter(s.getLines(),p,s.getBlock());
	}
	
	public void setSign(String[] lines, Player p, Block s) 
	{
		signSetter(lines, p, s);
	}
	
	
	
	private void signSetter(String[] lines, Player p, Block s) 
	{
		String type = SignOperator.getSignType(lines[0]);
		
		if (type == null)
			return;
		int[] lOneData = SignOperator.getTransFormat(lines[1]);
		if (lOneData == null)
			return;
		int[] lTwoData = SignOperator.getTransFormat(lines[2]);
		if (lTwoData == null)
			return;
		
		if (lTwoData[1] == lOneData[1]){
			p.sendMessage(plugName + similarTypeErr);
			return;
		}
		
		String location = s.getX() + ":" + s.getY() + ":" + s.getZ()  + ":" + s.getWorld().getName();
		String pName = p.getName();
		
		if (type.compareToIgnoreCase("global") == 0)
		{
			if (!PermissionsHandler.canSetGlobalSign(p))
			{
				p.sendMessage(PermissionsHandler.permissionErr);
				return;
			}
			p.sendMessage(plugName + "Panneau serveur ajouté à la liste :)");
			
			Boutique.signLocs.put(location, pName);
			Boutique.signLine1.put(location, lines[0]);
			Boutique.signLine2.put(location, lines[1]);
			Boutique.signLine3.put(location, lines[2]);
			
			
			plugin.fileIO.saveGlobalSigns();
			
			lines[3] = ChatColor.AQUA + "[Active]";
		}
		else if(type.compareToIgnoreCase("personal")==0)
		{
			
			if (!PermissionsHandler.canSetPersonalSign(p))
			{
				p.sendMessage(PermissionsHandler.permissionErr);
				return;
			}
			
			p.sendMessage(plugName + "Panneau joueur ajouté à la liste :)");
			
			
			//BoutiqueSignInfo newSign = new BoutiqueSignInfo(location, pName,  lines[0],  lines[1],  lines[2],  lines[3]);			
			//this.boutique_signs.remove(location);
			//this.boutique_signs.put(location, newSign);
			
			
			Boutique.signLocs.put(location, pName);
			Boutique.signLine1.put(location, lines[0]);
			Boutique.signLine2.put(location, lines[1]);
			Boutique.signLine3.put(location, lines[2]);
			
			lines[0] = p.getName();
			
			lines[3] = ChatColor.GREEN + "[Active]";
			
			
			plugin.fileIO.saveGlobalSigns();
		}
		
		/*
		lines[0] = "VENTE DE x LOT DE";
		lines[1] = "ITEM";
		lines[2] = "a 55 EU LE LOT";
		*/
		
		/*
		//lines[0] = "VENTE DE x LOT"
		*/					
		
		
		//((CraftWorld)s.getWorld()).getHandle().g(s.getX(),s.getY(),s.getZ());
	}
	
	public void useSign(Sign s, Player p) 
	{
		String location = s.getX() + ":" + s.getY() + ":" + s.getZ() + ":" + s.getWorld().getName();
		
		//See if the sign has been activated
		String signOwner = "";
		if (Boutique.signLocs.containsKey(location))
			signOwner = Boutique.signLocs.get(location);
		else
			return;	
		
		
		
		String signLine1 = "";
		if (Boutique.signLine1.containsKey(location))
			signLine1 = Boutique.signLine1.get(location);
		else
			return;	
		
		String signLine2 = "";
		if (Boutique.signLine2.containsKey(location))
			signLine2 = Boutique.signLine2.get(location);
		else
			return;	
		
		String signLine3 = "";
		if (Boutique.signLine3.containsKey(location))
			signLine3 = Boutique.signLine3.get(location);
		else
			return;	
			
		
		String signType = SignOperator.getSignType(signLine1);
		if(signType == null)
			return;
		
		//Get transaction data and check it.
		int[] lOneData = SignOperator.getTransFormat(signLine2);
		int[] lTwoData = SignOperator.getTransFormat(signLine3);
		
		if (lOneData == null || lTwoData == null){
			return;
		}
		
		
		
		
		Chest chest = null;
		
		if (signType.compareToIgnoreCase("personal") == 0) 
		{
			int chestFind = SignOperator.findChest(location, s, signOwner);
			
			if (chestFind == 1 || chestFind == 0)
			{
				chest = SignOperator.getChest(location, s);
			}
			else 
			{
				p.sendMessage(plugName + SignOperator.sendChestErr(chestFind));
				return;
			}
			
		}
		
		
		// See if player has permission to use signs.
		if(!PermissionsHandler.canUseSign(p))
		{
			p.sendMessage(plugName + PermissionsHandler.permissionErr);
			return;
		}
		
		if (lOneData[0] < -1)
		{
			p.sendMessage(plugName + SignOperator.sendTransFormatErr(lOneData[0]));
			return;
		}
		else if (lTwoData[0] < -1)
		{
			p.sendMessage(plugName + SignOperator.sendTransFormatErr(lTwoData[0]));
			return;
		}
		
		if (!Boutique.signLocs.containsKey(location))
		{
			p.sendMessage(plugName + activeErr);
			return;
		}
		
		// Check to see if the items were blacklisted
		if ((lOneData[1] > 0) && (SignOperator.isBlackListed(lOneData[1])))
		{
			p.sendMessage(plugName  + blacklistErr);
			return;
		}
		if ((lTwoData[1] > 0) && (SignOperator.isBlackListed(lTwoData[1])))
		{
			p.sendMessage(plugName + blacklistErr);
			return;
		}
		
		
		
		//FREEBIES
		if(lOneData[0] == 0 && lTwoData[1] > 0)
		{
			giveFree(signType, p, chest, lTwoData);
			
			
			//<MIKO>
			int itemAmount = lTwoData[0];
			int itemType = lTwoData[1];
			int itemDamage = lTwoData[2];
			int itemPrice = lOneData[0];
			
			String itemText = "";
			
			if(itemType == 35)
			{
				itemText = SignOperator.woolText(itemDamage);
			}
			else if(itemType == 351)
			{
				itemText = SignOperator.inkText(itemDamage);
			}
			else
			{
				itemText = Boutique.itemIdName.get(itemType);
			}
			
			
			SignOperator.UpdateFreeSign(s, chest, signType, itemAmount, itemType, itemDamage, itemText, itemPrice, "Eu");
			//</MIKO>
		}
		//DONATION
		else if(lTwoData[0] == 0 && lOneData[1] > 0)
		{
			getDonation(signType, s, p, chest, lOneData);			
		}
		//VENTE
		else if(lOneData[1] == -1)
		{
			sellItem(signType, p, chest, lOneData[0], lTwoData, signOwner);
			
			//<MIKO>
			int itemAmount = lTwoData[0];
			int itemType = lTwoData[1];
			int itemDamage = lTwoData[2];
			int itemPrice = lOneData[0];
			
			String itemText = "";
			
			if(itemType == 35)
			{
				itemText = SignOperator.woolText(itemDamage);
			}
			else if(itemType == 351)
			{
				itemText = SignOperator.inkText(itemDamage);
			}
			else
			{
				itemText = Boutique.itemIdName.get(itemType);
			}
			
			
			SignOperator.UpdateSellSign(s, chest, signType, itemAmount, itemType, itemDamage, itemText, itemPrice, "Eu");
			//</MIKO>
			
		}
		
		//ACHAT
		else if(lTwoData[1] == -1)
		{
			buyItem(signType, p, chest, lOneData, lTwoData[0], signOwner);
		}
		
		//ECHANGE
		else if (lTwoData[1] > 0 && lOneData[1] > 0)
		{
			tradeItems(signType, p, chest, lOneData, lTwoData);
			
			//<MIKO>
			int giveAmount = lOneData[0];
			int giveType = lOneData[1];
			int giveDamage = lOneData[2];
			String giveTypeText = "";
			
			int getAmount = lTwoData[0];
			int getType = lTwoData[1];
			int getDamage = lTwoData[2];
			String getTypeText = "";

			
			if(giveType == 35)
			{
				giveTypeText = SignOperator.woolText(giveDamage);
			}
			else if(giveType == 351)
			{
				giveTypeText = SignOperator.inkText(giveDamage);
			}
			else
			{
				giveTypeText = Boutique.itemIdName.get(giveType);
			}
			
			if(getType == 35)
			{
				getTypeText = SignOperator.woolText(getDamage);
			}
			else if(getType == 351)
			{
				getTypeText = SignOperator.inkText(getDamage);
			}
			else
			{
				getTypeText = Boutique.itemIdName.get(getType);
			}
			
			SignOperator.UpdateTradeSign(s, chest, signType, giveAmount, giveType,giveDamage,giveTypeText,getAmount, getType, getDamage,getTypeText);
			
		}
		//catch
		else {
			p.sendMessage(plugName + formatErr );
		}
	}

	private void tradeItems(String signType, Player p, Chest chest, int[] lOneData, int[] lTwoData) 
	{
		if (signType.compareToIgnoreCase(globalStr) == 0)
		{
			if (!PlayerOperator.playerHasEnough(lOneData[0], lOneData[1], lOneData[2], p))
			{
				p.sendMessage(plugName + PlayerOperator.playerStockErr);
			}
			else 
			{
				PlayerOperator.removeFromPlayer(lOneData[0], lOneData[1], lOneData[2], p);
				PlayerOperator.givePlayerItem(lTwoData[0], lTwoData[1], lTwoData[2], p);
			}
		}
		else if (signType.compareToIgnoreCase(personalStr) == 0)
		{
			if (!PlayerOperator.playerHasEnough(lOneData[0], lOneData[1], lOneData[2], p))
			{
				p.sendMessage(plugName + PlayerOperator.playerStockErr);
			}
			else if (!ChestOperator.containsEnough(lTwoData[0], lTwoData[1], lTwoData[2], chest))
			{
				p.sendMessage(plugName + ChestOperator.notEnoughErr);
			}
			else if (!ChestOperator.hasEnoughSpace(lOneData[0], lOneData[1], lOneData[2], chest))
			{
				p.sendMessage(plugName + ChestOperator.notEnoughSpaceErr);
			}
			else 
			{
				ChestOperator.removeFromChestStock(lTwoData[0], lTwoData[1], lTwoData[2], chest);
				ChestOperator.addToChestStock(lOneData[0], lOneData[1], lOneData[2], chest);
				PlayerOperator.removeFromPlayer(lOneData[0], lOneData[1], lOneData[2], p);
				PlayerOperator.givePlayerItem(lTwoData[0], lTwoData[1], lTwoData[2], p);
			}				
		}
	}

	private void buyItem(String signType, Player p, Chest chest, int[] lOneData, int costAmount, String signOwner) 
	{
		if (signType.compareToIgnoreCase(globalStr) == 0)
		{
			if (!PlayerOperator.playerHasEnough(lOneData[0], lOneData[1], lOneData[2], p))
			{
				p.sendMessage(plugName + PlayerOperator.playerStockErr);
				return;
			}
			else 
			{
				PlayerOperator.removeFromPlayer(lOneData[0], lOneData[1], lOneData[2], p);
				EconomyHandler.modifyMoney(p.getName(), costAmount);
				signOwner = "";
			}
			
		}
		else if (signType.compareToIgnoreCase(personalStr) == 0)
		{
			if (!PlayerOperator.playerHasEnough(lOneData[0], lOneData[1], lOneData[2], p))
			{
				p.sendMessage(plugName + PlayerOperator.playerStockErr);
				return;
			}
			if (!ChestOperator.hasEnoughSpace(lOneData[0], lOneData[1], lOneData[2], chest))
			{
				p.sendMessage(plugName + ChestOperator.notEnoughSpaceErr);
				return;
			}
			
			int econ = EconomyHandler.hasEnough(signOwner, costAmount);
			
			if (econ != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econ));
				return;
			}
			
			EconomyHandler.modifyMoney(signOwner, -costAmount);
			ChestOperator.addToChestStock(lOneData[0], lOneData[1], lOneData[2], chest);
			PlayerOperator.removeFromPlayer(lOneData[0], lOneData[1], lOneData[2], p);
			EconomyHandler.modifyMoney(p.getName(), costAmount);
			
			
		}
		
		p.sendMessage(plugName + "Tu as maintenant " + EconomyHandler.playerHave(p.getName()) + ".");
		
		try 
		{
			
			this.plugin.db.logTransaction(p.getLocation(), p.getName(), signOwner, lOneData[1] , lOneData[2] , lOneData[0], Double.parseDouble(Integer.toString(costAmount)),"toto","titi");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
		
	}
	
	/*
	 * 		Vente d'un objet
	 */
	
	private void sellItem(String signType, Player p, Chest chest, int costAmount, int[] lTwoData, String signOwner) 
	{
		//Signe Serveur
		if (signType.compareToIgnoreCase(globalStr) == 0)
		{
			int econ = EconomyHandler.hasEnough(p.getName(), costAmount);
			if (econ != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econ));
				return;
			}

			EconomyHandler.modifyMoney(p.getName(), -costAmount);
			PlayerOperator.givePlayerItem(lTwoData[0], lTwoData[1], lTwoData[2], p);
			signOwner = "";
		}
		//Signe Joueur
		else if (signType.compareToIgnoreCase(personalStr) == 0)
		{
			int econPlayer = EconomyHandler.hasEnough(p.getName(), costAmount);
			if (econPlayer != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econPlayer));
				return;
			}
			else if (!ChestOperator.containsEnough(lTwoData[0], lTwoData[1], lTwoData[2], chest))
			{
				p.sendMessage(plugName + ChestOperator.notEnoughErr);
				return;
			}
			
			int econOwner = EconomyHandler.modifyMoney(signOwner, costAmount);
			if (econOwner != 1)
			{
				p.sendMessage(plugName + EconomyHandler.getEconError(econPlayer));
				return;
			}
			
			EconomyHandler.modifyMoney(p.getName(), -costAmount);			
			ChestOperator.removeFromChestStock(lTwoData[0], lTwoData[1], lTwoData[2], chest);
			PlayerOperator.givePlayerItem(lTwoData[0], lTwoData[1], lTwoData[2], p);
		}
		
		
		p.sendMessage(plugName + "Il te reste " + EconomyHandler.playerHave(p.getName()));
			
		try 
		{
			this.plugin.db.logTransaction(p.getLocation(), signOwner, p.getName(), lTwoData[1] , lTwoData[2] , lTwoData[0], Double.parseDouble(Integer.toString(costAmount)),"toto","titi");
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		
		
	}
	
	
	
	

	private void getDonation(String signType, Sign s, Player p, Chest chest, int[] lOneData) {
		if (signType.compareToIgnoreCase(globalStr) == 0)
			return; //Global signs dont take donations atm.
		
		else if (signType.compareToIgnoreCase(personalStr) == 0){
			if (!PlayerOperator.playerHasEnough(lOneData[0], lOneData[1], lOneData[2], p))
				p.sendMessage(plugName + PlayerOperator.playerStockErr);
			else if (!ChestOperator.hasEnoughSpace(lOneData[0], lOneData[1], lOneData[2], chest))
				p.sendMessage(plugName + ChestOperator.notEnoughSpaceErr);
			else 
			{
				PlayerOperator.removeFromPlayer(lOneData[0], lOneData[1], lOneData[2], p);
				ChestOperator.addToChestStock(lOneData[0], lOneData[1], lOneData[2], chest);
				
				
				
			}
		}
	}

	private void giveFree(String signType, Player p, Chest chest, int[] lTwoData) {
		if (signType.compareToIgnoreCase(globalStr) == 0)
			PlayerOperator.givePlayerItem(lTwoData[0], lTwoData[1], lTwoData[2], p);
		else if (signType.compareToIgnoreCase(personalStr) == 0){
			if (ChestOperator.containsEnough(lTwoData[0], lTwoData[1], lTwoData[2], chest)){
				ChestOperator.removeFromChestStock(lTwoData[0], lTwoData[1], lTwoData[2], chest);
				PlayerOperator.givePlayerItem(lTwoData[0], lTwoData[1], lTwoData[2], p);
			}
			else
				p.sendMessage(plugName + ChestOperator.notEnoughErr);
		}
	}
	
	public void setChest(Sign s, Chest c, Player p)
	{
		String sLoc = s.getX() + ":" + s.getY() + ":" + s.getZ()  + ":" + s.getWorld().getName();
		String cLoc = c.getX() + ":" + c.getY() + ":" + c.getZ(); // chests can only be in the same world as the sign and close.
		
		int distX = s.getX() - c.getX();
		int distZ = s.getZ() - c.getZ();
		
		if (distX > maxDist || distZ > maxDist ) {
			p.sendMessage(plugName + "Le coffre est trop loin du panneau.");
			return;
		}
		
		
		if (Boutique.SignChest.get(sLoc) == cLoc){
			p.sendMessage(plugName + "Ce panneau est déjà relié à ce coffre :/");
			return;
		}
		
		
		Boutique.SignChest.put(sLoc, cLoc);
		
		p.sendMessage(plugName + "Panneau et coffre reliés !");
	}

	public void setOwner(Player p, String str, Sign s) 
	{
		if (!SignOperator.isSign(s))
		{
			p.sendMessage(plugName + "Ce panneau est mal configuré. Reposes le correctement avant.");
			return;
		}
		
		s.setLine(0, str);
		String location = s.getX() + ":" + s.getY() + ":" + s.getZ()  + ":" + s.getWorld().getName();
		Boutique.signLocs.put(location, str);
		
		//TODO: a changer
		plugin.fileIO.saveGlobalSigns();
		
		//((CraftWorld)s.getWorld()).getHandle().g(s.getX(),s.getY(),s.getZ());
	}

	public HashMap<String,BoutiqueSignInfo> getSigns() {
		return boutique_signs;
	}

	public void setSigns(HashMap<String,BoutiqueSignInfo> boutique_signs) {
		this.boutique_signs = boutique_signs;
	}
}
