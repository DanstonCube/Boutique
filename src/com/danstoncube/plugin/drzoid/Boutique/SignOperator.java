package com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;



public class SignOperator 
{

	private String noProtErr = "Les coffres ne sont pas protègés.";
	private String notOwnerErr = "Le proprio du panneau n'est pas le proprio du coffre.";
	private String noChestErr = "Ce panneau n'est pas relié à un coffre.";
	private String oddErr = "Hum y'a une erreur pas cool...";
	private String needOwnerErr = "Le coffre relié a besoin d'un proprio.";
	private String itemTypeErr = "Hein, je connais pas cet item oO";
	private String formattingErr = "Tu sais pas faire un panneau mec, je pige keudal.";
	
	private Boutique plugin;
	
	SignOperator(Boutique p)
	{
		plugin = p;
	}
	
	
	public static String getSignType(String str) 
	{
		if (str.compareToIgnoreCase("global") == 0 || str.compareToIgnoreCase("[Global]") == 0)
			return "global";
		else if (str.compareToIgnoreCase("web") == 0 || str.compareToIgnoreCase("[Web]") == 0)
			return "web";
		else if (str.length() > 0)
			return "personal";
		
		return null;
	}
	
	public boolean isSign(Block b) 
	{
		if (!(b.getState() instanceof Sign))
			return false;
		
		String location = b.getX() + ":" + b.getY() + ":" + b.getZ() + ":" + b.getWorld().getName();
		
		return Boutique.signLocs.containsKey(location);
	}
	
	public boolean isSign(Sign s) 
	{
		String location = s.getX() + ":" + s.getY() + ":" + s.getZ() + ":" + s.getWorld().getName();
		if(Boutique.signLocs.containsKey(location))
			return true;
		
		return false;
	}
	
	public boolean isBlackListed (int type) 
	{
		return !Boutique.sign.containsKey(type);
	}
	
	public boolean isBlackListed (String type) 
	{
		return !Boutique.itemNameId.containsKey(type);
	}
	
	public int[] getTransFormat(String str) 
	{
		//Returns int,int,int for amount : type : damage
		//Returns 0,0,0 if Free
		//Returns int,-1,0 if it is using money
		//Returns -2,0.0 if there is no economy system
		//Returns -3,0,0 if the item type specified does not exist.
		//Returns null if there is a formatting error.
		int[] retInt = new int[3];
		retInt[0] = 0;
		retInt[1] = 0;
		retInt[2] = 0; // if no damage set, defaults to 0
		
		if(str.startsWith("Free")) 
		{
			return retInt;	
		}
		else 
		{
			String[] s = str.split(":");
			if (s.length == 2)
			{
				if (str.endsWith("$"))
				{
					if(!EconomyHandler.currencyEnabled)
					{
						retInt[0] = -2;
						return retInt;
					}
					try
					{
						retInt[0] = Integer.parseInt(s[0]);
						retInt[1] = -1;
						return retInt;
					}
					catch(Exception e)
					{
						return null;
					}
				}
				else
				{
					try
					{
						retInt[0] = Integer.parseInt(s[0]);
						retInt[1] = Integer.parseInt(s[1]);
						if(Material.getMaterial(retInt[1]) == null)
						{
							retInt[0] = -3;
						}
						return retInt;
					}
					catch(Exception e)
					{
						return null;
					}
				}
			}
			//for handling itemdata
			else if (s.length == 3)
			{
				try
				{
					retInt[0] = Integer.parseInt(s[0]);
					retInt[1] = Integer.parseInt(s[1]);
					retInt[2] = Integer.parseInt(s[2]);
					if(Material.getMaterial(retInt[1]) == null)
					{
						retInt[0] = -3;
					}
					return retInt;
				}
				catch(Exception e)
				{
					return null;
				}
			}
		}
		return null;
	}
	
	
	public void UpdateSellSign(Sign s,Chest c, String signType, int giveAmount, int giveType, int giveDamage, String giveTypeText, int getAmount, String getTypeText)
	{			
		
		if(signType.compareToIgnoreCase("global") == 0)
		{
			//Sign Global
			//mise a jour du panneau
			s.setLine(0, ChatColor.GOLD + "[" + ChatColor.GREEN + "VENTE" + ChatColor.GOLD + "]"); // + ChatColor.WHITE + " de lots de");
			s.setLine(1, ChatColor.WHITE + "Lots de " + ChatColor.AQUA + "x" + giveAmount ); //ChatColor.BLACK + giveTypeText);			
			s.setLine(2, ChatColor.AQUA + giveTypeText);
			s.setLine(3, ChatColor.YELLOW +  String.valueOf(getAmount) + getTypeText + ChatColor.WHITE + " le lot");
			//s.setLine(3, ChatColor.GREEN + "[" + ChatColor.GOLD + "Serveur" + ChatColor.GREEN + "]");					
			s.update();
		}
		else
		{
			//Sign Player
			//recherche chest
			if(c!=null)
			{
				//compte le nombre de stacks dispos au total dans le chest						
				int lots = (int)(ChestOperator.contains(giveType, giveDamage, c) / giveAmount);		
				
				//couleur suivant la quantité
				String strLot = "";						
				if(lots == 0)		strLot = ChatColor.RED + String.valueOf(lots) + ChatColor.BLACK;
				else if(lots < 5)	strLot = ChatColor.YELLOW + String.valueOf(lots) + ChatColor.BLACK;
				else 				strLot = ChatColor.DARK_GREEN + String.valueOf(lots) + ChatColor.BLACK;
				
				String strNbLot = "";
				if(lots > 1)	strNbLot = "Lots de ";
				else			strNbLot = "Lot de ";
				
				String strAmount = ChatColor.YELLOW + String.valueOf(getAmount) + getTypeText;
				
				//mise a jour du panneau
				
				s.setLine(0, ChatColor.GREEN + "VEND " + strLot); // + ChatColor.WHITE + " de lots de");
				s.setLine(1, ChatColor.WHITE + strNbLot + ChatColor.AQUA + "x" + giveAmount ); //ChatColor.BLACK + giveTypeText);			
				s.setLine(2, ChatColor.AQUA + giveTypeText);
				s.setLine(3, strAmount + ChatColor.WHITE + " / lot");
				s.update();
			}
			
		}
		
	}
	
