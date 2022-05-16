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
	public abstract long get_default_size(String data_type_);
	public abstract long get_max_size(String data_type_);
	public abstract String get_value(String input_);
	public abstract String get_variable(String input_);
	
	protected abstract Connection connect_internal(String source_, Properties properties);
	
	private boolean _is_ok = false;
	
	public boolean is_ok() { return _is_ok; }
	public void is_ok(boolean is_ok_) { _is_ok = is_ok_; }
	
	public Connection connect(String source_, Properties properties_)
	{
		_is_ok = false;
		
		Connection output = connect_internal(source_, properties_);
		if (output != null) _is_ok = true;
		
		return output;
	}
}