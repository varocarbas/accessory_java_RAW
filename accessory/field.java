package accessory;

public class field 
{
	public data _data = null;
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

	public boolean equals(field field2_)
	{
		if (!is_ok(field2_)) return false;
		
		return 
		(
			data.are_equal(_data, field2_._data) &&
			arrays.are_equal(_further, field2_._further)
		);		
	}
	
	public static String to_string(field field_)
	{
		return (is_ok(field_) ? field_.toString() : strings.DEFAULT);	
	}
	
	public static boolean are_equal(field field1_, field field2_)
	{
		boolean is_ok1 = is_ok(field1_);
		boolean is_ok2 = is_ok(field2_);
		
		return ((!is_ok1 || !is_ok2) ? (is_ok1 == is_ok2) : field1_.equals(field2_));
	}
	
	public static boolean is_ok(field input_)
	{
		if (input_ == null) return false;

		return is_ok(input_._data, input_._further);
	}

	public static <x> boolean complies(x val_, field field_)
	{
		return ((!generic.is_ok(val_) || !is_ok(field_)) ? false : data.complies(val_, field_._data));
	}

	public field(field input_)
	{
		if (!is_ok(input_)) return;

		_data = new data(input_._data);
		_further = (String[])arrays.get_new(input_._further);
	}

	public field(data data_, String[] further_)
	{
		if (!is_ok(data_, further_)) return;

		_data = new data(data_);
		_further = (String[])arrays.get_new(further_);
	}

	private static boolean is_ok(data data_, String[] further_)
	{
		return generic.is_ok(data_);
	}
}