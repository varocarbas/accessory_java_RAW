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

	public static final String ERROR_DBS = types.ERROR_INI_DB_DBS;
	public static final String ERROR_SOURCE = types.ERROR_INI_DB_SOURCE;

	protected boolean _populated = false;
	protected boolean _includes_legacy = false;
	protected String _name = strings.DEFAULT;

	protected HashMap<String, Object> _dbs_setup = null;
	
	protected static void manage_error(String type_)
	{
		HashMap<String, Object> info = new HashMap<String, Object>();
		info.put(_keys.get_key(types.WHAT_TYPE), (strings.is_ok(type_) ? type_ : strings.DEFAULT));

		manage_error(info);
	}

	protected static void manage_error(HashMap<String, Object> info_)
	{
		errors._exit = true;

		errors.manage(info_);
	}

	protected void populate_all(String name_) { populate_all_internal(name_, false); }

	protected void populate_all(String name_, boolean includes_legacy_) { populate_all_internal(name_, includes_legacy_); }

	protected void populate_all(String name_, HashMap<String, Object> dbs_setup_) { populate_all(name_, false, dbs_setup_); } 

	protected void populate_all(String name_, boolean includes_legacy_, HashMap<String, Object> dbs_setup_) 
	{ 
		_dbs_setup = (dbs_setup_ == null ? new HashMap<String, Object>() : new HashMap<String, Object>(dbs_setup_));

		populate_all_internal(name_, includes_legacy_); 
	}

	protected void populate_all(String name_, String dbs_user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_) { populate_all(name_, dbs_user_, dbs_username_, dbs_password_, dbs_host_, dbs_encrypted_, false); } 

	protected void populate_all(String name_, String dbs_user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_) 
	{	
		_dbs_setup = new HashMap<String, Object>();
		
		if (strings.is_ok(dbs_user_)) _dbs_setup = parent_ini_db.get_setup_vals(null, dbs_user_, dbs_host_, dbs_encrypted_);
		else if (strings.is_ok(dbs_username_) && dbs_password_ != null) _dbs_setup = parent_ini_db.get_setup_vals(null, dbs_username_, dbs_password_, dbs_host_);

		populate_all_internal(name_, includes_legacy_);
	}

	private Object get_setup_val(String key_) { return arrays.get_value(_dbs_setup, key_); }
	
	private void populate_all_internal(String name_, boolean includes_legacy_) 
	{
		if (_populated) return;

		_name = (strings.is_ok(name_) ? name_ : _defaults.APP_NAME);
		_includes_legacy = includes_legacy_;

		String package_name = getClass().getPackageName();

		if (package_name.equals("accessory")) populate_all_internal_accessory();
		else populate_all_internal_other_all(package_name);

		config.update_basic(types.CONFIG_BASIC_NAME, _name);

		_populated = true;
	}

	private void populate_all_internal_accessory() 
	{
		_basic.populate();
		_starts.populate();
		_alls.populate();
		_defaults.populate();
		_keys.populate();

		_ini_config.populate();
	
		_ini_db.populate(_dbs_setup);
	}

	private void populate_all_internal_other_all(String package_) 
	{
		String[] classes = new String[] { "_basic", "_starts", "_alls", "_defaults", "_keys", "_ini_config", "_ini_db" };

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
			params = new Class<?>[] { HashMap.class };
			args = new Object[] { _dbs_setup };
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
						get_setup_val(types.CONFIG_DB_SETUP_CREDENTIALS_USER), get_setup_val(types.CONFIG_DB_SETUP_CREDENTIALS_USERNAME), 
						get_setup_val(types.CONFIG_DB_SETUP_CREDENTIALS_PASSWORD), get_setup_val(types.CONFIG_DB_SETUP_HOST), 
						get_setup_val(types.CONFIG_DB_SETUP_CREDENTIALS_ENCRYPTED) 
					};					
				}
				else if (count == 2)
				{
					params = null;
					args = null;
				}
				else return;
			}
			else return;
		}
		
		generic.call_static_method(method, args);
		
		generic.ignore_errors_persistent_end();		
	}
}