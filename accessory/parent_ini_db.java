package accessory;

import java.util.HashMap;
import java.util.Map.Entry;

public abstract class parent_ini_db 
{
	private boolean _populated = false;
	
	protected abstract boolean populate_all_dbs();

	public static boolean setup_vals_are_ok(HashMap<String, Object> setup_vals_)
	{
		String instance = _ini.get_generic_key(types.WHAT_INSTANCE);
		if (!arrays.keys_exist(setup_vals_, new String[] { types.CONFIG_DB, types.CONFIG_DB_SETUP, types.CONFIG_DB_SETUP_TYPE, instance })) return false;
		if (!strings.are_ok(new String[] { (String)setup_vals_.get(types.CONFIG_DB), (String)setup_vals_.get(types.CONFIG_DB_SETUP), (String)setup_vals_.get(types.CONFIG_DB_SETUP_TYPE) })) return false;
		
		return true;
	}
	
	protected void populate_all() 
	{
		if (_populated) return;
		
		String error = strings.DEFAULT;
		if (!populate_all_dbs()) error = types.ERROR_INI_DB_DBS;
		
		_populated = true;
		if (error.equals(strings.DEFAULT)) return;

		_ini.manage_error(error);
	}

	@SuppressWarnings("unchecked")
	protected boolean populate_db(String db_, HashMap<String, Object[]> sources_, HashMap<String, Object> setup_vals_)
	{
		boolean is_ok = true;

		String db = config.check_type(db_);
		if (!strings.is_ok(db) || !arrays.is_ok(sources_)) return false;

		config.update_ini(db_, types.CONFIG_DB_NAME, get_db_name_default(db_));		
		HashMap<String, Object> setup_vals = get_setup_vals(db, setup_vals_);
		
		for (Entry<String, Object[]> item: sources_.entrySet())
		{
			String id = item.getKey();
			Object[] temp = item.getValue();
			
			if (!populate_source(id, (String)temp[0], (HashMap<String, Object[]>)temp[1], setup_vals))
			{
				_ini.manage_error(types.ERROR_INI_DB_SOURCE);
				
				return false;
			}
		}
		
		return is_ok;
	}	

	protected HashMap<String, Object[]> add_source(String source_, String db_, HashMap<String, db_field> fields_info_, boolean add_default_fields_, HashMap<String, Object[]> all_sources_)
	{
		all_sources_.put(source_, new Object[] { get_table_default(source_, db_), get_fields(fields_info_, db_, add_default_fields_) });
		
		return all_sources_;
	}

	protected static HashMap<String, Object> get_setup_vals(String setup_, String user_, String host_, boolean encrypted_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		if (strings.is_ok(setup_)) return vals;
		
		vals.put(types.CONFIG_DB_SETUP, setup_);
		vals.put(types.CONFIG_DB_SETUP_CREDENTIALS_ENCRYPTED, encrypted_);
		if (strings.is_ok(user_)) vals.put(types.CONFIG_DB_SETUP_CREDENTIALS_USER, user_); 
		if (strings.is_ok(host_)) vals.put(types.CONFIG_DB_SETUP_HOST, host_); 

		return vals;	
	}

	protected static HashMap<String, Object> get_setup_vals(String setup_, String username_, String password_, String host_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();
		if (strings.is_ok(setup_)) return vals;
		
		vals.put(types.CONFIG_DB_SETUP, setup_);
		if (strings.is_ok(username_)) vals.put(types.CONFIG_DB_SETUP_CREDENTIALS_USERNAME, username_); 
		if (password_ != null) vals.put(types.CONFIG_DB_SETUP_CREDENTIALS_PASSWORD, password_); 
		if (strings.is_ok(host_)) vals.put(types.CONFIG_DB_SETUP_HOST, host_); 

		return vals;	
	}
	
	private HashMap<String, Object[]> get_fields(HashMap<String, db_field> info_, String db_, boolean add_default_)
	{		
		if (!arrays.is_ok(info_)) return null;

		HashMap<String, Object[]> fields = (add_default_ ? get_default_fields() : new HashMap<String, Object[]>());
		
		for (Entry<String, db_field> item: info_.entrySet())
		{
			String id = item.getKey();
			fields = add_field(id, get_col_default(id, db_), item.getValue(), fields);
		}
		
		return fields;
	}

