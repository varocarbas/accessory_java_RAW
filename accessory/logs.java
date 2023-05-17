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
	public static final String TIMESTAMP_FILE_NAME = dates.FORMAT_TIMESTAMP;
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

	public static ArrayList<String> get_generated_paths(String id_, boolean are_errors_) { return get_generated_paths(id_, are_errors_, null); }

	public static ArrayList<String> get_generated_paths(String id_, boolean are_errors_, long max_days_before_) { return get_generated_paths(id_, are_errors_, (max_days_before_ >= 0 ? dates.get_now_date().minusDays(max_days_before_) : null)); }
	
	public static ArrayList<String> get_generated_paths(String id_, boolean are_errors_, LocalDate soonest_)
	{
		String[] keywords = (strings.is_ok(id_) ? new String[] { id_ } : null);

		String dir = get_dir(are_errors_);
		
		if (soonest_ == null) return paths.get_all_files(dir, keywords);
		
		ArrayList<String> output = new ArrayList<String>();
		
		HashMap<String, LocalDate> all = paths.get_dates(dir, keywords);
		if (!arrays.is_ok(all)) return output;

		for (Entry<String, LocalDate> item: all.entrySet())
		{
			if (dates.is_before(item.getValue(), soonest_)) continue;
			
			output.add(item.getKey());
		}
		
		return output;
	}
	
	public static HashMap<String, Object> parse_file_line(String line_) { return dates.get_now_from_string(line_, TIMESTAMP_FILE_CONTENTS, true); }
	
	public static Object get_date_time(HashMap<String, Object> item_) { return dates.get_now_date_time(item_); }

	public static String get_further(HashMap<String, Object> item_) { return dates.get_now_further(item_); }
		
	private static String get_line(String message_, String format_)
	{
		if (!strings.is_ok(message_)) return strings.DEFAULT;
		
		return (strings.is_ok(format_) ? dates.add_now_to_string(message_, format_, false) : message_); 
	}
	
	private static boolean out_is_enabled(String type_) { return config.get_logs_boolean(type_); }
}