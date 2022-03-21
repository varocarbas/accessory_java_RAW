package accessory;

public class db_order 
{
	public boolean _is_ok = false;
	
	public String _source = strings.DEFAULT;
	public String _value = strings.DEFAULT; //Always stored as a field and only converted to col before outputting a string.
	public String _order = _defaults.DB_ORDER;
	public boolean _is_field = _defaults.DB_ORDER_FIELD; //_value being a field/col vs. something else like a condition.
	
	private static String _source_temp = strings.DEFAULT;
	
	public db_order(db_order input_)
	{
		_is_ok = false;
		if (!is_ok(input_)) return;

		_is_ok = true;
		_source = _source_temp;
		_value = input_._value;
		_order = input_._order;
		_is_field = input_._is_field;
	}
	
	public db_order(String source_, String value_, String order_, boolean is_field_)
	{
		_is_ok = false;
		if (!is_ok(source_, value_, order_)) return;

		_is_ok = true;
		_source = _source_temp;
		_value = value_;
		_order = check_order(order_);
		_is_field = is_field_;
	}
	
	public String toString()
	{	
		if (!is_ok(_source, _value, _order)) return strings.DEFAULT;

		String key = (_is_field ? db.get_variable(db.get_col(_source_temp, _value)) : _value);
		
		String output = key + " " + order_to_string(_order);
		
		return output;
	}

	public boolean equals(db_order order2_)
	{
		if (!is_ok(order2_)) return false;
		
		return 
		(
			db.sources_are_equal(_source_temp, order2_._source) &&
			generic.are_equal(_value, order2_._value) && 
			generic.are_equal(_order, order2_._order) &&
			(_is_field == order2_._is_field)
		);		
	}
	
	public static String to_string(db_order order_)
	{
		return (is_ok(order_) ? order_.toString() : strings.DEFAULT);	
	}
	
	public static String to_string(db_order[] orders_)
	{
		String output = "";
		if (!arrays.is_ok(orders_)) return output;
		
		for (db_order order: orders_)
		{
			if (!is_ok(order)) continue;
			
			if (!output.equals("")) output += ", ";
			output += order.toString();
		}

		return output;
	}
	
	public static boolean are_equal(db_order order1_, db_order order2_)
	{
		return generic.are_equal(order1_, order2_);
	}
	
	public static boolean is_ok(db_order input_)
	{
		return (input_ != null && is_ok(input_._source, input_._value, input_._order));
	}
	
	public static boolean order_is_ok(String order_)
	{
		return (strings.is_ok(check_order(order_)));
	}
	
	public static String check_order(String order_)
	{
		return _types.check_subtype(order_, _types.get_subtypes(_types.DB_ORDER, null), null, null);
	}

	public static String order_to_string(String order_)
	{
		return (order_is_ok(order_) ? _types.remove_type(order_, _types.DB_ORDER).toUpperCase() : strings.DEFAULT);
	}
	
	private static boolean is_ok(String source_, String value_, String order_)
	{
		_source_temp = db.check_source(source_);
		
		return (strings.is_ok(_source_temp) && strings.is_ok(value_) && order_is_ok(order_));
	}
}