/* Spatial Operations & Editing Tools for uDig
 * 
 * Axios Engineering under a funding contract with: 
 *      Diputación Foral de Gipuzkoa, Ordenación Territorial 
 *
 *      http://b5m.gipuzkoa.net
 *      http://www.axios.es 
 *
 * (C) 2006, Diputación Foral de Gipuzkoa, Ordenación Territorial (DFG-OT). 
 * DFG-OT agrees to licence under Lesser General Public License (LGPL).
 * 
 * You can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software 
 * Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package es.axios.udig.ui.editingtools.internal.presentation;

import net.refractions.udig.project.ui.tool.IToolContext;

import org.eclipse.jface.action.IStatusLineManager;

/**
 * Presents the messages on status bar
 * <p>
 * </p>
 * 
 * @author Gabriel Roldan (www.axios.es)
 * @author Mauricio Pazos (www.axios.es)
 * @since 1.1.0
 * @version $Id$
 */
public final class StatusBar {

    /**
     * Private constructor to indicate it is a pure utility class
     */
    private StatusBar() {
        // do nothing
    }

    public static void setStatusBarMessage( final IToolContext context, final String message ) {
        context.updateUI(new Runnable(){
            public void run() {
                if (context.getActionBars() == null)
                    return;
                IStatusLineManager bar = context.getActionBars().getStatusLineManager();
                if (bar != null) {
                    bar.setMessage(message);
                    bar.setErrorMessage(null);
                }
            }
        });
    }

    public static void setStatusErrorBarMessage( final IToolContext context, final String message ) {
        context.updateUI(new Runnable(){
            public void run() {
                if (context.getActionBars() == null)
                    return;
                IStatusLineManager bar = context.getActionBars().getStatusLineManager();
                if (bar != null) {
                    bar.setErrorMessage(message);
                    bar.setMessage(null);
                }
            }
        });
    }
}
