/*******************************************************************************
 * TabFormatter.java
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;



public class TabFormatter  {

    protected PrintWriter printWriter;
    boolean outputLabels = true;
	private NumberColumn nc;
    
    public TabFormatter(PrintWriter printWriter) {
        this.printWriter = printWriter;
        
    }
    
    public TabFormatter(String fileName) throws IOException{
    	printWriter = new PrintWriter(new FileWriter(fileName));
    }

    public TabFormatter(String fileName, NumberColumn nc) throws IOException{
    	this.nc = nc;
    	printWriter = new PrintWriter(new FileWriter(fileName));
    }

    public TabFormatter(PrintWriter printWriter, boolean labels) {

        this(printWriter);
        outputLabels = labels;
    }

    public TabFormatter(OutputStream stream) {
        this(new PrintWriter(new OutputStreamWriter(stream)));
    }

    public TabFormatter(OutputStream stream, NumberColumn nc)  {
        this(new PrintWriter(new OutputStreamWriter(stream)));
    	this.nc = nc;
    }
    
    public void logHeading(String heading) {
        if (heading != null) {
            String[] lines = heading.split("[\r\n]");
            for (String line : lines) {
                printWriter.println("# " + line);
            }
        }
        printWriter.flush();
    }

    public void logLine(String line) {
        printWriter.println(line);
        printWriter.flush();
    }

    public void logLabels(String[] labels) {
        if (outputLabels) {
            if (labels.length > 0) {
                printWriter.print(labels[0]);
            }

            for (int i = 1; i < labels.length; i++) {
                printWriter.print('\t');
                printWriter.print(labels[i]);
            }

            printWriter.println();
            printWriter.flush();
        }
    }

    public void logValues(String[] values) {

        if (values.length > 0) {
            printWriter.print(values[0]);
        }

        for (int i = 1; i < values.length; i++) {
            printWriter.print('\t');
            printWriter.print(values[i]);
        }

        printWriter.println();
        printWriter.flush();
    }

    public void logValues(int ite, double[] values) {
    	String[] sValues = new String[values.length+1];
    	sValues[0] = Integer.toString(ite);
    	for (int i = 0; i < values.length; i++) {
    		sValues[i+1] = nc.formatValue(values[i]);
			
		}
    	logValues(sValues);

    }
    


}
