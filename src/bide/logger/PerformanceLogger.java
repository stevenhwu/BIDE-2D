/*******************************************************************************
 * PerformanceLogger.java
 * 
 * This file is part of BIDE-2D
 * 
 * Copyright (C) 2012 Steven Wu
 * 
 * BIDE-2D is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * BIDE-2D is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with BIDE-2D.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package bide.logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;


public class PerformanceLogger {

	// the file that this logger is logging to or
	// null if the LogFormatter is logging to a non-file print stream
	String fileName;

	private String title = null;

	private boolean performanceReport;

	TabFormatter formatter;// = new TabFormatter(fileName);

	private long startTime;
	private NumberFormat numFormatter = NumberFormat.getNumberInstance();

	public PerformanceLogger(String fileName, TabFormatter formatter,
			boolean performanceReport) {

		this.formatter = formatter;
		// this.logEvery = logEvery;
		this.performanceReport = performanceReport;
		this.fileName = fileName;
	}

	/**
	 * Constructor. Will log every logEvery.
	 * 
	 * @param formatter
	 *            the formatter of this logger
	 * @param logEvery
	 *            logging frequency
	 */
	public PerformanceLogger(TabFormatter formatter, boolean performanceReport) {
		this(null, formatter, performanceReport);
	}

	/**
	 * Constructor. Will log every logEvery.
	 * 
	 * @param logEvery
	 *            logging frequency
	 */
	public PerformanceLogger(String fileName) throws IOException {
		this(fileName, new TabFormatter(new PrintWriter(
				new FileWriter(fileName))), true);
	}

	/**
	 * Constructor. Will log every logEvery.
	 * 
	 * @param logEvery
	 *            logging frequency
	 */
	public PerformanceLogger() {
		this(null, new TabFormatter(System.out), true);
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final String getTitle() {
		return title;
	}

	// public int getLogEvery() {
	// return logEvery;
	// }
	//
	// public void setLogEvery(int logEvery) {
	// this.logEvery = logEvery;
	// }

	// public final void addFormatter(TabFormatter formatter) {
	//
	// formatters.add(formatter);
	// }

	// public final void add(Loggable loggable) {
	//
	// LogColumn[] columns = loggable.getColumns();
	//
	// for (LogColumn column : columns) {
	// addColumn(column);
	// }
	// }
	//
	// public final void addColumn(LogColumn column) {
	//
	// columns.add(column);
	// }
	//
	// public final void addColumns(LogColumn[] columns) {
	//
	// for (LogColumn column : columns) {
	// addColumn(column);
	// }
	// }
	//
	// public final int getColumnCount() {
	// return columns.size();
	// }
	//
	// public final LogColumn getColumn(int index) {
	//
	// return columns.get(index);
	// }
	//
	// public final String getColumnLabel(int index) {
	//
	// return columns.get(index).getLabel();
	// }
	//
	// public final String getColumnFormatted(int index) {
	//
	// return columns.get(index).getFormatted();
	// }

	/**
	 * @return file name or null if this logger is logging to a non-file print
	 *         stream
	 */
	public final String getFileName() {
		return fileName;
	}

	protected void logHeading(String heading) {

		formatter.logHeading(heading);

	}

	protected void logLine(String line) {

		formatter.logLine(line);

	}

	protected void logLabels(String[] labels) {

		formatter.logLabels(labels);

	}

	protected void logValues(String[] values) {

		formatter.logValues(values);

	}

	public void logValues(int ite, double[] values) {

		formatter.logValues(ite, values);

	}

	public void startLogging(String[] labels) {

		if (title != null) {
			logHeading(title);
		}
		logLabels(labels);
		startLogging();
	}

	public void startLogging() {
		numFormatter.setMaximumFractionDigits(3);
		startTime = System.currentTimeMillis();
	}

	public void log(int state) {

		// if (logEvery > 0 && (state % logEvery == 0)) {

		// final int columnCount = value.length;

		// String[] values = new String[columnCount + (performanceReport ? 2 :
		// 1)];
		String[] values = new String[(performanceReport ? 2 : 1)];

		values[0] = Integer.toString(state);

		// for (int i = 0; i < columnCount; i++) {
		// values[i + 1] = getColumnFormatted(i);
		// }

		// if (performanceReport) {
		if (state > 0) {

			long timeTaken = System.currentTimeMillis() - startTime;

			double hoursPerMillionStates = timeTaken
					/ (3.6 * state);

			values[1] = numFormatter.format(hoursPerMillionStates)
					+ " hours/million states";
		} else {
			values[1] = "-";
		}
		// }

		logValues(values);
	}

	public void stopLogging(){
		
		long timeTaken = System.currentTimeMillis() - startTime;
		
		String[] values = new String[(performanceReport ? 2 : 1)];
		values[0] = "End";
		values[1] = Long.toString(timeTaken); 
//		System.out.println(timeTaken);
		logValues(values);

		
	}
	// private ArrayList<LogColumn> columns = new ArrayList<LogColumn>();

	// protected int logEvery = 0;

	public TabFormatter getFormatters() {
		return formatter;
	}

	public void setFormatters(TabFormatter fmt) {
		this.formatter = fmt;
	}

	// protected List<TabFormatter> formatters = new ArrayList<TabFormatter>();

}
