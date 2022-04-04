package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class logs 
{	
	public static final String SOURCE = types.CONFIG_LOGS_DB_SOURCE;
	public static final String FIELD_ID = types.CONFIG_LOGS_DB_FIELD_ID;
	public static final String FIELD_MESSAGE = types.CONFIG_LOGS_DB_FIELD_MESSAGE;
	
	public static final String SCREEN = types.CONFIG_LOGS_OUT_SCREEN;
	public static final String FILE = types.CONFIG_LOGS_OUT_FILE;
	public static final String DB = types.CONFIG_LOGS_OUT_DB;
	public static final String OUT_SCREEN = SCREEN;
	public static final String OUT_FILE = FILE;
	public static final String OUT_DB = DB;
	
	public static final String DEFAULT_TABLE = _defaults.LOGS_DB_TABLE;
	public static final String DEFAULT_COL_ID = _defaults.LOGS_DB_COL_ID;
	public static final String DEFAULT_COL_MESSAGE = _defaults.LOGS_DB_COL_MESSAGE;
	
	public static final boolean DEFAULT_SCREEN = _defaults.LOGS_SCREEN;
	public static final boolean DEFAULT_FILE = _defaults.LOGS_FILE;
	public static final boolean DEFAULT_DB = _defaults.LOGS_DB;
	
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
		
		if (out_is_ok(DB)) 
		{
			String type = types.CONFIG_DB_SETUP;
			String current = config.get_db(type);
			
			config.update_db(type, types.CONFIG_DB_SETUP_LOGS);
			
			update_db(message_, id);
			
			config.update_db(type, current);
		}
	}

	public static HashMap<String, Boolean> change_conn_info(HashMap<String, String> params_)
	{
		return config.update_db_conn_info(params_, types.CONFIG_LOGS_DB);
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

	public static void update_db(String message_, String id_)
	{
		String message = strings.remove_escape_many(new String[] { "'", "\"" }, message_, true);
		if (!strings.is_ok(message)) return;
		
		String source = types.CONFIG_LOGS_DB_SOURCE;
		HashMap<String, String> vals = db.adapt_inputs(source, null, logs.FIELD_ID, id_);
		if (!arrays.is_ok(vals)) return;
		
		vals = db.adapt_inputs(source, vals, logs.FIELD_MESSAGE, message);
		if (!arrays.is_ok(vals)) return;
		
		db.insert(db.get_table(source), vals);
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