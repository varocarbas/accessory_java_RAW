package accessory;

public class db_field 
{
	public boolean _is_ok = true;
	
	public data _data = null;
	public Object _default = null;
	public String[] _further = null;

	public String toString()
	{
		String output = "";
		if (generic.is_ok(_data)) output = _data.toString();
		
		if (generic.is_ok(_further)) 
		{
			if (strings.is_ok(output)) output += misc.SEPARATOR_ITEM;
			output += arrays.to_string(_further, null);
		} 
		if (!strings.is_ok(output)) return strings.DEFAULT;

		return (misc.BRACKET_MAIN_OPEN + output + misc.BRACKET_MAIN_CLOSE);
	}

	public boolean equals(db_field field2_)
	{
		if (!is_ok(field2_)) return false;
		
		return 
		(
			data.are_equal(_data, field2_._data) &&
			arrays.are_equal(_further, field2_._further)
		);		
	}
	
	public static String to_string(db_field field_)
	{
		return (is_ok(field_) ? field_.toString() : strings.DEFAULT);	
	}
	
	public static boolean are_equal(db_field field1_, db_field field2_)
	{
		return generic.are_equal(field1_, field2_);
	}
	
	public static boolean is_ok(db_field input_)
	{
		return (input_ != null && is_ok(input_._data, input_._default, input_._further));
	}

	public static <x> boolean complies(x val_, db_field field_)
	{
		if (!generic.is_ok(val_) || !is_ok(field_)) return false;
		
		size size = new size(field_._data._size);
		if (data.is_numeric(field_._data._type))
		{
			size._max = Math.pow(10, size._max);
			size._min = -1 * size._max;
		}

		return data.complies(val_, new data(field_._data._type, size));
	}

	public db_field(db_field input_)
	{
		_is_ok = false;
		if (!is_ok(input_)) return;

		_is_ok = true;
		_data = new data(input_._data);
		_default = input_._default;
		_further = (String[])arrays.get_new(input_._further);
	}

	public db_field(data data_, Object default_, String[] further_)
	{
		_is_ok = false;
		if (!is_ok(data_, default_, further_)) return;

		_is_ok = true;
		_data = new data(data_);
		_default = default_;
		_further = (String[])arrays.get_new(further_);
	}

	public static boolean further_is_ok(String further_)
	{
		return (strings.is_ok(check_further(further_)));
	}
	
	public static String check_further(String further_)
	{
		return types.check_subtype(further_, types.get_subtypes(types.DB_FIELD_FURTHER, null), null, null);
	}
	
	private static boolean is_ok(data data_, Object default_, String[] further_)
	{
		return (generic.is_ok(data_) && further_is_ok(further_));
	}
	
	private static boolean further_is_ok(String[] further_)
	{
		if (!arrays.is_ok(further_)) return true;
		
		for (String item: further_)
		{
			if (!further_is_ok(item)) return false;
		}
		
		return true;
	}
}