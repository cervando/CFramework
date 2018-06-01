package kmiddle2.log;

/**
 * Kuayolotl Middleware System
 * @author Karina Jaime <ajaime@gdl.cinvestav.mx>
 *
 * This file is part of Kuayolotl Middleware
 *
 * Kuayolotl Middleware is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *	
 * Kuayolotl Middleware is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *	
 * You should have received a copy of the GNU General Public License
 * along with Kuayolotl Middleware.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class XMLFileFormatter extends Formatter {

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	@Override
	public String format(LogRecord record) {
		
		
		StringBuilder sb = new StringBuilder();
		Calendar time = Calendar.getInstance();
		time.setTimeInMillis(record.getMillis());

        sb.append("<record>")
        	.append("<time>")
        		.append("<hour>")
        			.append(time.get(Calendar.HOUR_OF_DAY))
        		.append("</hour>")
        		.append("<minute>")
        			.append(time.get(Calendar.MINUTE))
        		.append("</minute>")
        		.append("<second>")
        			.append(time.get(Calendar.SECOND))
        		.append("</second>")
        		.append("<milisecond>")
        			.append(time.get(Calendar.MILLISECOND))
        		.append("</milisecond>")
        	.append("</time>")
            .append("<data>")
            	.append(formatMessage(record))
            .append("</data>")
          .append("</record>")
          .append(LINE_SEPARATOR);

        if (record.getThrown() != null) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                record.getThrown().printStackTrace(pw);
                pw.close();
                sb.append(sw.toString());
            } catch (Exception ex) {
                // ignore
            }
        }

        return sb.toString();
	}
	
	public String getHead(Handler h) {
	      return "<?xml version=\"1.0\" encoding=\"" + h.getEncoding() + "\" standalone=\"no\"?>" + LINE_SEPARATOR + 
	    		 "<!DOCTYPE rootElement>" + LINE_SEPARATOR +
	    		 "<log>" + LINE_SEPARATOR;
	}
	
	public String getTail(Handler h) {
	      return "</log>";
	}
}
