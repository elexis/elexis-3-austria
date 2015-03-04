package ch.elexis.artikel_at;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ch.elexis.artikel_at.data.Artikel_AT_Cache;
import ch.elexis.core.data.activator.CoreHub;
import ch.rgw.io.Settings;

public class Activator extends AbstractUIPlugin {
	
	public Activator(){}
	
	/*
	 * This activators sole purpose is to initialize the HashMap caches for the VidalLabelProvider
	 * 
	 * @author Marco Descher
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception{
		Settings globalCfg = CoreHub.globalCfg;
		if (globalCfg != null) {
			if (CoreHub.globalCfg.get(PreferenceConstants.ARTIKEL_AT_CACHEUPDATE_TIME, "invalid")
				.equalsIgnoreCase("invalid")) {
				Artikel_AT_Cache.updateCache();
			}
		}
	}
	
	@Override
	public void stop(BundleContext context) throws Exception{}
}
