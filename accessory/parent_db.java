package accessory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public abstract class parent_db extends parent
{
	//--- Unused abstract methods.
	public String toString() { return strings.DEFAULT; }
	//---
	
	protected abstract ArrayList<HashMap<String, String>> execute_query_type(String type_, String query_);
	public abstract String sanitise_string(String input_);
	public abstract ArrayList<HashMap<String, String>> execute(String type_, String table_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_);
	public abstract HashMap<String, Object> get_data_type(String data_type_);
	public abstract int get_default_size(String data_type_);
	public abstract int get_max_size(String data_type_);
	public abstract String get_value(String input_);
	public abstract String get_variable(String input_);
	public abstract Connection connect(Properties properties);
	
	public boolean is_ok() { return _is_ok; }
	
	public void update_is_ok(boolean is_ok_)
	{
		this._is_ok = is_ok_;
		db._is_ok = is_ok_;
	}
	
	public ArrayList<HashMap<String, String>> execute_query(String query_)
	{
		start_method();
		
		String type = strings.DEFAULT;
		
		String[] temp = strings.split(" ", query_, true);
		if (arrays.get_size(temp) >= 2) type = db.check_type(temp[0]);

		if (!strings.is_ok(type))
		{
			db.manage_error(types.ERROR_DB_QUERY, query_, null, null);
			
			return null;
		}
		
		return execute_query_type(type, query_);
	}
	
	private void start_method()
	{
		update_is_ok(false);
	}
}