	public void UpdateFreeSign(Sign s,Chest chest, String waplayer, String signType, int giveAmount, int giveType, int giveDamage, String giveTypeText, int getAmount, String getTypeText)
	{			
		if(chest==null && signType.compareToIgnoreCase("personal")==0) return;
		if(waplayer=="" && signType.compareToIgnoreCase("web")==0) return;
		
		if(signType.compareToIgnoreCase("global") == 0)
		{
			//Sign Global
			//mise a jour du panneau
			s.setLine(0, ChatColor.GOLD + "[" + ChatColor.GREEN + "GRATUIT" + ChatColor.GOLD + "]"); // + ChatColor.WHITE + " de lots de");
			s.setLine(1, ChatColor.WHITE + "Lots de " + ChatColor.AQUA + "x" + giveAmount ); //ChatColor.BLACK + giveTypeText);			
			s.setLine(2, ChatColor.AQUA + giveTypeText);
			s.setLine(3, "");
			s.update();
		}
		else
		{
			int lots=0;
			
			if(signType.compareToIgnoreCase("personal") == 0)
			{
				//Sign Player, recherche dans chest
				//compte le nombre de stacks dispos au total dans le chest						
				lots = (int)(ChestOperator.contains(giveType, giveDamage, chest) / giveAmount);		
			}
			else if(signType.compareToIgnoreCase("web") == 0)
			{
				//compte le nombre de stacks dispos au total dans le chest	
				lots = plugin.webitems.contains(waplayer, giveType, giveDamage) / giveAmount;		
			}
			else
			{
				return;
			}
			
			//couleur suivant la quantité
			String strLot = "";						
			if(lots == 0)		strLot = ChatColor.RED + String.valueOf(lots) + ChatColor.BLACK;
			else if(lots < 5)	strLot = ChatColor.YELLOW + String.valueOf(lots) + ChatColor.BLACK;
			else 				strLot = ChatColor.DARK_GREEN + String.valueOf(lots) + ChatColor.BLACK;
			
			String strNbLot = "";
			if(lots > 1)	strNbLot = "Lots de ";
			else			strNbLot = "Lot de ";
			
			//mise a jour du panneau
			
			s.setLine(0, ChatColor.GREEN + "GRATUIT " + strLot); // + ChatColor.WHITE + " de lots de");
			s.setLine(1, ChatColor.WHITE + strNbLot + ChatColor.AQUA + "x" + giveAmount ); //ChatColor.BLACK + giveTypeText);
			s.setLine(2, ChatColor.AQUA + giveTypeText);
			s.setLine(3, "");
			s.update();
			
			
		}
		
	}
	
