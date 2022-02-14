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
		
		if (out_is_ok(types._CONFIG_LOGS_OUT_CONSOLE)) update_console(message_);
		if (out_is_ok(types._CONFIG_LOGS_OUT_FILE)) update_file(message_, id);
		if (out_is_ok(types._CONFIG_LOGS_OUT_DB)) update_db(message_, id);
	}
	
	public static HashMap<String, Boolean> change_conn_info(HashMap<String, String> params_)
	{
    	return _config.update_conn_info(params_, types._CONFIG_LOGS_SQL);
	}
	
	private static void update_console(String message_)
	{
		String message = message_;
		if (!strings.is_ok(message)) return;
		
		System.out.println(message);
	}
	
	private static void update_file(String message_, String id_)
	{
		String message = message_;
		if (!strings.is_ok(message)) return;
		
		String path = get_path(id_);
		
		io.line_to_file(path, message, true, null, false);
	}
	
	private static void update_db(String message_, String id_)
	{
		String message = message_;
		if (!strings.is_ok(message)) return;
		
		String table = _config.get_logs(types._CONFIG_LOGS_SQL_TABLE);
		String col_id = _config.get_logs(types._CONFIG_LOGS_SQL_COL_ID);
		String col_message = _config.get_logs(types._CONFIG_LOGS_SQL_COL_MESSAGE);
		if (!strings.is_ok(table) || !strings.is_ok(col_id) || !strings.is_ok(col_message)) return;
		
		HashMap<String, String> vals = new HashMap<String, String>();
		vals.put(col_id, id_);
		vals.put(col_message, message);
		
		sql.insert(table, vals);
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