package accessory;

import java.util.HashMap;

public abstract class parent_ini 
{
	protected boolean _populated = false;
	
	protected abstract void populate_first_basic();
	protected abstract void populate_first_alls();
	protected abstract void populate_first_defaults();
	protected abstract void populate_config();
	protected abstract void populate_db();
	
	public static void manage_error(String type_)
	{
		errors._exit = true;
		
		HashMap<String, String> info = new HashMap<String, String>();
		info.put(get_generic_key(types.WHAT_TYPE), (strings.is_ok(type_) ? type_ : strings.DEFAULT));
		
		errors.manage(info);
	}
	
	//The keys in the generic class might not have been loaded yet, so better getting them directly from types.
	public static String get_generic_key(String what_)
	{
		String what = types.check_type(what_, types.get_subtypes(types.WHAT));
		
		return (strings.is_ok(what) ? types.what_to_key(what) : strings.DEFAULT);
	}
	
	//Method expected to be called every time a non-ini, non-first, non-parent abstract class is loaded.
	//It has to include all the load() methods of all the ini classes.
	protected static void load_internal(parent_ini instance_) 
	{
		if (instance_._populated) return;
		
		instance_.load_internal(); 
		instance_._populated = true;
	}
	
	protected void load_internal() 
	{
		populate_first();
		
		populate_config();
		populate_db();
	}
	
	//Loading all the first classes, the ones whose names start with "_".
	private void populate_first()
	{
		populate_first_basic();
		populate_first_alls();
		populate_first_defaults();
	}
}