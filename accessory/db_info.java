package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db_info
{
	public static final String SOURCE = _types.CONFIG_INFO_DB_SOURCE;
	
	public static final String KEY = _types.CONFIG_INFO_DB_FIELD_KEY;
	public static final String VALUE = _types.CONFIG_INFO_DB_FIELD_VALUE;
	public static final String IS_ENC = _types.CONFIG_INFO_DB_FIELD_IS_ENC;

	public static final int MAX_SIZE_KEY = 200;
	public static final int MAX_SIZE_VALUE = 200;

	public static final String ENCRYPTION_ID = "info";
	
	static boolean _is_quick = db_common.DEFAULT_IS_QUICK;
	
	@SuppressWarnings("unchecked")
	public static boolean add(HashMap<String, String> vals_, boolean is_enc_, boolean encrypt_, boolean truncate_)
	{
		boolean output = false;
		if (!arrays.is_ok(vals_)) return output;
		
		if (truncate_) truncate();
		
		Object all_vals = adapt_vals(vals_, is_enc_, encrypt_, !truncate_);
		if (!arrays.is_ok(all_vals)) return output;

		int count_wrong = 0;
		
		for (Object vals: (_is_quick ? (ArrayList<HashMap<String, String>>)all_vals : (ArrayList<HashMap<String, Object>>)all_vals))
		{
			boolean is_ok = false;
			
			if (_is_quick) 
			{
				db_quick.insert(SOURCE, (HashMap<String, String>)vals);
				
				is_ok = db_quick.is_ok(SOURCE);
			}
			else 
			{
				db.insert(SOURCE, (HashMap<String, Object>)vals);
				
				is_ok = db.is_ok(SOURCE);
			}
			
			if (!is_ok) count_wrong++;			
		}
		
		if (count_wrong > 0) generic.to_screen("Wrong records: " + count_wrong);
		else output = true;
		
		return output;
	}
	
	public static HashMap<String, String> get(boolean decrypt_) { return get_all(decrypt_); }
	
	public static HashMap<String, String> get(String[] decrypted_keys_) 
	{ 
		HashMap<String, String> output = get_all(true); 
		if (!arrays.is_ok(decrypted_keys_) || !arrays.is_ok(output)) return output;
		
		HashMap<String, String> output2 = new HashMap<String, String>();
		
		for (String key: decrypted_keys_)
		{
			if (!strings.is_ok(key) || !output.containsKey(key)) continue;

			output2.put(key, output.get(key));
		}
		
		return output2;
	}
	
	public static HashMap<String, String> get_all(boolean decrypt_) { return get_internal(db.DEFAULT_WHERE, decrypt_); }

	public static HashMap<String, String> get_encrypted(boolean decrypt_) { return get_internal(get_where_is_enc(true), decrypt_); }
	
	public static HashMap<String, String> get_decrypted() { return get_internal(get_where_is_enc(false), false); }
	
	public static void truncate() { db.truncate_table(SOURCE); }
	
	public static String get_where_is_enc(boolean is_enc_) { return (new db_where(SOURCE, db_common.get_field_quick_col(SOURCE, IS_ENC), db_where.OPERAND_EQUAL, db_common.get_val(SOURCE, is_enc_), true, db_where.DEFAULT_LINK, _is_quick)).toString(); }
	
	private static HashMap<String, String> get_internal(String where_, boolean decrypt_)
	{
		ArrayList<HashMap<String, String>> all_vals = (_is_quick ? db_quick.select(SOURCE, db_quick.get_cols(SOURCE), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select(SOURCE, db_common.get_fields(SOURCE), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER));
		if (!arrays.is_ok(all_vals)) return null;
	
		HashMap<String, String> output = new HashMap<String, String>();
	
		int count_wrong = 0;
		
		for (HashMap<String, String> vals: all_vals)
		{
			String key = vals.get(db_common.get_field_quick_col(SOURCE, KEY));
			String value = vals.get(db_common.get_field_quick_col(SOURCE, VALUE));
			boolean is_enc = (boolean)db.adapt_output_internal(vals.get(db_common.get_field_quick_col(SOURCE, IS_ENC)), data.BOOLEAN);
		
			boolean is_ok = true;
			
			if (decrypt_ && is_enc)
			{
				key = crypto.decrypt(key, ENCRYPTION_ID);
				if (!crypto.is_ok_last()) is_ok = false;
				
				value = crypto.decrypt(value, ENCRYPTION_ID);
				if (!crypto.is_ok_last()) is_ok = false;
			}
			
			if (is_ok) output.put(key, value);
			else count_wrong++;
		}

		if (count_wrong > 0) generic.to_screen("Wrong records: " + count_wrong);
		
		return output;
	}

	private static Object adapt_vals(HashMap<String, String> vals_, boolean is_enc_, boolean encrypt_, boolean add_existing_)
	{
		Object outputs = null;

		boolean encrypt = (encrypt_ && is_enc_);
		
		if (add_existing_)
		{
			if (encrypt)
			{
				outputs = adapt_vals_internal(crypto.encrypt(add_existing(vals_, get_encrypted(true)), ENCRYPTION_ID, true), true, outputs);
				outputs = adapt_vals_internal(get_decrypted(), false, outputs);
				
				truncate();
			}
			else outputs = adapt_vals_internal(vals_, is_enc_, outputs);
		}
		else outputs = adapt_vals_internal((encrypt ? crypto.encrypt(vals_, ENCRYPTION_ID, true) : new HashMap<String, String>(vals_)), is_enc_, outputs);
		
		return outputs;
	}
		
	@SuppressWarnings("unchecked")
	private static Object adapt_vals_internal(HashMap<String, String> vals_, boolean is_enc_, Object outputs_)
	{
		if (!arrays.is_ok(vals_)) return outputs_;
		
		ArrayList<HashMap<String, Object>> outputs = null;
		ArrayList<HashMap<String, String>> outputs_quick = null;

		String is_enc_quick = null;

		if (_is_quick) 
		{
			outputs_quick = (ArrayList<HashMap<String, String>>)arrays.get_new((ArrayList<HashMap<String, String>>)outputs_);
			
			is_enc_quick = db.adapt_input(is_enc_);
		}
		else outputs = (ArrayList<HashMap<String, Object>>)arrays.get_new((ArrayList<HashMap<String, Object>>)outputs_);

		for (Entry<String, String> item: vals_.entrySet())
		{
			String key = item.getKey();
			String value = item.getValue();
			
			if (_is_quick)
			{
				HashMap<String, String> output = new HashMap<String, String>();				
				output.put(db_quick.get_col(SOURCE, KEY), key);
				output.put(db_quick.get_col(SOURCE, VALUE), value);
				output.put(db_quick.get_col(SOURCE, IS_ENC), is_enc_quick);	

				outputs_quick.add(output);
			}
			else
			{
				HashMap<String, Object> output = new HashMap<String, Object>();				
				output.put(KEY, key);
				output.put(VALUE, value);
				output.put(IS_ENC, is_enc_);	
				
				outputs.add(output);				
			}			
		}
		
		return (_is_quick ? outputs_quick : outputs);
	}
	
	private static HashMap<String, String> add_existing(HashMap<String, String> vals_, HashMap<String, String> existing_)
	{
		HashMap<String, String> vals = new HashMap<String, String>(vals_);
		if (!arrays.is_ok(existing_)) return vals;
		
		for (Entry<String, String> item: existing_.entrySet())
		{
			String key = item.getKey();
			if (vals.containsKey(key)) continue;
			
			vals.put(key, item.getValue());
		}
		
		return vals;
	}
}