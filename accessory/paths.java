package accessory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class paths 
{	
	public static final String SEPARATOR_DIR = get_dir_separator();
	public static final String HOME = get_home_dir();

	public static final String EXTENSION_TEXT = ".txt";
	public static final String EXTENSION_JAR = ".jar";
	public static final String EXTENSION_INI = ".ini";
	public static final String EXTENSION_LOG = ".log";

	static { _ini.load(); }

	public static boolean exists(String path_)
	{
		return (strings.is_ok(path_) && (new File(path_)).exists());
	}

	public static String build(ArrayList<String> pieces, boolean last_file_)
	{
		return (!arrays.is_ok(pieces) ? strings.DEFAULT : build(arrays.to_array(pieces), last_file_));
	}

	public static String build(String[] pieces, boolean last_file_)
	{
		if (!arrays.is_ok(pieces)) return strings.DEFAULT;

		String path = "";
		int last_i = pieces.length - 1;
		int tot = 0;

		for (int i = 0; i <= last_i; i++)
		{
			String piece = pieces[i];
			if (!strings.is_ok(piece)) continue;

			tot++;
			if (!last_file_ || i != last_i) piece = normalise_dir(piece);	
			path += piece;	
		}
		if (tot < 1) return strings.DEFAULT;

		if (last_file_ && tot == 1) path = HOME + normalise_file(path);
		if (!last_file_) path = normalise_dir(path);

		return path;
	}

	public static String normalise_dir(String dir_)
	{
		String dir = dir_;
		if (!strings.is_ok(dir)) dir = "";

		dir = dir.trim();
		if (dir.substring(dir.length() - 1) != SEPARATOR_DIR) dir += SEPARATOR_DIR;

		return dir;
	}

	public static String normalise_file(String file_)
	{
		String file = file_;
		if (!strings.is_ok(file)) return strings.DEFAULT;

		file = file.trim();
		if (strings.get_start(file, 1).equals(SEPARATOR_DIR))
		{ 
			file = strings.get_end(file, 1);
			if (!strings.is_ok(file)) return strings.DEFAULT;

			file = file.trim();
		}

		return file;
	}

	public static String get_main_dir(String what_)
	{
		String what = types.check_aliases(what_);
		HashMap<String, String> params = get_update_main_dir_params(what);

		return 
		(
			!arrays.is_ok(params) ? strings.DEFAULT : _config.get
			(
				params.get(keys.TYPE), params.get(keys.KEY)
			)
		);
	}

	public static <x> boolean update_main_dir(String what_, x val_)
	{
		String what = types.check_aliases(what_);
		HashMap<String, String> params = get_update_main_dir_params(what);

		return 
		(
			!arrays.is_ok(params) ? false : _config.update
			(
				params.get(keys.TYPE), params.get(keys.KEY), val_
			)
		);
	}

	static String get_default_dir(String what_)
	{
		if (!strings.is_ok(what_)) return strings.DEFAULT;
		if (strings.are_equivalent(what_, keys.APP)) return get_dir_app_default();

		String[] targets = { keys.CREDENTIALS, keys.INI, keys.LOG };

		for (String target: targets)
		{
			if (strings.are_equivalent(what_, target))
			{
				String[] parts = { HOME, target };
				if (target.equals(keys.LOG)) parts = new String[] { get_dir_app_default() };

				return build(parts, false);
			}
		}

		return strings.DEFAULT;
	}

	private static String get_dir_separator()
	{
		return (strings.contains("win", System.getProperty("os.name"), true) ? "\\" : "/");
	}

	private static String get_dir_app_default()
	{
		return normalise_dir(System.getProperty("user.dir"));
	}

	private static String get_home_dir()
	{
		return normalise_dir(System.getProperty("user.home"));
	}

	private static HashMap<String, String> get_update_main_dir_params(String key_)
	{
		if (!strings.is_ok(key_)) return null;

		String type = types._CONFIG_BASIC;
		if (key_.equals(types._CONFIG_CREDENTIALS_FILE_DIR)) type = types._CONFIG_CREDENTIALS;

		HashMap<String, String> output = new HashMap<String, String>();
		output.put(keys.KEY, key_);
		output.put(keys.TYPE, type);

		return output;
	}
}