	public static void UpdateBuySign(Sign s, String signType, int giveAmount, int giveType, int giveDamage, String giveTypeText, int getAmount, String getTypeText)
	{
		if(signType.compareToIgnoreCase("global") == 0)
			s.setLine(0, ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "ACHAT" + ChatColor.GOLD + "]"); // + ChatColor.WHITE + " de lots de");
		else
			s.setLine(0, ChatColor.LIGHT_PURPLE + "ACHAT"); // + ChatColor.WHITE + " de lots de");
		
		s.setLine(1, ChatColor.WHITE + "Lots de " + ChatColor.AQUA + "x" + getAmount ); //ChatColor.BLACK + giveTypeText);			
		s.setLine(2, ChatColor.AQUA + getTypeText);
		s.setLine(3, ChatColor.WHITE + "pour " + ChatColor.YELLOW + giveAmount + " " + ChatColor.GOLD + giveTypeText + ChatColor.WHITE + " / lot");
		s.update();
	}
	
	public static void UpdateDonationSign(Sign s, String signType, int giveAmount, int giveType, int giveDamage, String giveTypeText, int getAmount, String getTypeText)
	{
		if(signType.compareToIgnoreCase("global") == 0)
		{
			s.setLine(0, ChatColor.GOLD + "[" + ChatColor.LIGHT_PURPLE + "DONATION" + ChatColor.GOLD + "]"); // + ChatColor.WHITE + " de lots de");
		}
		else
		{
			s.setLine(0, ChatColor.LIGHT_PURPLE + "DONATION"); // + ChatColor.WHITE + " de lots de");
		}
		
		s.setLine(1, ChatColor.WHITE + "Lots de " + ChatColor.AQUA + "x" + getAmount ); //ChatColor.BLACK + giveTypeText);			
		s.setLine(2, ChatColor.AQUA + getTypeText);
		s.setLine(3, ChatColor.WHITE + "");
		s.update();
	}
	
