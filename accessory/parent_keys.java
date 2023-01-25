package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public abstract class parent_keys 
{
	private static ArrayList<String> ROOTS = null;
	private static HashMap<String, String>[] TYPES_KEYS = null;

	public static String get_startup_key(String type_, String root_) 
	{
		HashMap<String, String> types_keys = get_startup_common(type_, root_);

		return (types_keys.containsKey(type_) ? types_keys.get(type_) : strings.DEFAULT);
	}

	public static String get_startup_type(String key_, String root_) 
	{
		HashMap<String, String> types_keys = get_startup_common(key_, root_);

		return (types_keys.containsValue(key_) ? (String)arrays.get_key(types_keys, key_) : strings.DEFAULT);
	}

	public static String get_key(String what_) { return _types.what_to_key(what_); }

	public static String get_key(String type_, String root_) { return _types.check_type(type_, _types.get_subtypes(root_), _types.ACTION_REMOVE, root_); }

	protected boolean _populated = false;

	protected abstract HashMap<String, String> get_startup_roots();

	protected abstract HashMap<String, HashMap<String, String>> get_startup_merged_roots();

	protected abstract HashMap<String, HashMap<String, String>> get_startup_merged_types();

	@SuppressWarnings("unchecked")
	protected void populate_internal()
	{
		if (_populated) return;

		ROOTS = (arrays.is_ok(ROOTS) ? new ArrayList<String>(ROOTS) : new ArrayList<String>());

		ArrayList<HashMap<String, String>> types_keys = (arrays.is_ok(TYPES_KEYS) ? arrays.to_arraylist(TYPES_KEYS) : new ArrayList<HashMap<String, String>>());

		types_keys = populate_internal_roots(types_keys);
		types_keys = populate_internal_merged_roots(types_keys);
		types_keys = populate_internal_merged_types(types_keys);
		
		TYPES_KEYS = (types_keys.size() > 0 ? arrays.to_array(types_keys) : (HashMap<String, String>[])new HashMap[1]);
	}

	private ArrayList<HashMap<String, String>> populate_internal_roots(ArrayList<HashMap<String, String>> types_keys_)
	{
		HashMap<String, String> roots = get_startup_roots();
		if (!arrays.is_ok(roots)) return types_keys_;

		for (Entry<String, String> item: roots.entrySet())
		{
			String root = item.getKey();
			String root_key = item.getValue();
			if (!strings.are_ok(new String[] { root, root_key })) continue;

			types_keys_ = populate_internal_loop(root, root, root_key, types_keys_, true);	
		}

		return types_keys_;
	}

	private ArrayList<HashMap<String, String>> populate_internal_merged_roots(ArrayList<HashMap<String, String>> types_keys_) { return populate_internal_merged(types_keys_, true); }

	private ArrayList<HashMap<String, String>> populate_internal_merged_types(ArrayList<HashMap<String, String>> types_keys_) { return populate_internal_merged(types_keys_, false); }
	
	private ArrayList<HashMap<String, String>> populate_internal_merged(ArrayList<HashMap<String, String>> types_keys_, boolean are_roots_)
	{
		HashMap<String, HashMap<String, String>> merged = (are_roots_ ? get_startup_merged_roots() : get_startup_merged_types());
		if (!arrays.is_ok(merged)) return types_keys_;

		for (Entry<String, HashMap<String, String>> item: merged.entrySet())
		{
			String root_id = item.getKey();
			if (!strings.is_ok(root_id)) continue;

			HashMap<String, String> items2 = item.getValue();
			if (!arrays.is_ok(items2)) continue;

			for (Entry<String, String> item2: items2.entrySet())
			{
				String root_type = item2.getKey();
				String root_key = item2.getValue();
				if (!strings.are_ok(new String[] { root_type, root_key })) continue;

				types_keys_ = populate_internal_loop(root_id, root_type, root_key, types_keys_, are_roots_);				
			}
		}

		return types_keys_;
	}
	
	@SuppressWarnings("unchecked")
	private ArrayList<HashMap<String, String>> populate_internal_loop(String root_id_, String root_type_, String root_key_, ArrayList<HashMap<String, String>> types_keys_, boolean is_root_)
	{
		int i = ROOTS.indexOf(root_id_);

		if (i < 0) 
		{
			ROOTS.add(root_id_);

			i = ROOTS.size() - 1;
		}

		HashMap<String, String> items = new HashMap<String, String>();

		String[] subtypes = (is_root_ ? _types.get_subtypes(root_type_) : null);

		if (arrays.is_ok(subtypes))
		{
			for (String subtype: subtypes) { items = add_item(subtype, get_key(subtype, root_key_), items); }
		}
		else items = add_item(root_type_, get_key(root_type_, root_key_), items);

		if (i == types_keys_.size()) types_keys_.add(items);
		else types_keys_.set(i, (HashMap<String, String>)arrays.add(types_keys_.get(i), items));

		return types_keys_;
	}

	private HashMap<String, String> add_item(String type_, String key_, HashMap<String, String> items_)
	{
		if (strings.is_ok(key_)) items_.put(type_, key_); 

		return items_;
	}

	private static HashMap<String, String> get_startup_common(String type_key_, String root_) 
	{
		HashMap<String, String> output = new HashMap<String, String>();
		if (!strings.are_ok(new String[] { type_key_, root_ })) return output;

		int i = ROOTS.indexOf(root_);

		return (i >= 0 ? new HashMap<String, String>(TYPES_KEYS[i]) : new HashMap<String, String>());
	}
}