package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class types 
{
	public static final String SEPARATOR = misc.SEPARATOR_NAME;

	//--------- To be synced with get_all_subtypes().

	//------ To be synced with the corresponding config methods/variables, mainly via config_ini.

	//Note for DB types: the sources/fields are the types/ids, constant, used in most of the code. 
	//The tables/cols are the values, variable, only used when performing the corresponding query.
	//The config class deals with the main in-memory setup, including that source-table/field-col mapping.
	//That is, the given type, the key which remains constant, (e.g., CONFIG_DB_FIELD_WHATEVER) is the 
	//source/field and the associated value, which can be modified at runtime, (e.g., "whatever") is the 
	//table/col.

	public static final String CONFIG = "config";
	public static final String CONFIG_CREDENTIALS = "config_credentials";
	public static final String CONFIG_CREDENTIALS_ENCRYPTED = "config_credentials_encrypted";
	public static final String CONFIG_CREDENTIALS_WHERE = "config_credentials_where";
	public static final String CONFIG_CREDENTIALS_WHERE_DB = "config_credentials_where_db";
	public static final String CONFIG_CREDENTIALS_WHERE_FILE = "config_credentials_where_file"; 
	public static final String CONFIG_CREDENTIALS_FILE = "config_credentials_file";
	public static final String CONFIG_CREDENTIALS_FILE_DIR = "config_credentials_file_dir";
	public static final String CONFIG_CREDENTIALS_FILE_EXTENSION = "config_credentials_file_extension";
	public static final String CONFIG_CREDENTIALS_FILE_SEPARATOR = "config_credentials_file_separator";
	public static final String CONFIG_CREDENTIALS_FILE_USERNAME = "config_credentials_file_username";
	public static final String CONFIG_CREDENTIALS_FILE_PASSWORD = "config_credentials_file_password";
	public static final String CONFIG_CREDENTIALS_FILE_ENCRYPTED = "config_credentials_file_encrypted";

	public static final String CONFIG_BASIC = "config_basic";
	public static final String CONFIG_BASIC_NAME = "config_basic_name";
	public static final String CONFIG_BASIC_DIR_APP = "config_basic_dir_app";
	public static final String CONFIG_BASIC_DIR_INI = "config_basic_dir_ini";

	public static final String CONFIG_LOGS = "config_logs";
	public static final String CONFIG_LOGS_DIR = "config_logs_dir";
	public static final String CONFIG_LOGS_DB = "config_logs_db";
	public static final String CONFIG_LOGS_DB_SOURCE = "config_logs_db_source";
	public static final String CONFIG_LOGS_DB_FIELD_ID = "config_logs_db_field_id";
	public static final String CONFIG_LOGS_DB_FIELD_MESSAGE = "config_logs_db_field_message";
	public static final String CONFIG_LOGS_OUT = "config_logs_out";
	public static final String CONFIG_LOGS_OUT_SCREEN = "config_logs_out_screen";
	public static final String CONFIG_LOGS_OUT_FILE = "config_logs_out_file";
	public static final String CONFIG_LOGS_OUT_DB = "config_logs_out_db";

	public static final String CONFIG_DB = "config_db";
	public static final String CONFIG_DB_MAX_POOL = "config_db_max_pool";
	public static final String CONFIG_DB_NAME = "config_db_name";
	public static final String CONFIG_DB_HOST = "config_db_host";
	public static final String CONFIG_DB_USER = "config_db_user";
	public static final String CONFIG_DB_ERROR_EXIT = "config_db_error_exit";
	public static final String CONFIG_DB_SETUP = "config_db_setup";
	public static final String CONFIG_DB_SETUP_MAIN = CONFIG_DB;
	public static final String CONFIG_DB_SETUP_LOGS = CONFIG_LOGS;
	public static final String CONFIG_DB_TYPE = "config_db_type";
	public static final String CONFIG_DB_TYPE_MYSQL = "config_db_type_mysql";
	public static final String CONFIG_DB_CREDENTIALS = "config_db_credentials";
	public static final String CONFIG_DB_CREDENTIALS_TYPE = "config_db_credentials_type";
	public static final String CONFIG_DB_CREDENTIALS_WHERE = "config_db_credentials_where";
	public static final String CONFIG_DB_CREDENTIALS_ENCRYPTED = "config_db_credentials_encrypted";
	public static final String CONFIG_DB_CREDENTIALS_USERNAME = "config_db_credentials_username";
	public static final String CONFIG_DB_CREDENTIALS_PASSWORD = "config_db_credentials_password";
	public static final String CONFIG_DB_DEFAULT_FIELD = "config_db_default_field";
	public static final String CONFIG_DB_DEFAULT_FIELD_ID = "config_db_default_field_id";
	public static final String CONFIG_DB_DEFAULT_FIELD_TIMESTAMP = "config_db_default_field_timestamp";
	
	public static final String CONFIG_TESTS = "config_tests";
	public static final String CONFIG_TESTS_DB = "config_tests_db";
	public static final String CONFIG_TESTS_DB_SOURCE = "config_tests_db_source";
	public static final String CONFIG_TESTS_DB_FIELD_INT = "config_tests_db_field_int";
	public static final String CONFIG_TESTS_DB_FIELD_STRING = "config_tests_db_field_string";
	public static final String CONFIG_TESTS_DB_FIELD_DECIMAL = "config_tests_db_field_decimal";
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
	public static final String DB_FIELD_FURTHER_KEY_UNIQUE = "db_field_further_key_unique";
	public static final String DB_FIELD_FURTHER_AUTO_INCREMENT = "db_field_further_auto_increment";
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
	public static final String DB_MYSQL = "db_mysql";
	public static final String DB_MYSQL_DATA = "db_mysql_data";
	public static final String DB_MYSQL_DATA_VARCHAR = "db_mysql_data_varchar";
	public static final String DB_MYSQL_DATA_TEXT = "db_mysql_data_text";
	public static final String DB_MYSQL_DATA_INT = "db_mysql_data_int";
	public static final String DB_MYSQL_DATA_TINYINT = "db_mysql_data_tinyint";
	public static final String DB_MYSQL_DATA_BIGINT = "db_mysql_data_bigint";
	public static final String DB_MYSQL_DATA_DECIMAL = "db_mysql_data_decimal";
	public static final String DB_MYSQL_DATA_TIMESTAMP = "db_mysql_data_timestamp";
	
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
	
	public static final String DATES = "dates";
	public static final String DATES_FORMAT = "dates_format";
	public static final String DATES_FORMAT_TIME = "dates_format_time";
	public static final String DATES_FORMAT_TIME_FULL = "dates_format_time_full";
	public static final String DATES_FORMAT_TIME_SHORT = "dates_format_time_short";
	public static final String DATES_FORMAT_DATE = "dates_format_date";
	public static final String DATES_FORMAT_DATE_TIME = "dates_format_date_time";
	
	public static final String ACTIONS = "actions";
	public static final String ACTIONS_ADD = "actions_add";
	public static final String ACTIONS_REMOVE = "actions_remove";
	public static final String ACTIONS_ENCRYPT = "actions_encrypt";
	public static final String ACTIONS_DECRYPT = "actions_decrypt";
	public static final String ACTIONS_START = "actions_start";
	public static final String ACTIONS_STOP = "actions_stop";
	
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
	
	public static final String ERROR = "error";
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
	public static final String ERROR_GENERIC = "error_generic";
	public static final String ERROR_GENERIC_METHOD = "error_generic_method";	
	public static final String ERROR_GENERIC_METHOD_GET = "error_generic_method_get";
	public static final String ERROR_GENERIC_METHOD_CALL = "error_generic_method_call";
	public static final String ERROR_TEST = "error_test";
	public static final String ERROR_TEST_RUN = "error_test_run";

	//--- Only added via default fields.
	static final String DB_FIELD_FURTHER_KEY_PRIMARY = "db_field_further_key_primary";
	static final String DB_FIELD_FURTHER_TIMESTAMP = "db_field_further_timestamp";
	//---
	//---------

	static { ini.load(); }
	
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
	
	public static String action_to_key(String action_)
	{
		return check_subtype(action_, types.get_subtypes(ACTIONS, null), ACTIONS_REMOVE, ACTIONS);
	}
	
	public static String check_multiple(String subtype_, HashMap<String, String[]> targets_)
	{
		String output = strings.DEFAULT;
		if (!arrays.is_ok(targets_)) return output;
		
		for (Entry<String, String[]> item: targets_.entrySet())
		{
			String key = item.getKey();
			
			for (String val: item.getValue())
			{
				if (strings.matches_any(subtype_, new String[] { key, val }, true)) return key;
			}
		}
		
		return output;
	}
	
	public static String check_subtype(String subtype_, String[] subtypes_, String action_add_remove_, String type_add_remove_)
	{	
		String output = strings.DEFAULT;
		
		String subtype2 = strings.normalise(subtype_);
		if (!strings.is_ok(subtype2)) return output;
		
		String type_add_remove = strings.normalise(type_add_remove_);
		String action = strings.normalise(action_add_remove_);
		
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

		String type = type_;
		if (!strings.contains_start(type, subtype_, false)) return subtype_;
		
		type += SEPARATOR;

		return strings.get_end(subtype_, type.length());
	}

	public static String add_type(String subtype_, String type_)
	{
		if (!strings.is_ok(subtype_) || !strings.is_ok(type_) || strings.contains_start(type_, subtype_, false)) return subtype_;

		return (type_ + SEPARATOR + subtype_);
	}

	public static String[] get_subtypes(String[] types_, String[] all_)
	{
		if (!arrays.is_ok(types_)) return get_subtypes(strings.DEFAULT, all_);

		ArrayList<String> subtypes = new ArrayList<String>();

		for (String type: types_)
		{
			subtypes.addAll(arrays.to_arraylist(get_subtypes(type, all_)));		
		}

		return arrays.to_array(subtypes);
	}

	public static String[] get_subtypes(String type_, String[] all_)
	{
		ArrayList<String> subtypes = new ArrayList<String>();
		
		String heading = (strings.is_ok(type_) ? type_ + SEPARATOR : null);
		boolean add_all = !strings.is_ok(heading);
		
		for (String subtype: (String[])arrays.remove_value((arrays.is_ok(all_) ? all_ : get_all_subtypes()), heading, false))
		{
			if (add_all || strings.contains_start(heading, subtype, false)) subtypes.add(subtype);
		}

		return arrays.to_array(subtypes);
	}

	private static String[] get_all_subtypes()
	{
		return new String[]
		{	
			CONFIG,
			CONFIG_CREDENTIALS, 
			CONFIG_CREDENTIALS_ENCRYPTED, CONFIG_CREDENTIALS_WHERE, 
			CONFIG_CREDENTIALS_WHERE_FILE, CONFIG_CREDENTIALS_WHERE_DB,
			CONFIG_CREDENTIALS_FILE,
			CONFIG_CREDENTIALS_FILE_DIR, CONFIG_CREDENTIALS_FILE_EXTENSION, 
			CONFIG_CREDENTIALS_FILE_SEPARATOR, CONFIG_CREDENTIALS_FILE_USERNAME, 
			CONFIG_CREDENTIALS_FILE_PASSWORD, CONFIG_CREDENTIALS_FILE_ENCRYPTED,
			CONFIG_BASIC,
			CONFIG_BASIC_NAME, CONFIG_BASIC_DIR_APP, CONFIG_BASIC_DIR_INI,
			CONFIG_LOGS,
			CONFIG_LOGS_DIR, 
			CONFIG_LOGS_OUT,
			CONFIG_LOGS_OUT_SCREEN, CONFIG_LOGS_OUT_FILE, CONFIG_LOGS_OUT_DB,
			CONFIG_LOGS_DB,
			CONFIG_LOGS_DB_SOURCE, CONFIG_LOGS_DB_FIELD_ID, CONFIG_LOGS_DB_FIELD_MESSAGE,
			CONFIG_DB,
			CONFIG_DB_MAX_POOL, CONFIG_DB_NAME, CONFIG_DB_HOST,  
			CONFIG_DB_USER, CONFIG_DB_ERROR_EXIT, CONFIG_DB_TYPE,
			CONFIG_DB_SETUP,
			CONFIG_DB_SETUP_MAIN, CONFIG_DB_SETUP_LOGS,
			CONFIG_DB_TYPE,
			CONFIG_DB_TYPE_MYSQL,
			CONFIG_DB_CREDENTIALS,
			CONFIG_DB_CREDENTIALS_TYPE, CONFIG_DB_CREDENTIALS_ENCRYPTED, 
			CONFIG_DB_CREDENTIALS_USERNAME, CONFIG_DB_CREDENTIALS_PASSWORD,
			CONFIG_DB_CREDENTIALS_WHERE,
			CONFIG_DB_DEFAULT_FIELD, 
			CONFIG_DB_DEFAULT_FIELD_ID, CONFIG_DB_DEFAULT_FIELD_TIMESTAMP, 
			CONFIG_TESTS, 
			CONFIG_TESTS_DB,
			CONFIG_TESTS_DB_SOURCE, CONFIG_TESTS_DB_FIELD_INT,
			CONFIG_TESTS_DB_FIELD_STRING, CONFIG_TESTS_DB_FIELD_DECIMAL,
			
			DB,
			DB_WHERE,
			DB_WHERE_OPERAND,
			DB_WHERE_OPERAND_EQUAL, DB_WHERE_OPERAND_NOT_EQUAL, DB_WHERE_OPERAND_GREATER,
			DB_WHERE_OPERAND_GREATER_EQUAL, DB_WHERE_OPERAND_LESS, DB_WHERE_OPERAND_LESS_EQUAL,
			DB_WHERE_LINK,
			DB_WHERE_LINK_AND, DB_WHERE_LINK_OR,
			DB_ORDER,
			DB_ORDER_ASC, DB_ORDER_DESC,
			DB_FIELD_FURTHER,
			DB_FIELD_FURTHER_KEY, DB_FIELD_FURTHER_KEY_PRIMARY,
			DB_FIELD_FURTHER_KEY_UNIQUE, DB_FIELD_FURTHER_AUTO_INCREMENT,
			DB_FIELD_FURTHER_TIMESTAMP,
			DB_QUERY,
			DB_QUERY_SELECT, DB_QUERY_INSERT, DB_QUERY_UPDATE, DB_QUERY_DELETE, 
			DB_QUERY_TABLE,
			DB_QUERY_TABLE_EXISTS, DB_QUERY_TABLE_CREATE, DB_QUERY_TABLE_DROP, DB_QUERY_TABLE_TRUNCATE,
			DB_MYSQL,
			DB_MYSQL_DATA_VARCHAR, DB_MYSQL_DATA_TEXT, DB_MYSQL_DATA_INT, DB_MYSQL_DATA_TINYINT, 
			DB_MYSQL_DATA_BIGINT, DB_MYSQL_DATA_DECIMAL,
			
			DATA,
			DATA_STRING, DATA_STRING_BIG, DATA_INT, DATA_LONG, DATA_DECIMAL, DATA_TIMESTAMP,
			DATA_BOOLEAN,
			DATA_BOOLEAN_TRUE, DATA_BOOLEAN_FALSE,
			
			DATES,
			DATES_FORMAT,
			DATES_FORMAT_TIME, DATES_FORMAT_TIME_FULL, DATES_FORMAT_TIME_SHORT,
			DATES_FORMAT_DATE, DATES_FORMAT_DATE_TIME,
			
			ACTIONS,
			ACTIONS_ADD, ACTIONS_REMOVE, ACTIONS_ENCRYPT, ACTIONS_DECRYPT,
			ACTIONS_START, ACTIONS_STOP,
			
			WHAT,
			WHAT_USER, WHAT_USERNAME, WHAT_PASSWORD, WHAT_DB, WHAT_HOST, WHAT_MAX, WHAT_MIN, WHAT_FILE, 
			WHAT_SCREEN, WHAT_INFO, WHAT_QUERY, WHAT_KEY, WHAT_VALUE, WHAT_FURTHER,
			WHAT_TYPE, WHAT_APP, WHAT_SERVER, WHAT_ID,
			WHAT_DIR,
			WHAT_DIR_CREDENTIALS, WHAT_DIR_INI, WHAT_DIR_LOGS, WHAT_DIR_APP,
			
			ERROR,
			ERROR_DB,
			ERROR_DB_TYPE, ERROR_DB_CONN, ERROR_DB_QUERY, ERROR_DB_INFO, ERROR_DB_CREDENTIALS,
			ERROR_DB_SOURCE, ERROR_DB_FIELD, ERROR_DB_VALS,
			ERROR_FILE,
			ERROR_FILE_WRITE, ERROR_FILE_READ,
			ERROR_STRING,
			ERROR_GENERIC,
			ERROR_GENERIC_METHOD,
			ERROR_GENERIC_METHOD_GET, ERROR_GENERIC_METHOD_CALL,
			ERROR_TEST,
			ERROR_TEST_RUN
		};		
	}
}