	public static void UpdateTradeSign(Sign s, Chest c, String signType, int giveAmount, int giveType, int giveDamage, String giveTypeText, int getAmount, int getType, int getDamage, String getTypeText)
	{
		if(signType.compareToIgnoreCase("global") == 0)
		{			
			s.setLine(0, ChatColor.GOLD + "[" + ChatColor.YELLOW + "ECHANGE" + ChatColor.GOLD + "]"); // + ChatColor.WHITE + " de lots de");		
			s.setLine(1, ChatColor.AQUA + giveTypeText + " x" + giveAmount); //ChatColor.BLACK + giveTypeText);			
			s.setLine(2, ChatColor.WHITE + "Contre");
			s.setLine(3, ChatColor.AQUA + getTypeText + " x" + getAmount);
		}
		else
		{
			
			if(c!=null)
			{
				//compte le nombre de stacks dispos au total dans le chest						
				int lots = (int)(ChestOperator.contains(getType, getDamage, c) / getAmount);		
				
				//couleur suivant la quantité
				String strLot = "";						
				if(lots == 0)		strLot = ChatColor.RED + String.valueOf(lots) + ChatColor.WHITE;
				else if(lots < 5)	strLot = ChatColor.YELLOW + String.valueOf(lots) + ChatColor.WHITE;
				else 				strLot = ChatColor.DARK_GREEN + String.valueOf(lots) + ChatColor.WHITE;
				
				if(lots > 1) 		strLot += " lots";
				else				strLot += " lot";
					
				s.setLine(0, ChatColor.YELLOW + "ECHANGE");
				s.setLine(1, ChatColor.AQUA + giveTypeText + " x" + giveAmount); //ChatColor.BLACK + giveTypeText);			
				s.setLine(2, ChatColor.WHITE + "Contre " + strLot + " de");
				s.setLine(3, ChatColor.AQUA + getTypeText + " x" + getAmount);
			}

		}
		s.update();
	}
	
	
	@Deprecated
	public void displaySignInfo(Sign s, Player p) 
	{
		String location = s.getX() + ":" + s.getY() + ":" + s.getZ() + ":" + s.getWorld().getName();
		String separator = "-----------------------------------";
		
		p.sendMessage(separator);
		if (!Boutique.signLocs.containsKey(location))
		{
			p.sendMessage(ChatColor.YELLOW + "Le panneau n'est pas actif.");
			p.sendMessage(ChatColor.YELLOW + "Activez le en reposant le panneau.");
			return;
		}		
		
		
		String signOwner = Boutique.signLocs.get(location);
		
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
		
		String signType = getSignType(signLine1);
		if(signType == null)
			return;
		
		int[] lOneData = this.getTransFormat(signLine2);
		int[] lTwoData = this.getTransFormat(signLine3);
		if ((lOneData == null)||(lTwoData == null))
			return;
		
		
		
		if(signType.compareToIgnoreCase("global") == 0)
		{
			p.sendMessage("Ce panneau fait partie du magasin du serveur.");
		}
		else if (signType.compareToIgnoreCase("personal") == 0)
		{
			p.sendMessage("Ce panneau fait partie du magasin de " + ChatColor.RED + Boutique.signLocs.get(location) + ChatColor.WHITE +  ".");
			if(getChest(location, s) == null) 
			{
				p.sendMessage(ChatColor.AQUA + "Aucun coffre n'est relié au panneau !");				
			}
		}
		
		
		if(lOneData[0] == -2) 
		{
			p.sendMessage("Il n'y a pas iConomy sur le serveur !");
			p.sendMessage(separator);
			return;
		}
		
		if (lOneData[0] == -3) 
		{	
			p.sendMessage("Erreur sur la première ligne du panneau: item incorrect.");
			return;
		}
		
		if (lTwoData[0] == -3) 
		{
			p.sendMessage("Erreur sur la deuxième ligne du panneau: item incorrect.");
			return;
		}		
		
		if (lOneData[0] == -3 || lTwoData[0] == -3)
		{
			p.sendMessage(separator);
			return;
		}
		
		int getAmount = lOneData[0];
		int giveAmount = lTwoData[0];
		int getType = lOneData[1];
		int giveType = lTwoData[1];
		int getDamage = lOneData[2];
		int giveDamage = lTwoData[2];
		
		
		//getTypeText
		
		String getTypeText = "";
		if (getType == 35)
		{
			getTypeText = woolText(getDamage);
		}
		else if (getType == 351)
		{
			getTypeText = inkText(getDamage);
		}
		else if (getType == -1)
		{
			getTypeText = EconomyHandler.currencyName;
		}
		else 
		{
			getTypeText = Boutique.itemIdName.get(getType); //Material.getMaterial(getType).toString().toLowerCase(Locale.FRENCH);
			if (getDamage != 0)
			{
				getTypeText += ", avec " + getDamage + " de dégats,";
			}
		}
		
		
		//giveTypeText
		
		String giveTypeText = "";
		if (giveType == 35)
		{
			giveTypeText = woolText(giveDamage);
		}
		else if (giveType == 351)
		{
			giveTypeText = inkText(giveDamage);
		}
		else if (giveType == -1)
		{
			giveTypeText = EconomyHandler.currencyName;
		}
		else 
		{
			giveTypeText = Boutique.itemIdName.get(giveType);
			if (giveDamage != 0)
			{
				giveTypeText += ", avec " + giveDamage + " de dégats,";
			}
		}
		
		
		
		
		
		
		//if the sign is handing away freebies.
		if(lOneData[0] == 0 || lOneData[1] == 0)
		{
			if (lTwoData[1] > 0)
			{	
				p.sendMessage("Un clic-droit sur ce panneau te donnera  " + ChatColor.RED + giveAmount +  ChatColor.WHITE + " "	+ ChatColor.RED + giveTypeText + ChatColor.WHITE + " gratuitement.");				
				
				if(signType.compareToIgnoreCase("global")!=0)
				{
					Chest c = SignOperator.getChest(location,s);
					if(c != null) 
					{
						plugin.signoperator.UpdateFreeSign(s, c, signOwner, signType, giveAmount, giveType, giveDamage, giveTypeText, getAmount, getTypeText);
					}
				}				
			}
			
		}
		//if the sign is taking donations
		else if(getAmount == 0 || giveAmount == 0)
		{
			if(signType.compareToIgnoreCase("global")==0)
			{
				p.sendMessage("Un panneau serveur (global) ne peut pas recevoir de donation.");
			}
			else
			{
				p.sendMessage("Ce panneau recoit des dons de " + ChatColor.RED + getAmount + ChatColor.WHITE + " " + ChatColor.RED + getTypeText + ChatColor.WHITE + ".");
				SignOperator.UpdateDonationSign(s, signType, giveAmount, giveType, giveDamage, giveTypeText, getAmount, getTypeText);
				
			}
		}
		
		
		
		
		//if the sign is selling items.
		else if(lOneData[1] == -1)
		{
			if (lTwoData[1] > 0)
			{
				p.sendMessage("Un clic-droit sur ce panneau te donnera " + ChatColor.RED + giveAmount + ChatColor.WHITE + " "
						+ ChatColor.RED + giveTypeText + ChatColor.WHITE + " pour la somme de " + ChatColor.RED + getAmount + " " + ChatColor.WHITE + getTypeText + ".");
			
				
				Chest c = SignOperator.getChest(location,s);
				this.UpdateSellSign(s, c, signType, giveAmount, giveType, giveDamage, giveTypeText, getAmount, getTypeText);
				
				
				
				
					
			}
		}
		
		
		//if the sign is buying items.
		else if(lTwoData[1] == -1){
			p.sendMessage("Ce panneau te donne " + ChatColor.RED + giveAmount 
					+ " " + ChatColor.WHITE + giveTypeText + " pour chaque " + ChatColor.RED + getAmount
					+ " " + getTypeText + ChatColor.WHITE + "(s) que tu lui donneras.");
			
			SignOperator.UpdateBuySign(s, signType, giveAmount, giveType, giveDamage, giveTypeText, getAmount, getTypeText);
			
		}
		//if the sign is trading for items.
		else{
			p.sendMessage("Ce panneau échange " + ChatColor.RED + giveAmount + ChatColor.WHITE + " " 
					+ ChatColor.RED + giveTypeText + ChatColor.WHITE + ", contre " 
					+ ChatColor.RED + getAmount + " " + getTypeText + ChatColor.WHITE + "(s).");
			
			Chest c = SignOperator.getChest(location,s);
			SignOperator.UpdateTradeSign(s, c, signType,  getAmount, getType,getDamage,getTypeText,giveAmount, giveType, giveDamage,giveTypeText);
			
		}
		
		
		
		p.sendMessage(separator);
	}

	
	public static String woolText(int damage) 
	{
		if (damage == 0)
			return "L. Blanche";
		if (damage == 1)
			return "L. Orange";
		if (damage == 2)
			return "L. Magenta";
		if (damage == 3)
			return "L. Bleu clair";
		if (damage == 4)
			return "L. Jaune";
		if (damage == 5)
			return "L. Vert clair";
		if (damage == 6)
			return "L. Rose";
		if (damage == 7)
			return "L. Grise";
		if (damage == 8)
			return "L. Gris clair";
		if (damage == 9)
			return "L. Cyan";
		if (damage == 10)
			return "L. Pourpre";
		if (damage == 11)
			return "L. Bleue";
		if (damage == 12)
			return "L. Marron";
		if (damage == 13)
			return "L. Verte";
		if (damage == 14)
			return "L. Rouge";
		if (damage == 15)
			return "L. Noire";
		
		return null;
	}

	
	public static String inkText(int damage) 
	{
		if (damage == 0)
			return "C. Noir";
		if (damage == 1)
			return "C. Rouge";
		if (damage == 2)
			return "C. Vert";
		if (damage == 3)
			return "C. Marron";
		if (damage == 4)
			return "C. Bleu";
		if (damage == 5)
			return "C. Pourpre";
		if (damage == 6)
			return "C. Cyan";
		if (damage == 7)
			return "C. Gris clair";
		if (damage == 8)
			return "C. Gris";
		if (damage == 9)
			return "C. Rose";
		if (damage == 10)
			return "C. Vert clair";
		if (damage == 11)
			return "C. Jaune";
		if (damage == 12)
			return "C. Bleu clair";
		if (damage == 13)
			return "C. Magenta";
		if (damage == 14)
			return "C. Orange";
		if (damage == 15)
			return "C. Blanc";
		
		return null;
	}

