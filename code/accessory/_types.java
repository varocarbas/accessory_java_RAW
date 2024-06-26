package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class _types extends parent_types
{
	private static _types _instance = new _types(); 

	public _types() { }
	public static void populate() { _instance.populate_internal(); }
	
	public static final String SEPARATOR = misc.SEPARATOR_NAME;

	//--------- To be synced with the populate_all_types() method below.

	//IMPORTANT: these constants follow pretty rigid and predictable naming conventions whose main
	//point is to facilitate their automated treatment (e.g., easy subtype extraction). In any case,
	//the most important aspect of these constants is them representing a major cornerstone of a common 
	//structure ensuring the most intuitive and coherent management of (programming) resources and 
	//information of any kind. Consequently, eventual breaches of the aforementioned naming conventions 
	//might happen in their most relevant parts: their associated values. For example, the values of the
	//CONFIG_DB_SETUP subtypes are, by default, copies of the corresponding DB root constants, which will 
	//surely not follow the CONFIG_DB_SETUP naming convention. In any case, these scenarios are quite 
	//exceptional and, in general, trusting the overall applicability of the naming conventions and the 
	//associated perks (e.g., using the check_type() methods below) is pretty sensible. In any case, that 
	//shouldn't be done in a blind, careless, disrespectful (respect the code, maaaaan!) manner, what is a quite 
	//bad idea when dealing with pretty much anything or anyone anyway.

	//------ To be synced with the corresponding config methods/variables like the ones in _ini_config or _ini_db.

	//Note for DB types: the sources/fields are the types/ids, constant, used in most of the code. 
	//The tables/cols are the values, variable, only used when performing the corresponding query.
	//The config class deals with the main in-memory setup, including that source-table/field-col mapping.
	//That is, the given type, the key which remains constant (e.g., CONFIG_DB_FIELD_WHATEVER), is the 
	//source/field and the associated value, which can be modified at runtime (e.g., "whatever"), is the 
	//table/col.

	public static final String CONFIG = "config";
	public static final String CONFIG_BASIC = "config_basic";
	public static final String CONFIG_BASIC_NAME = "config_basic_name";
	public static final String CONFIG_BASIC_DIR = "config_basic_dir";
	public static final String CONFIG_BASIC_DIR_APP = "config_basic_dir_app";
	public static final String CONFIG_BASIC_DIR_INI = "config_basic_dir_ini";
	public static final String CONFIG_BASIC_DIR_CREDENTIALS = "config_basic_dir_credentials";
	public static final String CONFIG_BASIC_DIR_LOGS = "config_basic_dir_logs";
	public static final String CONFIG_BASIC_DIR_LOGS_ERRORS = "config_basic_dir_logs_errors";
	public static final String CONFIG_BASIC_DIR_LOGS_ACTIVITY = "config_basic_dir_logs_activity";
	public static final String CONFIG_BASIC_DIR_CRYPTO = "config_basic_dir_crypto";
	public static final String CONFIG_BASIC_DIR_SOUNDS = "config_basic_dir_sounds";
	public static final String CONFIG_BASIC_DIR_BACKUPS = "config_basic_dir_backups";
	public static final String CONFIG_BASIC_DIR_BACKUPS_DBS = "config_basic_dir_backups_dbs";
	public static final String CONFIG_BASIC_DIR_BACKUPS_FILES = "config_basic_dir_backups_files";
	public static final String CONFIG_BASIC_DIR_INFO = "config_basic_dir_info";

	//--- CONFIG_DB variables deal with the generic, common configuration of DBs. Specific CONFIG constants
	//have to be added to manage each DB's specific information like, for example, sources and fields.

	//CONFIG_DB is only used for internal purposes. Its associated value, the corresponding root CONFIG constant,
	//is the config._info key under which all the DB information is stored. CONFIG_DB_NAME is associated with the
	//actual name of the DB. Hence, CONFIG_DB/CONFIG_DB_NAME is equivalent to source/table and field/col.
	public static final String CONFIG_DB = "config_db";
	public static final String CONFIG_DB_NAME = "config_db_name";

	//DB setups allow to support multiple basic configurations (e.g., different credentials or operating
	//conditions) at the same time. The value associated with the corresponding CONFIG_DB_SETUP is the
	//main key of the given DB, that is, the corresponding root CONFIG key, the one used to store
	//all its specific values in config._info. The default setup can be used with as many DBs as required.
	public static final String CONFIG_DB_SETUP = "config_db_setup";
	public static final String CONFIG_DB_SETUP_MAX_POOL = "config_db_setup_max_pool";
	public static final String CONFIG_DB_SETUP_HOST = "config_db_setup_host";
	public static final String CONFIG_DB_SETUP_CONNECT_TIMEOUT = "config_db_setup_connect_timeout";
	public static final String CONFIG_DB_SETUP_SOCKET_TIMEOUT = "config_db_setup_socket_timeout";
	public static final String CONFIG_DB_SETUP_TYPE = "config_db_setup_type";
	public static final String CONFIG_DB_SETUP_TYPE_MYSQL = "config_db_setup_type_mysql";
	public static final String CONFIG_DB_SETUP_CREDENTIALS = "config_db_setup_credentials"; 
	public static final String CONFIG_DB_SETUP_CREDENTIALS_USERNAME = "config_db_setup_credentials_username";
	public static final String CONFIG_DB_SETUP_CREDENTIALS_PASSWORD = "config_db_setup_credentials_password";
	//CONFIG_DB_SETUP_CREDENTIALS_USER is a generic ID which is required to encrypt credentials.
	public static final String CONFIG_DB_SETUP_CREDENTIALS_USER = "config_db_setup_credentials_user";
	public static final String CONFIG_DB_SETUP_CREDENTIALS_ENCRYPTED = "config_db_setup_credentials_encrypted";
	
	//The CONFIG_DB_SETUP_CREDENTIALS_MEMORY value will be ignored in all the setups after the first one (i.e., its credentials being used to make a DB query).
	//In any case, it is expected to be false in setups other than the main/default one.
	public static final String CONFIG_DB_SETUP_CREDENTIALS_MEMORY = "config_db_setup_credentials_memory";

	public static final String CONFIG_DB_DEFAULT = "config_db_default";
	public static final String CONFIG_DB_DEFAULT_FIELD = "config_db_default_field";
	public static final String CONFIG_DB_DEFAULT_FIELD_ID = "config_db_default_field_id";
	public static final String CONFIG_DB_DEFAULT_FIELD_TIMESTAMP = "config_db_default_field_timestamp";
	//---

	public static final String CONFIG_CREDENTIALS = "config_credentials";
	public static final String CONFIG_CREDENTIALS_WHERE = "config_credentials_where";
	public static final String CONFIG_CREDENTIALS_WHERE_DB = "config_credentials_where_db";
	public static final String CONFIG_CREDENTIALS_WHERE_FILE = "config_credentials_where_file"; 
	public static final String CONFIG_CREDENTIALS_FILE = "config_credentials_file";
	public static final String CONFIG_CREDENTIALS_FILE_EXTENSION = "config_credentials_file_extension";
	public static final String CONFIG_CREDENTIALS_FILE_USERNAME = "config_credentials_file_username";
	public static final String CONFIG_CREDENTIALS_FILE_PASSWORD = "config_credentials_file_password";
	public static final String CONFIG_CREDENTIALS_FILE_ENCRYPTED = "config_credentials_file_encrypted";
	public static final String CONFIG_CREDENTIALS_DB = CONFIG_DB_DEFAULT;
	public static final String CONFIG_CREDENTIALS_DB_SOURCE = "config_credentials_db_source";
	public static final String CONFIG_CREDENTIALS_DB_FIELD = "config_credentials_db_field";
	public static final String CONFIG_CREDENTIALS_DB_FIELD_ID = "config_credentials_db_field_id";
	public static final String CONFIG_CREDENTIALS_DB_FIELD_ID_ENC = "config_credentials_db_field_id_enc";
	public static final String CONFIG_CREDENTIALS_DB_FIELD_USER = "config_credentials_db_field_user";
	public static final String CONFIG_CREDENTIALS_DB_FIELD_USERNAME = "config_credentials_db_field_username";
	public static final String CONFIG_CREDENTIALS_DB_FIELD_PASSWORD = "config_credentials_db_field_password";
	public static final String CONFIG_CREDENTIALS_DB_FIELD_IS_ENC = "config_credentials_db_field_is_enc";

	public static final String CONFIG_TESTS = "config_tests";
	public static final String CONFIG_TESTS_DB = CONFIG_DB_DEFAULT;
	public static final String CONFIG_TESTS_DB_SOURCE = "config_tests_db_source";
	public static final String CONFIG_TESTS_DB_FIELD = "config_tests_db_field";
	public static final String CONFIG_TESTS_DB_FIELD_INT = "config_tests_db_field_int";
	public static final String CONFIG_TESTS_DB_FIELD_STRING = "config_tests_db_field_string";
	public static final String CONFIG_TESTS_DB_FIELD_DECIMAL = "config_tests_db_field_decimal";
	public static final String CONFIG_TESTS_DB_FIELD_BOOLEAN = "config_tests_db_field_boolean";
	
	public static final String CONFIG_INFO = "config_info";
	public static final String CONFIG_INFO_DB = "config_info_db";
	public static final String CONFIG_INFO_DB_SOURCE = "config_info_db_source";
	public static final String CONFIG_INFO_DB_FIELD = "config_info_db_field";
	public static final String CONFIG_INFO_DB_FIELD_KEY = "config_info_db_field_key";
	public static final String CONFIG_INFO_DB_FIELD_VALUE = "config_info_db_field_value";
	public static final String CONFIG_INFO_DB_FIELD_IS_ENC = "config_info_db_field_is_enc";

	public static final String CONFIG_CRYPTO = "config_crypto";
	public static final String CONFIG_CRYPTO_ALGO = "config_crypto_algo";
	public static final String CONFIG_CRYPTO_ALGO_CIPHER = "config_crypto_algo_cipher";
	public static final String CONFIG_CRYPTO_ALGO_KEY = "config_crypto_algo_key";
	public static final String CONFIG_CRYPTO_STORAGE = "config_crypto_storage";
	public static final String CONFIG_CRYPTO_STORAGE_FILES = "config_crypto_storage_files";
	public static final String CONFIG_CRYPTO_STORAGE_DB = "config_crypto_storage_db";
	public static final String CONFIG_CRYPTO_FILES = "config_crypto_files";
	public static final String CONFIG_CRYPTO_FILES_EXTENSION = "config_crypto_files_extension";
	public static final String CONFIG_CRYPTO_DB = "config_crypto_db";
	public static final String CONFIG_CRYPTO_DB_SOURCE = "config_crypto_db_source";
	public static final String CONFIG_CRYPTO_DB_FIELD = "config_crypto_db_field";
	public static final String CONFIG_CRYPTO_DB_FIELD_ALGO = "config_crypto_db_field_algo";
	public static final String CONFIG_CRYPTO_DB_FIELD_IV = "config_crypto_db_field_iv";
	public static final String CONFIG_CRYPTO_DB_FIELD_KEY = "config_crypto_db_field_key";
	public static final String CONFIG_CRYPTO_DB_FIELD_ID = "config_crypto_db_field_id";
	public static final String CONFIG_CRYPTO_LOG = "config_crypto_log";
	public static final String CONFIG_CRYPTO_LOG_INFO = "config_crypto_log_info";
	
	public static final String CONFIG_LOGS = "config_logs";
	public static final String CONFIG_LOGS_OUT = "config_logs_out";
	public static final String CONFIG_LOGS_OUT_SCREEN = "config_logs_out_screen";
	public static final String CONFIG_LOGS_OUT_FILE = "config_logs_out_file";
	public static final String CONFIG_LOGS_ERRORS = "config_logs_errors";
	public static final String CONFIG_LOGS_ERRORS_TIMESTAMP = "config_logs_errors_timestamp";
	
	public static final String CONFIG_NUMBERS = "config_numbers";
	public static final String CONFIG_NUMBERS_PERC_REF_LARGER = "config_numbers_perc_ref_larger";
	
	public static final String CONFIG_STRINGS = "config_strings";
	public static final String CONFIG_STRINGS_ENCODING = "config_strings_encoding";
	
	public static final String CONFIG_OS = "config_os";
	
	//The boolean flag associated with CONFIG_BASIC_IS_VIRTUAL_MACHINE is meant to account for restrictions which are
	//common in most virtualisation setups. For example, some applications may be unable to automate mouse movements.
	public static final String CONFIG_OS_IS_VIRTUAL_MACHINE = "config_os_is_virtual_machine";
	//------

	public static final String INI_INFO = "ini_info";
	public static final String INI_INFO_INCLUDES_LEGACY = "ini_info_includes_legacy";
	public static final String INI_INFO_APP_NAME = "ini_info_app_name";
	
	public static final String DB = "db";
	public static final String DB_QUICKER = "db_quicker";
	public static final String DB_QUICKER_MYSQL = "db_quicker_mysql";
	public static final String DB_WHERE = "db_where";
	public static final String DB_WHERE_OPERAND = "db_where_operand";
	public static final String DB_WHERE_OPERAND_EQUAL = "db_where_operand_equal";
	public static final String DB_WHERE_OPERAND_NOT_EQUAL = "db_where_operand_not_equal";
	public static final String DB_WHERE_OPERAND_GREATER = "db_where_operand_greater";
	public static final String DB_WHERE_OPERAND_GREATER_EQUAL = "db_where_operand_greater_equal";
	public static final String DB_WHERE_OPERAND_LESS = "db_where_operand_less";
	public static final String DB_WHERE_OPERAND_LESS_EQUAL = "db_where_operand_less_equal";
	public static final String DB_WHERE_OPERAND_LIKE = "db_where_operand_like";
	public static final String DB_WHERE_OPERAND_LIKE_START = "db_where_operand_like_start";
	public static final String DB_WHERE_OPERAND_LIKE_END = "db_where_operand_like_end";
	public static final String DB_WHERE_OPERAND_LIKE_BOTH = "db_where_operand_like_both";
	public static final String DB_WHERE_OPERAND_NOT_LIKE = "db_where_operand_not_like";
	public static final String DB_WHERE_OPERAND_NOT_LIKE_START = "db_where_operand_not_like_start";
	public static final String DB_WHERE_OPERAND_NOT_LIKE_END = "db_where_operand_not_like_end";
	public static final String DB_WHERE_OPERAND_NOT_LIKE_BOTH = "db_where_operand_not_like_both";
	public static final String DB_WHERE_LINK = "db_where_link";
	public static final String DB_WHERE_LINK_AND = "db_where_link_and";
	public static final String DB_WHERE_LINK_OR = "db_where_link_or";
	public static final String DB_ORDER = "db_order";
	public static final String DB_ORDER_ASC = "db_order_asc";
	public static final String DB_ORDER_DESC = "db_order_desc";
	public static final String DB_FIELD_FURTHER = "db_field_further";
	public static final String DB_FIELD_FURTHER_KEY = "db_field_further_key";
	public static final String DB_FIELD_FURTHER_KEY_UNIQUE = "db_field_further_key_unique";
	public static final String DB_FIELD_FURTHER_KEY_PRIMARY = "db_field_further_key_primary";
	public static final String DB_FIELD_FURTHER_AUTO_INCREMENT = "db_field_further_auto_increment";
	public static final String DB_FIELD_FURTHER_TIMESTAMP = "db_field_further_timestamp";
	public static final String DB_QUERY = "db_query";
	public static final String DB_QUERY_SELECT = "db_query_select";
	public static final String DB_QUERY_SELECT_COUNT = "db_query_select_count";
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
	public static final String DATA_STRING_SMALL = "data_string_small";
	public static final String DATA_STRING_BIG = "data_string_big";
	public static final String DATA_TINYINT = "data_tinyint";
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
	public static final String DATES_FORMAT_TIMESTAMP = "dates_format_timestamp";
	public static final String DATES_UNIT = "dates_unit";
	public static final String DATES_UNIT_SECONDS = "dates_unit_seconds";
	public static final String DATES_UNIT_MINUTES = "dates_unit_minutes";
	public static final String DATES_UNIT_HOURS = "dates_unit_hours";
	public static final String DATES_UNIT_DAYS = "dates_unit_days";

	public static final String ACTION = "action";
	public static final String ACTION_ADD = "action_add";
	public static final String ACTION_REMOVE = "action_remove";
	public static final String ACTION_ESCAPE = "action_escape";
	public static final String ACTION_UNESCAPE = "action_unescape";
	public static final String ACTION_REPLACE = "action_replace";
	public static final String ACTION_ENCRYPT = "action_encrypt";
	public static final String ACTION_DECRYPT = "action_decrypt";
	public static final String ACTION_START = "action_start";
	public static final String ACTION_STOP = "action_stop";

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
	public static final String WHAT_INSTANCE = "what_instance";
	public static final String WHAT_MESSAGE = "what_message";
	public static final String WHAT_PATH = "what_path";
	public static final String WHAT_IV = "what_iv";
	public static final String WHAT_ALGO = "what_algo";

	public static final String ERROR = "error";
	public static final String ERROR_DEFAULT = "error_default";
	public static final String ERROR_INI = "error_ini";
	public static final String ERROR_INI_DB = "error_ini_db";
	public static final String ERROR_INI_DB_DBS = "error_ini_db_dbs";
	public static final String ERROR_INI_DB_SOURCE = "error_ini_db_source";
	public static final String ERROR_DB = "error_db";
	public static final String ERROR_DB_INFO = "error_db_info";
	public static final String ERROR_DB_CREDENTIALS = "error_db_credentials";
	public static final String ERROR_DB_TYPE = "error_db_type";
	public static final String ERROR_DB_CONN = "error_db_conn";
	public static final String ERROR_DB_QUERY = "error_db_query";
	public static final String ERROR_DB_SOURCE = "error_db_source";
	public static final String ERROR_DB_FIELD = "error_db_field";
	public static final String ERROR_DB_VALS = "error_db_vals";
	public static final String ERROR_DB_BACKUP = "error_db_backup";
	public static final String ERROR_DB_RESTORE = "error_db_restore";
	public static final String ERROR_DB_QUICKER = "error_db_quicker";
	public static final String ERROR_FILE = "error_file";
	public static final String ERROR_FILE_WRITE = "error_file_write";
	public static final String ERROR_FILE_READ = "error_file_read";
	public static final String ERROR_FILE_DELETE = "error_file_delete";
	public static final String ERROR_DIR_CREATE = "error_dir_create";
	public static final String ERROR_DIR_DELETE = "error_dir_delete";
	public static final String ERROR_STREAM = "error_stream";
	public static final String ERROR_STRING = "error_string";
	public static final String ERROR_GENERIC = "error_generic";
	public static final String ERROR_GENERIC_METHOD = "error_generic_method";	
	public static final String ERROR_GENERIC_METHOD_GET = "error_generic_method_get";
	public static final String ERROR_GENERIC_METHOD_CALL = "error_generic_method_call";
	public static final String ERROR_TEST = "error_test";
	public static final String ERROR_TEST_RUN = "error_test_run";
	public static final String ERROR_CRYPTO = "error_crypto";
	public static final String ERROR_CRYPTO_ALGO = "error_crypto_algo";
	public static final String ERROR_CRYPTO_ALGO_CIPHER = "error_crypto_algo_cipher";
	public static final String ERROR_CRYPTO_ALGO_KEY = "error_crypto_algo_key";
	public static final String ERROR_CRYPTO_KEY = "error_crypto_key";
	public static final String ERROR_CRYPTO_IV = "error_crypto_iv";
	public static final String ERROR_CRYPTO_STORE = "error_crypto_store";
	public static final String ERROR_CRYPTO_STORE_ALGO_CIPHER = "error_crypto_store_algo_cipher";
	public static final String ERROR_CRYPTO_STORE_KEY = "error_crypto_store_key";
	public static final String ERROR_CRYPTO_STORE_IV = "error_crypto_store_iv";
	public static final String ERROR_CRYPTO_RETRIEVE = "error_crypto_retrieve";
	public static final String ERROR_CRYPTO_RETRIEVE_ALGO_CIPHER = "error_crypto_retrieve_algo_cipher";
	public static final String ERROR_CRYPTO_RETRIEVE_KEY = "error_crypto_retrieve_key";
	public static final String ERROR_CRYPTO_RETRIEVE_IV = "error_crypto_retrieve_iv";
	public static final String ERROR_CRYPTO_ENCRYPT = "error_crypto_encrypt";
	public static final String ERROR_CRYPTO_DECRYPT = "error_crypto_decrypt";
	public static final String ERROR_DATES = "error_dates";
	public static final String ERROR_DATES_STRING = "error_dates_string";
	public static final String ERROR_OS = "error_os";
	public static final String ERROR_OS_EXECUTE = "error_os_execute";
	//---------

	static String[] TYPES = null;
	
	public static String check_what(String what_) { return check_type(what_, WHAT); }

	public static String what_to_key(String what_) { return check_type(what_, get_subtypes(WHAT), ACTION_REMOVE, WHAT); }

	public static String check_action(String action_) { return check_type(action_, ACTION); }

	public static String action_to_key(String action_) { return check_type(action_, get_subtypes(ACTION), ACTION_REMOVE, ACTION); }

	public static String check_multiple(String subtype_, HashMap<String, String[]> targets_)
	{
		String output = strings.DEFAULT;
		if (!arrays.is_ok(targets_)) return output;

		for (Entry<String, String[]> item: targets_.entrySet())
		{
			String key = item.getKey();

			for (String val: item.getValue())
			{
				if (strings.matches_any(new String[] { key, val }, subtype_, true)) return key;
			}
		}

		return output;
	}

	public static String check_type(String type_) { return check_type(type_, (String)null); }
	
	public static String check_type(String type_, String root_) 
	{ 
		String output = strings.DEFAULT;
		if (strings.are_equivalent(type_, root_)) return strings.normalise(type_);
		
		String[] types = get_subtypes(root_);
		
		return (arrays.is_ok(types) ? check_type(type_, types) : output); 
	}

	public static String check_type(String type_, String[] types_) { return check_type(type_, types_, null, null); }

	public static String check_type(String type_, String[] types_, String action_add_remove_, String type_add_remove_)
	{	
		String output = strings.DEFAULT;

		String type2 = strings.normalise(type_);
		if (!strings.is_ok(type2)) return output;

		String type_add_remove = strings.normalise(type_add_remove_);
		String action = strings.normalise(action_add_remove_);

		for (String type: get_subtypes(strings.DEFAULT, types_))
		{
			if (strings.are_equal(type2, type) || (strings.is_ok(type_add_remove) && strings.are_equal(add_type(type2, type_add_remove), type)))
			{
				output = (strings.are_equal(action, ACTION_REMOVE) ? remove_type(type, type_add_remove) : type);
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

	public static boolean is_config_db(String type_) 
	{ 
		String type = check_type(type_);
		if (!strings.is_ok(type) || strings.contains_start(ERROR, type, false)) return false;

		return (strings.contains(DB + SEPARATOR, type, false) || strings.contains(SEPARATOR + DB, type, false));
	}

	public static boolean is_subtype_of(String subtype_, String type_) { return arrays.value_exists(get_subtypes(type_), subtype_); }

	public static String[] get_subtypes(String[] types_) { return get_subtypes(types_, null); }

	public static String[] get_subtypes(String[] types_, String[] all_)
	{
		if (!arrays.is_ok(types_)) return get_subtypes(strings.DEFAULT, all_);

		ArrayList<String> subtypes = new ArrayList<String>();
		for (String type: types_) { subtypes.addAll(arrays.to_arraylist(get_subtypes(type, all_))); }

		return arrays.to_array(subtypes);
	}

	public static String[] get_subtypes(String type_) { return get_subtypes(type_, null); }

	public static String[] get_subtypes(String type_, String[] all_) { return get_subtypes(type_, all_, false); }

	public static String[] get_subtypes(String type_, boolean only_roots_) { return get_subtypes(type_, null, only_roots_); }
	
	public static String[] get_subtypes(String type_, String[] all_, boolean only_roots_)
	{
		ArrayList<String> subtypes = new ArrayList<String>();

		String heading = (strings.is_ok(type_) ? type_ + SEPARATOR : null);
		boolean add_all = !strings.is_ok(heading);

		for (String subtype: (String[])arrays.remove_value((arrays.is_ok(all_) ? all_ : get_types()), heading, false))
		{
			if (add_all || strings.contains_start(heading, subtype, false)) subtypes.add(subtype);
		}

		if (only_roots_ && subtypes.size() > 0)
		{
			for (String subtype: new ArrayList<String>(subtypes))
			{
				boolean is_ok = true;
				
				for (String subtype2: new ArrayList<String>(subtypes))
				{
					if (strings.are_equal(subtype, subtype2) || !strings.contains_start(subtype2, subtype, false)) continue;
					
					is_ok = false;
					
					break;
				}
				
				if (!is_ok) subtypes.remove(subtype);
			}
		}
		
		return arrays.to_array(subtypes);
	}
	
	protected String[] get_constant_names_to_ignore() { return new String[] { "SEPARATOR" }; }
}