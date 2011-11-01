package com.danstoncube.plugin.drzoid.Boutique;

import java.io.File;
import java.util.logging.Logger;



import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

public class Boutique extends JavaPlugin
{
	public final Logger log = Logger.getLogger("Minecraft");
	public final String logPrefix = "[Boutique] ";
	
	public final String name = "Boutique";
	public final String displayname = "Boutique";
	public final String version = "2.0.0";
	
	//File Handler
	public File makeFolder;
	
	FileOperations fileio = new FileOperations(this);
	CommandOperator co = new CommandOperator(this);
	
	// Listeners
	public final BoutiqueBlockListener 	blockListener = 	new BoutiqueBlockListener(this);
	public final BoutiquePlayerListener playerListener = 	new BoutiquePlayerListener(this);
	public final BoutiqueServerListener serverListener = 	new BoutiqueServerListener(this);

	
	public BoutiqueDb db = new BoutiqueDb(this);
	public BoutiqueSignManager signmanager = new BoutiqueSignManager(this);

	
	
	
	
	public Configuration config;
	
	public BoutiqueConfiguration configuration;
	
	
	private static Boutique _instance = null;
	
	
	
	public static Boutique getInstance()
	{
		return _instance;
	}
	
	
	
	public void onEnable()  
	{
		log.info(logPrefix + "Chargement de Boutique !");
			
		Boutique._instance = this;
		 
		makeFolder = this.getDataFolder();
		
		
		config = this.getConfiguration();
		configuration = new BoutiqueConfiguration(this);
		
		fileio.checkDataFolder();
		fileio.loadItemsData();
		
		// TODO: flag use / not use flat file
		signmanager.loadGlobalSignData();
		
		
		if(db.setup())
		{
			log.info(logPrefix + "Logs Mysql activées");
			
			// TODO:
			db.loadGlobalSignData();
		}
		else
		{
			log.info(logPrefix + "Logs Mysql désactivées");
		}
		
		PluginManager pm = this.getServer().getPluginManager();

		pm.registerEvent(Event.Type.SIGN_CHANGE, this.blockListener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_BREAK, this.blockListener, Event.Priority.High, this);
		pm.registerEvent(Event.Type.BLOCK_PLACE, this.blockListener, Event.Priority.High, this);
        pm.registerEvent(Event.Type.PLAYER_INTERACT, this.playerListener, Event.Priority.Normal, this);
        
        //pour être au courant de l'activation/désactivation d'autres plugins (iConomy surtout)
        pm.registerEvent(Event.Type.PLUGIN_ENABLE, this.serverListener, Priority.Monitor, this);
        pm.registerEvent(Event.Type.PLUGIN_DISABLE, this.serverListener, Priority.Monitor, this);
        
        
       
        
        
       
        
		
        
		log.info(logPrefix + "version " + version + " activé.");
	}
	
	
	
	

	
	public void onDisable() 
	{		
		db.close();
		log.info(logPrefix + "version " + version + " désactivé.");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) 
	{	
		return co.command(sender, command, commandLabel, args);
	}
}
