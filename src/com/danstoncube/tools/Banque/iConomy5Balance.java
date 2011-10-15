/*    */ package com.danstoncube.tools.Banque;
/*    */ 
/*    */ import com.iConomy.iConomy;
/*    */ import java.util.logging.Level;
		 import org.bukkit.entity.Player;
		 import org.bukkit.plugin.Plugin;
/*    */ 
/*    */ public class iConomy5Balance extends Balance
/*    */ {
/*    */   private iConomy iconomy;
/*    */   private Plugin p;
/*    */ 
/*    */   public iConomy5Balance(Plugin p, iConomy iconomy)
/*    */   {
/* 15 */     super(p);
/* 16 */     this.p = p;
/*    */   }
/*    */ 
/*    */   public boolean hasEnough(Player p, double amount) {
/*    */     try {
/* 21 */       return iConomy.getAccount(p.getName()).getHoldings().hasEnough(amount);
/*    */     } catch (NullPointerException npe) {
/* 23 */       this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy5Balance] NullPointerException: " + npe.getMessage());
/* 24 */     }return false;
/*    */   }

			public boolean hasEnough(String p, double amount) 
			{
				try 
				{
					return iConomy.getAccount(p).getHoldings().hasEnough(amount);
				} 
				catch (NullPointerException npe) 
				{
					this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy5Balance] NullPointerException: " + npe.getMessage());
				}
				
				return false;
			}

/*    */   public boolean isEnabled()
/*    */   {
/* 29 */     return this.iconomy.isEnabled();
/*    */   }
/*    */ 
/*    */   public void add(Player p, double amount) {
/*    */     try {
/* 34 */       add(p.getName(), amount);
/*    */     } catch (NullPointerException npe) {
/* 36 */       this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy5Balance] NullPointerException: " + npe.getMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   public void add(String p, double amount) {
/*    */     try {
/* 42 */       iConomy.getAccount(p).getHoldings().add(amount);
/*    */     } catch (NullPointerException npe) {
/* 44 */       this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy5Balance] NullPointerException: " + npe.getMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   public void sub(Player p, double amount) {
/*    */     try {
/* 50 */       sub(p.getName(), amount);
/*    */     } catch (NullPointerException npe) {
/* 52 */       this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque] NullPointerException: " + npe.getMessage());
/*    */     }
/*    */   }
/*    */ 
/*    */   public void sub(String p, double amount) {
/*    */     try {
/* 58 */       iConomy.getAccount(p).getHoldings().subtract(amount);
/*    */     } catch (NullPointerException npe) {
/* 60 */       this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy5Balance] NullPointerException: " + npe.getMessage());
/*    */     }
/*    */   }

/*    */   public String getClassName() {
/* 65 */     return iConomy.class.getName();
/*    */   }

			public double balance(String paramString) 
			{
				
				try 
				{
					return iConomy.getAccount(paramString).getHoldings().balance();
				} 
				catch (NullPointerException npe) 
				{
					this.p.getServer().getLogger().log(Level.WARNING, "[com.danstoncube.tools.banque.iConomy5Balance] NullPointerException: " + npe.getMessage());
				}
				
				return -1;
			} 


}

