package accessory;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class dates 
{
	public static final String TIME = keys.TIME;
	public static final String TIME_SHORT = "time_short";
	public static final String TIME_FULL = TIME;
	public static final String DATE = keys.DATE;
	public static final String DATE_TIME = DATE + misc.SEPARATOR_NAME + TIME;
	public static final String DEFAULT = defaults.TIME_DATE;

	static { ini.load(); }

	public static String get_current_time(String format_, int offset_)
	{
		DateTimeFormatter formatter = get_formatter(format_);

		return (formatter == null ? strings.DEFAULT : formatter.format(get_current_time(0)));	
	}

	public static Boolean time_is_ok(LocalTime start_, LocalTime end_, int offset_)
	{
		if (!generic.is_ok(start_) || !generic.is_ok(end_)) return false;

		int offset = offset_;
		if (offset < 0) offset = 0;

		LocalTime current = get_current_time(offset).toLocalTime();

		return (current.compareTo(start_) >= 0 && current.compareTo(end_) < 0);	
	}

	public static String add_timestamp(String input_, String format_, Boolean is_content_)
	{
		if (!strings.is_ok(input_) || !strings.is_ok(format_)) return strings.DEFAULT;

		String sep = misc.SEPARATOR_CONTENT;
		String timestamp = get_current_time(format_, 0);

		if (!is_content_)
		{
			sep = misc.SEPARATOR_NAME;
			timestamp = timestamp.replaceAll(" ", sep);
		}

		return (timestamp + sep + input_);		
	}	

	public static long get_elapsed(long start_)
	{
		return (start_ < 0 ? 0 : get_elapsed_sec(start_));
	}

	public static long get_elapsed_sec(long start_)
	{
		long current = System.currentTimeMillis();

		return (start_ == 0 ? current : ((current - start_) / 1000));
	}
	
	public static String get_time_pattern(String format_)
	{
		String pattern = strings.DEFAULT;

		String format = format_;
		if (!strings.is_ok(format)) format = DEFAULT;

		if (format.equals(TIME_FULL) || format.equals(TIME)) pattern = "HH:mm:ss";
		if (format.equals(TIME_SHORT)) pattern = "HH:mm";
		if (format.equals(DATE_TIME)) pattern = "yyyy-MM-dd HH:mm:ss";
		if (format.equals(DATE)) pattern = "yyyy-MM-dd";

		return pattern;
	} 
	
	private static LocalDateTime get_current_time(int offset_)
	{
		int offset = offset_;
		if (offset < 0) offset = 0;

		return LocalDateTime.now().plusHours(offset);
	}
	
	private static DateTimeFormatter get_formatter(String format_)
	{
		String pattern = get_time_pattern(format_);

		return (!strings.is_ok(pattern) ? null : DateTimeFormatter.ofPattern(pattern));
	} 
}