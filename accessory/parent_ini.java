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

	protected String _dbs_user = null;
	protected String _dbs_username = null;
	protected String _dbs_password = null;
	protected String _dbs_host = null;
	protected boolean _dbs_encrypted = false;

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

	protected void populate_all(String name_, String dbs_user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_) { populate_all(name_, dbs_user_, dbs_username_, dbs_password_, dbs_host_, dbs_encrypted_, false); } 

	protected void populate_all(String name_, String dbs_user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_) 
	{		
		if (dbs_user_ != null) _dbs_user = dbs_user_;
		if (dbs_username_ != null) _dbs_username = dbs_username_;
		if (dbs_password_ != null) _dbs_password = dbs_password_;
		if (dbs_host_ != null) _dbs_host = dbs_host_;
		if (!_dbs_encrypted) _dbs_encrypted = dbs_encrypted_;

		populate_all_internal(name_, includes_legacy_);
	}

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
		_ini_db.populate(_dbs_user, _dbs_username, _dbs_password, _dbs_host, _dbs_encrypted);
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

		Class<?>[] params = null;
		Object[] args = null;

		if (class_.equals("_ini_db"))
		{
			params = new Class<?>[] { String.class, String.class, String.class, String.class, boolean.class };
			args = new Object[] { _dbs_user, _dbs_username, _dbs_password, _dbs_host, _dbs_encrypted };
		}

		generic.ignore_errors(true);

		Method method = generic.get_method(class1, method0, params);
		if (method == null && params != null)
		{
			params = null;
			args = null;

			method = generic.get_method(class1, method0, params);
		}

		generic.call_static_method(method, args);

		generic.ignore_errors_persistent_end();		
	}
}