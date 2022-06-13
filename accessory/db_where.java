package accessory;

import java.util.ArrayList;
import java.util.HashMap;

public class db_where extends parent
{	
	public static final String OPERAND_EQUAL = types.DB_WHERE_OPERAND_EQUAL;
	public static final String OPERAND_NOT_EQUAL = types.DB_WHERE_OPERAND_NOT_EQUAL;
	public static final String OPERAND_GREATER = types.DB_WHERE_OPERAND_GREATER;
	public static final String OPERAND_GREATER_EQUAL = types.DB_WHERE_OPERAND_GREATER_EQUAL;
	public static final String OPERAND_LESS = types.DB_WHERE_OPERAND_LESS;
	public static final String OPERAND_LESS_EQUAL = types.DB_WHERE_OPERAND_LESS_EQUAL;

	public static final String LINK_AND = types.DB_WHERE_LINK_AND;
	public static final String LINK_OR = types.DB_WHERE_LINK_OR;

	public static final String DEFAULT_OPERAND = OPERAND_EQUAL;
	public static final String DEFAULT_LINK = LINK_AND;
	public static final boolean DEFAULT_LITERAL = true;

	private String _source = strings.DEFAULT;
	private String _field = strings.DEFAULT;
	private String _operand = DEFAULT_OPERAND;
	private Object _value = null;
	private String _link = DEFAULT_LINK;
	private boolean _is_literal = DEFAULT_LITERAL; //_value as a literal (e.g., 5 -> '5') or something else (e.g., col + 1 -> col + 1).

	private String _temp_source = strings.DEFAULT;
	private String _temp_field = strings.DEFAULT;
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

	public static boolean operand_is_ok(String operand_) { return val_is_ok_common(operand_, types.DB_WHERE_OPERAND, DEFAULT_OPERAND); }

	public static String check_operand(String operand_) { return check_val_common(operand_, get_all_operands(), DEFAULT_OPERAND); }

	public static String operand_to_string(String operand_) { return val_to_string_common(operand_, get_all_operands(), DEFAULT_OPERAND); }

	public static boolean link_is_ok(String link_) { return val_is_ok_common(link_, get_all_links(), DEFAULT_LINK); }

	public static String check_link(String link_) { return check_val_common(link_, get_all_links(), DEFAULT_LINK); }

	public static String link_to_string(String link_) { return val_to_string_common(link_, types.DB_WHERE_LINK, DEFAULT_LINK); }

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

	public db_where(String field_, Object value_) { instantiate(db._cur_source, field_, DEFAULT_OPERAND, value_, DEFAULT_LITERAL, DEFAULT_LINK); }	

	public db_where(String source_, String field_, Object value_) { instantiate(source_, field_, DEFAULT_OPERAND, value_, DEFAULT_LITERAL, DEFAULT_LINK); }	

	public db_where(String source_, String field_, String operand_, Object value_) { instantiate(source_, field_, operand_, value_, DEFAULT_LITERAL, DEFAULT_LINK); }

	public db_where(String source_, String field_, String operand_, Object value_, String link_) { instantiate(source_, field_, operand_, value_, DEFAULT_LITERAL, link_); }

	public db_where(String source_, String field_, String operand_, Object value_, boolean is_literal_, String link_) { instantiate(source_, field_, operand_, value_, is_literal_, link_); }

	public String toString()
	{
		String output = strings.DEFAULT;
		if (!is_ok(_source, _field, _operand, _value, _link)) return output;

		String operand = operand_to_string(_temp_operand);
		String field = db.get_variable(_temp_source, db.get_col(_temp_source, _temp_field));

		String value = db.adapt_input(_temp_source, _temp_field, _value);
		if (value == null) return output;

		value = (_is_literal ? db.get_value(_temp_source, value) : value);

		output = "(" + field + operand + value + ")";

		return output;
	}

	public boolean equals(db_where where2_)
	{
		if (!is_ok(where2_)) return false;

		return 
		(
			db.sources_are_equal(_temp_source, where2_._source) &&
			generic.are_equal(_temp_field, where2_._field) &&
			generic.are_equal(_temp_operand, where2_._operand) &&
			generic.are_equal(_value, where2_._value) &&
			generic.are_equal(_temp_link, where2_._link) &&
			(_is_literal == where2_._is_literal)
		);		
	}

	public boolean is_ok() { return is_ok(_source, _field, _operand, _value, _link); }

	static HashMap<String, String[]> populate_all_operands()
	{
		HashMap<String, String[]> all = new HashMap<String, String[]>();

		all.put(OPERAND_EQUAL, new String[] { "=" });
		all.put(OPERAND_NOT_EQUAL, new String[] { "!=" });
		all.put(OPERAND_GREATER, new String[] { ">" });
		all.put(OPERAND_GREATER_EQUAL, new String[] { ">=" });
		all.put(OPERAND_LESS, new String[] { "<" });
		all.put(OPERAND_LESS_EQUAL, new String[] { "<=" });

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

	private static HashMap<String, String[]> get_all_links() { return _alls.DB_WHERE_LINKS; }

	private void instantiate(db_where input_)
	{
		instantiate_common();
		if (input_ == null || !input_.is_ok()) return;

		populate(input_._temp_source, input_._temp_field, input_._temp_operand, input_._value, input_._temp_link, input_._is_literal);
	}

	private void instantiate(String source_, String field_, String operand_, Object value_, boolean is_literal_, String link_)
	{
		instantiate_common();
		if (!is_ok(source_, field_, operand_, value_, link_)) return;

		populate(_temp_source, _temp_field, _temp_operand, value_, _temp_link, is_literal_);
	}

	private boolean is_ok(String source_, String field_, String operand_, Object value_, String link_)
	{
		_temp_source = db.check_source(source_);
		_temp_field = db.check_field(_temp_source, field_);
		_temp_operand = check_operand(operand_);
		_temp_link = check_link(link_);

		return (strings.are_ok(new String[] { _temp_source, _temp_field, _temp_operand }) && (value_ != null));
	}

	private void populate(String source_, String field_, String operand_, Object value_, String link_, boolean is_literal_)
	{
		_source = source_;
		_field = field_;
		_operand = operand_;
		_value = value_;	
		_is_literal = is_literal_;
		_link = link_;
	}
}