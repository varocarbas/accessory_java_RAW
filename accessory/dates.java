package accessory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public abstract class dates extends parent_static
{
	public static final String _ID = types.get_id(types.ID_DATES);
	
	public static final String FORMAT_TIME = types.DATES_FORMAT_TIME;
	public static final String FORMAT_TIME_FULL = types.DATES_FORMAT_TIME_FULL;
	public static final String FORMAT_TIME_SHORT = types.DATES_FORMAT_TIME_SHORT;
	public static final String FORMAT_DATE = types.DATES_FORMAT_DATE;
	public static final String FORMAT_DATE_TIME = types.DATES_FORMAT_DATE_TIME;
	public static final String FORMAT_TIMESTAMP = types.DATES_FORMAT_TIMESTAMP;
	
	public static final String SECONDS = types.DATES_UNIT_SECONDS;
	public static final String MINUTES = types.DATES_UNIT_MINUTES;
	public static final String HOURS = types.DATES_UNIT_HOURS;
	public static final String DAYS = types.DATES_UNIT_DAYS;
	
	public static final String ERROR_STRING = types.ERROR_DATES_STRING;
	
	public static final String DEFAULT_FORMAT = _defaults.DATES_FORMAT;
	public static final String DEFAULT_FORMAT_DATE_TIME = _defaults.DATES_FORMAT_DATE_TIME;
	public static final String DEFAULT_FORMAT_DATE = _defaults.DATES_FORMAT_DATE;
	public static final String DEFAULT_FORMAT_TIME = _defaults.DATES_FORMAT_TIME;
	public static final String DEFAULT_UNIT = _defaults.DATES_UNIT;
	public static final String DEFAULT_UNIT_DATE_TIME = _defaults.DATES_UNIT_DATE_TIME;
	public static final String DEFAULT_UNIT_DATE = _defaults.DATES_UNIT_DATE;
	public static final String DEFAULT_UNIT_TIME = _defaults.DATES_UNIT_TIME;
	public static final int DEFAULT_SIZE_DAYS = _defaults.DATES_SIZE_DAYS;
	public static final int DEFAULT_SIZE_HOURS = _defaults.DATES_SIZE_HOURS;
	
	public static LocalDateTime get_now() { return get_now(0); }
	
	public static LocalDateTime get_now(int offset_) { return LocalDateTime.now().plusHours(offset_); }

	public static String get_now_string() { return get_now_string(DEFAULT_FORMAT); }

	public static String get_now_string(String format_) { return get_now_string(format_, 0); }
	
	public static String get_now_string(String format_, int offset_) { return get_formatter(format_).format(get_now(offset_)); }

	public static boolean target_met(LocalDateTime start_, long target_) { return target_met(start_, DEFAULT_UNIT_DATE_TIME, target_); }

	public static boolean target_met(LocalDate start_, long target_) { return target_met(start_, DEFAULT_UNIT_DATE, target_); }

	public static boolean target_met(LocalTime start_, long target_) { return target_met(start_, DEFAULT_UNIT_TIME, target_); }

	public static boolean target_met(LocalDateTime start_, String unit_, long target_) { return (get_diff(start_, LocalDateTime.now(), unit_) >= target_); }

	public static boolean target_met(LocalDate start_, String unit_, long target_) { return (get_diff(start_, LocalDate.now(), unit_) >= target_); }

	public static boolean target_met(LocalTime start_, String unit_, long target_) { return (get_diff(start_, LocalTime.now(), unit_) >= target_); }

	public static long get_diff(String start_, String end_) { return get_diff(start_, end_, DEFAULT_UNIT); }

	public static long get_diff(String start_, String end_, String unit_) { return get_diff(start_, DEFAULT_FORMAT, end_, DEFAULT_FORMAT, unit_); }

	public static long get_diff(String start_, String format_start_, String end_, String format_end_, String unit_) { return get_diff(from_string(start_, format_start_), from_string(end_, format_end_), unit_); }
	
	public static long get_diff(LocalDateTime start_, LocalDateTime end_) { return get_diff(start_, end_, DEFAULT_UNIT_DATE_TIME); }
	
	public static long get_diff(LocalDate start_, LocalDate end_) { return get_diff(start_, end_, DEFAULT_UNIT_DATE); }
	
	public static long get_diff(LocalTime start_, LocalTime end_) { return get_diff(start_, end_, DEFAULT_UNIT_TIME); }

	public static long get_diff(LocalDateTime start_, LocalDateTime end_, String unit_) { return get_diff_internal(start_, end_, unit_, FORMAT_DATE_TIME); }
	
	public static long get_diff(LocalDate start_, LocalDate end_, String unit_) { return get_diff_internal(start_, end_, unit_, FORMAT_DATE); }
	
	public static long get_diff(LocalTime start_, LocalTime end_, String unit_) { return get_diff_internal(start_, end_, unit_, FORMAT_TIME); }
	
	private static long get_diff_internal(Object start_, Object end_, String unit_, String what_)
	{
		long output = 0;
		if (start_ == null || end_ == null) return output;
		
		Duration duration = null;

		if (what_.equals(FORMAT_DATE_TIME)) duration = Duration.between((LocalDateTime)start_, (LocalDateTime)end_);
		else if (what_.equals(FORMAT_DATE)) duration = Duration.between(((LocalDate)start_).atStartOfDay(), ((LocalDate)end_).atStartOfDay());  //!!!
		else if (what_.equals(FORMAT_TIME)) duration = Duration.between((LocalTime)start_, (LocalTime)end_);			
		else return output;

		boolean is_millis = true;

		try { output = duration.toMillis(); }
		catch (Exception e) 
		{
			is_millis = false;
			output = duration.toSeconds(); 
		}
		
		String unit = (strings.is_ok(unit_) ? unit_ : DEFAULT_UNIT);
		TimeUnit unit2 = (is_millis ? TimeUnit.MILLISECONDS : TimeUnit.SECONDS);
		
		if (unit.equals(SECONDS)) output = unit2.toSeconds(output);
		else if (unit.equals(MINUTES)) output = unit2.toMinutes(output);
		else if (unit.equals(HOURS)) output = unit2.toHours(output);
		else if (unit.equals(DAYS)) output = unit2.toDays(output);
		else output = 0;
		
		return output;
	}
	
	public static String add_timestamp(String input_, String format_, Boolean is_name_)
	{
		if (!strings.are_ok(new String[] { input_ })) return strings.DEFAULT;

		String sep = misc.SEPARATOR_CONTENT;
		String timestamp = get_now_string(format_);

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
	
	public static String get_pattern(String format_)
	{
		String pattern = strings.DEFAULT;

		String format = check_format(format_, true);
		
		if (format.equals(FORMAT_TIME_FULL) || format.equals(FORMAT_TIME)) pattern = "HH:mm:ss";
		if (format.equals(FORMAT_TIME_SHORT)) pattern = "HH:mm";
		if (format.equals(FORMAT_DATE_TIME) || format.equals(FORMAT_TIMESTAMP)) pattern = "yyyy-MM-dd HH:mm:ss";
		if (format.equals(FORMAT_DATE)) pattern = "yyyy-MM-dd";

		return pattern;
	}
	
	public static DateTimeFormatter get_formatter(String format_) 
	{ 
		DateTimeFormatter output = DateTimeFormatter.ofPattern(get_pattern(format_)); 
		output.withResolverStyle(ResolverStyle.SMART);
		
		return output;
	}
	
	public static String check_format(String format_, boolean use_default_)
	{
		String format = types.check_type(format_, types.DATES_FORMAT);
		
		return (strings.is_ok(format) ? format : (use_default_ ? DEFAULT_FORMAT : strings.DEFAULT));
	}

	public static int get_length(String format_) { return get_pattern(check_format(format_, false)).length(); }
	
	public static LocalDateTime from_string(String input_, String format_) 
	{ 
		LocalDateTime output = null;
		if (!strings.is_ok(input_)) return output;
		
		DateTimeFormatter formatter = get_formatter(format_); 
		
		try
		{
			if (is_date_time(format_)) output = LocalDateTime.parse(input_, formatter);
			else if (is_date(format_)) output = from_date(LocalDate.parse(input_, formatter));
			else if (is_time(format_)) output = from_time(LocalTime.parse(input_, formatter));
		}
		catch (Exception e) 
		{ 
			if (!ignore_errors_internal()) 
			{
				HashMap<String, String> info = new HashMap<String, String>();
				info.put("input", input_);
				info.put("format", format_);
				
				errors.manage(ERROR_STRING, e, info); 
			}	
		}
		
		return output;
	}

	public static LocalDateTime from_date(LocalDate input_) 
	{ 
		if (input_ == null) return null;
		
		LocalDateTime now = get_now();
		
		return LocalDateTime.of(input_.getYear(), input_.getMonth(), input_.getDayOfMonth(), now.getHour(), now.getMinute(), now.getSecond(), now.getNano());
	}
	
	public static LocalDateTime from_time(LocalTime input_) 
	{ 
		if (input_ == null) return null;
		
		LocalDateTime now = get_now();
		
		return LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), input_.getHour(), input_.getMinute(), input_.getSecond(), input_.getNano());
	}

	public static LocalDate to_date(LocalDateTime input_) { return (input_ == null ? null : LocalDate.of(input_.getYear(), input_.getMonth(), input_.getDayOfMonth())); }
	
	public static LocalTime to_time(LocalDateTime input_) { return (input_ == null ? null : LocalTime.of(input_.getHour(), input_.getMinute(), input_.getSecond(), input_.getNano())); }

	public static String to_string(LocalDateTime input_) { return to_string(input_, DEFAULT_FORMAT_DATE_TIME); }
	
	public static String to_string(LocalDate input_) { return to_string(input_, DEFAULT_FORMAT_DATE); }
	
	public static String to_string(LocalTime input_) { return to_string(input_, DEFAULT_FORMAT_TIME); }

	public static String to_string(LocalDateTime input_, String format_) { return get_formatter(format_).format(input_); }
	
	public static String to_string(LocalDate input_, String format_) { return (is_date(format_) ? get_formatter(format_).format(input_) : strings.DEFAULT); }
	
	public static String to_string(LocalTime input_, String format_) { return (is_time(format_) ? get_formatter(format_).format(input_) : strings.DEFAULT); }

	public static LocalDateTime get_random_datetime() { return LocalDateTime.now().plusDays((long)numbers.get_random_int(-1 * DEFAULT_SIZE_DAYS, DEFAULT_SIZE_DAYS)); }

	public static LocalDate get_random_date() { return LocalDate.now().plusDays((long)numbers.get_random_int(-1 * DEFAULT_SIZE_DAYS, DEFAULT_SIZE_DAYS)); }

	public static LocalTime get_random_time() { return LocalTime.now().plusHours((long)numbers.get_random_int(-1 * DEFAULT_SIZE_HOURS, DEFAULT_SIZE_HOURS)); }

	private static boolean is_date_time(String format_)
	{
		String format = check_format(format_, false);
		if (!strings.is_ok(format)) return false;
		
		return (format.equals(FORMAT_DATE_TIME) || format.equals(FORMAT_TIMESTAMP));
	}

	private static boolean is_date(String format_)
	{
		String format = check_format(format_, false);
		if (!strings.is_ok(format)) return false;
		
		return (format.equals(FORMAT_DATE));
	}

	private static boolean is_time(String format_)
	{
		String format = check_format(format_, false);
		if (!strings.is_ok(format)) return false;
		
		return (format.equals(FORMAT_TIME) || format.equals(FORMAT_TIME_FULL) || format.equals(FORMAT_TIME_SHORT));
	}
}