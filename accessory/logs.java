package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class logs extends parent_static
{	
	public static final String CONFIG_OUT_SCREEN = _types.CONFIG_LOGS_OUT_SCREEN;
	public static final String CONFIG_OUT_FILE = _types.CONFIG_LOGS_OUT_FILE;
	public static final String CONFIG_ERRORS_TIMESTAMP = _types.CONFIG_LOGS_ERRORS_TIMESTAMP;
	
	public static final String SCREEN = CONFIG_OUT_SCREEN;
	public static final String FILE = CONFIG_OUT_FILE;
	public static final String OUT_SCREEN = SCREEN;
	public static final String OUT_FILE = FILE;

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
		String message = get_message(message_, (add_timestamp_ ? dates.FORMAT_TIME_FULL : null));
		if (!strings.is_ok(message)) return;

		System.out.println(message);
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

		String message = get_message(message_, dates.FORMAT_TIMESTAMP);
		if (!strings.is_ok(message)) return;
		
		io.ignore_errors();
		io.line_to_file(path_, message, true);
	}

	public static String get_path(String id_, boolean is_error_) { return get_path(id_, is_error_, false); }
	
	public static String get_path(String id_, boolean is_error_, boolean add_timestamp_)
	{
		ArrayList<String> pieces = new ArrayList<String>();

		String dir = paths.get_dir(is_error_ ? paths.DIR_LOGS_ERRORS : paths.DIR_LOGS_ACTIVITY);
		if (strings.is_ok(dir)) pieces.add(dir);

		String file = id_ + paths.EXTENSION_LOG;
		if (add_timestamp_) file = dates.add_now_to_string(file, dates.FORMAT_DATE, true);
		
		pieces.add(file);

		return paths.build(arrays.to_array(pieces), true);
	}

	private static String get_message(String message_, String format_) 
	{
		String message = message_;
		if (!strings.is_ok(message)) return strings.DEFAULT;
		
		return (strings.is_ok(format_) ? dates.add_now_to_string(message, format_, false) : message); 
	}
	
	private static boolean out_is_enabled(String type_) { return config.get_logs_boolean(type_); }
}