package kmiddle2.log;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ConsoleFormatter extends Formatter {
	
	private String className;

	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	public ConsoleFormatter(String className) {
		this.className = className;
	}
	
	
	@Override
	public String format(LogRecord record) {

		return className + " : " + formatMessage(record) + LINE_SEPARATOR;
		
	}
}
