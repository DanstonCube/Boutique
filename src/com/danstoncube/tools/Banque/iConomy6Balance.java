package com.danstoncube.tools.Banque;

import com.iCo6.iConomy;
import com.iCo6.system.Accounts;
import java.util.logging.Level;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class iConomy6Balance extends Balance
{
	private iConomy iconomy;
	private Accounts accounts;
	private Plugin p;

	public iConomy6Balance(Plugin p, iConomy iconomy)
	{
		super(p);
		this.iconomy = iconomy;
		this.p = p;
		this.accounts = new Accounts();
	}
	
/*    */ 
/*    */   public String getClassName()
/*    */   {
/* 25 */     return this.iconomy.getClass().getName();
/*    */   }
/*    */ 
		   public boolean hasEnough(String p, double amount)
/*    */   {
/* 30 */     if (this.accounts.exists(p)) {
/* 31 */       return this.accounts.get(p).getHoldings().hasEnough(amount);
/*    */     }
/* 33 */     this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy6Balance] interacting failed with player " + p);
/* 34 */     return false;
/*    */   }

/*    */   public boolean hasEnough(Player p, double amount)
/*    */   {
/* 30 */     if (this.accounts.exists(p.getName())) {
/* 31 */       return this.accounts.get(p.getName()).getHoldings().hasEnough(amount);
/*    */     }
/* 33 */     this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy6Balance] interacting failed with player " + p.getName());
/* 34 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean isEnabled()
/*    */   {
/* 40 */     return this.iconomy.isEnabled();
/*    */   }
/*    */ 
/*    */   public void add(Player p, double amount)
/*    */   {
/* 45 */     add(p.getName(), amount);
/*    */   }
/*    */ 
/*    */   public void add(String p, double amount)
/*    */   {
/* 50 */     if (this.accounts.exists(p))
/* 51 */       this.accounts.get(p).getHoldings().add(amount);
/*    */     else
/* 53 */       this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy6Balance] interacting failed with player " + p);
/*    */   }
/*    */ 
/*    */   public void sub(Player p, double amount)
/*    */   {
/* 59 */     sub(p.getName(), amount);
/*    */   }
/*    */ 
/*    */   public void sub(String p, double amount)
/*    */   {
/* 64 */     if (this.accounts.exists(p))
/* 65 */       this.accounts.get(p).getHoldings().subtract(amount);
/*    */     else
/* 67 */       this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy6Balance] interacting failed with player " + p);
/*    */   }

			public double balance(String paramString) 
			{
				if (this.accounts.exists(paramString))
					return this.accounts.get(paramString).getHoldings().getBalance();
				else
					this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy6Balance] interacting failed with player " + p);
				
				return -1;
			} 


}
