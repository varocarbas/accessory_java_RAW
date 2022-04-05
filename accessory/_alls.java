package accessory;

import java.util.HashMap;

//This class is the counterpart of _defaults dealing with arrays. The main reason for this difference is the 
//unclear, irregular treatment given by Java to the initialisation of global static final variables involving arrays.
//The rules in this class are, consequently, different than the ones in _defaults:
//- All the populations will happen in the load method (called at the very beginning) which calls the corresponding local methods.
//- All the variables will be accessed directly in this class (i.e., no local copies like in _defaults) via local methods.
//!!!
abstract class _alls 
{
	public static Class<?>[] _arrays_classes_small = null;
	public static Class<?>[] _arrays_classes_array = null;
	public static Class<?>[] _arrays_classes_numeric = null;
	public static Class<?>[] _arrays_classes = null;

	public static Class<?>[] _numbers_classes = null;
	
	public static Class<?>[] _generic_classes = null;
	public static HashMap<Class<?>, Class<?>[]> _generic_class_equivalents = null;
	public static String[] _generic_default_method_names = null;
	
	public static HashMap<String, Class<?>> _data_classes = null;
	public static HashMap<Class<?>, Class<?>> _data_compatible = null;

	public static HashMap<String, parent_db> _db_dbs = null;
	
	private static boolean _populated = false;
	
	public static void populate() 
	{ 
		if (_populated) return;
		
		_arrays_classes_small = arrays.populate_all_classes_small();
		_arrays_classes_array = arrays.populate_all_classes_array();
		_arrays_classes_numeric = arrays.populate_all_classes_numeric();
		_arrays_classes = arrays.populate_all_classes();

		_numbers_classes = numbers.populate_all_classes();
		
		_generic_classes = generic.populate_all_classes();
		_generic_class_equivalents = generic.populate_all_class_equivalents();
		_generic_default_method_names = generic.populate_all_default_methods();
		
		_data_classes = data.populate_all_classes();
		_data_compatible = data.populate_all_compatible();
		
		_db_dbs = db.populate_all_dbs();
		
		_populated = true;
	} 
}