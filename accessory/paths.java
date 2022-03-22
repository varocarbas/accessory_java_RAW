package accessory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class paths 
{	
	public static final String DIR_HOME = get_home_dir();
	public static final String DIR_SEPARATOR = get_dir_separator();
	
	public static final String EXTENSION_TEXT = ".txt";
	public static final String EXTENSION_JAR = ".jar";
	public static final String EXTENSION_INI = ".ini";
	public static final String EXTENSION_LOG = ".log";

	public static final String DEFAULT_DIR_APP = _defaults.DIR_APP;
	public static final String DEFAULT_DIR_INI = _defaults.DIR_INI;
	public static final String DEFAULT_DIR_LOGS = _defaults.DIR_LOGS;
	public static final String DEFAULT_DIR_CREDENTIALS = _defaults.CREDENTIALS_FILE_DIR;
	
	static { ini.load(); }

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

		if (last_file_ && tot == 1) path = DIR_HOME + normalise_file(path);
		if (!last_file_) path = normalise_dir(path);

		return path;
	}

	public static String normalise_dir(String dir_)
	{
		String dir = dir_;
		if (!strings.is_ok(dir)) dir = "";

		dir = dir.trim();
		if (dir.substring(dir.length() - 1) != DIR_SEPARATOR) dir += DIR_SEPARATOR;

		return dir;
	}

	public static String normalise_file(String file_)
	{
		String file = file_;
		if (!strings.is_ok(file)) return strings.DEFAULT;

		file = file.trim();
		if (strings.get_start(file, 1).equals(DIR_SEPARATOR))
		{ 
			file = strings.get_end(file, 1);
			if (!strings.is_ok(file)) return strings.DEFAULT;

			file = file.trim();
		}

		return file;
	}

	public static String get_main_dir(String what_)
	{
		String output = strings.DEFAULT;
		
		String what = types.check_what(what_);
		if (!strings.is_ok(what)) return output;
		
		HashMap<String, String> params = get_update_main_dir_params(what);
		if (arrays.is_ok(params)) output = config.get(params.get(generic.TYPE), params.get(generic.KEY));
		
		return output;
	}
	
	public static <x> boolean update_main_dir(String what_, x val_)
	{
		boolean output = false;
		
		String what = types.check_what(what_);
		if (!strings.is_ok(what)) return output;
		
		HashMap<String, String> params = get_update_main_dir_params(what);
		if (arrays.is_ok(params)) output = config.update(params.get(generic.TYPE), params.get(generic.KEY), val_);
		
		return output;
	}

	static String get_default_dir(String what_)
	{
		String what = types.check_what(what_);
		
		if (!strings.is_ok(what)) return strings.DEFAULT;
		if (strings.are_equal(what, types.WHAT_DIR_APP)) return get_dir_app_default();

		String[] targets = { types.WHAT_DIR_CREDENTIALS, types.WHAT_DIR_INI, types.WHAT_DIR_LOGS };

		for (String target: targets)
		{
			if (strings.are_equal(what, target))
			{
				String[] parts = { DIR_HOME, target };
				if (target.equals(types.WHAT_DIR_LOGS)) parts = new String[] { get_dir_app_default() };

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

		String type = types.CONFIG_BASIC;
		if (key_.equals(types.CONFIG_CREDENTIALS_FILE_DIR)) type = types.CONFIG_CREDENTIALS;

		HashMap<String, String> output = new HashMap<String, String>();
		output.put(generic.KEY, key_);
		output.put(generic.TYPE, type);

		return output;
	}
}