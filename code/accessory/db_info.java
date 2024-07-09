package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class db_info
{
	public static final String KEY = _types.CONFIG_INFO_DB_FIELD_KEY;
	public static final String VALUE = _types.CONFIG_INFO_DB_FIELD_VALUE;
	public static final String IS_ENC = _types.CONFIG_INFO_DB_FIELD_IS_ENC;

	public static final int MAX_SIZE_KEY = 1000;
	public static final int MAX_SIZE_VALUE = 1000;

	public static final String ENCRYPTION_ID = "info";

	public static final String DEFAULT_SOURCE = _types.CONFIG_INFO_DB_SOURCE;
	
	private static String _source = DEFAULT_SOURCE;
	
	public static boolean is_quick() { return db_common.is_quick(get_source()); }
	
	public static void is_quick(boolean is_quick_) { db_common.is_quick(get_source(), is_quick_); }

	public static String get_source() { return _source; }
	
	public static boolean update_source(String compatible_source_)
	{
		boolean output = db.source_is_ok(compatible_source_);
		
		if (output) _source = compatible_source_;
		
		return output;
	}
		
	@SuppressWarnings("unchecked")
	public static boolean add(HashMap<String, String> vals_, boolean is_enc_, boolean encrypt_, boolean truncate_)
	{
		boolean output = false;
		if (!arrays.is_ok(vals_)) return output;
		
		if (truncate_) truncate();
		
		Object all_vals = adapt_vals(vals_, is_enc_, encrypt_, !truncate_);
		if (!arrays.is_ok(all_vals)) return output;

		String source = get_source();
		
		int count_wrong = 0;
		
		for (Object vals: (is_quick() ? (ArrayList<HashMap<String, String>>)all_vals : (ArrayList<HashMap<String, Object>>)all_vals))
		{
			boolean is_ok = false;
			
			if (is_quick()) 
			{
				db_quick.insert(source, (HashMap<String, String>)vals);
				
				is_ok = db_quick.is_ok(source);
			}
			else 
			{
				db.insert(source, (HashMap<String, Object>)vals);
				
				is_ok = db.is_ok(source);
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
	
	public static void truncate() { db.truncate_table(get_source()); }
	
	public static String get_where_is_enc(boolean is_enc_) 
	{ 
		String source = get_source();
		
		return (new db_where(source, db_common.get_field_quick_col(source, IS_ENC), db_where.OPERAND_EQUAL, db_common.get_val(source, is_enc_), true, db_where.DEFAULT_LINK, is_quick())).toString(); 
	}
	
	public static HashMap<String, db_field> get_fields_info()
	{
		HashMap<String, db_field> info = new HashMap<String, db_field>();

		info.put(KEY, db_common.get_field_string_big());
		info.put(VALUE, db_common.get_field_string_big());
		info.put(IS_ENC, db_common.get_field_is_enc());

		return info;
	}
	
	private static HashMap<String, String> get_internal(String where_, boolean decrypt_)
	{
		String source = get_source();
		
		ArrayList<HashMap<String, String>> all_vals = (is_quick() ? db_quick.select(source, db_quick.get_cols(source), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER) : db.select(source, db_common.get_fields(source), where_, db.DEFAULT_MAX_ROWS, db.DEFAULT_ORDER));
		if (!arrays.is_ok(all_vals)) return null;
	
		HashMap<String, String> output = new HashMap<String, String>();
	
		int count_wrong = 0;
		
		HashMap<String, Object> params = crypto.get_params(ENCRYPTION_ID);
		
		for (HashMap<String, String> vals: all_vals)
		{
			String key = vals.get(db_common.get_field_quick_col(source, KEY));
			String value = vals.get(db_common.get_field_quick_col(source, VALUE));
			boolean is_enc = (boolean)db.adapt_output_internal(vals.get(db_common.get_field_quick_col(source, IS_ENC)), data.BOOLEAN);
		
			boolean is_ok = true;
			
			if (decrypt_ && is_enc)
			{
				key = crypto.decrypt(key, params);
				if (!crypto.is_ok_last()) is_ok = false;

				value = crypto.decrypt(value, params);
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

		if (is_quick()) 
		{
			outputs_quick = (ArrayList<HashMap<String, String>>)arrays.get_new((ArrayList<HashMap<String, String>>)outputs_);
			
			is_enc_quick = db.adapt_input(is_enc_);
		}
		else outputs = (ArrayList<HashMap<String, Object>>)arrays.get_new((ArrayList<HashMap<String, Object>>)outputs_);

		String source = get_source();
		
		for (Entry<String, String> item: vals_.entrySet())
		{
			String key = item.getKey();
			String value = item.getValue();
			
			if (is_quick())
			{
				HashMap<String, String> output = new HashMap<String, String>();				
				output.put(db_quick.get_col(source, KEY), key);
				output.put(db_quick.get_col(source, VALUE), value);
				output.put(db_quick.get_col(source, IS_ENC), is_enc_quick);	

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
		
		return (is_quick() ? outputs_quick : outputs);
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