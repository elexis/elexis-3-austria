/*******************************************************************************
 * Copyright (c) 2015 MEDEVIT and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     MEDEVIT <office@medevit.at> - initial API and implementation
 *******************************************************************************/
package at.medevit.elexis.kassen.core.model;

import java.util.List;

import ch.elexis.core.data.constants.ExtensionPointConstantsData;
import ch.elexis.core.data.interfaces.IOptifier;
import ch.elexis.core.data.util.Extensions;
import ch.elexis.data.Fall;
import ch.elexis.data.PersistentObjectFactory;
import ch.rgw.tools.TimeTool;

public abstract class ForeignKassenLeistung extends KassenLeistung {
	
	public static final String FLD_FOREIGNPOSID = "foreignposid"; //$NON-NLS-1$
	public static final String FLD_FOREIGNPOSGROUPID = "foreignposgroupid"; //$NON-NLS-1$
	public static final String FLD_FOREIGNGROUPID = "foreigngroupid"; //$NON-NLS-1$
	public static final String FLD_FOREIGNCLASSNAME = "foreignclassname"; //$NON-NLS-1$
	public static final String FLD_FOREIGNVALIDFROMDATE = "foreignvalidfromdate"; //$NON-NLS-1$
	public static final String FLD_FOREIGNVALIDTODATE = "foreignvalidtodate"; //$NON-NLS-1$
	public static final String FLD_FOREIGNID = "foreignid"; //$NON-NLS-1$
	
	protected KassenLeistung foreignLeistung;
	protected static Class<? extends KassenLeistung> foreignClazz;
	protected static PersistentObjectFactory foreignFactory;
	
	public ForeignKassenLeistung(){
		
	}
	
	protected ForeignKassenLeistung(final String id){
		super(id);
	}
	
	protected void lazyInit(){
		if (foreignLeistung == null) {
			if (foreignClazz == null
				|| (foreignClazz != null && !foreignClazz.getName().equals(
					get(FLD_FOREIGNCLASSNAME)))) {
				List<PersistentObjectFactory> exts =
					Extensions.getClasses(ExtensionPointConstantsData.PERSISTENT_REFERENCE, "Class");
				for (PersistentObjectFactory po : exts) {
					Class ret = po.getClassforName(get(FLD_FOREIGNCLASSNAME));
					if (ret != null) {
						foreignClazz = ret;
						foreignFactory = po;
						break;
					}
				}
			}
			
			foreignLeistung =
				(KassenLeistung) foreignFactory.createFromString(foreignClazz.getName() + "::"
					+ get(FLD_FOREIGNID));
			
			if (foreignClazz == null)
				throw new IllegalStateException("Could not find foreign code for class "
					+ get(FLD_FOREIGNCLASSNAME));
			if (foreignLeistung == null)
				throw new IllegalStateException("Could not find foreign code for class "
					+ get(FLD_FOREIGNCLASSNAME) + " with id " + get(FLD_FOREIGNID));
		}
	}
	
	public abstract Class<? extends KassenLeistung> getConfiguredForeignClazz();
	
	@Override
	public IOptifier getOptifier(){
		lazyInit();
		return foreignLeistung.getOptifier();
	}
	
	@Override
	public DateRange getValidRange(){
		lazyInit();
		return foreignLeistung.getValidRange();
	}
	
	@Override
	public List<KassenCodes.SpecialityCode> getSpecialities(){
		lazyInit();
		return foreignLeistung.getSpecialities();
	}
	
	@Override
	public String getSpecialitiesAsString(){
		lazyInit();
		return foreignLeistung.getSpecialitiesAsString();
	}
	
	@Override
	public int getTP(TimeTool date, Fall fall){
		lazyInit();
		return foreignLeistung.getTP(date, fall);
	}
	
	@Override
	public double getFactor(TimeTool date, Fall fall){
		lazyInit();
		return foreignLeistung.getFactor(date, fall);
	}
	
	@Override
	public String getTitle(){
		lazyInit();
		return foreignLeistung.getTitle();
	}
	
	@Override
	public String getPosition(){
		lazyInit();
		return foreignLeistung.getPosition();
	}
	
	@Override
	public String getAdviceText(){
		lazyInit();
		return foreignLeistung.getAdviceText();
	}
	
	@Override
	public String getPositionGroup(){
		lazyInit();
		return foreignLeistung.getPositionGroup();
	}
	
	@Override
	public String getGroup(){
		lazyInit();
		return foreignLeistung.getGroup();
	}
	
	@Override
	public boolean isGroup(){
		lazyInit();
		return foreignLeistung.isGroup();
	}
	
	@Override
	public double getPointValue(){
		lazyInit();
		return foreignLeistung.getPointValue();
	}
	
	@Override
	public double getMoneyValue(){
		lazyInit();
		return foreignLeistung.getMoneyValue();
	}
}
