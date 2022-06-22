package accessory;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class crypto extends parent 
{
	public static final String KEY = "key";
	public static final String IV = "iv";

	public static final String CONFIG_FILE_CIPHER = types.CONFIG_CRYPTO_FILE_CIPHER;
	public static final String CONFIG_FILE_KEY = types.CONFIG_CRYPTO_FILE_KEY;
	public static final String CONFIG_FILE_EXTENSION = types.CONFIG_CRYPTO_FILE_EXTENSION;

	public static final String ERROR_KEY = types.ERROR_CRYPTO_KEY;
	public static final String ERROR_CIPHER = types.ERROR_CRYPTO_CIPHER;
	public static final String ERROR_ENCRYPT = types.ERROR_CRYPTO_ENCRYPT;
	public static final String ERROR_DECRYPT = types.ERROR_CRYPTO_DECRYPT;
	
	public static final String DEFAULT_ID = "crypto";
	public static final String DEFAULT_FILE_CIPHER = "cipher";
	public static final String DEFAULT_FILE_KEY = "key";
	public static final String DEFAULT_FILE_EXTENSION = strings.DEFAULT;
	public static final String DEFAULT_ALGORITHM_CIPHER = "AES/CTR/NoPadding";
	public static final String DEFAULT_ALGORITHM_KEY = "AES";
	public static final boolean DEFAULT_LOG_ENCRYPTION_INFO = true;	
	
	public static boolean _log_encryption_info = DEFAULT_LOG_ENCRYPTION_INFO;

	public Cipher _cipher_enc = null; 
	public Cipher _cipher_dec = null;
	public String _algo_cipher = DEFAULT_ALGORITHM_CIPHER;
	public String _path_iv = strings.DEFAULT;
	public String _algo_key = DEFAULT_ALGORITHM_KEY;
	public String _path_key = strings.DEFAULT;
	public String _in = strings.DEFAULT;
	public String _out = strings.DEFAULT;
	public String _path = strings.DEFAULT;
	public SecretKey _key = null;
	public byte[] _iv = null;

	private String _temp_algo_key = strings.DEFAULT; 
	private String _temp_algo_cipher = strings.DEFAULT;
	private boolean _is_ok = false;

	public String toString() { return strings.DEFAULT; }
	public boolean is_ok() { return _is_ok; }

	public static String get_class_id() { return types.get_id(types.ID_CRYPTO); }

	public static String get_extension() { return (String)config.get_crypto(types.CONFIG_CRYPTO_FILE_EXTENSION); }

	public static String get_file_full(String id_) { return paths.get_file_full((strings.is_ok(id_) ? id_ : DEFAULT_ID), get_extension()); }

	public static String[] encrypt_file(String path_, String id_) { return encrypt_decrypt_file(path_, id_, true); }

	public static String[] decrypt_file(String path_, String id_) { return encrypt_decrypt_file(path_, id_, false); }

	public static String[] encrypt(String[] inputs_, String id_) { return encrypt_decrypt(inputs_, id_, true); }

	public static String[] decrypt(String[] inputs_, String id_) { return encrypt_decrypt(inputs_, id_, false); }

	public static String encrypt(String input_, String id_) { return encrypt_decrypt(input_, id_, true); }

	public static String decrypt(String input_, String id_) { return encrypt_decrypt(input_, id_, false); }

	public crypto(String in_, String path_key_, String path_iv_, String algo_key_, String algo_cipher_, SecretKey key_, byte[] iv_) { instantiate(in_, path_key_, path_iv_, algo_key_, algo_cipher_, key_, iv_); }

	public void encrypt()
	{
		if (!is_ok_enc()) return;

		try 
		{
			update_cipher_enc();

			if (!arrays.is_ok(_iv)) 
			{
				_iv = _cipher_enc.getIV();
				if (!iv_to_file(_iv)) return;

				if (_log_encryption_info) log_encryption_info();
			}

			_out = Base64.getEncoder().encodeToString(_cipher_enc.doFinal(_in.getBytes()));
		} 
		catch (Exception e) 
		{ 
			manage_error(ERROR_ENCRYPT, e); 

			return;
		}

		_is_ok = true;
	}

	public void decrypt()
	{
		if (!is_ok_dec()) return;

		try 
		{
			update_cipher_dec();

			_out = new String(_cipher_dec.doFinal(Base64.getDecoder().decode(_in)));
		} 
		catch (Exception e) 
		{ 
			manage_error(ERROR_DECRYPT, e); 

			return;
		}

		_is_ok = true;
	}

	private void log_encryption_info()
	{
		HashMap<String, String> info = new HashMap<String, String>();

		info.put("algo_cipher", _algo_cipher);
		info.put("path_iv", _path_iv);
		info.put("algo_key", _algo_key);
		info.put("path_key", _path_key);

		logs.update_activity(info, get_class_id());
	}

	private static String[] encrypt_decrypt_file(String path_, String id_, boolean is_encrypt_) { return encrypt_decrypt(io.file_to_array(path_), id_, is_encrypt_); }	

	private static String[] encrypt_decrypt(String[] inputs_, String id_, boolean is_encrypt_)
	{
		int size = arrays.get_size(inputs_);
		if (size < 1) return null;

		String[] output = new String[size];
		HashMap<String, String> paths = get_default_paths(id_);

		crypto instance = new crypto(inputs_[0], paths.get(KEY), paths.get(IV), null, null, null, null);
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

	private static String encrypt_decrypt(String input_, String id_, boolean is_encrypt_)
	{
		HashMap<String, String> paths = get_default_paths(id_);

		crypto instance = new crypto(input_, paths.get(KEY), paths.get(IV), null, null, null, null);
		if (instance.is_ok()) 
		{
			if (is_encrypt_) instance.encrypt();
			else instance.decrypt();
		}

		return (instance.is_ok() ? instance._out : strings.DEFAULT);	
	}

	private static HashMap<String, String> get_default_paths(String id_)
	{
		HashMap<String, String> output = new HashMap<String, String>();

		for (String what: new String[] { KEY, IV }) { output.put(what, get_default_path(id_, what)); }

		return output;
	}

	private static String get_default_path(String id_, String what_) { return paths.build(new String[] { paths.get_dir(paths.DIR_CRYPTO), get_file_full((strings.is_ok(id_) ? id_ : DEFAULT_ID) + misc.SEPARATOR_NAME + what_) }, true); }

	private SecretKey key_from_file()
	{
		SecretKey output = null;
		Object temp = io.file_to_object(_path_key);

		if (temp != null && io.is_ok()) 
		{
			try { output = (SecretKey)temp;	}
			catch (Exception e) 
			{ 
				manage_error(ERROR_KEY, e);

				output = null;
			}
		}
		else manage_error();

		return output;
	}

	private byte[] iv_from_file()
	{
		byte[] output = null;
		byte[] temp = io.file_to_bytes(_path_iv);

		if (arrays.is_ok(temp) && io.is_ok()) output = arrays.get_new(temp);
		else manage_error();

		return output;
	}

	private boolean key_to_file(SecretKey key_)
	{
		io.object_to_file(_path_key, key_);

		boolean is_ok = io.is_ok();
		if (!is_ok) manage_error();

		return is_ok;
	}

	private boolean iv_to_file(byte[] iv_)
	{
		io.bytes_to_file(_path_iv, iv_);

		boolean is_ok = io.is_ok();
		if (!is_ok) manage_error();

		return is_ok;
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
		catch (Exception e) { manage_error(ERROR_KEY, e); }

		return output;
	}

	private void update_cipher_enc()
	{	
		if (_cipher_enc != null) return;

		try
		{	
			_cipher_enc = Cipher.getInstance(_algo_cipher);

			if (_key == null) 
			{
				_key = get_key();

				if (!key_to_file(_key)) 
				{
					manage_error();

					return;
				}
			}

			_cipher_enc.init(Cipher.ENCRYPT_MODE, _key, new SecureRandom());	
		}
		catch (Exception e) { manage_error(ERROR_ENCRYPT, e); }
	}

	private void update_cipher_dec()
	{	
		if (_cipher_dec != null) return;

		try 
		{
			_cipher_dec = Cipher.getInstance(_algo_cipher);

			_cipher_dec.init(Cipher.DECRYPT_MODE, _key, new IvParameterSpec(_iv));
		} 
		catch (Exception e) { manage_error(ERROR_DECRYPT, e); }
	}

	private void manage_error(String type_, Exception e_)
	{	
		manage_error();

		HashMap<String, Object> items = new HashMap<String, Object>();
		items.put(_keys.TYPE, type_);
		items.put("in", _in);
		items.put("path_key", _path_key);
		items.put("path_iv", _path_iv);
		items.put("algo_key", _algo_key);
		items.put("algo_cipher", _algo_cipher);

		errors.manage(items);
	}

	private void manage_error()
	{
		_is_ok = false;

		_cipher_enc = null;
		_cipher_dec = null;
		_in = strings.DEFAULT;
		_out = strings.DEFAULT;
		_path_key = strings.DEFAULT;
		_path_iv = strings.DEFAULT;
		_key = null;
		_iv = null;
	}

	private void instantiate(String in_, String path_key_, String path_iv_, String algo_key_, String algo_cipher_, SecretKey key_, byte[] iv_)
	{
		instantiate_common();
		if (!is_ok(in_, path_key_, path_iv_, algo_key_, algo_cipher_, key_, iv_)) return;

		populate(in_, path_key_, path_iv_, _temp_algo_key, _temp_algo_cipher, key_, iv_);
	}

	private boolean is_ok(String in_, String path_key_, String path_iv_, String algo_key_, String algo_cipher_, SecretKey key_, byte[] iv_)
	{
		if (!is_ok_common(in_, path_key_, path_iv_, algo_key_, algo_cipher_, true)) return false;

		_temp_algo_key = (strings.is_ok(algo_key_) ? algo_key_ : DEFAULT_ALGORITHM_KEY);
		_temp_algo_cipher = (strings.is_ok(algo_cipher_) ? algo_cipher_ : DEFAULT_ALGORITHM_CIPHER);

		return true;
	}

	private boolean is_ok_common(String in_, String path_key_, String path_iv_, String algo_key_, String algo_cipher_, boolean is_start_)
	{
		boolean is_ok = (in_ != null && strings.is_ok(path_key_) && strings.is_ok(path_iv_));

		if (is_ok && !is_start_) is_ok = (strings.is_ok(algo_key_) && strings.is_ok(algo_cipher_));

		return is_ok;
	}

	private boolean is_ok_enc()
	{
		_is_ok = false;

		return is_ok_common(_in, _path_key, _path_iv, _algo_key, _algo_cipher, false);
	}

	private boolean is_ok_dec()
	{
		_is_ok = false;

		if (!is_ok_common(_in, _path_key, _path_iv, _algo_key, _algo_cipher, false)) return false;

		if (_key == null) 
		{
			_key = key_from_file();
			if (_key == null) return false;
		}

		if (!arrays.is_ok(_iv)) 
		{
			_iv = iv_from_file();
			if (!arrays.is_ok(_iv)) return false;
		}

		return true;
	}

	private void populate(String in_, String path_key_, String path_iv_, String algo_key_, String algo_cipher_, SecretKey key_, byte[] iv_)
	{
		_is_ok = true;

		_in = in_;
		_path_key = path_key_;
		_path_iv = path_iv_;
		_algo_key = algo_key_;
		_algo_cipher = algo_cipher_;

		if (key_ != null) _key = key_;
		if (iv_ != null) _iv = arrays.get_new(iv_);
	}
}