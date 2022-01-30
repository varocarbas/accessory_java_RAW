package accessory;

import java.util.ArrayList;

public class types 
{
	static { _ini.load(); }
	
	private static final String SEPARATOR = misc.SEPARATOR_NAME;
	
	//--- To be synced with get_all_subtypes().

	//--- To be synced with the corresponding _ini and _config methods/variables.
	public static final String _CONFIG_SQL = "_config_sql";
	public static final String _CONFIG_SQL_TYPE = "_config_sql_type";
	public static final String _CONFIG_SQL_MAX_POOL = "_config_sql_max_pool";
	public static final String _CONFIG_SQL_DB = "_config_sql_db";
	public static final String _CONFIG_SQL_HOST = "_config_sql_host";
	public static final String _CONFIG_SQL_USER = "_config_sql_user";
	public static final String _CONFIG_SQL_ERROR_EXIT = "_config_sql_error_exit";
	public static final String _CONFIG_SQL_CREDENTIALS_TYPE = "_config_sql_credentials_type";
	public static final String _CONFIG_SQL_CREDENTIALS_WHERE = "_config_sql_credentials_where";
	public static final String _CONFIG_SQL_CREDENTIALS_ENCRYPTED = "_config_sql_credentials_encrypted";
	public static final String _CONFIG_SQL_CREDENTIALS_USERNAME = "_config_sql_credentials_username";
	public static final String _CONFIG_SQL_CREDENTIALS_PASSWORD = "_config_sql_credentials_password";

	public static final String _CONFIG_BASIC = "_config_basic";
	public static final String _CONFIG_BASIC_NAME = "_config_basic_name";
	public static final String _CONFIG_BASIC_DIR_APP = "_config_basic_dir_app";
	public static final String _CONFIG_BASIC_DIR_INI = "_config_basic_dir_ini";
	public static final String _CONFIG_BASIC_DIR_ERRORS = "_config_basic_dir_errors";
	public static final String _CONFIG_BASIC_DIR_CREDENTIALS = "_config_basic_dir_credentials";
	//---------
	
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

	public static String check_subtype(String subtype_, String[] subtypes_, String add_remove_, String type_add_remove_)
	{
		for (String subtype: get_subtypes(null, subtypes_))
		{
			String output = subtype;
			String subtype2 = strings.get_default();
			
			if (strings.are_equivalent(add_remove_, keys.ADD)) 
			{
				subtype2 = add_type(subtype_, type_add_remove_);
			}
			else if (strings.are_equivalent(add_remove_, keys.REMOVE)) 
			{
				subtype2 = remove_type(subtype, type_add_remove_);
			}
			
			if 
			(
				strings.are_equivalent(subtype, subtype_) ||
				strings.are_equivalent(subtype2, subtype_) ||
				strings.are_equivalent(subtype2, subtype)
			)
			{ return output; }
		}
		
		return strings.get_default();
	}
	
	public static String remove_type(String subtype_, String type_)
	{
		if (!strings.is_ok(type_)) return subtype_;
		
		String type = type_ + SEPARATOR;
		
		return strings.get_end(subtype_, type.length());
	}
	
	public static String add_type(String subtype_, String type_)
	{
		if (!strings.is_ok(type_)) return subtype_;

		return (type_ + SEPARATOR + subtype_);
	}

	public static String[] get_subtypes(String type_, String[] all_)
	{
		ArrayList<String> subtypes = new ArrayList<String>();
		String heading = (strings.is_ok(type_) ? type_ + SEPARATOR : null);
		
		for (String subtype: (arrays.is_ok(all_) ? all_ : get_all_subtypes()))
		{
			if (heading == null || strings.contains_start(heading, subtype, false)) subtypes.add(subtype);
		}
			
		return arrays.to_array(subtypes);
	}
	
	static String[] get_all_subtypes()
	{
		return new String[]
		{			
			//_CONFIG_SQL
			_CONFIG_SQL_TYPE, _CONFIG_SQL_MAX_POOL, _CONFIG_SQL_DB, _CONFIG_SQL_HOST,  
			_CONFIG_SQL_USER, _CONFIG_SQL_ERROR_EXIT, _CONFIG_SQL_CREDENTIALS_TYPE,
			_CONFIG_SQL_CREDENTIALS_WHERE, _CONFIG_SQL_CREDENTIALS_ENCRYPTED, 
			_CONFIG_SQL_CREDENTIALS_USERNAME, _CONFIG_SQL_CREDENTIALS_PASSWORD,
			
			//_CONFIG_BASIC
			_CONFIG_BASIC_NAME, _CONFIG_BASIC_DIR_APP, _CONFIG_BASIC_DIR_ERRORS, _CONFIG_BASIC_DIR_INI,
			
			//ERROR_SQL
			ERROR_SQL_TYPE, ERROR_SQL_CONN, ERROR_SQL_QUERY, ERROR_SQL_INFO, ERROR_SQL_CREDENTIALS,
			
			//ERROR_FILE
			ERROR_FILE_WRITE, ERROR_FILE_READ,
			
			//SQL
			SQL_MYSQL
		};		
	}
}