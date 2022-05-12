package accessory;

public class db_order extends parent
{	
	public static final String ASC = types.DB_ORDER_ASC;
	public static final String DESC = types.DB_ORDER_DESC;
	public static final String ORDER_ASC = ASC;
	public static final String ORDER_DESC = DESC;

	public static final String DEFAULT_ORDER = _defaults.DB_ORDER;
	public static final boolean DEFAULT_IS_FIELD = _defaults.DB_ORDER_IS_FIELD;
	
	private String _source = strings.DEFAULT;
	private String _field_condition = strings.DEFAULT; //When _is_field is true, it is always treated as a field except inside to_string() methods, where it is converted into a col.
	private String _order = DEFAULT_ORDER;
	private boolean _is_field = DEFAULT_IS_FIELD; //_field_condition being a field/col vs. something else like a condition.

	private String _temp_source = strings.DEFAULT;
	private String _temp_field_condition = strings.DEFAULT;
	private String _temp_order = strings.DEFAULT;
	
	public static boolean are_equal(db_order order1_, db_order order2_) { return are_equal_common(order1_, order2_); }

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

	public static boolean order_is_ok(String order_) { return val_is_ok_common(order_, types.DB_ORDER, DEFAULT_ORDER); }

	public static String check_order(String order_) { return check_val_common(order_, types.DB_ORDER, DEFAULT_ORDER); }

	public static String order_to_string(String order_) { return val_to_string_common(order_, types.DB_ORDER, DEFAULT_ORDER); }

	public db_order(db_order input_) { instantiate(input_); }

	public db_order(String source_, String field_condition_, String order_) { instantiate(source_, field_condition_, order_, DEFAULT_IS_FIELD); }

	public db_order(String source_, String field_condition_, String order_, boolean is_field_) { instantiate(source_, field_condition_, order_, is_field_); }

	public String toString()
	{	
		if (!is_ok(_source, _field_condition, _order, _is_field)) return strings.DEFAULT;

		String field_condition = (_is_field ? db.get_variable(_temp_source, db.get_col(_temp_source, _temp_field_condition)) : _temp_field_condition);

		String output = field_condition + " " + order_to_string(_order);

		return output;
	}

	public boolean equals(db_order order2_)
	{
		if (!is_ok(order2_)) return false;

		return 
		(
			db.sources_are_equal(_temp_source, order2_._source) &&
			generic.are_equal(_temp_field_condition, order2_._field_condition) && 
			generic.are_equal(_temp_order, order2_._order) &&
			(_is_field == order2_._is_field)
		);		
	}

	public boolean is_ok() { return is_ok(_source, _field_condition, _order, _is_field); }

	private void instantiate(db_order input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._temp_source, input_._temp_field_condition, input_._temp_order, input_._is_field);
	}

	private void instantiate(String source_, String field_condition_, String order_, boolean is_field_)
	{
		instantiate_common();
		if (!is_ok(source_, field_condition_, order_, is_field_)) return;

		populate(_temp_source, _temp_field_condition, _temp_order, is_field_);
	}

	private boolean is_ok(String source_, String field_condition_, String order_, boolean is_field_)
	{
		_temp_source = db.check_source(source_);
		_temp_field_condition = (is_field_ ? db.check_field(_temp_source, field_condition_) : field_condition_);
		_temp_order = types.check_type(order_, types.DB_ORDER);
		
		return (strings.are_ok(new String[] { _temp_source, _temp_field_condition, _temp_order }));
	}

	private void populate(String source_, String field_condition_, String order_, boolean is_field_)
	{
		_source = source_;
		_field_condition = field_condition_;
		_order = order_;
		_is_field = is_field_;
	}
}