	private HashMap<String, Object[]> add_field(String id_, String col_, db_field field_, HashMap<String, Object[]> all_fields_)
	{
		all_fields_.put(id_, new Object[] { col_, field_ });
		
		return all_fields_;
	}

	private String get_db_name_default(String db_) { return strings.remove(new String[] { types.SEPARATOR, "config", "db" }, db_); }

	private String get_table_default(String source_, String db_) 
	{ 
		String name = strings.remove(new String[] { db_, types.SEPARATOR + "source" }, source_);
		
		if (!strings.is_ok(name)) name = strings.remove(new String[] { types.SEPARATOR, "config", "db", "source", "default" }, source_);
		
		return name;
	}

	private String get_col_default(String field_, String db_) 
	{ 
		String name = strings.remove(new String[] { db_, types.SEPARATOR + "field" + types.SEPARATOR }, field_); 
		
		if (!strings.is_ok(name)) name = strings.remove(new String[] { types.SEPARATOR, "config", "db", "field", "default" }, field_);

		return name;
	}

	private boolean populate_source(String source_, String table_, HashMap<String, Object[]> fields_, HashMap<String, Object> setup_vals_)
	{
		if (!setup_vals_are_ok(setup_vals_)) return false;

		String db = config.check_type((String)setup_vals_.get(types.CONFIG_DB));
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
		
		if (!accessory.db.add_source_ini(source, fields, setup_vals_)) return false;
		if (!config.update_ini(db, source, table_)) return false;
		
		return true;
	}

	private HashMap<String, Object[]> get_default_fields()
	{
		HashMap<String, Object[]> default_fields = new HashMap<String, Object[]>();
		
		default_fields = add_field(types.CONFIG_DB_DEFAULT_FIELD_ID, "_id", new db_field(data.INT, new String[] { db_field.KEY_PRIMARY, db_field.AUTO_INCREMENT }), new HashMap<String, Object[]>());
		default_fields = add_field(types.CONFIG_DB_DEFAULT_FIELD_TIMESTAMP, "_timestamp", new db_field(data.TIMESTAMP, new String[] { db_field.TIMESTAMP }), default_fields);
		
		return default_fields;
	}
	
	private HashMap<String, Object> get_setup_vals(String db_, HashMap<String, Object> vals_)
	{
		HashMap<String, Object> vals = new HashMap<String, Object>(arrays.is_ok(vals_) ? vals_ : get_setup_default());
		
		if (!vals.containsKey(types.CONFIG_DB)) vals.put(types.CONFIG_DB, db_);
		if (!vals.containsKey(types.CONFIG_DB_SETUP)) vals.put(types.CONFIG_DB_SETUP, db_);
		if (!vals.containsKey(types.CONFIG_DB_SETUP_TYPE)) vals.put(types.CONFIG_DB_SETUP_TYPE, _defaults.DB_TYPE);
		
		vals = parent_ini_config.get_config_default_generic(types.CONFIG_DB_SETUP_CREDENTIALS, vals);
		if (arrays.value_exists(config.update_ini((String)vals.get(types.CONFIG_DB_SETUP), vals), false)) return null;

		vals.put(_ini.get_generic_key(types.WHAT_INSTANCE), db.get_instance_ini((String)vals.get(types.CONFIG_DB_SETUP_TYPE)));
		
		return vals;
	}

	private HashMap<String, Object> get_setup_default()
	{
		HashMap<String, Object> vals = new HashMap<String, Object>();

		vals.put(types.CONFIG_DB_SETUP, _defaults.DB_SETUP);
		vals.put(types.CONFIG_DB_SETUP_TYPE, _defaults.DB_TYPE);
		vals.put(types.CONFIG_DB_SETUP_MAX_POOL, _defaults.DB_MAX_POOL);
		vals.put(types.CONFIG_DB_SETUP_HOST, _defaults.DB_HOST);

		return vals;
	}
}