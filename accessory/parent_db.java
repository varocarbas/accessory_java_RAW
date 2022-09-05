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
	public abstract double get_max_value(String data_type_);
	public abstract String get_value(String input_);
	public abstract String get_variable(String input_);
	public abstract String get_select_count_col();
	public abstract boolean table_exists(String table_name_);
	public abstract void drop_table(String table_name_);
	public abstract void create_table(String table_name_, HashMap<String, db_field> cols_);
	public abstract void create_table_like(String table_name_, String table_like_name_);
	public abstract void backup_table(String table_source_, String table_backup_);
	public abstract void backup_db_to_file(String any_source_);
	public abstract void restore_db_from_file(String any_source_);
	public abstract String get_db_backup_path(String any_source_);
	public abstract String get_db_restore_path(String any_source_);
	
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
	
	public static Connection connect_static(String type_, String source_, Properties properties_, String db_name_, String host_)
	{
		db.is_ok(false);
		
		Connection output = null;
		
		if (strings.are_equal(type_, db.MYSQL)) output = db_mysql.connect_internal_static(source_, properties_, db_name_, host_);

		if (output != null) db.is_ok(true);
		
		return output;		
	}
}