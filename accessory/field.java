package accessory;

public class field 
{
	public data _data = null;
	public String[] _further = null;

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
		return data.is_ok(data_);
	}
}