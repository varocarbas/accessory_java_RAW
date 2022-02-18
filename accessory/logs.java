package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public class logs 
{
	static { _ini.load(); }

	public static void update(String message_, String id_)
	{
		if (!strings.is_ok(message_)) return;

		String id = id_;
		if (!strings.is_ok(id)) id = _config.get_basic(types._CONFIG_BASIC_NAME);

		if (out_is_ok(types._CONFIG_LOGS_OUT_SCREEN)) update_screen(message_);
		
		if (out_is_ok(types._CONFIG_LOGS_OUT_FILE)) update_file(message_, id);
		
		if (out_is_ok(types._CONFIG_LOGS_OUT_DB)) 
		{
			db.update_config_type(types._CONFIG_LOGS);
			update_db(message_, id);
			db.update_config_type(types._CONFIG_DB);
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
		String message = message_;
		if (!strings.is_ok(message)) return;

		String table = types._CONFIG_LOGS_DB_TABLE;
		HashMap<String, String> vals = db.get_table_vals(table, null, types._CONFIG_LOGS_DB_COL_ID, id_);
		if (!arrays.is_ok(vals)) return;
		
		vals = db.get_table_vals(table, vals, types._CONFIG_LOGS_DB_COL_MESSAGE, message);
		if (!arrays.is_ok(vals)) return;
		
		db.insert(db.get_table_name(table), vals);
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