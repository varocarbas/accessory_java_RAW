package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class logs extends parent_static
{	
	public static final String SCREEN = types.CONFIG_LOGS_OUT_SCREEN;
	public static final String FILE = types.CONFIG_LOGS_OUT_FILE;
	public static final String OUT_SCREEN = SCREEN;
	public static final String OUT_FILE = FILE;

	public static final boolean DEFAULT_SCREEN = _defaults.LOGS_SCREEN;
	public static final boolean DEFAULT_FILE = _defaults.LOGS_FILE;

	public static String get_class_id() { return types.get_id(types.ID_LOGS); }

	public static void update_activity(HashMap<String, String> inputs_, String id_) { update_file(arrays.to_string(inputs_, misc.SEPARATOR_ITEM, misc.SEPARATOR_KEYVAL, null), id_, false); }

	public static void update(String message_, String id_, boolean is_error_)
	{
		if (!strings.is_ok(message_)) return;

		if (parent_tests.is_running() || out_is_enabled(SCREEN)) update_screen(message_);

		if (parent_tests.is_running())
		{	
			generic.to_screen("Only screen logs while tests are running.");

			return;
		}

		if (out_is_enabled(FILE)) update_file(message_, id_, is_error_);
	}

	public static void update_screen(String message_)
	{
		String message = message_;
		if (!strings.is_ok(message)) return;

		generic.to_screen(message);
	}

	public static void update_file(String message_, String id_, boolean is_error_)
	{
		String id = id_;
		if (!strings.is_ok(id)) id = config.get_basic(types.CONFIG_BASIC_NAME);

		String message = message_;
		if (!strings.is_ok(message)) return;

		io.ignore_errors();
		io.line_to_file(get_path(id, is_error_), dates.add_timestamp(message, false), true);
	}

	private static String get_path(String id_, boolean is_error_)
	{
		ArrayList<String> pieces = new ArrayList<String>();

		String dir = paths.get_dir(is_error_ ? paths.DIR_LOGS_ERRORS : paths.DIR_LOGS_ACTIVITY);
		if (strings.is_ok(dir)) pieces.add(dir);

		String file = id_ + paths.EXTENSION_LOG;
		pieces.add(file);

		return paths.build(arrays.to_array(pieces), true);
	}

	private static boolean out_is_enabled(String type_) { return strings.to_boolean(config.get_logs(type_)); }
}