	public static Chest getChest(String location, Sign s) {
		if(Boutique.SignChest.containsKey(location)) {
			String[] chestLoc = Boutique.SignChest.get(location).split(":");
			try {
				int x = Integer.parseInt(chestLoc[0]);
				int y = Integer.parseInt(chestLoc[1]);
				int z = Integer.parseInt(chestLoc[2]);
				
				Block c = s.getWorld().getBlockAt(x, y, z);
				if (c.getState() instanceof Chest){
					return (Chest)c.getState();
				}
			}
			catch (Exception e) {
				System.out.println("[Boutique] Erreur dans le fichier globals.txt");
			}
		}
		else {
			Block c = s.getBlock().getRelative(BlockFace.DOWN);
			if (c.getState() instanceof Chest)
				return (Chest)c.getState();
		}
		return null;
	}

	public static int findChest(String location, Sign s, String pName) {
		// Format for chest location is x:y:z
		// Returns 1 if chest is the sign owners.
		// Returns 0 if there is no chest protection.
		// Returns -1 if the person is not the chest owner.
		// Returns -2 if the chest stored has been removed.
		// Returns -3 if something odd happened.
		// Returns -4 if the chest has no owner.
		// returns -5 if there is no chest.
		if(Boutique.SignChest.containsKey(location)) {
			String[] chestLoc = Boutique.SignChest.get(location).split(":");
			try {
				int x = Integer.parseInt(chestLoc[0]);
				int y = Integer.parseInt(chestLoc[1]);
				int z = Integer.parseInt(chestLoc[2]);
				
				Block c = s.getWorld().getBlockAt(x, y, z);
				if (c.getState() instanceof Chest)
				{
					return 0;
				}
				else 
				{
					Boutique.SignChest.remove(location);
					return -2;
				}
			}
			catch (Exception e) {
				return -3;
			}
		}
		else {
			Block c = s.getBlock().getRelative(BlockFace.DOWN);
			if (c.getState() instanceof Chest)
			{
				return 0;
			}
			else 
			{
				return -5;
			}
		}
	}

