package com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.plugin.Plugin;

import com.danstoncube.tools.Banque.iConomy5Balance;
import com.danstoncube.tools.Banque.iConomy6Balance;
import com.danstoncube.tools.Banque.DummyBalance;
import com.danstoncube.tools.Banque.Balance;

public class EconomyHandler 
{
	
	//public static Plugin iconomy = null;
	
	public static Balance balance = null;
	
	public static String currencyName = "Eu";
	
	public final static String noConomyErr = "Les banques sont en grève.";
	public final static String noAccountErr = "Ce compte n'éxiste pas :(";
	public final static String noFundsErr = "You or they do not have enough funds for that.";
	public final static String oddErr = "Hum, y'a une erreur pas cool :/";

	public static boolean currencyEnabled = false;
	
	
	
	public static void setupEconomy(Boutique pluginold) 
	{
		//plugin.log.info(plugin.logPrefix + "Recherche d'iConomy.");
		
		
		Boutique plugin = Boutique.getInstance();
		
		for (Plugin p : plugin.getServer().getPluginManager().getPlugins())
		{
			if (p.getClass().getName().equals("com.iConomy.iConomy")) 
			{
				plugin.log.info(plugin.logPrefix + "utilise iConomy5");
				balance = new iConomy5Balance(plugin, (com.iConomy.iConomy)p);
				currencyEnabled = true;
			}

			if (p.getClass().getName().equals("com.iCo6.iConomy")) 
			{
				plugin.log.info(plugin.logPrefix + "utilise iConomy6");
				balance = new iConomy6Balance(plugin, (com.iCo6.iConomy)p);
				currencyEnabled = true;
      		}
		}
		
		if(balance == null)
		{
			balance = new DummyBalance(plugin);
			plugin.log.severe(plugin.logPrefix + "iConomy 5/6 introuvable ! :(");
			currencyEnabled = false;
		}
		
		
	}

	
	public static int hasEnough(String p, double amount) 
	{
		// returns -3 if something odd happened
		// returns -2 if account doesnt have enough
		// returns -1 if no money system
		// returns 0 if the player account can't be found
		// returns +1 if successful
		
		if (!currencyEnabled){
			return -1;
		}
		
		if(balance != null)
		{
			if(!balance.hasEnough(p, amount))
			{
				return -2;
			}
			else
			{
				return 1;
			}
		}
		else
		{
			return -3;
		}
		
		
		/*
		if (iconomy != null){
			if (!(iConomy.hasAccount(pName))){
				return 0;
			}
			
			if (iConomy.getAccount(pName).getHoldings().hasEnough(amount))
				return 1;
			else return -2;
		}
		
		return -3;
		*/
	}
	
	
	
	//@SuppressWarnings("deprecation")
	public static int modifyMoney(String pName, Double costAmount) 
	{
		// returns -3 if something odd happened.
		// returns -2 if they don't have enough money.
		// returns -1 if no money system
		// returns 0 if the player account can't be found
		// returns +1 if successful
		if (!currencyEnabled)
		{
			return -1;
		}
		
		if(balance != null)
		{
			
			if(costAmount > -1)
			{
				balance.add(pName, costAmount);
			}
			else
			{
				if(balance.hasEnough(pName, costAmount))
				{
					balance.add(pName, costAmount);
				}
				else
				{
					return -2;
				}
			}
			
			return 1;
			
		}

		return -3;
	}
	
	

	@Deprecated
	public static int modifyMoney(String pName, Integer costAmount) 
	{
		return modifyMoney(pName, Double.parseDouble(Integer.toString(costAmount)));
	}
	
	
	public static String playerHave(String pName) 
	{		
		if(balance != null)
		{
			return Double.toString(balance.balance(pName));
		}		
		return noConomyErr;
	}

	public static String getEconError(int econ) 
	{
		if (econ == 0)
			return "T'es interdit bancaire mec.";
		if (econ == -1)
			return noConomyErr;
		if (econ == -2)
			return "Tu n'as pas assez de thunes, ou bien c'est ton acheteur qui est à sec !";
		if (econ == -3)
			return oddErr;
		return "Could not find error code.";
	}
}
