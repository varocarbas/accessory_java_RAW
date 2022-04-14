package accessory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class dates 
{	
	public static final String TIME = types.DATES_FORMAT_TIME;
	public static final String TIME_FULL = types.DATES_FORMAT_TIME_FULL;
	public static final String TIME_SHORT = types.DATES_FORMAT_TIME_SHORT;
	public static final String DATE = types.DATES_FORMAT_DATE;
	public static final String DATE_TIME = types.DATES_FORMAT_DATE_TIME;
	
	public static final int SIZE_TIME = get_pattern(TIME).length();
	public static final int SIZE_TIME_FULL = get_pattern(TIME_FULL).length();
	public static final int SIZE_TIME_SHORT = get_pattern(TIME_SHORT).length();
	public static final int SIZE_DATE = get_pattern(DATE).length();
	public static final int SIZE_DATE_TIME = get_pattern(DATE_TIME).length();
	public static final int SIZE_TIMESTAMP = SIZE_DATE_TIME;

	public static final String DEFAULT_FORMAT = _defaults.DATES_FORMAT;
	
	static { _ini.load(); }

	public static String get_current_time(String format_, int offset_)
	{
		DateTimeFormatter formatter = get_formatter(format_);

		return (formatter == null ? strings.DEFAULT : formatter.format(get_current_time(0)));	
	}
	
	public static String add_timestamp(String input_, String format_, Boolean is_name_)
	{
		if (!strings.are_ok(new String[] { input_, format_ })) return strings.DEFAULT;

		String sep = misc.SEPARATOR_CONTENT;
		String timestamp = get_current_time(format_, 0);

		if (is_name_)
		{
			sep = misc.SEPARATOR_NAME;
			timestamp = timestamp.replaceAll(" ", sep);
		}

		return (timestamp + sep + input_);		
	}	

	public static long get_elapsed(long start_)
	{
		long current = System.currentTimeMillis();

		return (start_ <= 0 ? current : ((current - start_) / 1000));
	}
	
	private static LocalDateTime get_current_time(int offset_)
	{
		int offset = offset_;
		if (offset < 0) offset = 0;

		return LocalDateTime.now().plusHours(offset);
	}
	
	private static DateTimeFormatter get_formatter(String format_)
	{
		String pattern = get_pattern(format_);

		return (!strings.is_ok(pattern) ? null : DateTimeFormatter.ofPattern(pattern));
	}
	
	private static String get_pattern(String format_)
	{
		String pattern = strings.DEFAULT;

		String format = types.check_type(format_, types.get_subtypes(types.DATES_FORMAT));
		if (!strings.is_ok(format)) format = DEFAULT_FORMAT;
		
		if (format.equals(TIME_FULL) || format.equals(TIME)) pattern = "HH:mm:ss";
		if (format.equals(TIME_SHORT)) pattern = "HH:mm";
		if (format.equals(DATE_TIME)) pattern = "yyyy-MM-dd HH:mm:ss";
		if (format.equals(DATE)) pattern = "yyyy-MM-dd";

		return pattern;
	} 
}