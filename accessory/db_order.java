package accessory;

public class db_order 
{
	public boolean _is_ok = true;
	
	public String _source = strings.DEFAULT;
	public String _value = strings.DEFAULT;
	public String _order = defaults.DB_ORDER;
	public boolean _is_field = defaults.DB_ORDER_FIELD; //_value being a field/col vs. something else like a condition.

	public String toString()
	{	
		if (!is_ok(_source, _value, _order)) return strings.DEFAULT;

		String key = (_is_field ? db.get_variable(db.get_col(_source, _value)) : _value);
		
		String output = key + " " + order_to_string(_order);
		
		return output;
	}

	public boolean equals(db_order order2_)
	{
		if (!is_ok(order2_)) return false;
		
		return 
		(
			db.sources_are_equal(_source, order2_._source) &&
			generic.are_equal(_value, order2_._value) && 
			generic.are_equal(_order, order2_._order) &&
			(_is_field == order2_._is_field)
		);		
	}
	
	public static String to_string(db_order order_)
	{
		return (is_ok(order_) ? order_.toString() : strings.DEFAULT);	
	}
	
	public static boolean are_equal(db_order order1_, db_order order2_)
	{
		return generic.are_equal(order1_, order2_);
	}
	
	public static boolean is_ok(db_order input_)
	{
		return (input_ != null && is_ok(input_._source, input_._value, input_._order));
	}
	
	public db_order(db_order input_)
	{
		_is_ok = false;
		if (!is_ok(input_)) return;

		_is_ok = true;
		_source = input_._source;
		_value = input_._value;
		_order = input_._order;
		_is_field = input_._is_field;
	}

	public db_order(String source_, String value_, String order_, boolean is_field_)
	{
		_is_ok = false;
		if (!is_ok(source_, value_, order_)) return;
		
		_is_ok = true;
		_source = db.check_source(source_);
		_value = value_;
		_order = check_order(order_);
		_is_field = is_field_;
	}
	
	public static boolean order_is_ok(String order_)
	{
		return (strings.is_ok(check_order(order_)));
	}
	
	public static String check_order(String order_)
	{
		return types.check_subtype(order_, types.get_subtypes(types.DB_ORDER, null), null, null);
	}

	public static String order_to_string(String order_)
	{
		return (order_is_ok(order_) ? types.remove_type(order_, types.DB_ORDER).toUpperCase() : strings.DEFAULT);
	}
	
	private static boolean is_ok(String source_, String value_, String order_)
	{
		return (db.source_is_ok(source_) && strings.is_ok(value_) && order_is_ok(order_));
	}
}