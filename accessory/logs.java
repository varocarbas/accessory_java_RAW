package accessory;

import java.util.ArrayList;

public abstract class logs 
{		
	public static final String SCREEN = types.CONFIG_LOGS_OUT_SCREEN;
	public static final String FILE = types.CONFIG_LOGS_OUT_FILE;
	public static final String OUT_SCREEN = SCREEN;
	public static final String OUT_FILE = FILE;
	
	public static final String DEFAULT_TABLE = _defaults.LOGS_DB_TABLE;
	public static final String DEFAULT_COL_ID = _defaults.LOGS_DB_COL_ID;
	public static final String DEFAULT_COL_MESSAGE = _defaults.LOGS_DB_COL_MESSAGE;
	
	public static final boolean DEFAULT_SCREEN = _defaults.LOGS_SCREEN;
	public static final boolean DEFAULT_FILE = _defaults.LOGS_FILE;
	
	static { ini.load(); }

	public static void update(String message_, String id_)
	{
		if (!strings.is_ok(message_)) return;

		String id = id_;
		if (!strings.is_ok(id)) id = config.get_basic(types.CONFIG_BASIC_NAME);

		if (tests._running || out_is_ok(SCREEN)) update_screen(message_);
		
		if (tests._running)
		{	
			System.out.println("Only screen logs while tests are running.");
			
			return;
		}
		
		if (out_is_ok(FILE)) update_file(message_, id);
	}

	public static void update_screen(String message_)
	{
		String message = message_;
		if (!strings.is_ok(message)) return;

		System.out.println(message);
	}

	public static void update_file(String message_, String id_)
	{
		String message = message_;
		if (!strings.is_ok(message)) return;

		io.line_to_file(get_path(id_), dates.add_timestamp(message, null, false), true, false);
	}

	private static String get_path(String id_)
	{
		String file = id_;
		file += paths.EXTENSION_LOG;

		ArrayList<String> pieces = new ArrayList<String>();

		String dir = paths.get_dir(paths.DIR_LOGS);
		if (strings.is_ok(dir)) pieces.add(dir);
		pieces.add(file);

		return paths.build(arrays.to_array(pieces), true);
	}

	private static boolean out_is_ok(String type_)
	{
		return strings.to_boolean(config.get_logs(type_));
	}
}