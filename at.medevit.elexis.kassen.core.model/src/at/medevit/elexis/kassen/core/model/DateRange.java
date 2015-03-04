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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class representing two Date object specifying a range in time.
 * The range is open to the future until a To date is set.
 * 
 * @author thomas
 *
 */
public class DateRange {

	private static final SimpleDateFormat dateformat = new SimpleDateFormat("dd.MM.yyyy"); //$NON-NLS-1$
	
	public static final String ABS_CURRENTMONTH = "MONTH";
	public static final String ABS_CURRENTQUARTER = "QUARTER";
	public static final String ABS_CURRENTYEAR = "YEAR";
	
	Date from = null;
	Date to = null;
	
	/**
	 * Create a DateRange for an absolute time span. The allowed definitions are
	 * 
	 * <li>MONTH {@link ABS_CURRENTMONTH}</li>
	 * <li>QUARTER {@link ABS_CURRENTQUARTER}</li>
	 * <li>YEAR {@link ABS_CURRENTYEAR}</li>
	 * 
	 * @param definition
	 * @return
	 * @throws ParseException
	 */
	public static DateRange getAbsolutRange(String definition) throws ParseException {
		if(ABS_CURRENTMONTH.equalsIgnoreCase(definition)) {
			return new DateRange(getCurrentMonthStart(), getCurrentMonthEnd());
		} else if(ABS_CURRENTQUARTER.equalsIgnoreCase(definition)) {
			return new DateRange(getCurrentQuarterStart(), getCurrentQuarterEnd());
		} else if(ABS_CURRENTYEAR.equalsIgnoreCase(definition)) {
			return new DateRange(getCurrentYearStart(), getCurrentYearEnd());
		} else {
			throw new ParseException("Unknown date definition " + definition, 0);
		}
	}
	
	public DateRange(Date from) {
		this.from = from;
	}

	public DateRange(Date from, Date to) {
		this.from = from;
		this.to = to;
	}
	
	public boolean isIncluded(Date date) {
		if(to == null) {
			if(date.after(from))
				return true;
			return false;
		} else {
			if(date.after(from) && date.before(to))
				return true;
			return false;
		}
	}

	public void setToDate(Date to) {
		this.to = to;
	}
	
	public void setFromDate(Date from) {
		this.from = from;
	}
	
	public Date getFromDate() {
		return from;
	}

	/**
	 * Get the date as String formatted as parameter using SimpleDateFormat.
	 * 
	 * @param format
	 * @return formatted Date or '-' if the Date is not set
	 */
	public String getFromDateAsString(String format) {
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		if(from != null)
			return dateformat.format(from);
		else
			return "-";
	}
	
	public Date getToDate() {
		return to;
	}

	/**
	 * Get the date as String formatted as parameter using SimpleDateFormat.
	 * 
	 * @param format
	 * @return formatted Date or '-' if the Date is not set
	 */
	public String getToDateAsString(String format) {
		SimpleDateFormat dateformat = new SimpleDateFormat(format);
		if(to != null)
			return dateformat.format(to);
		else
			return "-";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof DateRange))
			return false;
		DateRange other = (DateRange)obj;

		boolean equalFrom = false;
		if(from == null && other.from == null)
			equalFrom = true;
		else if(from == null)
			equalFrom = false;
		else if(other.from == null)
			equalFrom = false;		
		else if(from.equals(other.from))
			equalFrom = true;
		
		boolean equalTo = false;
		if(to == null && other.to == null)
			equalTo = true;
		else if(to == null)
			equalTo = false;
		else if(other.to == null)
			equalTo = false;
		else if(to.equals(other.to))
			equalTo = true;
		
		return equalFrom && equalTo;
	}
	
	@Override
	public String toString() {
		if(to != null)
			return dateformat.format(from) + "-" + dateformat.format(to);
		else
			return dateformat.format(from);
	}

	private static Calendar getCurrentDayStart() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, cal.getMinimum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMinimum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMinimum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMinimum(Calendar.MILLISECOND));
		return cal;
	}
	
	private static Calendar getCurrentDayEnd() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, cal.getMaximum(Calendar.HOUR_OF_DAY));
		cal.set(Calendar.MINUTE, cal.getMaximum(Calendar.MINUTE));
		cal.set(Calendar.SECOND, cal.getMaximum(Calendar.SECOND));
		cal.set(Calendar.MILLISECOND, cal.getMaximum(Calendar.MILLISECOND));
		return cal;
	}
	
	private static Date getCurrentMonthStart() {
		Calendar cal = getCurrentDayStart();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	private static Date getCurrentMonthEnd() {
		Calendar cal = getCurrentDayEnd();
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
	
	private static Date getCurrentQuarterStart() {
		Calendar cal = getCurrentDayStart();
		int month = cal.get(Calendar.MONTH);
		
		if(month >= Calendar.JANUARY && month <= Calendar.MARCH) {
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			return cal.getTime();
		} else if (month >= Calendar.APRIL && month <= Calendar.JUNE) {
			cal.set(Calendar.MONTH, Calendar.APRIL);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			return cal.getTime();
		} else if (month >= Calendar.JULY && month <= Calendar.SEPTEMBER) {
			cal.set(Calendar.MONTH, Calendar.JULY);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			return cal.getTime();
		} else {
			cal.set(Calendar.MONTH, Calendar.OCTOBER);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			return cal.getTime();
		}
	}
	
	private static Date getCurrentQuarterEnd() {
		Calendar cal = getCurrentDayEnd();
		int month = cal.get(Calendar.MONTH);
		
		if(month >= Calendar.JANUARY && month <= Calendar.MARCH) {
			cal.set(Calendar.MONTH, Calendar.MARCH);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		} else if (month >= Calendar.APRIL && month <= Calendar.JUNE) {
			cal.set(Calendar.MONTH, Calendar.JUNE);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		} else if (month >= Calendar.JULY && month <= Calendar.SEPTEMBER) {
			cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		} else {
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
			return cal.getTime();
		}
	}
	
	private static Date getCurrentYearStart() {
		Calendar cal = getCurrentDayStart();
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	private static Date getCurrentYearEnd() {
		Calendar cal = getCurrentDayEnd();
		cal.set(Calendar.MONTH, cal.getMaximum(Calendar.MONTH));
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}
}
