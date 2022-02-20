package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class types 
{
	public static final String SEPARATOR = misc.SEPARATOR_NAME;

	//------ To be synced with get_all_subtypes().

	//--- To be synced with the corresponding _ini and _config methods/variables.

	//Note for DB types: the sources/fields are the types/ids, constant, used in most of the code. 
	//The tables/cols are the values, variable, only used when performing the corresponding query.
	
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
	public static final String _CONFIG_LOGS_DB = "_config_logs_db";
	public static final String _CONFIG_LOGS_DB_SOURCE = "_config_logs_db_source";
	public static final String _CONFIG_LOGS_DB_FIELD_ID = "_config_logs_db_field_id";
	public static final String _CONFIG_LOGS_DB_FIELD_MESSAGE = "_config_logs_db_field_message";
	public static final String _CONFIG_LOGS_OUT = "_config_logs_out";
	public static final String _CONFIG_LOGS_OUT_SCREEN = "_config_logs_out_screen";
	public static final String _CONFIG_LOGS_OUT_FILE = "_config_logs_out_file";
	public static final String _CONFIG_LOGS_OUT_DB = "_config_logs_out_db";

	public static final String _CONFIG_DB = "_config_db";
	public static final String _CONFIG_DB_MAX_POOL = "_config_db_max_pool";
	public static final String _CONFIG_DB_NAME = "_config_db_name";
	public static final String _CONFIG_DB_HOST = "_config_db_host";
	public static final String _CONFIG_DB_USER = "_config_db_user";
	public static final String _CONFIG_DB_ERROR_EXIT = "_config_db_error_exit";
	public static final String _CONFIG_DB_SETUP = "_config_db_setup";
	public static final String _CONFIG_DB_SETUP_MAIN = _CONFIG_DB;
	public static final String _CONFIG_DB_SETUP_LOGS = _CONFIG_LOGS;
	public static final String _CONFIG_DB_TYPE = "_config_db_type";
	public static final String _CONFIG_DB_TYPE_MYSQL = "_config_db_type_mysql";
	public static final String _CONFIG_DB_CREDENTIALS = "_config_db_credentials";
	public static final String _CONFIG_DB_CREDENTIALS_TYPE = "_config_db_credentials_type";
	public static final String _CONFIG_DB_CREDENTIALS_WHERE = "_config_db_credentials_where";
	public static final String _CONFIG_DB_CREDENTIALS_ENCRYPTED = "_config_db_credentials_encrypted";
	public static final String _CONFIG_DB_CREDENTIALS_USERNAME = "_config_db_credentials_username";
	public static final String _CONFIG_DB_CREDENTIALS_PASSWORD = "_config_db_credentials_password";
	public static final String _CONFIG_DB_FIELDS_DEFAULT = "_config_db_fields_default";
	public static final String _CONFIG_DB_FIELDS_DEFAULT_ID = "_config_db_fields_default_id";
	public static final String _CONFIG_DB_FIELDS_DEFAULT_TIMESTAMP = "_config_db_fields_default_timestamp";
	//------

	public static final String DATA = "data";
	public static final String DATA_STRING = "data_string";
	public static final String DATA_INTEGER = "data_integer";
	public static final String DATA_DECIMAL = "data_decimal";
	public static final String DATA_BOOLEAN = "data_boolean";

	public static final String ERROR_DB = "error_db";
	public static final String ERROR_DB_INFO = "error_db_info";
	public static final String ERROR_DB_CREDENTIALS = "error_db_credentials";
	public static final String ERROR_DB_TYPE = "error_db_type";
	public static final String ERROR_DB_CONN = "error_db_conn";
	public static final String ERROR_DB_QUERY = "error_db_query";

	public static final String ERROR_FILE = "error_file";
	public static final String ERROR_FILE_WRITE = "error_file_write";
	public static final String ERROR_FILE_READ = "error_file_read";

	public static final String ERROR_GENERIC = "error_generic";
	public static final String ERROR_GENERIC_METHOD = "error_generic_method";	
	public static final String ERROR_GENERIC_METHOD_GET = "error_generic_method_get";
	public static final String ERROR_GENERIC_METHOD_CALL = "error_generic_method_call";
	
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
			if (!strings.is_ok(heading) || strings.contains_start(heading, subtype2, false)) subtypes.add(subtype2);
		}

		return arrays.to_array(subtypes);
	}

	private static String[] get_all_subtypes()
	{
		return new String[]
		{						
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
			_CONFIG_LOGS_OUT_SCREEN, _CONFIG_LOGS_OUT_FILE, _CONFIG_LOGS_OUT_DB,
			//_CONFIG_LOGS_DB
			_CONFIG_LOGS_DB_SOURCE, _CONFIG_LOGS_DB_FIELD_ID, _CONFIG_LOGS_DB_FIELD_MESSAGE,

			//_CONFIG_DB
			_CONFIG_DB_MAX_POOL, _CONFIG_DB_NAME, _CONFIG_DB_HOST,  
			_CONFIG_DB_USER, _CONFIG_DB_ERROR_EXIT, _CONFIG_DB_TYPE,
			//_CONFIG_DB_SETUP
			_CONFIG_DB_SETUP_MAIN, _CONFIG_DB_SETUP_LOGS,
			//_CONFIG_DB_TYPE
			_CONFIG_DB_TYPE_MYSQL,
			//_CONFIG_DB_CREDENTIALS
			_CONFIG_DB_CREDENTIALS_TYPE, _CONFIG_DB_CREDENTIALS_ENCRYPTED, 
			_CONFIG_DB_CREDENTIALS_USERNAME, _CONFIG_DB_CREDENTIALS_PASSWORD,
			_CONFIG_DB_CREDENTIALS_WHERE,
			//_CONFIG_DB_FIELDS_DEFAULT 
			_CONFIG_DB_FIELDS_DEFAULT_ID, _CONFIG_DB_FIELDS_DEFAULT_TIMESTAMP, 

			//DATA
			DATA_STRING, DATA_INTEGER, DATA_DECIMAL, DATA_BOOLEAN,
			
			//ERROR_DB
			ERROR_DB_TYPE, ERROR_DB_CONN, ERROR_DB_QUERY, ERROR_DB_INFO, ERROR_DB_CREDENTIALS,

			//ERROR_FILE
			ERROR_FILE_WRITE, ERROR_FILE_READ,

			//ERROR_GENERIC
			//ERROR_GENERIC_METHOD
			ERROR_GENERIC_METHOD_GET, ERROR_GENERIC_METHOD_CALL
		};		
	}
}	