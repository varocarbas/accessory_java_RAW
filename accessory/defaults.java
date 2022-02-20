package accessory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class defaults 
{		
	public static final String APP_NAME = keys.APP;
	public static final String DIR_APP = paths.get_default_dir(keys.APP);
	public static final String DIR_INI = paths.get_default_dir(keys.INI);
	public static final String DIR_LOGS = paths.get_default_dir(keys.LOG);
	public static final String TIME_DATE = keys.TIME; 

	static final boolean CREDENTIALS_ENCRYPTED = false;
	static final String CREDENTIALS_WHERE = types._CONFIG_CREDENTIALS_WHERE_FILE;
	static final String CREDENTIALS_FILE_DIR = paths.get_default_dir(keys.CREDENTIALS);
	static final String CREDENTIALS_FILE_EXTENSION = (String)defaults.get_class(String.class);
	static final String CREDENTIALS_FILE_SEPARATOR = misc.SEPARATOR_NAME;
	static final String CREDENTIALS_FILE_USERNAME = keys.USERNAME;
	static final String CREDENTIALS_FILE_PASSWORD = keys.PASSWORD;
	static final String CREDENTIALS_FILE_ENCRYPTED = keys.ENCRYPT;

	static final String DB_TYPE = types._CONFIG_DB_TYPE_MYSQL;
	static final String DB_SETUP = types._CONFIG_DB_SETUP_MAIN;
	static final String DB_MAX_POOL = "500";
	static final String DB_NAME = (String)defaults.get_class(String.class);
	static final String DB_HOST = "localhost";
	static final String DB_USER = (String)defaults.get_class(String.class);
	static final String DB_FIELDS_DEFAULT_ID = "_id";
	static final String DB_FIELDS_DEFAULT_TIMESTAMP = "_timestamp";
	static final String DB_CREDENTIALS_TYPE = types.remove_type(types._CONFIG_DB_TYPE, types._CONFIG_DB);
	static final String DB_CREDENTIALS_WHERE = CREDENTIALS_WHERE;
	static final String DB_CREDENTIALS_USERNAME = (String)defaults.get_class(String.class);
	static final String DB_CREDENTIALS_PASSWORD = (String)defaults.get_class(String.class);
	static final boolean DB_CREDENTIALS_ENCRYPTED = CREDENTIALS_ENCRYPTED;
	static final boolean DB_ERROR_EXIT = true;
	
	static final String LOGS_DB_SOURCE = keys.LOG;
	static final String LOGS_DB_FIELD_ID = keys.ID;
	static final String LOGS_DB_FIELD_MESSAGE = keys.MESSAGE;

	static final boolean LOGS_SCREEN = true;
	static final boolean LOGS_FILE = true;
	static final boolean LOGS_DB = false;

	static final Class<?> CLASS_NUMBERS = Double.class; 
	static final Class<?> CLASS_ARRAYS = ArrayList.class;

	static { _ini.load(); }

	public static Object get(Object var_)
	{
		if (var_ == null) return null;

		Class<?> type = null;

		if (var_ instanceof String) type = String.class;
		else if (var_ instanceof Integer) type = Integer.class;
		else if (var_ instanceof Double) type = Double.class;
		else if (var_ instanceof Boolean) type = Boolean.class;
		else if (var_ instanceof HashMap) type = HashMap.class;
		else if (var_ instanceof ArrayList) type = ArrayList.class;
		else if (var_ instanceof Array) type = Array.class;

		return get_class(type);	
	}

	public static Object get_class(Class<?> type_)
	{
		Object output = null;
		if (type_ == null) return output;

		if (type_.equals(String.class)) output = "";
		else if (type_.equals(Integer.class)) output = (Integer)0;
		else if (type_.equals(Double.class)) output = (Double)0.0;
		else if (type_.equals(Boolean.class)) output = false;

		return output;	
	}
}