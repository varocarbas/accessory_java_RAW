package accessory;

import java.util.ArrayList;

public class db_order extends parent
{	
	public static final String ORDER_ASC = _types.DB_ORDER_ASC;
	public static final String ORDER_DESC = _types.DB_ORDER_DESC;

	public static final String DEFAULT_ORDER = _types.DB_ORDER_ASC;
	public static final boolean DEFAULT_IS_FIELD_COL = true;
	public static final boolean DEFAULT_IS_QUICK = false;
	
	private String _source = strings.DEFAULT;
	private String _field_col_else = strings.DEFAULT; //When _is_field_col is true, it is a field when _is_quick is false and a col otherwise. When _is_field_col is false, it is always something else other than a field/col.
	private String _order = DEFAULT_ORDER;
	private boolean _is_field_col = DEFAULT_IS_FIELD_COL; //When it is true, _field_col_else is a field/col, otherwise it is something else, like a condition.
	private boolean _is_quick = DEFAULT_IS_QUICK; //It is equivalent to the db "_quick" methods, where all the fields are assumed to be cols and no value checks are performed.

	private String _temp_source = strings.DEFAULT;
	private String _temp_field_col_else = strings.DEFAULT;
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

	public static boolean order_is_ok(String order_) { return val_is_ok_common(order_, _types.DB_ORDER, DEFAULT_ORDER); }

	public static String check_order(String order_) { return check_val_common(order_, _types.DB_ORDER, DEFAULT_ORDER); }

	public static String order_to_string(String order_) { return val_to_string_common(order_, _types.DB_ORDER, DEFAULT_ORDER); }

	public static db_order[] get_orders_desc(String source_, String[] fields_) { return get_orders_desc_asc(source_, fields_, ORDER_DESC, false); }

	public static db_order[] get_orders_desc_quick(String source_, String[] cols_) { return get_orders_desc_asc(source_, cols_, ORDER_DESC, true); }
	
	public static db_order[] get_orders_asc(String source_, String[] fields_) { return get_orders_desc_asc(source_, fields_, ORDER_ASC, false); }
	
	public static db_order[] get_orders_asc_quick(String source_, String[] cols_) { return get_orders_desc_asc(source_, cols_, ORDER_ASC, true); }

	public db_order(db_order input_) { instantiate(input_); }

	public db_order(String field_col_else_, String order_) { instantiate(db._cur_source, field_col_else_, order_, DEFAULT_IS_FIELD_COL, DEFAULT_IS_QUICK); }

	public db_order(String source_, String field_col_else_, String order_) { instantiate(source_, field_col_else_, order_, DEFAULT_IS_FIELD_COL, DEFAULT_IS_QUICK); }

	public db_order(String field_col_else_, String order_, boolean is_field_col_) { instantiate(db._cur_source, field_col_else_, order_, is_field_col_, DEFAULT_IS_QUICK); }

	public db_order(String source_, String field_col_else_, String order_, boolean is_field_col_) { instantiate(source_, field_col_else_, order_, is_field_col_, DEFAULT_IS_QUICK); }

	public db_order(String source_, String field_col_else_, String order_, boolean is_field_col_, boolean is_quick_) { instantiate(source_, field_col_else_, order_, is_field_col_, is_quick_); }

	public String toString()
	{	
		if (!is_ok(_source, _field_col_else, _order, _is_field_col, _is_quick)) return strings.DEFAULT;

		String field_col_else = strings.DEFAULT;
		
		if (_is_field_col) field_col_else = db.get_variable(_temp_source, (_is_quick ? _temp_field_col_else : db.get_col(_temp_source, _temp_field_col_else)));
		else field_col_else = _temp_field_col_else;

		String output = field_col_else + " " + order_to_string(_order);

		return output;
	}

	public boolean equals(db_order order2_)
	{
		if (!is_ok(order2_)) return false;

		return 
		(
			db.sources_are_equal(_temp_source, order2_._source) && generic.are_equal(_temp_field_col_else, order2_._field_col_else) && 
			generic.are_equal(_temp_order, order2_._order) && (_is_field_col == order2_._is_field_col) && (_is_quick == order2_._is_quick) 
		);		
	}

	public boolean is_ok() { return is_ok(_source, _field_col_else, _order, _is_field_col, _is_quick); }

	private static db_order[] get_orders_desc_asc(String source_, String[] fields_cols_, String order_, boolean is_quick_)
	{
		String source = db.check_source(source_);
		String order = check_order(order_);
		if (!strings.is_ok(source) || !strings.is_ok(order) || !arrays.is_ok(fields_cols_)) return null;

		ArrayList<db_order> output = new ArrayList<db_order>();

		for (String field_col: fields_cols_) { output.add(new db_order(source, field_col, order, DEFAULT_IS_FIELD_COL, is_quick_)); }

		return arrays.to_array(output);
	}

	private void instantiate(db_order input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._temp_source, input_._temp_field_col_else, input_._temp_order, input_._is_field_col, input_._is_quick);
	}

	private void instantiate(String source_, String field_col_else_, String order_, boolean is_field_col_, boolean is_quick_)
	{
		instantiate_common();
		if (!is_ok(source_, field_col_else_, order_, is_field_col_, is_quick_)) return;

		populate(_temp_source, _temp_field_col_else, _temp_order, is_field_col_, is_quick_);
	}

	private boolean is_ok(String source_, String field_col_else_, String order_, boolean is_field_col_, boolean is_quick_)
	{
		_temp_source = db.check_source(source_);
		_temp_field_col_else = ((is_field_col_ && !is_quick_) ? db.check_field(_temp_source, field_col_else_) : field_col_else_);
		_temp_order = _types.check_type(order_, _types.DB_ORDER);

		return (strings.are_ok(new String[] { _temp_source, _temp_field_col_else, _temp_order }));
	}

	private void populate(String source_, String field_col_else_, String order_, boolean is_field_col_, boolean is_quick_)
	{
		_source = source_;
		_field_col_else = field_col_else_;
		_order = order_;
		_is_field_col = is_field_col_;
		_is_quick = is_quick_;
	}
}