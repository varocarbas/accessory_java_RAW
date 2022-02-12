  package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class types 
{
	public static final String SEPARATOR = misc.SEPARATOR_NAME;
	
	//--- To be synced with get_all_subtypes().

	//--- To be synced with the corresponding _ini and _config methods/variables.
	public static final String _CONFIG_SQL = "_config_sql";
	public static final String _CONFIG_SQL_TYPE = "_config_sql_type";
	public static final String _CONFIG_SQL_MAX_POOL = "_config_sql_max_pool";
	public static final String _CONFIG_SQL_DB = "_config_sql_db";
	public static final String _CONFIG_SQL_HOST = "_config_sql_host";
	public static final String _CONFIG_SQL_USER = "_config_sql_user";
	public static final String _CONFIG_SQL_ERROR_EXIT = "_config_sql_error_exit";
	public static final String _CONFIG_SQL_CREDENTIALS = "_config_sql_credentials";
	public static final String _CONFIG_SQL_CREDENTIALS_TYPE = "_config_sql_credentials_type";
	public static final String _CONFIG_SQL_CREDENTIALS_WHERE = "_config_sql_credentials_where";
	public static final String _CONFIG_SQL_CREDENTIALS_ENCRYPTED = "_config_sql_credentials_encrypted";
	public static final String _CONFIG_SQL_CREDENTIALS_USERNAME = "_config_sql_credentials_username";
	public static final String _CONFIG_SQL_CREDENTIALS_PASSWORD = "_config_sql_credentials_password";

	public static final String _CONFIG_CREDENTIALS = "_config_credentials";
	public static final String _CONFIG_CREDENTIALS_ENCRYPTED = "_config_credentials_encrypted";
	public static final String _CONFIG_CREDENTIALS_WHERE = "_config_credentials_where";
	public static final String _CONFIG_CREDENTIALS_WHERE_DB = "_config_credentials_where_db";
	public static final String _CONFIG_CREDENTIALS_WHERE_FILE = "_config_credentials_where_file"; 
	public static final String _CONFIG_CREDENTIALS_FILE = "_config_credentials_file";
	public static final String _CONFIG_CREDENTIALS_FILE_DIR = "_config_credentials_file_dir";
	public static final String _CONFIG_CREDENTIALS_FILE_EXTENSION = "_config_credentials_file_extension";
	public static final String _CONFIG_CREDENTIALS_FILE_SEPARATOR = "_config_credentials_file_separator";
	public static final String _CONFIG_CREDENTIALS_FILE_USERNAME = "_config_credentials_file_username";
	public static final String _CONFIG_CREDENTIALS_FILE_PASSWORD = "_config_credentials_file_password";
	public static final String _CONFIG_CREDENTIALS_FILE_ENCRYPTED = "_config_credentials_file_encrypted";
	
	public static final String _CONFIG_BASIC = "_config_basic";
	public static final String _CONFIG_BASIC_NAME = "_config_basic_name";
	public static final String _CONFIG_BASIC_DIR_APP = "_config_basic_dir_app";
	public static final String _CONFIG_BASIC_DIR_INI = "_config_basic_dir_ini";
	
	public static final String _CONFIG_LOGS = "_config_logs";
	public static final String _CONFIG_LOGS_DIR = "_config_logs_dir";
	public static final String _CONFIG_LOGS_SQL = "_config_logs_sql";
	public static final String _CONFIG_LOGS_SQL_TABLE = "_config_logs_sql_table";
	public static final String _CONFIG_LOGS_SQL_COL_ID = "_config_logs_sql_col_id";
	public static final String _CONFIG_LOGS_SQL_COL_MESSAGE = "_config_logs_sql_col_message";
	public static final String _CONFIG_LOGS_OUT = "_config_logs_out";
	public static final String _CONFIG_LOGS_OUT_CONSOLE = "_config_logs_out_console";
	public static final String _CONFIG_LOGS_OUT_FILE = "_config_logs_out_file";
	public static final String _CONFIG_LOGS_OUT_DB = "_config_logs_out_db";
	//------
	
	public static final String SQL = "sql";
	public static final String SQL_MYSQL = "sql_mysql";
	
	public static final String ERROR_SQL = "error_sql";
	public static final String ERROR_SQL_INFO = "error_sql_info";
	public static final String ERROR_SQL_CREDENTIALS = "error_sql_credentials";
	public static final String ERROR_SQL_TYPE = "error_sql_type";
	public static final String ERROR_SQL_CONN = "error_sql_conn";
	public static final String ERROR_SQL_QUERY = "error_sql_query";

	public static final String ERROR_FILE = "error_file";
	public static final String ERROR_FILE_WRITE = "error_file_write";
	public static final String ERROR_FILE_READ = "error_file_read";
	
	//---------------------------
	
	private static HashMap<String, String> aliases = new HashMap<String, String>();
	
	static { _ini.load(); }
	
	public static String check_aliases(String candidate_)
	{
		if (!strings.is_ok(candidate_)) return strings.DEFAULT;
		
		for (Entry<String, String> item: aliases.entrySet())
		{
			if (candidate_.equals(item.getKey())) return item.getValue();
		}
		
		return candidate_;
	}

	public static boolean update_aliases(String alias_, String type_)
	{
		if (!strings.is_ok(alias_) || !strings.is_ok(type_)) return false;
		
		aliases.put(alias_, type_);
		
		return true;
	}
	
	public static String check_subtype(String subtype_, String[] subtypes_, String add_remove_, String type_add_remove_)
	{
		String subtype0 = check_aliases(subtype_);
		String type_add_remove = check_aliases(type_add_remove_);
		
		for (String subtype: get_subtypes(strings.DEFAULT, subtypes_))
		{
			String output = subtype0;
			String subtype2 = strings.DEFAULT;
			
			if (strings.are_equivalent(add_remove_, keys.ADD)) 
			{
				subtype2 = add_type(subtype0, type_add_remove);
			}
			else if (strings.are_equivalent(add_remove_, keys.REMOVE)) 
			{
				subtype2 = remove_type(subtype, type_add_remove);
			}
			
			if 
			(
				strings.are_equivalent(subtype, subtype0) ||
				strings.are_equivalent(subtype2, subtype0) ||
				strings.are_equivalent(subtype2, subtype)
			)
			{ return output; }
		}
		
		return strings.DEFAULT;
	}
	
	public static String remove_type(String subtype_, String type_)
	{
		if (!strings.is_ok(subtype_) || !strings.is_ok(type_)) return subtype_;
		
		String subtype = check_aliases(subtype_);
		String type = check_aliases(type_);
		type += SEPARATOR;
		
		return strings.get_end(subtype, type.length());
	}
	
	public static String add_type(String subtype_, String type_)
	{
		if (!strings.is_ok(subtype_) || !strings.is_ok(type_)) return subtype_;
		
		String subtype = check_aliases(subtype_);
		String type = check_aliases(type_);

		return (type + SEPARATOR + subtype);
	}

	public static String[] get_subtypes(String[] types_, String[] all_)
	{
		if (!arrays.is_ok(types_)) return get_subtypes(strings.DEFAULT, all_);
	
		ArrayList<String> subtypes = new ArrayList<String>();
		
		for (String type: types_)
		{
			subtypes.addAll(arrays.to_arraylist(get_subtypes(check_aliases(type), all_)));		
		}
		
		return arrays.to_array(subtypes);
	}
	
	public static String[] get_subtypes(String type_, String[] all_)
	{
		String type = check_aliases(type_);
		
		ArrayList<String> subtypes = new ArrayList<String>();
		String heading = (strings.is_ok(type) ? type + SEPARATOR : null);

		for (String subtype: (arrays.is_ok(all_) ? all_ : get_all_subtypes()))
		{
			String subtype2 = check_aliases(subtype);
			if (strings.contains_start(heading, subtype2, false)) subtypes.add(subtype2);
		}
			
		return arrays.to_array(subtypes);
	}
	
	private static String[] get_all_subtypes()
	{
		return new String[]
		{			
			//_CONFIG_SQL
			_CONFIG_SQL_TYPE, _CONFIG_SQL_MAX_POOL, _CONFIG_SQL_DB, _CONFIG_SQL_HOST,  
			_CONFIG_SQL_USER, _CONFIG_SQL_ERROR_EXIT, 
			//_CONFIG_SQL_CREDENTIALS
			_CONFIG_SQL_CREDENTIALS_TYPE, _CONFIG_SQL_CREDENTIALS_ENCRYPTED, 
			_CONFIG_SQL_CREDENTIALS_USERNAME, _CONFIG_SQL_CREDENTIALS_PASSWORD,
			_CONFIG_SQL_CREDENTIALS_WHERE,
	
			//_CONFIG_CREDENTIALS
			_CONFIG_CREDENTIALS_ENCRYPTED, 
			//_CONFIG_CREDENTIALS_WHERE
			_CONFIG_CREDENTIALS_WHERE_FILE, _CONFIG_CREDENTIALS_WHERE_DB,
			//_CONFIG_CREDENTIALS_FILE
			_CONFIG_CREDENTIALS_FILE_DIR, _CONFIG_CREDENTIALS_FILE_EXTENSION, 
			_CONFIG_CREDENTIALS_FILE_SEPARATOR, _CONFIG_CREDENTIALS_FILE_USERNAME, 
			_CONFIG_CREDENTIALS_FILE_PASSWORD, _CONFIG_CREDENTIALS_FILE_ENCRYPTED,
			
			//_CONFIG_BASIC
			_CONFIG_BASIC_NAME, _CONFIG_BASIC_DIR_APP, _CONFIG_BASIC_DIR_INI,
			
			//_CONFIG_LOGS
			_CONFIG_LOGS_DIR, 
			//_CONFIG_LOGS_OUT
			_CONFIG_LOGS_OUT_CONSOLE, _CONFIG_LOGS_OUT_FILE, _CONFIG_LOGS_OUT_DB,
			//_CONFIG_LOGS_SQL
			_CONFIG_LOGS_SQL_TABLE,

			//ERROR_SQL
			ERROR_SQL_TYPE, ERROR_SQL_CONN, ERROR_SQL_QUERY, ERROR_SQL_INFO, ERROR_SQL_CREDENTIALS,
			
			//ERROR_FILE
			ERROR_FILE_WRITE, ERROR_FILE_READ,
			
			//SQL
			SQL_MYSQL
		};		
	}
}