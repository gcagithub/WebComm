package com.webComm.plugin;


/**
 * The Interface IPlugin.
 */
public interface IPlugin {
	
	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName();
	
	/**
	 * Gets the copyright.
	 * 
	 * @return the copyright
	 */
	public String getCopyright();
	
	/**
	 * On load.
	 * 
	 * @return true, if successful
	 */
	public boolean onLoad();
	
	/**
	 * Inits the plugin.
	 * 
	 * @return true, if successful
	 */
	public boolean initPlugin();
	
	/**
	 * Stop plugin.
	 * 
	 * @return true, if successful
	 */
	public boolean stopPlugin();
	
	/**
	 * On unload.
	 * 
	 * @return true, if successful
	 */
	public boolean onUnload();
	
	
	/**
	 * On populate actions.
	 */
	public void onPopulateActions();
	
	/**
	 * Execute action.
	 * 
	 * @param actionname
	 *            the actionname
	 */
	public void executeAction(String actionname);
	
}
