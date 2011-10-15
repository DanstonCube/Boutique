/**
 * 
 */
package com.danstoncube.plugin.drzoid.Boutique;

import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author miko
 *
 */
public class BoutiqueDb
{
	private final Boutique plugin;

	 
	public String sqlTablePrefix = "Boutique_";
	public String sqlHost = "localhost";
	public String sqlDb = "boutique";
	public String sqlUser = "boutique_sqluser";
	public String sqlPass = "boutique_sqlpass";
	
	private DatabaseHandler manageDB;
	
	public Boolean enabled = false;
	
	
	BoutiqueDb(Boutique plugin)
	{
		this.plugin = plugin;
	}
	
	
		
	public boolean setup()
	{
		
		plugin.getConfiguration();

		sqlTablePrefix = this.plugin.config.getString("sqlTablePrefix", "Boutique_");
		sqlHost =  this.plugin.config.getString("sqlHost", "localhost");
		sqlUser = this.plugin.config.getString("sqlUser", "boutique_sqlpass");
		sqlPass =  this.plugin.config.getString("sqlPass", "boutique_sqlpass");
		sqlDb =  this.plugin.config.getString("sqlDb", "minecraft");
		
		if (sqlTablePrefix.equals(null)) 	{plugin.log.severe(plugin.logPrefix + "MySQL table prefix is not defined"); }		
		if (sqlHost.equals(null)) 			{plugin.log.severe(plugin.logPrefix + "MySQL host is not defined"); }
		if (sqlUser.equals(null)) 			{plugin.log.severe(plugin.logPrefix + "MySQL username is not defined"); }
		if (sqlPass.equals(null)) 			{plugin.log.severe(plugin.logPrefix + "MySQL  password is not defined"); }
		if (sqlDb.equals(null)) 			{plugin.log.severe(plugin.logPrefix + "MySQL database is not defined"); }
		
		plugin.config.save();
		
		
		
		initialize();
		try 
		{
			if(!checkConnection())
			{
				plugin.log.severe(plugin.logPrefix + "Connexion impossible à Mysql ! (" + sqlUser + "/" + sqlHost+ ")");
				close();
				return false;
			}
			

			checkTables();

		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		close();
		
		return true;
	}
	
	
	public Boolean logTransaction(String trade_from, String trade_to, int item_id, int item_damage, int item_qty, Double costAmount, String item_shortname, String item_name) throws SQLException, MalformedURLException, InstantiationException, IllegalAccessException
	{
		
		String query="";
		query += "INSERT INTO " + sqlTablePrefix + "transactions";
		query += "(trade_from, trade_to, item_id, item_damage, item_quantity, item_price, item_name, item_shortname)";
		query += "VALUES (?,?,?,?,?,?,?,?)";
		
		//open();
		
		Connection conn = openConnection();
		
		PreparedStatement stmt = conn.prepareStatement(query);
		stmt.setString(1, trade_from);
		stmt.setString(2, trade_to);
		stmt.setInt(3, item_id);
		stmt.setInt(4, item_damage);
		stmt.setInt(5, item_qty);
		stmt.setDouble(6, costAmount);
		stmt.setString(7, item_name);
		stmt.setString(8, item_shortname);
		stmt.executeUpdate();

		close();


		return false;
	}
	
	
	
	public Boolean checkTables() throws MalformedURLException, InstantiationException, IllegalAccessException 
	{
		//Table transaction
		if (!checkTable(sqlTablePrefix + "transactions")) 
		{
			plugin.log.info(plugin.logPrefix + "Creation de la table " + sqlTablePrefix + "transactions");
			String query = "CREATE TABLE " + sqlTablePrefix + "transactions (id INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(id), trade_from VARCHAR(255), trade_to VARCHAR(255), item_id INT, item_damage INT, item_quantity INT, item_price DOUBLE, item_name VARCHAR(255), item_shortname VARCHAR(12));";
			createTable(query.toString());
		}
		
		
		//TODO: ajout futur tables 
		
		return true;		
	}
	
	
	
	public Boolean initialize() {
		
		this.manageDB = new DatabaseHandler(this, this.sqlHost, this.sqlDb, this.sqlUser, this.sqlPass);
		
		return false;
	}
	
	public void writeInfo(String toWrite) {
		if (toWrite != null) {
			plugin.log.info(plugin.logPrefix + toWrite);
		}
	}
	
	public void writeError(String toWrite, Boolean severe) {
		if (severe) {
			if (toWrite != null) {
				plugin.log.severe(plugin.logPrefix + toWrite);
			}
		} else {
			if (toWrite != null) {
				plugin.log.warning(plugin.logPrefix + toWrite);
			}
		}
	}
	
	public ResultSet sqlQuery(String query) throws MalformedURLException, InstantiationException, IllegalAccessException {
		return this.manageDB.sqlQuery(query);
	}
	
	public Boolean createTable(String query) {
		return this.manageDB.createTable(query);
	}
	
	public void insertQuery(String query) throws MalformedURLException, InstantiationException, IllegalAccessException {
		this.manageDB.insertQuery(query);
	}
	
	public void updateQuery(String query) throws MalformedURLException, InstantiationException, IllegalAccessException {
		this.manageDB.updateQuery(query);
	}
	
	public void deleteQuery(String query) throws MalformedURLException, InstantiationException, IllegalAccessException {
		this.manageDB.deleteQuery(query);
	}
	
	public Boolean checkTable(String table) throws MalformedURLException, InstantiationException, IllegalAccessException {
		return this.manageDB.checkTable(table);
	}
	
	public Boolean wipeTable(String table) throws MalformedURLException, InstantiationException, IllegalAccessException {
		return this.manageDB.wipeTable(table);
	}
	
	
	public Connection openConnection() throws MalformedURLException, InstantiationException, IllegalAccessException, SQLException {
		return this.manageDB.openConnection();
	}
	
	public Connection getConnection() throws MalformedURLException, InstantiationException, IllegalAccessException {
		return this.manageDB.getConnection();
	}
	
	public void close() {
		this.manageDB.closeConnection();
	}
	
	public Boolean checkConnection() throws MalformedURLException, InstantiationException, IllegalAccessException {
		return this.manageDB.checkConnection();
	}
	
	
	
	
	
	

}



