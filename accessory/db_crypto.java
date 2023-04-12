package accessory;

import java.util.HashMap;

import javax.crypto.SecretKey;

public abstract class db_crypto 
{
	public static final String ID = _types.CONFIG_CRYPTO_DB_FIELD_ID;
	public static final String ALGO = _types.CONFIG_CRYPTO_DB_FIELD_ALGO;
	public static final String IV = _types.CONFIG_CRYPTO_DB_FIELD_IV;
	public static final String KEY = _types.CONFIG_CRYPTO_DB_FIELD_KEY;

	public static final int MAX_SIZE_ID = db_common.MAX_SIZE_KEY;
	public static final int MAX_SIZE_ALGO = db_common.MAX_SIZE_KEY;
	public static final int MAX_SIZE_IV = 100;
	
	public static final String WHAT_KEY = crypto.WHAT_KEY;
	public static final String WHAT_IV = crypto.WHAT_IV;
	public static final String WHAT_ALGO = crypto.WHAT_ALGO;

	public static final String DEFAULT_SOURCE = _types.CONFIG_CRYPTO_DB_SOURCE;
	
	static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	private static String _source = DEFAULT_SOURCE;
	
	public static String get_source() { return _source; }
	
	public static boolean update_source(String compatible_source_)
	{
		boolean output = db.source_is_ok(compatible_source_);
		
		if (output) _source = compatible_source_; 	
		
		return output;
	}
	
	public static boolean add(String id_, String algo_, SecretKey key_, byte[] iv_)
	{
		boolean output = false;
		
		String algo = db_common.adapt_string(algo_, MAX_SIZE_ALGO);
		if (!strings.is_ok(algo)) algo = crypto.get_algo_cipher_or_default();
		
		String id = db_common.adapt_string(id_, MAX_SIZE_ID);
		String key = strings.from_object(key_);
		String iv = strings.from_bytes_base64(iv_);	
		if (!strings.are_ok(new String[] { id, algo, key, iv }) || iv.length() > MAX_SIZE_IV) return false;
		
		String where = get_where_id(id);
		
		String source = get_source();
		
		if (_is_quick) 
		{
			String col_id = db_quick.get_col(source, ID);
			
			HashMap<String, String> vals = new HashMap<String, String>();
			vals.put(col_id, id);
			vals.put(db_quick.get_col(source, ALGO), algo);
			vals.put(db_quick.get_col(source, KEY), key);
			vals.put(db_quick.get_col(source, IV), iv);
			
			db_quick.insert_update(source, col_id, vals, where); 

			output = db_quick.is_ok(source);
		}
		else 
		{
			HashMap<String, Object> vals = new HashMap<String, Object>();
			vals.put(ID, id);
			vals.put(ALGO, algo);
			vals.put(KEY, key);
			vals.put(IV, iv);
			
			db.insert_update(source, vals, where);
			
			output = db.is_ok(source);
		}
	
		return output;
	}
	
	public static HashMap<String, Object> get(String id_)
	{
		HashMap<String, Object> output = null;

		HashMap<String, String> temp = null;
		
		String where = get_where_id(id_);
		
		String source = get_source();
		
		if (_is_quick) temp = db_quick.select_one(source, db_quick.get_cols(source), where, db.DEFAULT_ORDER);
		else temp = db.select_one(source, db_common.get_fields(source), where, db.DEFAULT_ORDER);
		
		if (!arrays.is_ok(temp)) return output;
				
		String algo = temp.get(db_common.get_field_quick_col(source, ALGO));
		if (!strings.is_ok(algo)) algo = crypto.get_algo_cipher_or_default();
		
		SecretKey key = null;
		try { key = (SecretKey)strings.to_object(temp.get(db_common.get_field_quick_col(source, KEY))); }
		catch (Exception e) { key = null; }
		
		byte[] iv = strings.to_bytes_base64(temp.get(db_common.get_field_quick_col(source, IV)));
		
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
		String source = get_source();
		
		db.delete(source, get_where_id(id_));
		
		return db.is_ok(source);
	}
	
	public static String get_where_id(String id_) 
	{ 
		String source = get_source();
		
		String id = db_common.adapt_string(id_, MAX_SIZE_ID);
		
		return (strings.is_ok(id) ? (new db_where(source, db_common.get_field_quick_col(source, ID), db_where.OPERAND_EQUAL, id, true, db_where.DEFAULT_LINK, _is_quick)).toString() : strings.DEFAULT); 
	}
	
	public static HashMap<String, db_field> get_fields_info()
	{
		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(ID, db_common.get_field_string(MAX_SIZE_ID, true));
		info.put(ALGO, db_common.get_field_string(MAX_SIZE_ALGO));
		info.put(IV, db_common.get_field_string(MAX_SIZE_IV));
		info.put(KEY, db_common.get_field_string_big());

		return info;
	}
}