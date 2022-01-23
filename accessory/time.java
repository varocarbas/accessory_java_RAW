package accessory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class time 
{
	public static final String FORMAT_TIME_SHORT = "time_short";
	public static final String FORMAT_TIME_FULL = "time_full";
	public static final String FORMAT_DATE_TIME = "date_time";
	public static final String FORMAT_DATE = "date";
	public static final String FORMAT_DEFAULT = FORMAT_TIME_FULL;
	
	public static String get_current_time(String format_, int offset_)
	{
		DateTimeFormatter formatter = get_formatter(format_);

		return 
		(
			formatter == null ? null : formatter.format
			(
				get_current_time(0)
			)
		);	
	}
	
	private static LocalDateTime get_current_time(int offset_)
	{
		int offset = offset_;
		if (offset < 0) offset = 0;
		
		return LocalDateTime.now().plusHours(offset);
	}
	
	private static String get_format_time_pattern(String format_)
	{
		String pattern = null;

		String format = format_;
		if (!strings.is_ok(format)) format = FORMAT_DEFAULT;
		
		if (format.equals(FORMAT_TIME_SHORT)) pattern = "HH:mm";
		if (format.equals(FORMAT_TIME_FULL)) pattern = "HH:mm:ss";
		if (format.equals(FORMAT_DATE_TIME)) pattern = "yyyy-MM-dd HH:mm:ss";
		if (format.equals(FORMAT_DATE)) pattern = "yyyy-MM-dd";
		
		return pattern;
	} 
		
	private static DateTimeFormatter get_formatter(String format_)
	{
		String pattern = get_format_time_pattern(format_);

		return 
		(
			!strings.is_ok(pattern) ? null : 
			DateTimeFormatter.ofPattern(pattern)  
		);
	} 
	
	public static Boolean time_is_ok(LocalTime start_, LocalTime end_, int offset_)
	{
		int offset = offset_;
		if (offset < 0) offset = 0;
		
		LocalTime current = get_current_time(offset).toLocalTime();

		return
		(
			current.compareTo(start_) >= 0 &&
			current.compareTo(end_) < 0			
		);	
	}
	
	public static String add_timestamp(String input_, String format_, Boolean is_content_)
	{
		String sep = misc.SEPARATOR_CONTENT;
		String timestamp = get_current_time(format_, 0);
		
		if (!is_content_)
		{
			sep = misc.SEPARATOR_NAME;
			timestamp = timestamp.replaceAll(" ", sep);
		}
		
		return (timestamp + sep + input_);		
	}
}
