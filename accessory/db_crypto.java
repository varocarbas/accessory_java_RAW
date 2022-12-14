package accessory;

import java.util.HashMap;

import javax.crypto.SecretKey;

public abstract class db_crypto 
{
	public static final String SOURCE = types.CONFIG_CRYPTO_DB_SOURCE;

	public static final String ID = types.CONFIG_CRYPTO_DB_FIELD_ID;
	public static final String ALGO = types.CONFIG_CRYPTO_DB_FIELD_ALGO;
	public static final String IV = types.CONFIG_CRYPTO_DB_FIELD_IV;
	public static final String KEY = types.CONFIG_CRYPTO_DB_FIELD_KEY;

	public static final int MAX_SIZE_ID = db_common.MAX_SIZE_KEY;
	public static final int MAX_SIZE_ALGO = db_common.MAX_SIZE_KEY;
	public static final int MAX_SIZE_IV = 100;
	
	public static final String WHAT_KEY = crypto.WHAT_KEY;
	public static final String WHAT_IV = crypto.WHAT_IV;
	public static final String WHAT_ALGO = crypto.WHAT_ALGO;
	
	private static String[] _fields = null;
	private static String[] _cols = null;
	private static HashMap<String, String> _fields_cols = null;
	
	private static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	public static boolean is_quick() { return _is_quick; }
	
	public static void is_quick(boolean is_quick_) { _is_quick = is_quick_; }

	public static boolean add(String id_, String algo_, SecretKey key_, byte[] iv_)
	{
		boolean output = false;
		
		String algo = db_common.adapt_string(algo_, MAX_SIZE_ALGO);
		if (!strings.is_ok(algo)) algo = crypto.get_algo_cipher_or_default();
		
		String id = db_common.adapt_string(id_, MAX_SIZE_ID);
		String key = strings.from_object(key_);
		String iv = strings.from_bytes(iv_);	
		if (!strings.are_ok(new String[] { id, algo, key, iv }) || iv.length() > MAX_SIZE_IV) return false;
		
		String where = get_where_id(id);
		
		if (_is_quick) 
		{
			String col_id = get_col(ID);
			
			HashMap<String, String> vals = new HashMap<String, String>();
			vals.put(col_id, id);
			vals.put(get_col(ALGO), algo);
			vals.put(get_col(KEY), key);
			vals.put(get_col(IV), iv);
			
			db_quick.insert_update(SOURCE, col_id, vals, where); 

			output = db_quick.is_ok(SOURCE);
		}
		else 
		{
			HashMap<String, Object> vals = new HashMap<String, Object>();
			vals.put(ID, id);
			vals.put(ALGO, algo);
			vals.put(KEY, key);
			vals.put(IV, iv);
			
			db.insert_update(SOURCE, vals, where);
			
			output = db.is_ok(SOURCE);
		}
	
		return output;
	}
	
	public static HashMap<String, Object> get(String id_)
	{
		HashMap<String, Object> output = null;

		HashMap<String, String> temp = null;
		
		String where = get_where_id(id_);
		
		if (_is_quick) temp = db_quick.select_one(SOURCE, get_cols(), where, db.DEFAULT_ORDER);
		else temp = db.select_one(SOURCE, get_fields(), where, db.DEFAULT_ORDER);
		
		if (!arrays.is_ok(temp)) return output;
				
		String algo = temp.get(get_field_col(ALGO));
		if (!strings.is_ok(algo)) algo = crypto.get_algo_cipher_or_default();
		
		SecretKey key = null;
		try { key = (SecretKey)strings.to_object(temp.get(get_field_col(KEY))); }
		catch (Exception e) { key = null; }
		
		byte[] iv = strings.to_bytes(temp.get(get_field_col(IV)));
		
		if (strings.is_ok(algo) && key != null && iv != null) 
		{
			output = new HashMap<String, Object>();
			
			output.put(WHAT_ALGO, algo);
			output.put(WHAT_KEY, key);
			output.put(WHAT_IV, iv);	
		}
					
		return output;
	}
	
	public static boolean delete(String id_)
	{
		db.delete(SOURCE, get_where_id(id_));
		
		return db.is_ok(SOURCE);
	}
	
	public static String get_where_id(String id_) 
	{ 
		String id = db_common.adapt_string(id_, MAX_SIZE_ID);
		
		return (strings.is_ok(id) ? (new db_where(SOURCE, get_field_col(ID), db_where.OPERAND_EQUAL, id, true, db_where.DEFAULT_LINK, _is_quick)).toString() : strings.DEFAULT); 
	}
	
	public static String get_field_col(String field_) { return (_is_quick ? get_col(field_) : field_); }

	public static String get_col(String field_) 
	{ 
		get_fields_cols();
		
		return (_fields_cols.containsKey(field_) ? _fields_cols.get(field_) : strings.DEFAULT); 
	}

	public static String[] get_fields() 
	{
		if (_fields == null) _fields = new String[] { ID, ALGO, IV, KEY };
		
		return _fields; 
	}

	public static String[] get_cols() 
	{
		if (_cols == null) get_fields_cols();
		
		return db.get_cols(_fields_cols); 
	}

	public static HashMap<String, String> get_fields_cols() 
	{ 
		if (_fields_cols == null) _fields_cols = db.get_fields_cols(SOURCE, get_fields());
			
		return _fields_cols; 
	} 

	static void start() { get_fields_cols(); }
}