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
package at.medevit.elexis.kassen.core.model.expressions;

public class ExpressionTagNames {

	/** The tag name of the and expression (value: <code>and</code>) */
	public static final String AND= "and"; //$NON-NLS-1$

	/** The tag name of the or expression (value: <code>or</code>) */
	public static final String OR= "or"; //$NON-NLS-1$

	/** The tag name of the not expression (value: <code>not</code>) */
	public static final String NOT= "not"; //$NON-NLS-1$

	/** The tag name of the equals expression (value: <code>equals</code>) */
	public static final String EQUALS= "equals"; //$NON-NLS-1$
		
	/** The tag name of the mandant speciality is position expression (value: <code>mandantspecialityis</code>) */
	public static final String MANDANTSPECIALITYIS= "mandantspecialityis"; //$NON-NLS-1$
	
	/** The tag name of the position in group expression (value: <code>positioningroup</code>) */
	public static final String POSITIONINGROUP= "positioningroup"; //$NON-NLS-1$

	/** The tag name of the konsultation include verrechenbar expression (value: <code>konsultationincudesposition</code>) */
	public static final String KONSULTATIONINCLUDESPOSITION= "konsultationincudesposition"; //$NON-NLS-1$

	/** The tag name of the points range expression (value: <code>konsultationincudesposition</code>) */
	public static final String POINTRANGE= "pointrange"; //$NON-NLS-1$
}
