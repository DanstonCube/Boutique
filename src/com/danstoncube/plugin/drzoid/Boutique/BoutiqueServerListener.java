package  com.danstoncube.plugin.drzoid.Boutique;

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListener;
import org.bukkit.plugin.Plugin;




public class BoutiqueServerListener extends ServerListener {

	private Boutique plugin;

    public BoutiqueServerListener(Boutique plugin) {
        this.plugin = plugin;
    }

   
    @Override
    public void onPluginDisable(PluginDisableEvent event) 
    {
        /*
    	if (EconomyHandler.iconomy != null) {
            if (event.getPlugin().getDescription().getName().equals("iConomy")) {
            	EconomyHandler.iconomy = null;
                plugin.log.info("["+plugin.displayname+"] Un-hooked from iConomy.");
            }
        }
        */
    }

    @Override
    public void onPluginEnable(PluginEnableEvent event) {
    	
    	//On teste si permissions est up
    	if(PermissionsHandler.permissions == null)
    	{
    		Plugin Permissions = plugin.getServer().getPluginManager().getPlugin("Permissions");
    		if(Permissions!=null)
    		{
    			PermissionsHandler.permissions = Permissions;
    			PermissionsHandler.setupPermissions(this.plugin);
    		}
    	}
    	
    	/*
    	//On teste si iConomy est up
        if (EconomyHandler.balance == null) 
        {
        	EconomyHandler.setupEconomy(plugin);
        }
        
    	*/
    	
    	
    	
    }
}