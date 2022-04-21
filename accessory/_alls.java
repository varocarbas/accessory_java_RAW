package accessory;

import java.util.HashMap;

//This class includes all the array constants to be loaded at the very beginning, except for the ones included in _basic. 
//Rules for all the constants in this class:
//- All the values will be assigned in the main method below by calling the corresponding methods in other classes.
//- All these constants will be accessed directly here, via the corresponding local methods (i.e., no local copies).

abstract class _alls 
{
	public static String[] TYPES_ALL = null;
	public static String[] TYPES_CONFIG_BOOLEAN = null;
	
	public static Class<?>[] ARRAYS_CLASSES_SMALL = null;
	public static Class<?>[] ARRAYS_CLASSES_ARRAY = null;
	public static Class<?>[] ARRAYS_CLASSES_NUMERIC = null;
	public static Class<?>[] ARRAYS_CLASSES = null;
	
	public static HashMap<Boolean, String[]> STRINGS_BOOLEANS = null;
	
	public static Class<?>[] NUMBERS_CLASSES = null;
	
	public static Class<?>[] GENERIC_CLASSES = null;
	public static HashMap<Class<?>, Class<?>[]> GENERIC_CLASSES_EQUIVALENTS = null;
	public static String[] GENERIC_DEFAULT_METHOD_NAMES = null;
	public static HashMap<String, String> GENERIC_KEYS = null;
	
	public static HashMap<String, Class<?>> DATA_CLASSES = null;
	public static HashMap<Class<?>, Class<?>> DATA_COMPATIBLE = null;

	public static HashMap<String, String[]> DB_WHERE_OPERANDS = null;
	public static HashMap<String, String[]> DB_WHERE_LINKS = null;
	
	public static String[] CONFIG_NOT_UPDATE = null;
	
	private static boolean _populated = false;
	
	public static void populate() 
	{ 
		if (_populated) return;

		TYPES_ALL = types.populate_all_subtypes();
		TYPES_CONFIG_BOOLEAN = types.populate_all_config_boolean();
		
		ARRAYS_CLASSES_SMALL = arrays.populate_all_classes_small();
		ARRAYS_CLASSES_ARRAY = arrays.populate_all_classes_array();
		ARRAYS_CLASSES_NUMERIC = arrays.populate_all_classes_numeric();
		ARRAYS_CLASSES = arrays.populate_all_classes();
		
		STRINGS_BOOLEANS = strings.populate_all_booleans();
		
		NUMBERS_CLASSES = numbers.populate_all_classes();
		
		GENERIC_CLASSES = generic.populate_all_classes();
		GENERIC_CLASSES_EQUIVALENTS = generic.populate_all_class_equivalents();
		GENERIC_DEFAULT_METHOD_NAMES = generic.populate_all_default_methods();
		GENERIC_KEYS = generic.populate_all_keys();
		
		DATA_CLASSES = data.populate_all_classes();
		DATA_COMPATIBLE = data.populate_all_compatible();
		
		DB_WHERE_OPERANDS = db_where.populate_all_operands();
		DB_WHERE_LINKS = db_where.populate_all_links();
		
		_populated = true;
	} 
}