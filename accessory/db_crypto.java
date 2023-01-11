package accessory;

import java.util.HashMap;

import javax.crypto.SecretKey;

public abstract class db_crypto 
{
	public static final String SOURCE = _types.CONFIG_CRYPTO_DB_SOURCE;

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
	
	static String[] _fields = null;
	static String[] _cols = null;
	static HashMap<String, String> _fields_cols = null;
	
	static boolean _is_quick = db_common.DEFAULT_IS_QUICK;

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
		
		if (_is_quick) 
		{
			String col_id = db_common.get_col(SOURCE, ID);
			
			HashMap<String, String> vals = new HashMap<String, String>();
			vals.put(col_id, id);
			vals.put(db_common.get_col(SOURCE, ALGO), algo);
			vals.put(db_common.get_col(SOURCE, KEY), key);
			vals.put(db_common.get_col(SOURCE, IV), iv);
			
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
		
		if (_is_quick) temp = db_quick.select_one(SOURCE, db_common.get_cols_inbuilt(SOURCE), where, db.DEFAULT_ORDER);
		else temp = db.select_one(SOURCE, db_common.get_fields_inbuilt(SOURCE), where, db.DEFAULT_ORDER);
		
		if (!arrays.is_ok(temp)) return output;
				
		String algo = temp.get(db_common.get_field_col_inbuilt(SOURCE, ALGO));
		if (!strings.is_ok(algo)) algo = crypto.get_algo_cipher_or_default();
		
		SecretKey key = null;
		try { key = (SecretKey)strings.to_object(temp.get(db_common.get_field_col_inbuilt(SOURCE, KEY))); }
		catch (Exception e) { key = null; }
		
		byte[] iv = strings.to_bytes_base64(temp.get(db_common.get_field_col_inbuilt(SOURCE, IV)));
		
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
		
		return (strings.is_ok(id) ? (new db_where(SOURCE, db_common.get_field_col_inbuilt(SOURCE, ID), db_where.OPERAND_EQUAL, id, true, db_where.DEFAULT_LINK, _is_quick)).toString() : strings.DEFAULT); 
	}
		
	static void populate_fields() { _fields = db_common.add_default_fields(SOURCE, new String[] { ID, ALGO, IV, KEY }); }
}