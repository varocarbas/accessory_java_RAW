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
	
	public static final String _CONFIG_TESTS = "_config_tests";
	public static final String _CONFIG_TESTS_DB = "_config_tests_db";
	public static final String _CONFIG_TESTS_DB_SOURCE = "_config_tests_db_source";
	public static final String _CONFIG_TESTS_DB_FIELD_INT = "_config_tests_db_field_int";
	public static final String _CONFIG_TESTS_DB_FIELD_STRING = "_config_tests_db_field_string";
	public static final String _CONFIG_TESTS_DB_FIELD_DECIMAL = "_config_tests_db_field_decimal";
	//------
	
	public static final String DB = "db";
	public static final String DB_WHERE = "db_where";
	public static final String DB_WHERE_OPERAND = "db_where_operand";
	public static final String DB_WHERE_OPERAND_EQUAL = "db_where_operand_equal";
	public static final String DB_WHERE_OPERAND_NOT_EQUAL = "db_where_operand_not_equal";
	public static final String DB_WHERE_OPERAND_GREATER = "db_where_operand_greater";
	public static final String DB_WHERE_OPERAND_GREATER_EQUAL = "db_where_operand_greater_equal";
	public static final String DB_WHERE_OPERAND_LESS = "db_where_operand_less";
	public static final String DB_WHERE_OPERAND_LESS_EQUAL = "db_where_operand_less_equal";
	public static final String DB_WHERE_LINK = "db_where_link";
	public static final String DB_WHERE_LINK_AND = "db_where_link_and";
	public static final String DB_WHERE_LINK_OR = "db_where_link_or";
	public static final String DB_ORDER = "db_order";
	public static final String DB_ORDER_ASC = "db_order_asc";
	public static final String DB_ORDER_DESC = "db_order_desc";
	public static final String DB_FIELD_FURTHER = "db_field_further";
	public static final String DB_FIELD_FURTHER_KEY = "db_field_further_key";
	static final String DB_FIELD_FURTHER_KEY_PRIMARY = "db_field_further_key_primary"; //Added via default fields.
	public static final String DB_FIELD_FURTHER_KEY_UNIQUE = "db_field_further_key_unique";
	public static final String DB_FIELD_FURTHER_AUTO_INCREMENT = "db_field_further_auto_increment";
	public static final String DB_FIELD_FURTHER_TIMESTAMP = "db_field_further_timestamp";
	
	public static final String DB_QUERY = "db_query";
	public static final String DB_QUERY_SELECT = "db_query_select";
	public static final String DB_QUERY_INSERT = "db_query_insert";
	public static final String DB_QUERY_UPDATE = "db_query_update";
	public static final String DB_QUERY_DELETE = "db_query_delete";
	public static final String DB_QUERY_TABLE = "db_query_table";
	public static final String DB_QUERY_TABLE_EXISTS = "db_query_table_exists";
	public static final String DB_QUERY_TABLE_CREATE = "db_query_table_create";
	public static final String DB_QUERY_TABLE_DROP = "db_query_table_drop";
	public static final String DB_QUERY_TABLE_TRUNCATE = "db_query_table_truncate";

	public static final String MYSQL = "mysql";
	public static final String MYSQL_DATA = "mysql_data";
	public static final String MYSQL_DATA_VARCHAR = "mysql_data_varchar";
	public static final String MYSQL_DATA_TEXT = "mysql_data_text";
	public static final String MYSQL_DATA_INT = "mysql_data_int";
	public static final String MYSQL_DATA_TINYINT = "mysql_data_tinyint";
	public static final String MYSQL_DATA_BIGINT = "mysql_data_bigint";
	public static final String MYSQL_DATA_DECIMAL = "mysql_data_decimal";
	public static final String MYSQL_DATA_TIMESTAMP = "mysql_data_timestamp";
	
	public static final String DATA = "data";
	public static final String DATA_STRING = "data_string";
	public static final String DATA_STRING_BIG = "data_string_big";
	public static final String DATA_INT = "data_int";
	public static final String DATA_LONG = "data_long";
	public static final String DATA_DECIMAL = "data_decimal";
	public static final String DATA_TIMESTAMP = "data_timestamp";
	public static final String DATA_BOOLEAN = "data_boolean";
	public static final String DATA_BOOLEAN_TRUE = "data_boolean_true";
	public static final String DATA_BOOLEAN_FALSE = "data_boolean_false";
	
	public static final String ACTIONS = "actions";
	public static final String ACTIONS_ADD = "actions_add";
	public static final String ACTIONS_REMOVE = "actions_remove";
	public static final String ACTIONS_ENCRYPT = "actions_encrypt";
	public static final String ACTIONS_DECRYPT = "actions_decrypt";
	
	public static final String WHAT = "what";
	public static final String WHAT_USER = "what_user";
	public static final String WHAT_USERNAME = "what_username";
	public static final String WHAT_PASSWORD = "what_password";
	public static final String WHAT_DB = "what_db";
	public static final String WHAT_HOST = "what_host";
	public static final String WHAT_MAX = "what_max";
	public static final String WHAT_MIN = "what_min";
	public static final String WHAT_FILE = "what_file";
	public static final String WHAT_SCREEN = "what_screen";
	public static final String WHAT_INFO = "what_info";
	public static final String WHAT_QUERY = "what_query";
	public static final String WHAT_KEY = "what_key";
	public static final String WHAT_VALUE = "what_value";
	public static final String WHAT_FURTHER = "what_further";
	public static final String WHAT_TYPE = "what_type";
	public static final String WHAT_APP = "what_app";
	public static final String WHAT_SERVER = "what_server";
	public static final String WHAT_ID = "what_id";
	
	public static final String WHAT_DIR = "what_dir";
	public static final String WHAT_DIR_CREDENTIALS = "what_dir_credentials";
	public static final String WHAT_DIR_INI = "what_dir_ini";
	public static final String WHAT_DIR_LOGS = "what_dir_logs";
	public static final String WHAT_DIR_APP = "what_dir_app";
	
	public static final String ERROR_DB = "error_db";
	public static final String ERROR_DB_INFO = "error_db_info";
	public static final String ERROR_DB_CREDENTIALS = "error_db_credentials";
	public static final String ERROR_DB_TYPE = "error_db_type";
	public static final String ERROR_DB_CONN = "error_db_conn";
	public static final String ERROR_DB_QUERY = "error_db_query";
	public static final String ERROR_DB_SOURCE = "error_db_source";
	public static final String ERROR_DB_FIELD = "error_db_field";
	public static final String ERROR_DB_VALS = "error_db_vals";
	
	public static final String ERROR_FILE = "error_file";
	public static final String ERROR_FILE_WRITE = "error_file_write";
	public static final String ERROR_FILE_READ = "error_file_read";

	public static final String ERROR_STRING = "error_string";
	public static final String ERROR_STRING_SPLIT = "error_string_split";
	
	public static final String ERROR_GENERIC = "error_generic";
	public static final String ERROR_GENERIC_METHOD = "error_generic_method";	
	public static final String ERROR_GENERIC_METHOD_GET = "error_generic_method_get";
	public static final String ERROR_GENERIC_METHOD_CALL = "error_generic_method_call";

	public static final String ERROR_TEST = "error_test";
	public static final String ERROR_TEST_RUN = "error_test_run";
	
	//---------------------------

	private static HashMap<String, String> aliases = new HashMap<String, String>();

	static { ini.load(); }

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

	public static String check_what(String what_)
	{
		return check_subtype(what_, types.get_subtypes(WHAT, null), null, null);
	}
	
	public static String what_to_key(String what_)
	{
		return check_subtype(what_, types.get_subtypes(WHAT, null), ACTIONS_REMOVE, WHAT);
	}
	
	public static String check_action(String action_)
	{
		return check_subtype(action_, types.get_subtypes(ACTIONS, null), null, null);
	}
	
	public static String check_subtype(String subtype_, String[] subtypes_, String action_add_remove_, String type_add_remove_)
	{	
		String output = strings.DEFAULT;
		
		String subtype2 = check_aliases(strings.normalise(subtype_));
		if (!strings.is_ok(subtype2)) return output;
		
		String type_add_remove = check_aliases(strings.normalise(type_add_remove_));
		String action = check_aliases(strings.normalise(action_add_remove_));
		
		for (String subtype: get_subtypes(strings.DEFAULT, subtypes_))
		{
			if
			(
				strings.are_equal(subtype2, subtype) || 
				(
					strings.is_ok(type_add_remove) && strings.are_equal
					(
						add_type(subtype2, type_add_remove), subtype
					)
				)
			)
			{
				output =
				(
					strings.are_equal(action, ACTIONS_REMOVE) ?
					remove_type(subtype, type_add_remove) : subtype
				);
				
				break;
			}
		}

		return output;
	}

	public static String remove_type(String subtype_, String type_)
	{
		if (!strings.is_ok(subtype_) || !strings.is_ok(type_)) return subtype_;

		String subtype = check_aliases(subtype_);
		String type = check_aliases(type_);
		if (!strings.contains_start(type, subtype, false)) return subtype_;
		
		type += SEPARATOR;

		return strings.get_end(subtype, type.length());
	}

	public static String add_type(String subtype_, String type_)
	{
		if (!strings.is_ok(subtype_) || !strings.is_ok(type_) || strings.contains_start(type_, subtype_, false)) return subtype_;

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

			//_CONFIG_TESTS 
			//_CONFIG_TESTS_DB
			_CONFIG_TESTS_DB_SOURCE, _CONFIG_TESTS_DB_FIELD_INT,
			_CONFIG_TESTS_DB_FIELD_STRING, _CONFIG_TESTS_DB_FIELD_DECIMAL,
			
			//DB
			//DB_WHERE
			//DB_WHERE_OPERAND
			DB_WHERE_OPERAND_EQUAL, DB_WHERE_OPERAND_NOT_EQUAL, DB_WHERE_OPERAND_GREATER,
			DB_WHERE_OPERAND_GREATER_EQUAL, DB_WHERE_OPERAND_LESS, DB_WHERE_OPERAND_LESS_EQUAL,
			//DB_WHERE_LINK
			DB_WHERE_LINK_AND, DB_WHERE_LINK_OR,
			//DB_ORDER
			DB_ORDER_ASC, DB_ORDER_DESC,
			//DB_FIELD_FURTHER
			DB_FIELD_FURTHER_KEY, DB_FIELD_FURTHER_KEY_PRIMARY,
			DB_FIELD_FURTHER_KEY_UNIQUE, DB_FIELD_FURTHER_AUTO_INCREMENT,
			DB_FIELD_FURTHER_TIMESTAMP,
			//DB_QUERY
			DB_QUERY_SELECT, DB_QUERY_INSERT, DB_QUERY_UPDATE, DB_QUERY_DELETE, 
			//DB_QUERY_TABLE
			DB_QUERY_TABLE_EXISTS, DB_QUERY_TABLE_CREATE, DB_QUERY_TABLE_DROP, DB_QUERY_TABLE_TRUNCATE,

			//MYSQL
			MYSQL_DATA_VARCHAR, MYSQL_DATA_TEXT, MYSQL_DATA_INT, MYSQL_DATA_TINYINT, MYSQL_DATA_BIGINT, MYSQL_DATA_DECIMAL,
			
			//DATA
			DATA_STRING, DATA_STRING_BIG, DATA_INT, DATA_LONG, DATA_DECIMAL, DATA_TIMESTAMP,
			DATA_BOOLEAN,
			DATA_BOOLEAN_TRUE, DATA_BOOLEAN_FALSE,
			
			//ACTIONS
			ACTIONS_ADD, ACTIONS_REMOVE, ACTIONS_ENCRYPT, ACTIONS_DECRYPT,
			
			//WHAT
			WHAT_USER, WHAT_USERNAME, WHAT_PASSWORD, WHAT_DB, WHAT_HOST, WHAT_MAX, WHAT_MIN, WHAT_FILE, 
			WHAT_SCREEN, WHAT_INFO, WHAT_QUERY, WHAT_KEY, WHAT_VALUE, WHAT_FURTHER,
			WHAT_TYPE,
			
			//ERROR_DB
			ERROR_DB_TYPE, ERROR_DB_CONN, ERROR_DB_QUERY, ERROR_DB_INFO, ERROR_DB_CREDENTIALS,
			ERROR_DB_SOURCE, ERROR_DB_FIELD, ERROR_DB_VALS,
			
			//ERROR_FILE
			ERROR_FILE_WRITE, ERROR_FILE_READ,
			
			//ERROR_STRING
			ERROR_STRING_SPLIT,
			
			//ERROR_GENERIC
			//ERROR_GENERIC_METHOD
			ERROR_GENERIC_METHOD_GET, ERROR_GENERIC_METHOD_CALL,
			
			//ERROR_TEST
			ERROR_TEST_RUN
		};		
	}
}