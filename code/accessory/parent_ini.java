package accessory;

import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class parent_ini 
{
	//The value of the LEGACY_PACKAGE constant is the name of the package expected to include all the legacy ini classes.
	//This legacy support basically means that it is possible to have two overlapping ini configurations which can be 
	//enabled/disabled by simply changing the value of a boolean variable. For example, there could be a main/new set of 
	//tables or DB setup whose values could be partially or completely overwritten from the legacy package.
	public static final String LEGACY_PACKAGE = "legacy";  

	public static final String ERROR_DBS = _types.ERROR_INI_DB_DBS;
	public static final String ERROR_SOURCE = _types.ERROR_INI_DB_SOURCE;

	protected HashMap<String, Object> DBS_SETUP = null;
	protected String[] TYPES_TO_IGNORE = null;
	
	protected boolean INCLUDES_LEGACY = _ini.DEFAULT_INCLUDES_LEGACY;
	protected String NAME = _ini.DEFAULT_NAME;
	protected String USER = _ini.DEFAULT_USER;

	protected boolean _populated = false;
	
	protected static void manage_error(String type_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put(_keys.get_key(_types.WHAT_TYPE), (strings.is_ok(type_) ? type_ : strings.DEFAULT));

		manage_error(info);
	}

	protected static void manage_error(HashMap<String, Object> info_)
	{
		errors._exit = true;

		errors.manage(info_);
	}

	protected void populate_all(String name_) { populate_all_internal(name_, false); }

	protected void populate_all(String name_, boolean includes_legacy_) { populate_all_internal(name_, includes_legacy_); }

	protected void populate_all(String name_, HashMap<String, Object> dbs_setup_) { populate_all(name_, dbs_setup_, null); } 

	protected void populate_all(String name_, HashMap<String, Object> dbs_setup_, String[] types_to_ignore_) { populate_all(name_, false, dbs_setup_, types_to_ignore_); } 

	protected void populate_all(String name_, boolean includes_legacy_, HashMap<String, Object> dbs_setup_, String[] types_to_ignore_) 
	{ 
		DBS_SETUP = (dbs_setup_ == null ? new HashMap<String, Object>() : new HashMap<String, Object>(dbs_setup_));

		if (types_to_ignore_ != null) TYPES_TO_IGNORE = arrays_quick.get_new(types_to_ignore_);
		
		populate_all_internal(name_, includes_legacy_); 
	}

	protected void populate_all(String name_, String user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_) { populate_all(name_, user_, dbs_username_, dbs_password_, dbs_host_, dbs_encrypted_, false); } 

	protected void populate_all(String name_, String user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_) 
	{
		if (strings.is_ok(user_)) USER = user_;
		
		DBS_SETUP = new HashMap<String, Object>();
		
		if (strings.is_ok(user_)) DBS_SETUP = db.get_setup_vals(null, user_, dbs_host_, dbs_encrypted_);
		else if (strings.is_ok(dbs_username_) && dbs_password_ != null) DBS_SETUP = db.get_setup_vals(null, dbs_username_, dbs_password_, dbs_host_);

		populate_all_internal(name_, includes_legacy_);
	}
	
	private void populate_all_internal(String name_, boolean includes_legacy_) 
	{
		if (_populated) return;
		
		if (strings.is_ok(name_)) NAME = name_;
		
		INCLUDES_LEGACY = includes_legacy_;

		String package_name = getClass().getPackageName();

		if (package_name.equals("accessory")) populate_all_internal_accessory();
		else populate_all_internal_other_all(package_name);

		config.update_basic(_types.CONFIG_BASIC_NAME, NAME);

		DBS_SETUP = null;
		TYPES_TO_IGNORE = null;
		
		_populated = true;
	}
	
	private void populate_all_internal_accessory() 
	{
		_basic.populate();
		
		_types.populate();
		
		_alls.populate();
		
		_keys.populate(TYPES_TO_IGNORE);
		
		_defaults.populate();

		_ini_config.populate();
	
		_ini_db.populate(DBS_SETUP, TYPES_TO_IGNORE);
		
		_starts.populate();
	}

	private void populate_all_internal_other_all(String package_) 
	{
		String[] classes = new String[] { "_basic", "_types", "_alls", "_defaults", "_keys", "_ini_config", "_ini_db", "_starts" };

		for (String class0: classes) { populate_all_internal_other_class(package_, class0); }

		if (_ini.includes_legacy() && !package_.equals(LEGACY_PACKAGE)) populate_all_internal_other_all(LEGACY_PACKAGE);
	}

	private void populate_all_internal_other_class(String package_, String class_) 
	{
		String method0 = "populate";

		String name = package_ + "." + class_;
		Class<?> class1 = generic.get_class_from_name(name);
		if (class1 == null) return;

		generic.ignore_errors(true);

		Class<?>[] params = null;
		Object[] args = null;
		
		if (class_.equals("_ini_db"))
		{
			params = new Class<?>[] { HashMap.class, String[].class };
			args = new Object[] { new HashMap<String, Object>(DBS_SETUP), (String[])arrays.get_new(TYPES_TO_IGNORE) };
		}
		else if (class_.equals("_keys"))
		{
			params = new Class<?>[] { String[].class };
			args = new Object[] { (String[])arrays.get_new(TYPES_TO_IGNORE) };
		}
		
		Method method = null;
		
		int max = 2;
		int count = 0;
		
		while (count < max)
		{
			method = generic.get_method(class1, method0, params);
			if (method != null) break;
			
			count++;
			
			if (class_.equals("_ini_db"))
			{
				if (count == 1)
				{
					params = new Class<?>[] { String.class, String.class, String.class, String.class, boolean.class };
					
					args = new Object[] 
					{ 
						_ini.get_dbs_setup_val(DBS_SETUP, _ini.DBS_SETUP_CREDENTIALS_USER), _ini.get_dbs_setup_val(DBS_SETUP, _ini.DBS_SETUP_CREDENTIALS_USERNAME), 
						_ini.get_dbs_setup_val(DBS_SETUP, _ini.DBS_SETUP_CREDENTIALS_PASSWORD), _ini.get_dbs_setup_val(DBS_SETUP, _ini.DBS_SETUP_HOST), 
						_ini.get_dbs_setup_val(DBS_SETUP, _ini.DBS_SETUP_CREDENTIALS_ENCRYPTED) 
					};					
				}
				else if (count == 2)
				{
					params = null;
					args = null;
				}
				else return;
			}
			else if (class_.equals("_keys"))
			{
				if (count == 1)
				{
					params = null;
					args = null;	
				}
			}
			else return;
		}
		
		generic.call_static_method(method, args);
		
		generic.ignore_errors_persistent_end();		
	}
}