	public String sendChestErr(int errorNum) 
	{
		if (errorNum == 0) {
			return this.noProtErr;
		}
		else if (errorNum == -1) {
			return this.notOwnerErr;
		}
		else if (errorNum == -2) {
			return this.noChestErr;
		}
		else if (errorNum == -3) {
			return this.oddErr;
		}
		else if (errorNum == -4) {
			return this.needOwnerErr;
		}
		else if (errorNum == -5) {
			return this.noChestErr;
		}
		return "Could not find error code.";
	}
	
	
	public String sendTransFormatErr(int errorNum) {
		
		if (errorNum == -2) {
			return EconomyHandler.noConomyErr;
		}
		else if (errorNum == -3) {
			return this.itemTypeErr;
		}
		else if (errorNum == -4) {
			return this.formattingErr;
		}
		return "Could not find error code.";
	}
	
	
	public static boolean isSignOwner(Sign s, String str)
	{
		
		String location = s.getX() + ":" + s.getY() + ":" + s.getZ() + ":" + s.getWorld().getName();
		if(Boutique.signLocs.containsKey(location))
			if (Boutique.signLocs.get(location).compareToIgnoreCase(str) == 0)
				return true;
		
		return false;
	}

	 public static Sign[] getAttachedSigns(Block b) {
		    Sign[] signs = new Sign[5];

		    Block check = b.getRelative(BlockFace.UP);
		    if (((check instanceof Sign)) && 
		      (check.getType() == Material.SIGN_POST)) {
		      signs[0] = ((Sign)check);
		    }
		    check = b.getRelative(BlockFace.NORTH);
		    if (((check instanceof Sign)) && 
		      (check.getType() == Material.WALL_SIGN) && 
		      (check.getData() == 4)) {
		      signs[1] = ((Sign)check);
		    }
		    check = b.getRelative(BlockFace.EAST);
		    if (((check instanceof Sign)) && 
		      (check.getType() == Material.WALL_SIGN) && 
		      (check.getData() == 2)) {
		      signs[2] = ((Sign)check);
		    }
		    check = b.getRelative(BlockFace.SOUTH);
		    if (((check instanceof Sign)) && 
		      (check.getType() == Material.WALL_SIGN) && 
		      (check.getData() == 5)) {
		      signs[3] = ((Sign)check);
		    }
		    check = b.getRelative(BlockFace.WEST);
		    if (((check instanceof Sign)) && 
		      (check.getType() == Material.WALL_SIGN) && 
		      (check.getData() == 3)) {
		      signs[4] = ((Sign)check);
		    }
		    return signs;
		  }
	
	public boolean canBreakSign(Sign s, Player p) {
		if (!this.isSign(s))
			return false;
		
		boolean bool = false;
		bool = isSignOwner(s,p.getName());
		if (!bool)
			bool = p.isOp();
		
		return bool;
	}
}
