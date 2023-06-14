package accessory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class logs extends parent_static
{	
	public static final String CONFIG_OUT_SCREEN = _types.CONFIG_LOGS_OUT_SCREEN;
	public static final String CONFIG_OUT_FILE = _types.CONFIG_LOGS_OUT_FILE;
	public static final String CONFIG_ERRORS_TIMESTAMP = _types.CONFIG_LOGS_ERRORS_TIMESTAMP;
	
	public static final String SCREEN = CONFIG_OUT_SCREEN;
	public static final String FILE = CONFIG_OUT_FILE;
	public static final String OUT_SCREEN = SCREEN;
	public static final String OUT_FILE = FILE;

	public static final String WHAT_DATE_TIME = dates.WHAT_DATE_TIME;
	public static final String WHAT_FURTHER = dates.WHAT_FURTHER;
	
	public static final String TIMESTAMP_SCREEN = dates.FORMAT_TIME_FULL;
	public static final String TIMESTAMP_FILE_NAME = dates.FORMAT_DATE;
	public static final String TIMESTAMP_FILE_CONTENTS = dates.FORMAT_TIMESTAMP;
	
	public static final boolean DEFAULT_OUT_SCREEN = true;
	public static final boolean DEFAULT_OUT_FILE = true;
	public static final boolean DEFAULT_ERRORS_TIMESTAMP = false;

	public static boolean out_screen(boolean out_screen_) { return config.update_logs(CONFIG_OUT_SCREEN, out_screen_); }

	public static boolean out_screen() { return config.get_logs_boolean(CONFIG_OUT_SCREEN); }

	public static boolean out_file(boolean out_file_) { return config.update_logs(CONFIG_OUT_FILE, out_file_); }

	public static boolean out_file() { return config.get_logs_boolean(CONFIG_OUT_FILE); }

	public static boolean errors_timestamp(boolean errors_timestamp_) { return config.update_logs(CONFIG_ERRORS_TIMESTAMP, errors_timestamp_); }

	public static boolean errors_timestamp() { return config.get_logs_boolean(CONFIG_ERRORS_TIMESTAMP); }

	public static void update_activity(HashMap<String, String> inputs_, String id_) { update_file(arrays.to_string(inputs_, misc.SEPARATOR_ITEM, misc.SEPARATOR_KEYVAL, null), id_, false); }

	public static void update(String message_, String id_, boolean is_error_)
	{
		if (!strings.is_ok(message_)) return;

		if (out_is_enabled(SCREEN)) update_screen(message_);

		if (out_is_enabled(FILE)) update_file(message_, id_, is_error_);
	}

	public static void update_screen(String message_) { update_screen(message_, true); }

	public static void update_screen(String message_, boolean add_timestamp_)
	{
		String line = get_line(message_, (add_timestamp_ ? TIMESTAMP_SCREEN : null));
		if (!strings.is_ok(line)) return;

		System.out.println(line);
	}

	public static void update_file(String message_, String id_, boolean is_error_) { update_file(message_, id_, is_error_, errors_timestamp()); }
	
	public static void update_file(String message_, String id_, boolean is_error_, boolean add_timestamp_path_)
	{		
		String id = id_;
		if (!strings.is_ok(id)) id = (String)config.get_basic(_types.CONFIG_BASIC_NAME);
		
		update_file(message_, get_path(id, is_error_, add_timestamp_path_));
	}
	
	public static void update_file(String message_, String path_)
	{
		if (parent_tests.is_running())
		{	
			if (!out_is_enabled(SCREEN)) update_screen(message_);
			
			generic.to_screen("Only screen logs while tests are running.");

			return;
		}

		String line = get_line(message_, TIMESTAMP_FILE_CONTENTS);
		if (!strings.is_ok(line)) return;
		
		io.ignore_errors();
		io.line_to_file(path_, line, true);
	}
	
	public static String get_path(String id_, boolean is_error_) { return get_path(id_, is_error_, false); }
	
	public static String get_path(String id_, boolean is_error_, boolean add_timestamp_)
	{
		ArrayList<String> pieces = new ArrayList<String>();

		String dir = get_dir(is_error_);
		if (strings.is_ok(dir)) pieces.add(dir);

		String file = id_ + paths.EXTENSION_LOG;
		if (add_timestamp_) file = dates.add_now_to_string(file, TIMESTAMP_FILE_NAME, true);
		
		pieces.add(file);

		return paths.build(arrays.to_array(pieces), true);
	}

	public static String get_dir(boolean is_error_) { return paths.get_dir(is_error_ ? paths.DIR_LOGS_ERRORS : paths.DIR_LOGS_ACTIVITY); }

	@SuppressWarnings("unchecked")
	public static ArrayList<String> get_generated_paths_new(String id_, boolean are_errors_, long days_from_today_) { return (ArrayList<String>)get_generated_paths_dates_new(id_, are_errors_, days_from_today_, true); }
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> get_generated_paths_new(String id_, boolean are_errors_, LocalDate soonest_) { return (ArrayList<String>)get_generated_paths_dates_new(id_, are_errors_, soonest_, true); }

	@SuppressWarnings("unchecked")
	public static ArrayList<String> get_generated_paths_old(String id_, boolean are_errors_, long days_from_first_) { return (ArrayList<String>)get_generated_paths_dates_old(id_, are_errors_, days_from_first_, true); }
	
	@SuppressWarnings("unchecked")
	public static ArrayList<String> get_generated_paths_old(String id_, boolean are_errors_, LocalDate latest_) { return (ArrayList<String>)get_generated_paths_dates_old(id_, are_errors_, latest_, true); }

	@SuppressWarnings("unchecked")
	public static HashMap<String, LocalDate> get_generated_dates_new(String id_, boolean are_errors_, long days_from_today_) { return (HashMap<String, LocalDate>)get_generated_paths_dates_new(id_, are_errors_, days_from_today_, false); }
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, LocalDate> get_generated_dates_new(String id_, boolean are_errors_, LocalDate soonest_) { return (HashMap<String, LocalDate>)get_generated_paths_dates_new(id_, are_errors_, soonest_, false); }

	@SuppressWarnings("unchecked")
	public static HashMap<String, LocalDate> get_generated_dates_old(String id_, boolean are_errors_, long days_from_first_) { return (HashMap<String, LocalDate>)get_generated_paths_dates_old(id_, are_errors_, days_from_first_, false); }
	
	@SuppressWarnings("unchecked")
	public static HashMap<String, LocalDate> get_generated_dates_old(String id_, boolean are_errors_, LocalDate latest_) { return (HashMap<String, LocalDate>)get_generated_paths_dates_old(id_, are_errors_, latest_, false); }
	
	public static HashMap<String, Object> parse_file_line(String line_) { return dates.get_now_from_string(line_, TIMESTAMP_FILE_CONTENTS, false); }
	
	public static Object get_date_time(HashMap<String, Object> item_) { return dates.get_now_date_time(item_); }

	public static String get_further(HashMap<String, Object> item_) { return dates.get_now_further(item_); }
		
	private static String get_line(String message_, String format_)
	{
		if (!strings.is_ok(message_)) return strings.DEFAULT;
		
		return (strings.is_ok(format_) ? dates.add_now_to_string(message_, format_, false) : message_); 
	}
	
	private static boolean out_is_enabled(String type_) { return config.get_logs_boolean(type_); }
	
	private static Object get_generated_paths_dates_new(String id_, boolean are_errors_, long days_from_today_, boolean is_paths_) { return get_generated_paths_dates_common(id_, are_errors_, (days_from_today_ >= 0 ? dates.get_now_date().minusDays(days_from_today_) : null), is_paths_, true); }
	
	private static Object get_generated_paths_dates_new(String id_, boolean are_errors_, LocalDate soonest_, boolean is_paths_) { return get_generated_paths_dates_common(id_, are_errors_, soonest_, is_paths_, true); }
	
	private static Object get_generated_paths_dates_old(String id_, boolean are_errors_, long days_from_first_, boolean is_paths_) 
	{ 
		Object output = null;
		
		String[] keywords = (strings.is_ok(id_) ? new String[] { id_ } : null);

		String dir = get_dir(are_errors_);

		HashMap<String, LocalDate> dates = paths.get_dates(dir, keywords, true);
		
		LocalDate latest = null;
		
		if (arrays.is_ok(dates))
		{
			LocalDate start = null;
			
			for (Entry<String, LocalDate> item: dates.entrySet())
			{
				LocalDate date = item.getValue();
				
				if (start == null || accessory.dates.is_before(date, start)) start = date;
			}
			
			if (start != null) latest = start.plusDays(days_from_first_);
		}
		
		if (latest == null) output = (is_paths_ ? paths.get_all_files(dir, keywords, true) : dates);
		else output = get_generated_paths_dates_internal(latest, false, dates, is_paths_);
	
		return output;
	}
	
	private static Object get_generated_paths_dates_old(String id_, boolean are_errors_, LocalDate latest_, boolean is_paths_) { return get_generated_paths_dates_common(id_, are_errors_, latest_, is_paths_, false); }
		
	private static Object get_generated_paths_dates_common(String id_, boolean are_errors_, LocalDate soonest_latest_, boolean is_paths_, boolean is_soonest_)
	{
		Object output = null;
		
		String[] keywords = (strings.is_ok(id_) ? new String[] { id_ } : null);

		String dir = get_dir(are_errors_);

		HashMap<String, LocalDate> dates = paths.get_dates(dir, keywords, true);
		
		if (soonest_latest_ == null) output = (is_paths_ ? paths.get_all_files(dir, keywords, true) : dates);
		else output = get_generated_paths_dates_internal(soonest_latest_, is_soonest_, dates, is_paths_);
	
		return output;
	}

	@SuppressWarnings("unchecked")
	private static Object get_generated_paths_dates_internal(LocalDate target_, boolean is_soonest_, HashMap<String, LocalDate> dates_, boolean is_paths_)
	{
		Object output = (is_paths_ ? new ArrayList<String>() : new HashMap<String, LocalDate>());
		if (!arrays.is_ok(dates_)) return output;
		
		for (Entry<String, LocalDate> item: dates_.entrySet())
		{
			String path = item.getKey();
			LocalDate date = item.getValue();
			
			if ((is_soonest_ && dates.is_before(date, target_)) || (!is_soonest_ && dates.is_after(date, target_))) continue;
			
			if (is_paths_) ((ArrayList<String>)output).add(path);
			else ((HashMap<String, LocalDate>)output).put(path, date);
		}
		
		return output;
	}
}