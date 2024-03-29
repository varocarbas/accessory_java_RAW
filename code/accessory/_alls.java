package accessory;

import java.util.ArrayList;
import java.util.HashMap;

//This class includes all the array constants to be loaded at the very beginning, except for the ones included in _basic. 
//Rules for all the constants in this class:
//- All the values will be assigned in the main method below by calling the corresponding local methods in other classes.
//- All these constants will be accessed directly here, via the corresponding local methods (i.e., no local copies).

public class _alls extends parent_first
{
	private static _alls _instance = new _alls(); 

	public _alls() { }
	public static void populate() { _instance.populate_internal_common(); }

	public static Class<?>[] ARRAYS_CLASSES_SMALL = null;
	public static Class<?>[] ARRAYS_CLASSES_ARRAY = null;
	public static Class<?>[] ARRAYS_CLASSES_NUMERIC = null;
	public static Class<?>[] ARRAYS_CLASSES = null;

	public static HashMap<Boolean, String[]> STRINGS_BOOLEANS = null;
	public static char[] STRINGS_EXPS = null;

	public static Class<?>[] NUMBERS_CLASSES = null;

	public static Class<?>[] GENERIC_CLASSES = null;
	public static HashMap<Class<?>, Class<?>[]> GENERIC_CLASSES_EQUIVALENTS = null;
	public static String[] GENERIC_DEFAULT_METHOD_NAMES = null;

	public static HashMap<String, Class<?>> DATA_CLASSES = null;
	public static HashMap<Class<?>, Class<?>[]> DATA_COMPATIBLE = null;

	public static String[] DB_SOURCES_INBUILT = null;
	public static String[] DB_QUERIES_DATA = null;

	public static ArrayList<String> DB_SQL_TYPES = null;
	
	public static HashMap<String, String[]> DB_WHERE_OPERANDS = null;
	public static HashMap<String, String[]> DB_WHERE_OPERANDS_LIKE = null;
	public static HashMap<String, String[]> DB_WHERE_OPERANDS_NOT_LIKE = null;
	public static HashMap<String, String[]> DB_WHERE_LINKS = null;

	public static String[] DB_FIELD_TYPES_NO_SIZE = null;
	
	public static String[] DB_FIELD_SERIALISATION_LABELS = null;
	
	public static String[] CONFIG_NOT_UPDATE = null;

	public static String[] CRYPTO_WHATS = null;
	
	public static String[] MISC_SUPPORTED_SOUND_EXTENSIONS = null;

	protected void populate_internal() 
	{ 
		ARRAYS_CLASSES_SMALL = arrays.populate_all_classes_small();
		ARRAYS_CLASSES_ARRAY = arrays.populate_all_classes_array();
		ARRAYS_CLASSES_NUMERIC = arrays.populate_all_classes_numeric();
		ARRAYS_CLASSES = arrays.populate_all_classes();

		STRINGS_BOOLEANS = strings.populate_all_booleans();
		STRINGS_EXPS = strings.populate_all_exps();

		NUMBERS_CLASSES = numbers.populate_all_classes();

		GENERIC_CLASSES = generic.populate_all_classes();
		GENERIC_CLASSES_EQUIVALENTS = generic.populate_all_class_equivalents();
		GENERIC_DEFAULT_METHOD_NAMES = generic.populate_all_default_methods();

		DATA_CLASSES = data.populate_all_classes();
		DATA_COMPATIBLE = data.populate_all_compatible();

		DB_SOURCES_INBUILT = db_common.populate_all_sources_inbuilt();
		DB_QUERIES_DATA = db.populate_all_queries_data();

		DB_SQL_TYPES = db_sql.populate_all_types();
		
		DB_WHERE_OPERANDS = db_where.populate_all_operands();
		DB_WHERE_OPERANDS_LIKE = db_where.populate_all_operands_like();
		DB_WHERE_OPERANDS_NOT_LIKE = db_where.populate_all_operands_not_like();
		DB_WHERE_LINKS = db_where.populate_all_links();

		DB_FIELD_TYPES_NO_SIZE = db_field.populate_all_types_no_size();

		DB_FIELD_SERIALISATION_LABELS = db_field.populate_all_serialisation_labels();
		
		CONFIG_NOT_UPDATE = config.populate_all_not_update();
		
		CRYPTO_WHATS = crypto.populate_all_whats();
		
		MISC_SUPPORTED_SOUND_EXTENSIONS = misc.populate_all_supported_sound_extensions();
	} 
}