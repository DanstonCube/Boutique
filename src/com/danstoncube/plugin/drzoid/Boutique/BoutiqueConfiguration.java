package com.danstoncube.plugin.drzoid.Boutique;

public class BoutiqueConfiguration
{
	private Boutique plugin;
	

	
	public String sqlTablePrefix = "Boutique_";
	public String sqlHost = "localhost";
	public String sqlDb = "boutique";
	public String sqlUser = "boutique_sqluser";
	public String sqlPass = "boutique_sqlpass";
	
	public Boolean useMysql = false;
	public Boolean useWebAuction = false;
	
	BoutiqueConfiguration(Boutique p)
	{
		this.plugin = p;
		
		if(plugin.config == null)
		{
			plugin.config = plugin.getConfiguration();
		}
		
		
		useMysql =  plugin.config.getBoolean("useMysql", false);
		useWebAuction =  plugin.config.getBoolean("useWebAuction", false);
		
		sqlHost =  plugin.config.getString("sqlHost", "localhost");
		sqlTablePrefix = plugin.config.getString("sqlTablePrefix", "Boutique_");
		sqlHost =  plugin.config.getString("sqlHost", "localhost");
		sqlUser = plugin.config.getString("sqlUser", "boutique_sqlpass");
		sqlPass =  plugin.config.getString("sqlPass", "boutique_sqlpass");
		sqlDb =  plugin.config.getString("sqlDb", "minecraft");
		
		plugin.config.save();		
	}
	
}
