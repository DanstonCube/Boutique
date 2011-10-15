package com.danstoncube.tools.Banque;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DummyBalance extends Balance
{
/*    */   public DummyBalance(Plugin p)
/*    */   {
/*  9 */     super(p);
/*    */   }
/*    */   public void add(Player p, double amount) {
/*    */   }
/*    */   public void add(String p, double amount) {
/*    */   }
/*    */   public void sub(Player p, double amount) {
/*    */   }
/*    */   public void sub(String p, double amount) {  }
/*    */ 
/* 18 */   public boolean hasEnough(Player p, double amount) { return true; }
/*    */ 
/*    */   public String getClassName()
/*    */   {
/* 22 */     return getClass().getName();
/*    */   }
/*    */ 
/*    */   public boolean isEnabled() {
/* 26 */     return true;
/*    */   }
/*    */

			public boolean hasEnough(String paramString, double paramDouble) 
			{
				return false;
			}

			public double balance(String paramString) 
			{
				return 0;
			} 

}

/* Location:           G:\Downloads\ShowCaseStandalone.jar
 * Qualified Name:     com.miykeal.showCaseStandalone.Balance.DummyBalance
 * JD-Core Version:    0.6.0
 */