package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class parent_ini_db 
{
	private boolean _populated = false;

	private static String[] SOURCES_TO_IGNORE = null;
	private static HashMap<String, Object[]> DEFAULT_FIELDS = null;
	private static String[] DEFAULT_FIELD_TYPES = null;
	
	public static HashMap<String, Object> get_setup_vals(String db_name_, String setup_, String user_, String host_, boolean encrypted_) { return add_db_to_setup(db_name_, get_setup_vals(setup_, user_, host_, encrypted_));	}

	public static HashMap<String, Object> get_setup_vals(String setup_, String user_, String host_, boolean encrypted_)
	{
		HashMap<String, Object> vals = get_setup_vals_default();

		if (strings.is_ok(setup_)) vals.put(_types.CONFIG_DB_SETUP, setup_);
		if (strings.is_ok(user_)) vals.put(_types.CONFIG_DB_SETUP_CREDENTIALS_USER, user_); 
		if (strings.is_ok(host_)) vals.put(_types.CONFIG_DB_SETUP_HOST, host_); 

		vals.put(_types.CONFIG_DB_SETUP_CREDENTIALS_ENCRYPTED, encrypted_);

		return vals;	
	}

	public static HashMap<String, Object> get_setup_vals(String db_name_, String setup_, String username_, String password_, String host_) { return add_db_to_setup(db_name_, get_setup_vals(setup_, username_, password_, host_));	}

	public static HashMap<String, Object> get_setup_vals(String setup_, String username_, String password_, String host_)
	{
		HashMap<String, Object> vals = get_setup_vals_default();

		if (strings.is_ok(setup_)) vals.put(_types.CONFIG_DB_SETUP, setup_);
		if (strings.is_ok(username_)) vals.put(_types.CONFIG_DB_SETUP_CREDENTIALS_USERNAME, username_); 
		if (password_ != null) vals.put(_types.CONFIG_DB_SETUP_CREDENTIALS_PASSWORD, password_); 
		if (strings.is_ok(host_)) vals.put(_types.CONFIG_DB_SETUP_HOST, host_); 

		vals.put(_types.CONFIG_DB_SETUP_CREDENTIALS_ENCRYPTED, false);

		return vals;	
	}

	public static HashMap<String, Object> get_setup_vals_default()
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();

		vals.put(_types.CONFIG_DB_SETUP, db.DEFAULT_SETUP);
		vals.put(_types.CONFIG_DB_SETUP_TYPE, db.DEFAULT_TYPE);
		vals.put(_types.CONFIG_DB_SETUP_MAX_POOL, db.DEFAULT_MAX_POOL);
		vals.put(_types.CONFIG_DB_SETUP_HOST, db.DEFAULT_HOST);
		vals.put(_types.CONFIG_DB_SETUP_CREDENTIALS_MEMORY, db.DEFAULT_CREDENTIALS_MEMORY);
		
		return vals;
	}

	public static boolean setup_vals_are_ok(HashMap<String, Object> setup_vals_)
	{
		if (!arrays.keys_exist(setup_vals_, db.SETUP_IDS)) return false;
		if (!strings.are_ok(new String[] { (String)setup_vals_.get(_types.CONFIG_DB), (String)setup_vals_.get(_types.CONFIG_DB_SETUP), (String)setup_vals_.get(_types.CONFIG_DB_SETUP_TYPE) })) return false;

		return true;
	}

	public static String[] get_default_field_types()
	{
		if (DEFAULT_FIELD_TYPES == null) populate_default_field_types();
		
		return DEFAULT_FIELD_TYPES;
	}

	protected abstract boolean populate_all_dbs(HashMap<String, Object> dbs_setup_);

	protected void populate_all() 
	{
		if (_populated) return;

		populate_all_internal(null, null); 
	}

	protected void populate_all(HashMap<String, Object> dbs_setup_) { populate_all(dbs_setup_, null); }
	
	protected void populate_all(HashMap<String, Object> dbs_setup_, String[] sources_to_ignore_) 
	{
		if (_populated) return;

		populate_all_internal(dbs_setup_, sources_to_ignore_); 
	}
	
	protected void populate_all(String dbs_user_, String dbs_username_, String dbs_password_, String dbs_host_, boolean dbs_encrypted_) 
	{
		if (_populated) return;

		HashMap<String, Object> dbs_setup = null;

		if (dbs_user_ != null) dbs_setup = get_setup_vals(null, dbs_user_, dbs_host_, dbs_encrypted_);
		else if (dbs_username_ != null && dbs_password_ != null) dbs_setup = get_setup_vals(null, dbs_username_, dbs_password_, dbs_host_);

		populate_all_internal(dbs_setup, null);
	}

	protected boolean populate_db(String db_, String name_, HashMap<String, Object[]> sources_, HashMap<String, Object> setup_vals_)
	{
		if (!arrays.is_ok(sources_)) return false;

		boolean is_ok = true;
		String db = config.check_type(db_);

		config.update_ini(db, _types.CONFIG_DB_NAME, (strings.is_ok(name_) ? name_ : get_db_name_default(db)));		
		HashMap<String, Object> setup_vals = get_setup_vals(db, setup_vals_);

		String any_source = null;
		
		for (Entry<String, Object[]> item: sources_.entrySet())
		{
			String id = item.getKey();
			if (arrays.value_exists(SOURCES_TO_IGNORE, id)) continue;
			
			if (any_source == null) any_source = id;
			
			Object[] temp = (Object[])arrays.get_new(item.getValue());

			if (!populate_source(id, get_table(temp), get_fields(temp), setup_vals, get_add_default_fields(temp)))
			{
				HashMap<String, Object> info = new HashMap<String, Object>();
				info.put(_keys.get_key(_types.WHAT_TYPE), parent_ini.ERROR_SOURCE);
				info.put("source", id);

				_ini.manage_error(info);

				return false;
			}
		}

		start_quicker(setup_vals, any_source);
		
		return is_ok;
	}	

	protected HashMap<String, Object[]> add_source(String source_, String table_, String db_, HashMap<String, db_field> fields_info_, boolean add_default_fields_, HashMap<String, Object[]> all_sources_)
	{
		if (!strings.is_ok(source_) || !arrays.is_ok(fields_info_)) return all_sources_;
		
		String table = (strings.is_ok(table_) ? table_ : get_table_default(source_, db_));
		
		HashMap<String, Object[]> fields = get_fields(fields_info_, source_, table, db_, add_default_fields_);
		
		all_sources_.put(source_, new Object[] { table, fields, add_default_fields_ });

		return all_sources_;
	}
	
	protected boolean populate_source(String source_, String table_, HashMap<String, Object[]> fields_, HashMap<String, Object> setup_vals_, boolean includes_default_fields_)
	{
		if (!setup_vals_are_ok(setup_vals_)) return false;

		String db = config.check_type((String)setup_vals_.get(_types.CONFIG_DB));
		String source = config.check_type(source_);
		if (!strings.is_ok(db) || !strings.is_ok(source) || !strings.is_ok(table_) || !arrays.is_ok(fields_)) return false;

		HashMap<String, db_field> fields = new HashMap<String, db_field>();		

		for (Entry<String, Object[]> item: fields_.entrySet())
		{
			String id = item.getKey();
			Object[] val = item.getValue();

			String col = (String)val[0];
			config.update_ini(db, id, col);

			db_field field = (db_field)val[1];
			fields.put(id, field);
		}
		
		if (!accessory.db.add_source_ini(source, fields, setup_vals_, includes_default_fields_)) return false;
		if (!config.update_ini(db, source, table_)) return false;

		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static HashMap<String, Object[]> get_fields(Object[] source_info_) { return arrays.get_new_hashmap_xy((HashMap<String, Object[]>)source_info_[1]); }

	private static String get_table(Object[] source_info_) { return (String)source_info_[0]; }

	private static boolean get_add_default_fields(Object[] source_info_) { return (boolean)source_info_[2]; }
	
	private HashMap<String, Object[]> get_fields(HashMap<String, db_field> info_, String source_, String table_, String db_, boolean add_default_)
	{		
		HashMap<String, Object[]> fields = arrays.get_new_hashmap_xy(add_default_ ? get_default_fields() : new HashMap<String, Object[]>());

		String table = (strings.is_ok(table_) ? table_ : get_table_default(source_, db_));

		for (Entry<String, db_field> item: info_.entrySet())
		{
			String id = item.getKey();
			
			fields = add_field(id, get_col_default(id, source_, table, db_), item.getValue(), fields);
		}
		
		return fields;
	}
	
	private static HashMap<String, Object[]> add_field(String id_, String col_, db_field field_, HashMap<String, Object[]> all_fields_)
	{		
		all_fields_.put(id_, new Object[] { col_, field_ });

		return all_fields_;
	}

	private void start_quicker(HashMap<String, Object> setup_vals_, String any_source_)
	{
		if (setup_vals_.get(_types.CONFIG_DB_SETUP_TYPE).equals(db_quicker_mysql.TYPE)) db_quicker_mysql.update_conn_info(any_source_);
	}
	
	private void populate_all_internal(HashMap<String, Object> dbs_setup_, String[] sources_to_ignore_) 
	{	
		if (sources_to_ignore_ != null) SOURCES_TO_IGNORE = arrays_quick.get_new(sources_to_ignore_);
		
		perform_first_actions();
		
		String error = strings.DEFAULT;
		
		if (populate_all_dbs(dbs_setup_)) perform_actions_after_population();
		else error = parent_ini.ERROR_DBS;

		SOURCES_TO_IGNORE = null;
		
		_populated = true;
		if (error.equals(strings.DEFAULT)) return;

		_ini.manage_error(error);
	}

	private void perform_first_actions()
	{
		db.INSTANCE = _keys.get_key(_types.WHAT_INSTANCE);
		db.SETUP_IDS = new String[] { _types.CONFIG_DB, _types.CONFIG_DB_SETUP, _types.CONFIG_DB_SETUP_TYPE, db.INSTANCE };		
	}
	
	private void perform_actions_after_population()
	{
		db_common.populate_is_quick_ini();
		
		db_quick.populate_cols_ini();
		
		db_quick.populate_quicker_ini();
	}
	
	private String get_db_name_default(String db_) { return get_default_common(db_, null, null, null); }

	private String get_table_default(String source_, String db_) 
	{ 
		String output = strings.remove(new String[] { _types.SEPARATOR + "source" }, source_);

		return get_default_common(output, db_, null, null);
	}

	private String get_col_default(String field_, String source_, String table_, String db_) 
	{ 
		String output = strings.remove(new String[] { "common_field" + _types.SEPARATOR, "default_field" + _types.SEPARATOR, "field" + _types.SEPARATOR }, field_); 

		return get_default_common(output, db_, source_, table_);
	}

	private String get_default_common(String input_, String db_, String source_, String table_) 
	{ 
		String output = input_;
		
		String source = source_;
		String target = _types.SEPARATOR + "source";
		if (strings.contains_end(target, source, true)) source = strings.substring_before(target, source, true);
		
		String[] items = new String[] { db_, source, table_, "config", "db" };
		for (String item: items) { output = strings.remove(new String[] { _types.SEPARATOR + item, item + _types.SEPARATOR, item }, output); }

		return output;
	}
	
	private static void populate_default_field_types()
	{
		ArrayList<String> temp = new ArrayList<String>();
		
		for (Entry<String, Object[]> item: get_default_fields().entrySet()) { temp.add(item.getKey()); }
			
		DEFAULT_FIELD_TYPES = arrays.to_array(temp);
	}
	
	private static HashMap<String, Object[]> get_default_fields()
	{
		if (DEFAULT_FIELDS == null) populate_default_fields();
		
		return DEFAULT_FIELDS;
	}
	
	private static void populate_default_fields()
	{		
		DEFAULT_FIELDS = add_field(db.FIELD_ID, "_id", new db_field(data.INT, new String[] { db_field.KEY_PRIMARY, db_field.AUTO_INCREMENT }), new HashMap<String, Object[]>());
		DEFAULT_FIELDS = add_field(db.FIELD_TIMESTAMP, "_timestamp", new db_field(data.TIMESTAMP, new String[] { db_field.TIMESTAMP }), DEFAULT_FIELDS);
	}

	private HashMap<String, Object> get_setup_vals(String db_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>(arrays.is_ok(vals_) ? vals_ : get_setup_vals_default());

		if (!vals.containsKey(_types.CONFIG_DB)) vals.put(_types.CONFIG_DB, db_);
		if (!vals.containsKey(_types.CONFIG_DB_SETUP)) vals.put(_types.CONFIG_DB_SETUP, db_);
		if (!vals.containsKey(_types.CONFIG_DB_SETUP_TYPE)) vals.put(_types.CONFIG_DB_SETUP_TYPE, db.DEFAULT_TYPE);

		vals = parent_ini_config.get_config_default_generic(_types.CONFIG_DB_SETUP_CREDENTIALS, vals);
		if (arrays.value_exists(config.update_ini((String)vals.get(_types.CONFIG_DB_SETUP), vals), false)) return null;

		vals.put(db.INSTANCE, db.get_instance_ini((String)vals.get(_types.CONFIG_DB_SETUP_TYPE)));

		return vals;
	}

	private static HashMap<String, Object> add_db_to_setup(String db_name_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>(vals_);

		if (strings.is_ok(db_name_)) vals.put(_types.CONFIG_DB_NAME, db_name_);
		
		return vals;	
	}
}