package accessory;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public abstract class dates extends parent_static
{
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

	public static final String TZ_MADRID = "Europe/Madrid";
	public static final String TZ_LONDON = "Europe/London";
	public static final String TZ_NY = "America/New_York";
	public static final String TZ_LA = "America/Los_Angeles";
	public static final String TZ_TOKYO = "Asia/Tokyo";
	public static final String TZ_SYDNEY = "Australia/Sydney";
	
	public static final int MAX_OFFSET = 27 * 60;
	
	public static final String ERROR_STRING = types.ERROR_DATES_STRING;
	
	public static final String DEFAULT_FORMAT = FORMAT_DATE_TIME;
	public static final String DEFAULT_FORMAT_DATE_TIME = FORMAT_DATE_TIME;
	public static final String DEFAULT_FORMAT_DATE = FORMAT_DATE;
	public static final String DEFAULT_FORMAT_TIME = FORMAT_TIME;
	public static final String DEFAULT_FORMAT_TIMESTAMP = FORMAT_TIMESTAMP;
	public static final String DEFAULT_UNIT = SECONDS;
	public static final String DEFAULT_UNIT_DATE_TIME = DAYS;
	public static final String DEFAULT_UNIT_DATE = DAYS;
	public static final String DEFAULT_UNIT_TIME = SECONDS;
	public static final String DEFAULT_UNIT_TIMESTAMP = MINUTES;
	public static final int DEFAULT_SIZE_DAYS = 50;
	public static final int DEFAULT_SIZE_HOURS = 10;
	public static final int DEFAULT_OFFSET = 0;

	private static int _offset = DEFAULT_OFFSET;
	
	public static LocalDateTime get_now() { return get_now(_offset); }

	public static LocalDateTime get_now(int offset_mins_) { return LocalDateTime.now().plusMinutes(check_offset(offset_mins_)); }

	public static LocalDate get_now_date() { return LocalDate.now(); }

	public static LocalTime get_now_time() { return get_now_time(_offset); }

	public static LocalTime get_now_time(int offset_mins_) { return LocalTime.now().plusMinutes(check_offset(offset_mins_)); }
	
	public static int get_offset() { return _offset; }

	public static void update_offset() { update_offset(DEFAULT_OFFSET); }
	
	public static void update_offset(int offset_) { _offset = check_offset(offset_); }

	public static void update_offset(String tz_other_) { get_offset(null, tz_other_, true); }
	
	public static int get_offset(String tz_current_, String tz_other_, boolean update_global_) 
	{ 
		ZoneId current = (strings.is_ok(tz_current_) ? get_timezone(tz_current_) : get_current_timezone());
		if (current == null) current = get_current_timezone();
		
		ZoneId other = get_timezone(tz_other_);
		if (other == null) return DEFAULT_OFFSET;
		
		LocalDateTime now = LocalDateTime.now();
		
		int offset = (int)Duration.between(now.atZone(other), now.atZone(current)).toMinutes();
		offset = check_offset(offset);
		if (update_global_) _offset = offset;
		
		return offset;
	}

	public static ZoneId get_current_timezone() { return ZoneId.systemDefault(); }
	
	public static ZoneId get_timezone(String tz_)
	{
		ZoneId output = null;
		if (!strings.is_ok(tz_)) return output;
		
		try { output = ZoneId.of(tz_); }
		catch (Exception e) { output = null; }
	
		return output;
	}
	
	public static String get_timestamp() { return get_now_string(DEFAULT_FORMAT_TIMESTAMP); }

	public static String get_now_string() { return get_now_string(DEFAULT_FORMAT); }

	public static String get_now_string(String format_) { return get_now_string(format_, 0); }

	public static String get_now_string(String format_, int offset_) { return get_formatter(format_).format(get_now(offset_)); }

	public static boolean target_met(LocalDateTime start_, long target_) { return target_met(start_, DEFAULT_UNIT_DATE_TIME, target_); }

	public static String update_timestamp(long increase_) { return update_timestamp(to_string(get_now(), DEFAULT_FORMAT_TIMESTAMP), increase_); }

	public static String update_timestamp(String timestamp_, long increase_) { return update_timestamp(timestamp_, DEFAULT_UNIT_TIMESTAMP, increase_); }

	public static String update_timestamp(String timestamp_, String unit_, long increase_) { return update(timestamp_, DEFAULT_FORMAT_TIMESTAMP, unit_, increase_); }

	public static String update(String input_, String format_, String unit_, long increase_)
	{
		String output = strings.DEFAULT;

		LocalDateTime input = from_string(input_, format_);
		if (input == null || !strings.is_ok(unit_)) return output;

		return (increase_ == 0 ? input_ : to_string(update(input, unit_, increase_), format_));
	}

	public static LocalDateTime update(LocalDateTime input_, String unit_, long increase_)
	{
		if (input_ == null || !strings.is_ok(unit_)) return null;

		LocalDateTime output = LocalDateTime.from(input_);
		if (increase_ == 0) return output;

		if (unit_.equals(SECONDS)) output = output.plusSeconds(increase_);
		else if (unit_.equals(MINUTES)) output = output.plusMinutes(increase_);
		else if (unit_.equals(HOURS)) output = output.plusHours(increase_);
		else if (unit_.equals(DAYS)) output = output.plusDays(increase_);

		return output;
	}

	public static boolean target_met(LocalDate start_, long target_) { return target_met(start_, DEFAULT_UNIT_DATE, target_); }

	public static boolean target_met(LocalTime start_, long target_) { return target_met(start_, DEFAULT_UNIT_TIME, target_); }

	public static boolean target_met(LocalDateTime start_, String unit_, long target_) { return (get_diff(start_, get_now(), unit_) >= target_); }

	public static boolean target_met(LocalDate start_, String unit_, long target_) { return (get_diff(start_, get_now_date(), unit_) >= target_); }

	public static boolean target_met(LocalTime start_, String unit_, long target_) { return (get_diff(start_, get_now_time(), unit_) >= target_); }

	public static boolean passed(LocalDateTime input_) { return (get_diff(input_, get_now(), SECONDS) > 0); }

	public static boolean passed(LocalDate input_) { return (get_diff(input_, get_now_date(), DAYS) > 0); }

	public static boolean passed(LocalTime input_) { return (get_diff(input_, get_now_time(), SECONDS) > 0); }

	public static long get_diff(String start_, String end_) { return get_diff(start_, end_, DEFAULT_UNIT); }

	public static long get_diff(String start_, String end_, String unit_) { return get_diff(start_, DEFAULT_FORMAT, end_, DEFAULT_FORMAT, unit_); }

	public static long get_diff(String start_, String format_start_, String end_, String format_end_, String unit_) { return get_diff(from_string(start_, format_start_), from_string(end_, format_end_), unit_); }

	public static long get_diff(LocalDateTime start_, LocalDateTime end_) { return get_diff(start_, end_, DEFAULT_UNIT_DATE_TIME); }

	public static long get_diff(LocalDate start_, LocalDate end_) { return get_diff(start_, end_, DEFAULT_UNIT_DATE); }

	public static long get_diff(LocalTime start_, LocalTime end_) { return get_diff(start_, end_, DEFAULT_UNIT_TIME); }

	public static long get_diff(LocalDateTime start_, LocalDateTime end_, String unit_) { return get_diff_internal(start_, end_, unit_, FORMAT_DATE_TIME); }

	public static long get_diff(LocalDate start_, LocalDate end_, String unit_) { return get_diff_internal(start_, end_, unit_, FORMAT_DATE); }

	public static long get_diff(LocalTime start_, LocalTime end_, String unit_) { return get_diff_internal(start_, end_, unit_, FORMAT_TIME); }

	public static String add_timestamp(String input_, Boolean is_name_) { return add_now_string(input_, DEFAULT_FORMAT_TIMESTAMP, is_name_); }	

	public static String add_now_string(String input_, String format_, Boolean is_name_)
	{
		String output = strings.DEFAULT;
		if (!strings.is_ok(input_)) return output;

		String sep = misc.SEPARATOR_CONTENT;
		output = get_now_string(format_);

		if (is_name_)
		{
			sep = misc.SEPARATOR_NAME;
			output = output.replaceAll(" ", sep);
		}

		output += sep + input_;

		return output;			
	}
	
	public static long start_elapsed() { return get_elapsed(); }
	
	public static long get_elapsed() { return get_elapsed(0); }
	
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

	public static HashMap<String, Integer> get_seconds_minutes(int secs_) { return get_seconds_minutes_hours(secs_, SECONDS, MINUTES); }

	public static HashMap<String, Integer> get_minutes_hours(int mins_) { return get_seconds_minutes_hours(mins_, MINUTES, HOURS); }

	public static String seconds_to_time(int secs_) { return seconds_to_time(secs_, false); }
	
	public static String seconds_to_time(int secs_, boolean no_hours_) 
	{ 		
		HashMap<String, Integer> temp = get_seconds_minutes(secs_);
		if (!arrays.is_ok(temp)) return strings.DEFAULT;
		
		int secs = temp.get(SECONDS);
		int mins = temp.get(MINUTES);
		int hours = 0;

		String output = "";
		
		if (!no_hours_)
		{
			temp = get_minutes_hours(mins);
			
			mins = temp.get(MINUTES);
			hours = temp.get(HOURS);
			
			output = String.format("%02d", hours) + ":";
		}
		
		output += String.format("%02d", mins) + ":";
		output += String.format("%02d", secs);
		
		return output; 
	}

	public static int time_to_seconds(String time_) { return time_to_seconds(time_, false); }
	
	public static int time_to_seconds(String time_, boolean no_hours_)
	{
		int output = 0;

		String[] temp = strings.split(":", time_);
		int size = arrays.get_size(temp);
		if (size < 2 || size > 3) return output;

		int i2 = 3;
		if (no_hours_) i2--;
		
		for (int i = 0; i < size; i++) 
		{ 
			i2--;
			if (i2 < 0) continue;
			
			output += (Math.pow(60, i2) * strings.to_number_int(temp[i])); 
		}

		return output;
	}

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
			HashMap<String, Object> info = new HashMap<String, Object>();
			info.put("input", input_);
			info.put("format", format_);

			manage_error(ERROR_STRING, e, info);
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

	public static LocalDateTime get_random_datetime() { return get_now().plusDays((long)numbers.get_random_int(-1 * DEFAULT_SIZE_DAYS, DEFAULT_SIZE_DAYS)); }

	public static LocalDate get_random_date() { return get_now_date().plusDays((long)numbers.get_random_int(-1 * DEFAULT_SIZE_DAYS, DEFAULT_SIZE_DAYS)); }

	public static LocalTime get_random_time() { return get_now_time().plusHours((long)numbers.get_random_int(-1 * DEFAULT_SIZE_HOURS, DEFAULT_SIZE_HOURS)); }
	
	private static HashMap<String, Integer> get_seconds_minutes_hours(int val1_, String key1_, String key2_) 
	{ 
		if (val1_ < 0) return null;
		
		HashMap<String, Integer> output = new HashMap<String, Integer>();

		int val1 = val1_;
		int val2 = 0;
		
		if (val1 >= 60)
		{
			val2 = val1 / 60;
			val1 -= (60 * val2);
		}
		
		output.put(key1_, val1);
		output.put(key2_, val2);
	
		return output;
	}

	private static int check_offset(int offset_) { return (Math.abs(offset_) > MAX_OFFSET ? DEFAULT_OFFSET : offset_); }

	private static long get_diff_internal(Object start_, Object end_, String unit_, String what_)
	{
		long output = 0;
		if (start_ == null || end_ == null) return output;

		Duration duration = null;

		if (what_.equals(FORMAT_DATE_TIME)) duration = Duration.between((LocalDateTime)start_, (LocalDateTime)end_);
		else if (what_.equals(FORMAT_DATE)) duration = Duration.between(((LocalDate)start_).atStartOfDay(), ((LocalDate)end_).atStartOfDay()); //!!!
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

	private static boolean is_date_time(String format_) { return is_common(format_, new String[] { FORMAT_DATE_TIME, FORMAT_TIMESTAMP }); }

	private static boolean is_date(String format_) { return is_common(format_, new String[] { FORMAT_DATE }); }

	private static boolean is_time(String format_) { return is_common(format_, new String[] { FORMAT_TIME, FORMAT_TIME_FULL, FORMAT_TIME_SHORT }); }

	private static boolean is_common(String format_, String[] targets_)
	{
		String format = check_format(format_, false);
		if (!strings.is_ok(format)) return false;

		for (String target: targets_)
		{
			if (format.equals(target)) return true;
		}

		return false;
	}
}