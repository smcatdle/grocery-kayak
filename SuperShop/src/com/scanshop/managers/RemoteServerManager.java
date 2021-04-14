/**
 * 
 */
package com.scanshop.managers;


import android.content.Context;

import com.company.propertyprice.listview.MyListActivity;
import com.company.propertyprice.listview.SwipeDismissActivity;
import com.scanshop.plugins.SearchProductsPlugin;

/**
 * @author smcardle
 *
 */
public class RemoteServerManager {

	private SearchProductsPlugin searchProductsPlugin = null;
	
	public RemoteServerManager() {
		searchProductsPlugin = new SearchProductsPlugin();
	}
	
	public void search(String searchString, Context context, MyListActivity callback) {
		searchProductsPlugin.search(searchString, null, context, callback);
	}
	
}
