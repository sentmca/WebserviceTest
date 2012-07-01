package org.ofbiz.party.test.config;

import java.util.ResourceBundle;

/**
 * @author Sharif Ul Islam
 * Date: Feb 24, 2010
 */

public class ResourceMgr {

	private static String configBundle = "ConfigBundle";

	public static String getResourceFromConfigBundle(String key){
		return ResourceBundle.getBundle(configBundle).getString(key);
	}

}