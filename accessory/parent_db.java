package accessory;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

public abstract class parent_db
{	
	public abstract ArrayList<HashMap<String, String>> execute_query(String source_, String query_);
	public abstract String sanitise_string(String input_);
	public abstract ArrayList<HashMap<String, String>> execute(String source_, String type_, String[] cols_, HashMap<String, String> vals_, String where_, int max_rows_, String order_, HashMap<String, db_field> cols_info_);
	public abstract HashMap<String, Object> get_data_type(String data_type_);
	public abstract int get_default_size(String data_type_);
	public abstract int get_max_size(String data_type_);
	public abstract String get_value(String input_);
	public abstract String get_variable(String input_);
	protected abstract Connection connect_internal(String source_, Properties properties);
	
	public boolean _is_ok = false;

	public Connection connect(String source_, Properties properties_)
	{
		_is_ok = false;
		
		return connect_internal(source_, properties_);
	}
}