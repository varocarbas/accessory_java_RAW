package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public class db_where extends parent
{	
	public static final String OPERAND_EQUAL = _types.DB_WHERE_OPERAND_EQUAL;
	public static final String OPERAND_NOT_EQUAL = _types.DB_WHERE_OPERAND_NOT_EQUAL;
	public static final String OPERAND_GREATER = _types.DB_WHERE_OPERAND_GREATER;
	public static final String OPERAND_GREATER_EQUAL = _types.DB_WHERE_OPERAND_GREATER_EQUAL;
	public static final String OPERAND_LESS = _types.DB_WHERE_OPERAND_LESS;
	public static final String OPERAND_LESS_EQUAL = _types.DB_WHERE_OPERAND_LESS_EQUAL;
	
	public static final String OPERAND_LIKE_START = _types.DB_WHERE_OPERAND_LIKE_START;
	public static final String OPERAND_LIKE_END = _types.DB_WHERE_OPERAND_LIKE_END;
	public static final String OPERAND_LIKE_BOTH = _types.DB_WHERE_OPERAND_LIKE_BOTH;
	public static final String OPERAND_NOT_LIKE_START = _types.DB_WHERE_OPERAND_NOT_LIKE_START;
	public static final String OPERAND_NOT_LIKE_END = _types.DB_WHERE_OPERAND_NOT_LIKE_END;
	public static final String OPERAND_NOT_LIKE_BOTH = _types.DB_WHERE_OPERAND_NOT_LIKE_BOTH;
	
	public static final String LINK_AND = _types.DB_WHERE_LINK_AND;
	public static final String LINK_OR = _types.DB_WHERE_LINK_OR;

	public static final String DEFAULT_OPERAND = OPERAND_EQUAL;
	public static final String DEFAULT_OPERAND_LIKE = OPERAND_LIKE_BOTH;
	public static final String DEFAULT_OPERAND_NOT_LIKE = OPERAND_NOT_LIKE_BOTH;
	
	public static final String DEFAULT_LINK = LINK_AND;
	public static final boolean DEFAULT_IS_LITERAL = true;
	public static final boolean DEFAULT_IS_QUICK = false;
	
	private String _source = strings.DEFAULT;
	private String _field_col = strings.DEFAULT; //It refers to a field, except when _is_quick is true.
	private String _operand = DEFAULT_OPERAND;
	private Object _value = null; //It is checked and adapted to the given source/field, except when _is_quick is true.
	private String _link = DEFAULT_LINK;
	private boolean _is_literal = DEFAULT_IS_LITERAL; //It defines _value as a literal (e.g., 5 -> '5') or as something else (e.g., col + 1 -> col + 1).
	private boolean _is_quick = DEFAULT_IS_QUICK; //It is equivalent to the db "_quick" methods, where all the fields are assumed to be cols and no value checks are performed.
	
	private String _temp_source = strings.DEFAULT;
	private String _temp_field_col = strings.DEFAULT;
	private String _temp_operand = strings.DEFAULT;
	private String _temp_link = strings.DEFAULT;

	public static boolean are_equal(db_where where1_, db_where where2_) { return are_equal_common(where1_, where2_); }

	public static String to_string(ArrayList<db_where> wheres_) { return to_string(arrays.to_array(wheres_)); }
	
	public static String to_string(db_where[] wheres_)
	{
		String output = "";
		if (!arrays.is_ok(wheres_)) return output;

		String last_link = null;
		int tot = 0;

		for (db_where where: wheres_)
		{
			if (!is_ok(where)) continue;

			String temp = where.toString();
			if (!strings.is_ok(temp)) continue;

			if (!output.equals("")) output += " " + link_to_string(last_link) + " ";
			output += temp;

			last_link = (link_is_ok(where._link) ? where._link : DEFAULT_LINK);
			tot++;
		}
		if (tot > 1) output = "(" + output + ")";

		return output;	
	}

	public static boolean operand_is_ok(String operand_) { return val_is_ok_common(operand_, _types.DB_WHERE_OPERAND); }

	public static String check_operand(String operand_) { return check_val_common(operand_, get_all_operands(), DEFAULT_OPERAND); }

	public static boolean operand_is_like_not_like(String operand_) { return (operand_like_is_ok(operand_) || operand_not_like_is_ok(operand_)); }

	public static boolean operand_like_is_ok(String operand_like_) { return val_is_ok_common(operand_like_, _types.DB_WHERE_OPERAND_LIKE); }

	public static String check_operand_like(String operand_like_) { return check_val_common(operand_like_, populate_all_operands_like(), DEFAULT_OPERAND_LIKE); }

	public static boolean operand_not_like_is_ok(String operand_not_like_) { return val_is_ok_common(operand_not_like_, _types.DB_WHERE_OPERAND_NOT_LIKE); }

	public static String check_operand_not_like(String operand_not_like_) { return check_val_common(operand_not_like_, populate_all_operands_not_like(), DEFAULT_OPERAND_NOT_LIKE); }

	public static String operand_to_string(String operand_) { return val_to_string_common(operand_, get_all_operands(), DEFAULT_OPERAND); }

	public static String operand_like_to_string(String operand_like_) { return val_to_string_common(operand_like_, get_all_operands_like(), DEFAULT_OPERAND_LIKE); }

	public static String operand_not_like_to_string(String operand_not_like_) { return val_to_string_common(operand_not_like_, get_all_operands_not_like(), DEFAULT_OPERAND_LIKE); }

	public static boolean link_is_ok(String link_) { return val_is_ok_common(link_, get_all_links()); }

	public static String check_link(String link_) { return check_val_common(link_, get_all_links(), DEFAULT_LINK); }

	public static String link_to_string(String link_) { return val_to_string_common(link_, _types.DB_WHERE_LINK); }

	public static String join(db_where[] where1s_, db_where[] where2s_, String link_) { return join(to_string(where1s_), to_string(where2s_), link_); }

	public static String join(String where1_, String where2_, String link_)
	{
		String output = "";

		if (!strings.is_ok(where1_) || !strings.is_ok(where2_)) 
		{
			if (strings.is_ok(where1_)) output = where1_;
			else if (strings.is_ok(where2_)) output = where2_;

			return output;
		}

		String link = check_link(link_);
		if (!strings.is_ok(link)) return output;

		return (where1_ + " " + link_to_string(link_) + " " + where2_);
	}

	public db_where(db_where input_) { instantiate(input_); }

	public db_where(String field_col_, Object value_) { instantiate(db._cur_source, field_col_, DEFAULT_OPERAND, value_, DEFAULT_IS_LITERAL, DEFAULT_LINK, DEFAULT_IS_QUICK); }	

	public db_where(String source_, String field_col_, Object value_) { instantiate(source_, field_col_, DEFAULT_OPERAND, value_, DEFAULT_IS_LITERAL, DEFAULT_LINK, DEFAULT_IS_QUICK); }	

	public db_where(String source_, String field_col_, String operand_, Object value_) { instantiate(source_, field_col_, operand_, value_, DEFAULT_IS_LITERAL, DEFAULT_LINK, DEFAULT_IS_QUICK); }

	public db_where(String source_, String field_col_, String operand_, Object value_, String link_) { instantiate(source_, field_col_, operand_, value_, DEFAULT_IS_LITERAL, link_, DEFAULT_IS_QUICK); }

	public db_where(String source_, String field_col_, String operand_, Object value_, boolean is_literal_, String link_) { instantiate(source_, field_col_, operand_, value_, is_literal_, link_, DEFAULT_IS_QUICK); }

	public db_where(String source_, String field_col_, String operand_, Object value_, boolean is_literal_, String link_, boolean is_quick_) { instantiate(source_, field_col_, operand_, value_, is_literal_, link_, is_quick_); }

	public String serialise() { return toString(); }
	
	public String toString()
	{
		String output = strings.DEFAULT;
		if (!is_ok(_source, _field_col, _operand, _value, _link, _is_quick)) return output;

		String value = (_is_quick ? db.adapt_input(_value) : db.adapt_input(_temp_source, _temp_field_col, _value));
		if (value == null) return output;

		String col = db.get_variable(_temp_source, (_is_quick ? _temp_field_col : db.get_col(_temp_source, _temp_field_col)));
			
		String operand = null;

		boolean is_like = operand_like_is_ok(_temp_operand);
		boolean is_not_like = (!is_like && operand_not_like_is_ok(_temp_operand));

		if (is_like || is_not_like)
		{
			operand = " " + (is_like ? operand_like_to_string(_temp_operand) : operand_not_like_to_string(_temp_operand)) + " ";
					
			if (strings.matches_any(new String[] { OPERAND_LIKE_START, OPERAND_LIKE_BOTH, OPERAND_NOT_LIKE_START, OPERAND_NOT_LIKE_BOTH }, _temp_operand, false)) value = "%" + value;
			if (strings.matches_any(new String[] { OPERAND_LIKE_END, OPERAND_LIKE_BOTH, OPERAND_NOT_LIKE_END, OPERAND_NOT_LIKE_BOTH }, _temp_operand, false)) value += "%";
		}
		else operand = operand_to_string(_temp_operand);
		
		value = (_is_literal ? db.get_value(_temp_source, value) : value);
		
		output = (col + operand + value); 

		return output;
	}

	public boolean equals(db_where where2_)
	{
		if (!is_ok(where2_)) return false;

		return 
		(
			db.sources_are_equal(_temp_source, where2_._source) && generic.are_equal(_temp_field_col, where2_._field_col) &&
			generic.are_equal(_temp_operand, where2_._operand) && generic.are_equal(_value, where2_._value) && 
			generic.are_equal(_temp_link, where2_._link) && (_is_literal == where2_._is_literal) && (_is_quick == where2_._is_quick)
		);		
	}

	public boolean is_ok() { return is_ok(_source, _field_col, _operand, _value, _link, _is_quick); }

	static HashMap<String, String[]> populate_all_operands()
	{
		HashMap<String, String[]> all = new HashMap<String, String[]>();

		all.put(OPERAND_EQUAL, new String[] { "=" });
		all.put(OPERAND_NOT_EQUAL, new String[] { "!=" });
		all.put(OPERAND_GREATER, new String[] { ">" });
		all.put(OPERAND_GREATER_EQUAL, new String[] { ">=" });
		all.put(OPERAND_LESS, new String[] { "<" });
		all.put(OPERAND_LESS_EQUAL, new String[] { "<=" });

		all.putAll(populate_all_operands_like());
		all.putAll(populate_all_operands_not_like());

		return all;
	}

	static HashMap<String, String[]> populate_all_operands_like()
	{
		HashMap<String, String[]> all = new HashMap<String, String[]>();

		all.put(OPERAND_LIKE_START, new String[] { "like" });
		all.put(OPERAND_LIKE_END, new String[] { "like" });
		all.put(OPERAND_LIKE_BOTH, new String[] { "like" });
		
		return all;
	}

	static HashMap<String, String[]> populate_all_operands_not_like()
	{
		HashMap<String, String[]> all = new HashMap<String, String[]>();

		all.put(OPERAND_NOT_LIKE_START, new String[] { "not like" });
		all.put(OPERAND_NOT_LIKE_END, new String[] { "not like" });
		all.put(OPERAND_NOT_LIKE_BOTH, new String[] { "not like" });
		
		return all;
	}
	
	static HashMap<String, String[]> populate_all_links()
	{
		HashMap<String, String[]> all = new HashMap<String, String[]>();

		all.put(LINK_AND, new String[] { "and" });
		all.put(LINK_OR, new String[] { "or" });

		return all;
	}

	private static HashMap<String, String[]> get_all_operands() { return _alls.DB_WHERE_OPERANDS; }

	private static HashMap<String, String[]> get_all_operands_like() { return _alls.DB_WHERE_OPERANDS_LIKE; }

	private static HashMap<String, String[]> get_all_operands_not_like() { return _alls.DB_WHERE_OPERANDS_NOT_LIKE; }
	
	private static HashMap<String, String[]> get_all_links() { return _alls.DB_WHERE_LINKS; }

	private void instantiate(db_where input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._temp_source, input_._temp_field_col, input_._temp_operand, input_._value, input_._temp_link, input_._is_literal, input_._is_quick);
	}

	private void instantiate(String source_, String field_col_, String operand_, Object value_, boolean is_literal_, String link_, boolean is_quick_)
	{
		instantiate_common();
		if (!is_ok(source_, field_col_, operand_, value_, link_, is_quick_)) return;

		populate(_temp_source, _temp_field_col, _temp_operand, value_, _temp_link, is_literal_, is_quick_);
	}

	private boolean is_ok(String source_, String field_col_, String operand_, Object value_, String link_, boolean is_quick_)
	{
		_temp_source = db.check_source(source_);
		_temp_field_col = (is_quick_ ? field_col_ : db.check_field(_temp_source, field_col_));
		_temp_operand = check_operand(operand_);
		_temp_link = check_link(link_);

		return (strings.are_ok(new String[] { _temp_source, _temp_field_col, _temp_operand }) && (value_ != null));
	}

	private void populate(String source_, String field_col_, String operand_, Object value_, String link_, boolean is_literal_, boolean is_quick_)
	{
		_source = source_;
		_field_col = field_col_;
		_operand = operand_;
		_value = value_;
		_is_literal = ((!generic.is_string(value_) || strings.is_number((String)value_) || operand_is_like_not_like(operand_)) ? true : is_literal_);
		_link = link_;
		_is_quick = is_quick_;
	}
}