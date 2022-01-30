package accessory;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class defaults 
{	
	static { _ini.load(); }
	
	static final String CREDENTIALS_WHERE = keys.FILE;
	static final boolean CREDENTIALS_ENCRYPTED = false;
	
	static final String DIR_APP = paths.get_default_dir(keys.APP);
	static final String DIR_CREDENTIALS = paths.get_default_dir(keys.CREDENTIALS);
	static final String DIR_INI = paths.get_default_dir(keys.INI);
	static final String DIR_ERRORS = paths.get_default_dir(keys.ERRORS);
	
	static final String SQL_TYPE = types.SQL_MYSQL;
	static final String SQL_MAX_POOL = "500";
	static final String SQL_HOST = "localhost";
	static final boolean SQL_ERROR_EXIT = true;
	static final String SQL_CREDENTIALS_TYPE = SQL_TYPE;
	static final String SQL_CREDENTIALS_WHERE = CREDENTIALS_WHERE;
	static final boolean SQL_CREDENTIALS_ENCRYPTED = false;
	
	public static <x> x get_generic()
	{
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public static Object get(Object var_, boolean max_)
	{
		Class type = null;
		if (var_ == null) return type;
		
		if (var_ instanceof String) type = String.class;
		else if (var_ instanceof Integer) type = Integer.class;
		else if (var_ instanceof Double) type = Double.class;
		else if (var_ instanceof Boolean) type = Boolean.class;
		else if (var_ instanceof HashMap) type = HashMap.class;
		else if (var_ instanceof ArrayList) type = ArrayList.class;
		else if (var_ instanceof Array) type = Array.class;
		
		return get_type(type, max_);	
	}
	
	@SuppressWarnings("rawtypes")
	public static Object get_type(Class type_, boolean max_)
	{
		Object output = null;
		if (type_ == null) return output;
		
		if (type_ == String.class) output = "";
		else if (type_ == Integer.class) 
		{
			output = (max_ ? Integer.MAX_VALUE : Integer.MIN_VALUE);
		}
		else if (type_ == Double.class) 
		{
			output = (max_ ? Double.MAX_VALUE : Double.MIN_VALUE);
		}
		else if (type_ == Boolean.class) output = false;
		
		return output;	
	}
}
