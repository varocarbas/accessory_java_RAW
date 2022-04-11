package accessory;

import java.util.HashMap;

//This class includes all the array constants to be loaded at the very beginning, except for the ones included in _basic. 
//Rules for all the constants in this class:
//- All the values will be assigned in the main method below by calling the corresponding methods in other classes.
//- All these constants will be accessed directly here, via the corresponding local methods (i.e., no local copies).

abstract class _alls 
{
	public static Class<?>[] ARRAYS_CLASSES_SMALL = null;
	public static Class<?>[] ARRAYS_CLASSES_ARRAY = null;
	public static Class<?>[] ARRAYS_CLASSES_NUMERIC = null;
	public static Class<?>[] ARRAYS_CLASSES = null;

	public static Class<?>[] NUMBERS_CLASSES = null;
	
	public static Class<?>[] GENERIC_CLASSES = null;
	public static HashMap<Class<?>, Class<?>[]> GENERIC_CLASSES_EQUIVALENTS = null;
	public static String[] GENERIC_DEFAULT_METHOD_NAMES = null;
	public static HashMap<String, String> GENERIC_KEYS = null;
	
	public static HashMap<String, Class<?>> DATA_CLASSES = null;
	public static HashMap<Class<?>, Class<?>> DATA_COMPATIBLE = null;

	public static String[] DB_SETUPS = null;
	public static HashMap<String, parent_db> DB_DBS = null;
	
	private static boolean _populated = false;
	
	public static void populate() 
	{ 
		if (_populated) return;
		
		ARRAYS_CLASSES_SMALL = arrays.populate_all_classes_small();
		ARRAYS_CLASSES_ARRAY = arrays.populate_all_classes_array();
		ARRAYS_CLASSES_NUMERIC = arrays.populate_all_classes_numeric();
		ARRAYS_CLASSES = arrays.populate_all_classes();

		NUMBERS_CLASSES = numbers.populate_all_classes();
		
		GENERIC_CLASSES = generic.populate_all_classes();
		GENERIC_CLASSES_EQUIVALENTS = generic.populate_all_class_equivalents();
		GENERIC_DEFAULT_METHOD_NAMES = generic.populate_all_default_methods();
		GENERIC_KEYS = generic.populate_all_keys();
		
		DATA_CLASSES = data.populate_all_classes();
		DATA_COMPATIBLE = data.populate_all_compatible();
		
		DB_SETUPS = db.populate_all_setups();
		DB_DBS = db.populate_all_dbs();
		
		_populated = true;
	} 
}