package com.danstoncube.tools.Banque;


import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class Balance
{
  public Balance(Plugin p)
  {
  }

  public abstract String getClassName();

  public abstract boolean hasEnough(Player paramPlayer, double paramDouble);
  
  public abstract boolean hasEnough(String paramString, double paramDouble);
  
  public abstract double balance(String paramString);
  
  public abstract boolean isEnabled();

  public abstract void add(Player paramPlayer, double paramDouble);

  public abstract void add(String paramString, double paramDouble);

  public abstract void sub(Player paramPlayer, double paramDouble);

  public abstract void sub(String paramString, double paramDouble);
}