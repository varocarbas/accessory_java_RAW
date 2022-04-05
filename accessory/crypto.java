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
	public static final String CIPHER = "cipher";
	public static final String KEY = "key";
	public static final String IV = "iv";

	public static final String DEFAULT_ID = _defaults.CRYPTO_ID;
	public static final String DEFAULT_ALGORITHM_CIPHER = _defaults.CRYPTO_ALGORITHM_CIPHER;
	public static final String DEFAULT_ALGORITHM_KEY = _defaults.CRYPTO_ALGORITHM_KEY;

	public String _algo_cipher = DEFAULT_ALGORITHM_CIPHER;
	public String _path_iv = strings.DEFAULT;
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

	public static String[] encrypt(String[] inputs_, String id_)
	{
		int size = arrays.get_size(inputs_);
		if (size < 1) return null;
		
		String[] output = new String[size];
		HashMap<String, String> paths = get_default_paths(id_);

		int last_i = size - 1;
		
		crypto instance = new crypto(inputs_[0], paths.get(KEY), paths.get(IV), null, null, null, null);

		int i = -1;
		
		while (i < last_i)
		{
			i++;
			instance._in = inputs_[i];
			
			instance.encrypt();
			if (!instance.is_ok()) return null;
			
			output[i] = instance._out;
		}
		
		return output;
	}
	
	public static String encrypt(String input_, String id_)
	{
		HashMap<String, String> paths = get_default_paths(id_);

		crypto instance = new crypto(input_, paths.get(KEY), paths.get(IV), null, null, null, null);
		if (instance.is_ok()) instance.encrypt();

		return (instance.is_ok() ? instance._out : strings.DEFAULT);
	}

	public static String decrypt(String input_, String id_)
	{
		HashMap<String, String> paths = get_default_paths(id_);

		crypto instance = new crypto(input_, paths.get(KEY), paths.get(IV), null, null, null, null);
		if (instance.is_ok()) instance.decrypt();

		return (instance.is_ok() ? instance._out : strings.DEFAULT);
	}

	public crypto(String in_, String path_key_, String path_iv_, String algo_key_, String algo_cipher_, SecretKey key_, byte[] iv_)
	{
		instantiate(in_, path_key_, path_iv_, algo_key_, algo_cipher_, key_, iv_);
	}

	public void encrypt()
	{
		if (!is_ok_encrypt()) return;

		try 
		{
			Cipher cipher = get_cipher();
			
			if (_key == null) _key = get_key();
			cipher.init(Cipher.ENCRYPT_MODE, _key, new SecureRandom());

			if (!arrays.is_ok(_iv)) _iv = cipher.getIV();
			if (!key_iv_to_file(_key, _iv)) return;	

			_out = Base64.getEncoder().encodeToString(cipher.doFinal(_in.getBytes()));
		} 
		catch (Exception e) { manage_error(types.ERROR_CRYPTO_ENCRYPT, e); }

		_is_ok = true;
	}

	public void decrypt()
	{
		if (!is_ok_decrypt()) return;

		try 
		{
			Cipher cipher = get_cipher();

			cipher.init(Cipher.DECRYPT_MODE, _key, new IvParameterSpec(_iv));

			_out = new String(cipher.doFinal(Base64.getDecoder().decode(_in)));
		} 
		catch (Exception e) { manage_error(types.ERROR_CRYPTO_DECRYPT, e); }

		_is_ok = true;
	}

	private static HashMap<String, String> get_default_paths(String id_)
	{
		HashMap<String, String> output = new HashMap<String, String>();

		String[] whats = new String[] { KEY, IV };

		for (String what: whats)
		{
			output.put(what, get_default_path(id_, what));
		}

		return output;
	}

	private static String get_default_path(String id_, String what_)
	{
		String id = (strings.is_ok(id_) ? id_ : DEFAULT_ID);

		return paths.build(new String[] { paths.get_dir(paths.DIR_CRYPTO), get_file_full(id + misc.SEPARATOR_NAME + what_) }, true);
	}

	private SecretKey key_from_file()
	{
		SecretKey output = null;

		Object temp = io.file_to_object(_path_key);
		if (temp != null && io._is_ok) 
		{
			try
			{
				output = (SecretKey)temp;	
			}
			catch (Exception e) 
			{ 
				manage_error(types.ERROR_CRYPTO_KEY, e);

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
		if (arrays.is_ok(temp) && io._is_ok) output = arrays.get_new(temp);
		else manage_error();

		return output;
	}

	private boolean key_iv_to_file(SecretKey key_, byte[] iv_)
	{
		io.object_to_file(_path_key, key_);

		boolean is_ok = io._is_ok;
		if (is_ok) 
		{
			io.bytes_to_file(_path_iv, iv_);
			is_ok = io._is_ok;
		}

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

	private void manage_error(String type_, Exception e_)
	{	
		manage_error();

		HashMap<String, String> items = new HashMap<String, String>();
		items.put(generic.TYPE, type_);
		items.put("in", _in);
		items.put("path_key", _path_key);
		items.put("path_iv", _path_iv);
		items.put("algo_key", _algo_key);
		items.put("algo_cipher", _algo_cipher);

		errors.manage(items, false);
	}

	private void manage_error()
	{
		_is_ok = false;

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

		populate(in_, path_key_, path_iv_, _temp_string1, _temp_string2, key_, iv_);
	}

	private boolean is_ok(String in_, String path_key_, String path_iv_, String algo_key_, String algo_cipher_, SecretKey key_, byte[] iv_)
	{
		if (!is_ok_common(in_, path_key_, path_iv_, algo_key_, algo_cipher_, true)) return false;

		_temp_string1 = (strings.is_ok(algo_key_) ? algo_key_ : DEFAULT_ALGORITHM_KEY);
		_temp_string2 = (strings.is_ok(algo_cipher_) ? algo_cipher_ : DEFAULT_ALGORITHM_CIPHER);

		return true;
	}

	private boolean is_ok_common(String in_, String path_key_, String path_iv_, String algo_key_, String algo_cipher_, boolean is_start_)
	{
		boolean is_ok = (in_ != null && strings.is_ok(path_key_) && strings.is_ok(path_iv_));

		if (is_ok && !is_start_) is_ok = (strings.is_ok(algo_key_) && strings.is_ok(algo_cipher_));

		return is_ok;
	}

	private boolean is_ok_encrypt()
	{
		_is_ok = false; //To be set to true after a successful encryption.

		return is_ok_common(_in, _path_key, _path_iv, _algo_key, _algo_cipher, false);
	}

	private boolean is_ok_decrypt()
	{
		_is_ok = false; //To be set to true after a successful decryption.

		if (!is_ok_common(_in, _path_key, _path_iv, _algo_key, _algo_cipher, false)) return false;

		if (_key == null) 
		{
			_key = key_from_file();
			if (_key == null) return false;
		}

		if (_iv == null) 
		{
			_iv = iv_from_file();
			if (_iv == null) return false;
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