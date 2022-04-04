package accessory;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class crypto extends parent 
{	
	public static final String CIPHER = "cipher";
	public static final String KEY = "key";
	
	public static final String DEFAULT_ID = _defaults.CRYPTO_ID;
	public static final String DEFAULT_ALGORITHM_CIPHER = _defaults.CRYPTO_ALGORITHM_CIPHER;
	public static final String DEFAULT_ALGORITHM_KEY = _defaults.CRYPTO_ALGORITHM_KEY;

	public String _algo_cipher = DEFAULT_ALGORITHM_CIPHER;
	public String _path_cipher = strings.DEFAULT;
	public String _algo_key = DEFAULT_ALGORITHM_KEY;
	public String _path_key = strings.DEFAULT;
	public String _in = strings.DEFAULT;
	public String _out = strings.DEFAULT;
	public String _path = strings.DEFAULT;
	public SecretKey _key = null;
	public byte[] _iv = null;
	
	public String toString() { return strings.DEFAULT; }
	public boolean is_ok() { return _is_ok; }

	public static String get_extension()
	{
		return config.get_crypto(types.CONFIG_CRYPTO_FILE_EXTENSION);
	}
	
	public static String get_file_full(String id_)
	{
		return paths.get_file_full((strings.is_ok(id_) ? id_ : DEFAULT_ID), get_extension());
	}
	
	public static String decrypt(String input_, String id_)
	{
		String output = strings.DEFAULT;
		
		return output;
	}

	public static String encrypt(String input_, String id_)
	{
		String output = strings.DEFAULT;
	
		String path_cipher = get_default_path(id_, CIPHER);
		String path_key = get_default_path(id_, KEY);

		crypto instance = new crypto(input_, null, path_cipher, null, path_key, null, null);
		instance.encrypt();
		if (!instance.is_ok()) return output;
	
		errors._triggered = false;	
		if (errors._triggered)
		{
			errors._triggered = false;
			
			return output;
		}
		
		errors._triggered = false;
		
		output = instance._out;
		
		return output;
	}
	
	public crypto(String in_, String algo_cipher_, String path_cipher_, String algo_key_, String path_key_, SecretKey key_, byte[] iv_)
	{
		instantiate(in_, algo_cipher_, path_cipher_, algo_key_, path_key_, key_, iv_);
	}
	
	public void decrypt()
	{
		if (!strings.is_ok(_in)) return;
		
		try 
		{

		} 
		catch (Exception e) { manage_error(types.ERROR_CRYPTO_DECRYPT, e); }
	}

	public void encrypt()
	{
		if (!strings.is_ok(_in)) return;
		
		try 
		{
			Cipher cipher = get_cipher();
			_key = get_key();
			
			cipher.init(Cipher.ENCRYPT_MODE, _key, new SecureRandom());
			
			_iv = cipher.getIV();
			_out = Base64.getEncoder().encodeToString(cipher.doFinal(_in.getBytes()));
			
			errors._triggered = false;
		
			io.bytes_to_file(_path_cipher, _iv);
			io.object_to_file(_path_key, _key);
			
			if (errors._triggered)
			{
				errors._triggered = false;
				_is_ok = false;
				
				manage_error();
			}
		} 
		catch (Exception e) { manage_error(types.ERROR_CRYPTO_ENCRYPT, e); }
	}

	private static String get_default_path(String id_, String what_)
	{
		String id = (strings.is_ok(id_) ? id_ : DEFAULT_ID);
		
		return paths.build
		(
			new String[] 
			{
				paths.get_dir(paths.DIR_CRYPTO), 
				get_file_full(id + misc.SEPARATOR_NAME + what_) 
			}, 
			true
		);
	}
	
	private void info_to_file(String path_, SecretKey key_, byte[] iv_)
	{
		
	}

	private Object[] info_from_file(String path_)
	{
		Object[] output = null;
		
		return output;
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
		catch (Exception e) { manage_error(types.ERROR_CRYPTO_KEY, e); }

		return output;
	}
	
	private Cipher get_cipher()
	{
		Cipher cipher = null;

		try
		{
			cipher = Cipher.getInstance(_algo_cipher);
		}
		catch (Exception e) { manage_error(types.ERROR_CRYPTO_CIPHER, e); }
		
		return cipher;
	}
	
	private void manage_error()
	{
		_is_ok = false;
		
		_in = strings.DEFAULT;
		_out = strings.DEFAULT;
		_path_cipher = strings.DEFAULT;
		_path_key = strings.DEFAULT;
		_key = null;
		_iv = null;
	}
	
	private void manage_error(String type_, Exception e_)
	{	
		manage_error();
		
		HashMap<String, String> items = new HashMap<String, String>();
		items.put(generic.TYPE, type_);
		items.put("in", _in);
		items.put("algo_cipher", _algo_cipher);
		items.put("path_cipher", _path_cipher);
		items.put("algo_key", _algo_key);
		items.put("path_key", _path_key);
		
		errors.manage(items, false);
	}	

	private void instantiate(String in_, String algo_cipher_, String path_cipher_, String algo_key_, String path_key_, SecretKey key_, byte[] iv_)
	{
		instantiate_common();
		if (!is_ok(in_, algo_cipher_, path_cipher_, algo_key_, path_key_, key_, iv_)) return;

		populate(in_, _temp_string1, path_cipher_, _temp_string2, path_key_, key_, iv_);

	}

	private boolean is_ok(String in_, String algo_cipher_, String path_cipher_, String algo_key_, String path_key_, SecretKey key_, byte[] iv_)
	{
		if (!strings.is_ok(in_) || !paths.exists(path_cipher_) || !paths.exists(path_key_)) return false;
		
		_temp_string1 = (strings.is_ok(algo_cipher_) ? algo_cipher_ : DEFAULT_ALGORITHM_CIPHER);
		_temp_string2 = (strings.is_ok(algo_key_) ? algo_key_ : DEFAULT_ALGORITHM_KEY);
		
		return true;
	}

	private void populate(String in_, String algo_cipher_, String path_cipher_, String algo_key_, String path_key_, SecretKey key_, byte[] iv_)
	{
		_is_ok = true;

		_in = in_;
		_algo_cipher = algo_cipher_;
		_path_cipher = path_cipher_;
		_algo_key = algo_key_;
		_path_cipher = path_cipher_;
		if (key_ != null) _key = key_;
		if (iv_ != null) _iv = arrays.get_new(iv_);
	}
}