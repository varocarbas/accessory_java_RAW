package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class logs 
{
	static { ini.load(); }

	public static void update(String message_, String id_)
	{
		if (!strings.is_ok(message_)) return;

		String id = id_;
		if (!strings.is_ok(id)) id = _config.get_basic(types._CONFIG_BASIC_NAME);

		if (tests._running || out_is_ok(types._CONFIG_LOGS_OUT_SCREEN)) update_screen(message_);
		
		if (tests._running)
		{	
			System.out.println("Only screen logs while tests are running.");
			
			return;
		}
		
		if (out_is_ok(types._CONFIG_LOGS_OUT_FILE)) update_file(message_, id);
		
		if (out_is_ok(types._CONFIG_LOGS_OUT_DB)) 
		{
			String type = types._CONFIG_DB_SETUP;
			String current = _config.get_db(type);
			
			_config.update_db(type, types._CONFIG_DB_SETUP_LOGS);
			
			update_db(message_, id);
			
			_config.update_db(type, current);
		}
	}

	public static HashMap<String, Boolean> change_conn_info(HashMap<String, String> params_)
	{
		return _config.update_db_conn_info(params_, types._CONFIG_LOGS_DB);
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

		String path = get_path(id_);

		io.line_to_file(path, message, true, null, false);
	}

	public static void update_db(String message_, String id_)
	{
		String message = strings.remove_escape_many(new String[] { "'", "\"" }, message_, true);
		if (!strings.is_ok(message)) return;
		
		String source = types._CONFIG_LOGS_DB_SOURCE;
		HashMap<String, String> vals = db.adapt_inputs(source, null, types._CONFIG_LOGS_DB_FIELD_ID, id_);
		if (!arrays.is_ok(vals)) return;
		
		vals = db.adapt_inputs(source, vals, types._CONFIG_LOGS_DB_FIELD_MESSAGE, message);
		if (!arrays.is_ok(vals)) return;
		
		db.insert(db.get_table(source), vals);
	}

	private static String get_path(String id_)
	{
		String file = id_;
		file += paths.EXTENSION_LOG;

		ArrayList<String> pieces = new ArrayList<String>();

		String dir = _config.get_basic(types._CONFIG_LOGS_DIR);
		if (strings.is_ok(dir)) pieces.add(dir);
		pieces.add(file);

		return paths.build(pieces, true);
	}

	private static boolean out_is_ok(String type_)
	{
		String type = types.check_aliases(type_);

		return strings.to_boolean(_config.get_logs(type));
	}
}