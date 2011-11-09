package com.danstoncube.Boutique;

import org.bukkit.util.config.Configuration;

public class BoutiqueConfiguration
{

	
	public String sqlTablePrefix;
	public String sqlHost;
	public String sqlDb;
	public String sqlUser;
	public String sqlPass = "boutique_sqlpass";
	
	public Boolean useMysql = false;
	public Boolean useWebAuction = false;
	
	private Configuration _configuration = null;
	
	BoutiqueConfiguration(Boutique plugin)
	{
		_configuration = plugin.getConfiguration();
		
		useMysql =  		_configuration.getBoolean("useMysql", false);
		useWebAuction =  	_configuration.getBoolean("useWebAuction", false);
		
		sqlHost =  			_configuration.getString("sqlHost", "localhost");
		sqlTablePrefix = 	_configuration.getString("sqlTablePrefix", "Boutique_");
		sqlUser = 			_configuration.getString("sqlUser", "boutique_sqluser");
		sqlPass =  			_configuration.getString("sqlPass", "boutique_sqlpass");
		sqlDb =  			_configuration.getString("sqlDb", "minecraft");
		
		_configuration.save();		
	}
	
}
