package accessory;

public class _ini extends parent_ini
{	
	private static _ini _instance = new _ini();

	public _ini() { }

	public static boolean is_populated() { return _instance._populated; }
	public static boolean includes_legacy() { return _instance._includes_legacy; }
	public static String get_name_start() { return _instance._name; }

	//One of the start() overloads below these lines has to be called before using any of the resources of this library.
	//In case of emulating this structure, the order in which the specific start() methods are being called does matter.
	//One of the accessory._ini.start() methods below is always called first. Afterwards, it's the turn of the chosen start() 
	//method from the main library/app. And lastly, the start() methods from other additional libraries come into picture.
	//The includes_legacy_ variable can be safely ignored in those emulations because only its value in the accessory._ini instance matters.

	public static void start() { if (!_instance._populated) start(null, false); }

	public static void start(String name_, boolean includes_legacy_) { if (!_instance._populated) _instance.populate_all(name_, includes_legacy_); }

	public static void start(String name_, String dbs_user_, String dbs_host_, boolean dbs_encrypted_, boolean includes_legacy_) { if (!_instance._populated) _instance.populate_all(name_, dbs_user_, null, null, dbs_host_, dbs_encrypted_, includes_legacy_); }

	public static void start(String name_, String dbs_username_, String dbs_password_, String dbs_host_, boolean includes_legacy_) { if (!_instance._populated) _instance.populate_all(name_, null, dbs_username_, dbs_password_, dbs_host_, false, includes_legacy_); }
}