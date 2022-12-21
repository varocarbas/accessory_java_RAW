package accessory;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class crypto extends parent 
{
	public static final String _ID = "crypto";
	
	public static final String WHAT_KEY = _keys.KEY;
	public static final String WHAT_IV = "iv";
	public static final String WHAT_ALGO = "algo";
	public static final String WHAT_ALGO_CIPHER = WHAT_ALGO;

	public static final String CONFIG_ALGO_CIPHER = types.CONFIG_CRYPTO_ALGO_CIPHER;
	public static final String CONFIG_ALGO_KEY = types.CONFIG_CRYPTO_ALGO_KEY;
	public static final String CONFIG_STORAGE = types.CONFIG_CRYPTO_STORAGE;
	public static final String CONFIG_STORAGE_FILES = types.CONFIG_CRYPTO_STORAGE_FILES;
	public static final String CONFIG_STORAGE_DB = types.CONFIG_CRYPTO_STORAGE_DB;
	public static final String CONFIG_FILES_EXTENSION = types.CONFIG_CRYPTO_FILES_EXTENSION;
	public static final String CONFIG_LOG_INFO = types.CONFIG_CRYPTO_LOG_INFO;

	public static final String ERROR_ALGO_CIPHER = types.ERROR_CRYPTO_ALGO_CIPHER;
	public static final String ERROR_ALGO_KEY = types.ERROR_CRYPTO_ALGO_KEY;
	public static final String ERROR_KEY = types.ERROR_CRYPTO_KEY;
	public static final String ERROR_IV = types.ERROR_CRYPTO_IV;
	public static final String ERROR_STORE = types.ERROR_CRYPTO_STORE;
	public static final String ERROR_STORE_ALGO_CIPHER = types.ERROR_CRYPTO_STORE_ALGO_CIPHER;
	public static final String ERROR_STORE_KEY = types.ERROR_CRYPTO_STORE_KEY;
	public static final String ERROR_STORE_IV = types.ERROR_CRYPTO_STORE_IV;
	public static final String ERROR_RETRIEVE = types.ERROR_CRYPTO_RETRIEVE;
	public static final String ERROR_RETRIEVE_ALGO_CIPHER = types.ERROR_CRYPTO_RETRIEVE_ALGO_CIPHER;
	public static final String ERROR_RETRIEVE_KEY = types.ERROR_CRYPTO_RETRIEVE_KEY;
	public static final String ERROR_RETRIEVE_IV = types.ERROR_CRYPTO_RETRIEVE_IV;
	public static final String ERROR_ENCRYPT = types.ERROR_CRYPTO_ENCRYPT;
	public static final String ERROR_DECRYPT = types.ERROR_CRYPTO_DECRYPT;
	
	public static final String DEFAULT_ID = _ID;
	public static final String DEFAULT_STORAGE = CONFIG_STORAGE_FILES;
	public static final String DEFAULT_ALGO_CIPHER = "AES/CTR/NoPadding";
	public static final String DEFAULT_ALGO_KEY = "AES";
	public static final String DEFAULT_FILES_EXTENSION = strings.DEFAULT;
	public static final boolean DEFAULT_LOG_INFO = false;	
	
	private static boolean _is_ok_last = false;
	
	private boolean _is_ok = false;
	private String _id = strings.DEFAULT;
	private String _in = strings.DEFAULT;
	private String _out = strings.DEFAULT;
	private Cipher _cipher_enc = null; 
	private Cipher _cipher_dec = null;
	private String _algo_cipher = null;
	private String _algo_key = DEFAULT_ALGO_KEY;
	private SecretKey _key = null;
	private byte[] _iv = null;

	public String toString() { return strings.DEFAULT; }
	public boolean is_ok() { return _is_ok; }
	
	public static boolean is_ok_last() { return _is_ok_last; }
	
	public static boolean update_algo_cipher(String algo_cipher_) { return (strings.is_ok(algo_cipher_) ? config.update_crypto(CONFIG_ALGO_CIPHER, algo_cipher_) : false); }

	public static String get_algo_cipher() { return (String)config.get_crypto(CONFIG_ALGO_CIPHER); }

	public static String get_algo_cipher_or_default() 
	{
		String output = get_algo_cipher();
	
		return (strings.is_ok(output) ? output : DEFAULT_ALGO_CIPHER);
	}

	public static boolean update_algo_key(String algo_key_) { return (strings.is_ok(algo_key_) ? config.update_crypto(CONFIG_ALGO_KEY, algo_key_) : false); }

	public static String get_algo_key() { return (String)config.get_crypto(CONFIG_ALGO_KEY); }

	public static String get_algo_key_or_default() 
	{
		String output = get_algo_key();
	
		return (strings.is_ok(output) ? output : DEFAULT_ALGO_KEY);
	}
	
	public static void store_in_files() { config.update_crypto(CONFIG_STORAGE, CONFIG_STORAGE_FILES); }

	public static void store_in_db() { config.update_crypto(CONFIG_STORAGE, CONFIG_STORAGE_DB); }

	public static boolean is_stored_in_files() { return strings.are_equal(types.check_type((String)config.get_crypto(CONFIG_STORAGE), CONFIG_STORAGE), CONFIG_STORAGE_FILES); }

	public static boolean update_files_extension(String files_extension_) { return (strings.is_ok(files_extension_) ? config.update_crypto(CONFIG_FILES_EXTENSION, files_extension_) : false); }

	public static String get_files_extension() { return (String)config.get_crypto(CONFIG_FILES_EXTENSION); }

	public static void update_log_info(boolean log_info_) { config.update_crypto(CONFIG_LOG_INFO, log_info_); }

	public static boolean get_log_info() { return config.get_crypto_boolean(CONFIG_LOG_INFO); }
	
	public static String[] encrypt_file(String path_, String id_) { return encrypt_decrypt_file(path_, id_, true); }

	public static String[] decrypt_file(String path_, String id_) { return encrypt_decrypt_file(path_, id_, false); }

	public static String[] encrypt(String[] inputs_, String id_) { return encrypt_decrypt(inputs_, id_, true); }

	public static String[] decrypt(String[] inputs_, String id_) { return encrypt_decrypt(inputs_, id_, false); }

	public static HashMap<String, String> encrypt(HashMap<String, String> inputs_, String id_, boolean keys_too_) { return encrypt_decrypt(inputs_, id_, keys_too_, true); }

	public static HashMap<String, String> decrypt(HashMap<String, String> inputs_, String id_, boolean keys_too_) { return encrypt_decrypt(inputs_, id_, keys_too_, false); }
	
	public static String encrypt(String input_, String id_) { return encrypt_decrypt(input_, id_, true); }

	public static String decrypt(String input_, String id_) { return encrypt_decrypt(input_, id_, false); }

	public void encrypt()
	{
		try 
		{
			if (!encrypt_internal()) return;

			_out = strings.from_bytes_base64(_cipher_enc.doFinal(_in.getBytes()));
		
			update_is_ok(true);
		} 
		catch (Exception e) { manage_error(ERROR_ENCRYPT, e); }
	}

	public void decrypt()
	{
		try 
		{
			if (!decrypt_internal()) return;

			byte[] temp = strings.to_bytes_base64(_in);
			if (temp == null) return;
			
			_out = new String(_cipher_dec.doFinal(temp));

			update_is_ok(true);
		} 
		catch (Exception e) { manage_error(ERROR_DECRYPT, e); }
	}

	static String[] populate_all_whats() { return new String[] { WHAT_ALGO_CIPHER, WHAT_KEY, WHAT_IV }; }

	private static String[] get_all_whats() { return _alls.CRYPTO_WHATS; }

	private static String[] encrypt_decrypt_file(String path_, String id_, boolean is_encrypt_) { return encrypt_decrypt(io.file_to_array(path_), id_, is_encrypt_); }	

	private static String[] encrypt_decrypt(String[] inputs_, String id_, boolean is_encrypt_)
	{
		int size = arrays.get_size(inputs_);
		if (size < 1) return null;

		String[] output = new String[size];

		crypto instance = new crypto(inputs_[0], id_);
		if (!instance.is_ok()) return null;

		int last_i = size - 1;
		int i = -1;

		while (i < last_i)
		{
			i++;
			instance._in = inputs_[i];

			if (is_encrypt_) instance.encrypt();
			else instance.decrypt();

			if (!instance.is_ok()) return null;

			output[i] = instance._out;
		}

		return output;
	}

	private static HashMap<String, String> encrypt_decrypt(HashMap<String, String> inputs_, String id_, boolean keys_too_, boolean is_encrypt_)
	{
		if (!arrays.is_ok(inputs_)) return null;

		HashMap<String, String> outputs = new HashMap<String, String>();

		crypto instance = null;

		int start_i = (keys_too_ ? 0 : 1);
		
		for (Entry<String, String> input: inputs_.entrySet())
		{	
			String[] temp = new String[2];
			
			for (int i = start_i; i < 2; i++)
			{
				String val = (i == 0 ? input.getKey() : input.getValue());
				
				if (instance == null) 
				{
					instance = new crypto(val, id_);					
					if (!instance.is_ok()) return null;
				}
				else instance._in = val;
				
				if (is_encrypt_) instance.encrypt();
				else instance.decrypt();

				if (!instance.is_ok()) return null;

				temp[i] = instance._out;
			}
			
			outputs.put((keys_too_ ? temp[0] : input.getKey()), temp[1]);
		}

		return outputs;
	}

	private static String encrypt_decrypt(String input_, String id_, boolean is_encrypt_)
	{
		crypto instance = new crypto(input_, id_);
		
		if (instance.is_ok()) 
		{
			if (is_encrypt_) instance.encrypt();
			else instance.decrypt();
		}

		return (instance.is_ok() ? instance._out : strings.DEFAULT);	
	}
	
	private static String get_path(String id_, String what_) { return paths.build(new String[] { paths.get_dir(paths.DIR_CRYPTO), paths.get_file_full((strings.is_ok(id_) ? id_ : DEFAULT_ID) + misc.SEPARATOR_NAME + what_, get_files_extension()) }, true); }
	
	private crypto(String in_, String id_) { instantiate(in_, id_); }

	private void log_encryption_info()
	{
		HashMap<String, String> info = new HashMap<String, String>();

		info.put("algo_cipher", _algo_cipher);
		info.put("algo_key", _algo_key);
		
		if (is_stored_in_files())
		{
			for (String what: get_all_whats()) { info.put("path_" + what, get_path(_id, what)); }
		}

		logs.update_activity(info, _ID);
	}

	private SecretKey get_key()
	{
		SecretKey output = null;

		try
		{
			KeyGenerator keyGen = KeyGenerator.getInstance(_algo_key);
			keyGen.init(new SecureRandom());
			
			output = keyGen.generateKey();	
		}
		catch (Exception e) 
		{ 
			output = null;
			
			manage_error(ERROR_KEY, e); 
		}

		return output;
	}

	private boolean encrypt_internal() { return (start_enc_dec(true) && update_cipher_enc()); }

	private boolean update_cipher_enc()
	{	
		if (_cipher_enc != null) return true;

		boolean is_ok = false;
		
		_key = null;
		_iv = null;
		
		try
		{	
			_key = get_key();

			if (_key == null) 
			{
				manage_error(ERROR_KEY);

				return is_ok;
			}

			_cipher_enc = Cipher.getInstance(_algo_cipher);
			
			_cipher_enc.init(Cipher.ENCRYPT_MODE, _key, new SecureRandom());	
			
			_iv = _cipher_enc.getIV();
			
			if (!arrays.is_ok(_iv)) 
			{
				manage_error(ERROR_IV);

				return is_ok;
			}
			
			if (store()) 
			{
				is_ok = true;
				
				if (get_log_info()) log_encryption_info();				
			}
		}
		catch (Exception e) { manage_error(ERROR_ENCRYPT, e); }
		
		return is_ok;
	}

	private boolean decrypt_internal() { return (start_enc_dec(false) && update_cipher_dec()); }

	private boolean update_cipher_dec()
	{	
		if (_cipher_dec != null) return true;

		boolean is_ok = false;

		try 
		{
			_cipher_dec = Cipher.getInstance(_algo_cipher);

			_cipher_dec.init(Cipher.DECRYPT_MODE, _key, new IvParameterSpec(_iv));

			is_ok = true;
		} 
		catch (Exception e) { manage_error(ERROR_DECRYPT, e); }
		
		return is_ok;
	}

	private boolean start_enc_dec(boolean is_enc_)
	{
		update_is_ok(false);
		
		if (!is_ok_common(_in, _id, false)) return false;
		
		if (is_enc_)
		{
			_algo_cipher = get_algo_cipher_or_default();
			if (!strings.is_ok(_algo_cipher)) return manage_error(ERROR_ALGO_CIPHER);

			_algo_key = get_algo_key_or_default();		
			if (!strings.is_ok(_algo_key)) return manage_error(ERROR_ALGO_KEY);
		}
		else 
		{
			HashMap<String, Object> items = retrieve();
			if (items == null) return manage_error(ERROR_RETRIEVE);
			
			_algo_cipher = (String)items.get(WHAT_ALGO_CIPHER);
			if (!strings.is_ok(_algo_cipher)) return manage_error(ERROR_RETRIEVE_ALGO_CIPHER);
			
			_key = (SecretKey)items.get(WHAT_KEY);
			if (_key == null) return manage_error(ERROR_RETRIEVE_KEY);
					
			_iv = (byte[])items.get(WHAT_IV);
			if (_iv == null) return manage_error(ERROR_RETRIEVE_IV);
		}
				
		return true;
	}
	
	private HashMap<String, Object> retrieve()
	{
		HashMap<String, Object> output = null;
		
		if (is_stored_in_files()) output = retrieve_from_files();
		else 
		{
			output = db_crypto.get(_id);
			
			if (output == null) manage_error(ERROR_RETRIEVE);
		}
		
		return output;
	}
	
	private HashMap<String, Object> retrieve_from_files()
	{
		String algo = (String)retrieve_from_file(WHAT_ALGO_CIPHER);
		if (!strings.is_ok(algo)) algo = DEFAULT_ALGO_CIPHER;

		SecretKey key = (SecretKey)retrieve_from_file(WHAT_KEY);
		byte[] iv = (byte[])retrieve_from_file(WHAT_IV);
		
		if (!strings.is_ok(algo) || key == null || iv == null) return null;

		HashMap<String, Object> output = new HashMap<String, Object>();
		
		output.put(WHAT_ALGO_CIPHER, algo);
		output.put(WHAT_KEY, key);
		output.put(WHAT_IV, iv);				

		return output;
	}
	
	private Object retrieve_from_file(String what_)
	{
		Object output = null;

		boolean is_ok = true;
		
		String path = get_path(_id, what_);
		
		if (what_.equals(WHAT_ALGO_CIPHER))
		{
			output = io.file_to_string(path, true);
			
			is_ok = true;
		}
		else if (what_.equals(WHAT_KEY))
		{
			Object temp = io.file_to_object(path);
			
			if (io.is_ok()) 
			{
				try 
				{ 
					output = (SecretKey)temp; 
				
					is_ok = true;
				}
				catch (Exception e) { }
			}
		}
		else if (what_.equals(WHAT_IV))
		{
			output = io.file_to_bytes(path);
			
			is_ok = (io.is_ok() && arrays.is_ok(output));
		}

		if (!is_ok) 
		{
			output = null;
			
			manage_error(what_, false);
		}
		
		return output;
	}

	private boolean store() 
	{ 
		boolean output = false;
		
		if (is_stored_in_files()) output = store_in_files_internal(); 
		else
		{
			output = db_crypto.add(_id, _algo_cipher, _key, _iv);
			
			if (!output) manage_error(ERROR_STORE);
		}
	
		if (!output) remove();
		
		return output;
	}
	
	private boolean store_in_files_internal() 
	{ 
		for (String what: get_all_whats())
		{
			if (!store_in_file(what)) return false;
		}
		
		return true;
	}

	private boolean store_in_file(String what_)
	{
		String path = get_path(_id, what_);
		
		if (what_.equals(WHAT_ALGO_CIPHER)) io.line_to_file(path, _algo_cipher, false);
		else if (what_.equals(WHAT_KEY)) io.object_to_file(path, _key);
		else if (what_.equals(WHAT_IV)) io.bytes_to_file(path, _iv);

		boolean output = io.is_ok();
		if (!output) manage_error(what_, true);

		return output;
	}

	private void remove() 
	{
		if (is_stored_in_files())
		{
			for (String what: get_all_whats()) { io.delete_file(get_path(_id, what)); }
		}
		else db_crypto.delete(_id);
	}
	
	private void update_is_ok(boolean is_ok_)
	{
		_is_ok = is_ok_;
		_is_ok_last = is_ok_;
	}
	
	private boolean manage_error(String what_, boolean store_)
	{
		String type = null;
		
		if (what_.equals(WHAT_ALGO_CIPHER)) type = (store_ ? ERROR_STORE_ALGO_CIPHER : ERROR_RETRIEVE_ALGO_CIPHER);
		else if (what_.equals(WHAT_KEY)) type = (store_ ? ERROR_STORE_KEY : ERROR_RETRIEVE_KEY);
		else if (what_.equals(WHAT_IV)) type = (store_ ? ERROR_STORE_IV : ERROR_RETRIEVE_IV);
		
		return manage_error(type);
	}
	
	private boolean manage_error(String type_) 
	{ 
		manage_error(type_, null); 
	
		return false;
	}
	
	private void manage_error(String type_, Exception e_)
	{	
		HashMap<String, Object> items = new HashMap<String, Object>();		
		
		items.put(errors.TYPE, type_);
		if (e_ != null) items.put(errors.MESSAGE, strings.to_string(e_));
		
		items.put("id", _id);
		items.put("in", _in);
		items.put("algo_key", _algo_key);
		items.put("algo_cipher", _algo_cipher);

		if (is_stored_in_files())
		{
			for (String what: get_all_whats()) { items.put("path_" + what, get_path(_id, what)); }
		}
		
		manage_error();
		
		errors.manage(items);
	}

	private void manage_error()
	{
		update_is_ok(false);

		_cipher_enc = null;
		_cipher_dec = null;
		_id = strings.DEFAULT;
		_in = strings.DEFAULT;
		_out = strings.DEFAULT;
		_key = null;
		_iv = null;
	}

	private void instantiate(String in_, String id_)
	{
		instantiate_common();
		
		if (!is_ok_common(in_, id_, true)) return;
		
		populate(in_, id_);
	}

	private boolean is_ok_common(String in_, String id_, boolean is_start_) 
	{ 
		update_is_ok(false);
		
		boolean is_ok = (in_ != null); 
	
		if (!is_start_)
		{
			if (!strings.is_ok(id_)) _id = DEFAULT_ID;
			
			if (!strings.is_ok(_id)) is_ok = false;
		}
		
		return is_ok;
	}
	
	private void populate(String in_, String id_)
	{
		update_is_ok(true);

		_in = in_;
		_id = id_;
	}
}