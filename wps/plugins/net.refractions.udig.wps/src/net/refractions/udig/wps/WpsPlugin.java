/*
 * uDig - User Friendly Desktop Internet GIS client http://udig.refractions.net (C) 2004,
 * Refractions Research Inc. This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; version 2.1 of the License. This library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 */
package net.refractions.udig.wps;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class WpsPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static WpsPlugin plugin;
	//Resource bundle.
	private ResourceBundle resourceBundle;
	public static final String ID = "net.refractions.udig.wps"; //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public WpsPlugin() {
		super();
		WpsPlugin.plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		resourceBundle = null;
		super.stop(context);
	}

	/**
	 * @return x
	 * Returns the shared instance.
	 */
	public static WpsPlugin getDefault() {
		return WpsPlugin.plugin;
	}

	/**
	 * @param key 
	 * @return x
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = WpsPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * @return x
	 * Returns the plugin's resource bundle,
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle("net.refractions.udig.wps.WpsPluginResources"); //$NON-NLS-1$
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}
    
    /**
     * Logs the Throwable in the plugin's log.
     * <p>
     * This will be a user visable ERROR if:
     * <ul>
     * <li>t is an Exception we are assuming it is human readable or if a message is provided
     * </ul>
     * </p>
     * @param message 
     * @param t 
     */
    public static void log( String message, Throwable t ) {
        int status = t instanceof Exception || message != null ? IStatus.ERROR : IStatus.WARNING;
        getDefault().getLog().log(new Status(status, ID, IStatus.OK, message, t));
    }
    /**
     * Messages that only engage if getDefault().isDebugging()
     * <p>
     * It is much prefered to do this:<pre><code>
     * private static final String RENDERING = "net.refractions.udig.project/render/trace";
     * if( ProjectUIPlugin.getDefault().isDebugging() && "true".equalsIgnoreCase( RENDERING ) ){
     *      System.out.println( "your message here" );
     * }
     * </code></pre>
     * </p>
     * @param message 
     * @param e 
     */
    public static void trace( String message, Throwable e) {
        if( getDefault().isDebugging() ) {
            if( message != null ) System.out.println( message );
            if( e != null ) e.printStackTrace();
        }
    }
    /**
     * Performs the Platform.getDebugOption true check on the provided trace
     * <p>
     * Note: ProjectUIPlugin.getDefault().isDebugging() must also be on.
     * <ul>
     * <li>Trace.RENDER - trace rendering progress
     * </ul>
     * </p> 
     * @param trace currently only RENDER is defined
     * @return true if -debug is on for this plugin 
     */
    public static boolean isDebugging( final String trace ){
        return getDefault().isDebugging() &&
            "true".equalsIgnoreCase(Platform.getDebugOption(trace)); //$NON-NLS-1$    
    }
}
