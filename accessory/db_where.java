package accessory;

import java.util.HashMap;

public class db_where 
{	
	public static final String EQUAL = types.DB_WHERE_OPERAND_EQUAL;
	public static final String NOT_EQUAL = types.DB_WHERE_OPERAND_NOT_EQUAL;
	public static final String GREATER = types.DB_WHERE_OPERAND_GREATER;
	public static final String GREATER_EQUAL = types.DB_WHERE_OPERAND_GREATER_EQUAL;
	public static final String LESS = types.DB_WHERE_OPERAND_LESS;
	public static final String LESS_EQUAL = types.DB_WHERE_OPERAND_LESS_EQUAL;
	public static final String OPERATOR_EQUAL = EQUAL;
	public static final String OPERATOR_NOT_EQUAL = NOT_EQUAL;
	public static final String OPERATOR_GREATER = GREATER;
	public static final String OPERATOR_GREATER_EQUAL = GREATER_EQUAL;
	public static final String OPERATOR_LESS = LESS;
	public static final String OPERATOR_LESS_EQUAL = LESS_EQUAL;
	
	public static final String AND = types.DB_WHERE_LINK_AND;
	public static final String OR = types.DB_WHERE_LINK_OR;
	public static final String LINK_AND = AND;
	public static final String LINK_OR = OR;
	
	public static final String DEFAULT_OPERAND = _defaults.DB_WHERE_OPERAND;
	public static final String DEFAULT_LINK = _defaults.DB_WHERE_LINK;
	public static final boolean DEFAULT_LITERAL = _defaults.DB_WHERE_LITERAL;
	
	public boolean _is_ok = false;
	
	public String _source = strings.DEFAULT;
	public String _key = strings.DEFAULT;
	public String _operand = DEFAULT_OPERAND;
	public Object _value = null;
	public String _link = DEFAULT_LINK;
	public boolean _is_literal = _defaults.DB_WHERE_LITERAL; //_value as a literal (e.g., 5 -> '5') or something else (e.g., col + 1 -> col + 1).
	
	private static String _temp_source = strings.DEFAULT;
	private static String _temp_operand = strings.DEFAULT;
	private static String _temp_link = strings.DEFAULT;
	private static HashMap<String, String[]> _all_operands = new HashMap<String, String[]>();
	private static HashMap<String, String[]> _all_links = new HashMap<String, String[]>();	
	
	static { ini(); }
		
	public db_where(db_where input_)
	{
		_is_ok = false;
		if (!is_ok(input_)) return;
		
		populate_main(input_._key, input_._value, input_._is_literal);
	}

	public db_where(String source_, String key_, Object value_)
	{
		_is_ok = false;
		if (!is_ok(source_, key_, null, value_, null)) return;
		
		populate_main(key_, value_, DEFAULT_LITERAL);
	}	

	public db_where(String source_, String key_, String operand_, Object value_)
	{
		_is_ok = false;
		if (!is_ok(source_, key_, operand_, value_, null)) return;
		
		populate_main(key_, value_, _defaults.DB_WHERE_LITERAL);
	}
	
	public db_where(String source_, String key_, String operand_, Object value_, String link_)
	{
		_is_ok = false;
		if (!is_ok(source_, key_, operand_, value_, link_)) return;
		
		populate_main(key_, value_, DEFAULT_LITERAL);
	}
	
	public db_where(String source_, String key_, String operand_, Object value_, boolean is_literal_, String link_)
	{
		_is_ok = false;
		if (!is_ok(source_, key_, operand_, value_, link_)) return;
		
		populate_main(key_, value_, is_literal_);
	}
	
	public String toString()
	{
		if (!is_ok(_source, _key, _operand, _value, _link)) return strings.DEFAULT;
		
		String operand = operand_to_string(_temp_operand);
		String key = db.get_variable(db.get_col(_temp_source, _key));
		String value = strings.to_string(_value);
		value = (_is_literal ? db.get_value(value) : value);
		
		String output = "(" + key + operand + value + ")";
				
		return output;
	}

	public boolean equals(db_where where2_)
	{
		if (!is_ok(where2_)) return false;
		
		return 
		(
			db.sources_are_equal(_temp_source, where2_._source) &&
			generic.are_equal(_key, where2_._key) &&
			generic.are_equal(_temp_operand, where2_._operand) &&
			generic.are_equal(_value, where2_._value) &&
			generic.are_equal(_temp_link, where2_._link) &&
			(_is_literal == where2_._is_literal)
		);		
	}
	
	public static String to_string(db_where where_)
	{
		return (is_ok(where_) ? where_.toString() : strings.DEFAULT);	
	}
	
	public static String to_string(db_where[] wheres_)
	{
		String output = "";
		if (!arrays.is_ok(wheres_)) return output;
		
		String last_link = null;
		int tot = 0;
		
		for (db_where where: wheres_)
		{
			if (!is_ok(where)) continue;
			tot++;
			
			if (!output.equals("")) output += " " + link_to_string(last_link) + " ";
			output += where.toString();
			
			last_link = (link_is_ok(where._link) ? where._link : DEFAULT_LINK);
		}
		
		if (tot > 1) output = "(" + output + ")";
		
		return output;	
	}
	
	public static String operand_to_string(String operand_)
	{
		check_operand(operand_);
		
		return ((!strings.is_ok(_temp_operand) || !_all_operands.containsKey(_temp_operand)) ? strings.DEFAULT : _all_operands.get(_temp_operand)[0]);
	}
	
	public static String link_to_string(String link_)
	{
		return (link_is_ok(link_) ? types.remove_type(link_, types.DB_WHERE_LINK).toUpperCase() : strings.DEFAULT);
	}
	
	public static boolean are_equal(db_where where1_, db_where where2_)
	{
		return generic.are_equal(where1_, where2_);
	}
	
	public static boolean is_ok(db_where input_)
	{
		return (input_ != null && is_ok(input_._source, input_._key, input_._operand, input_._value, input_._link));
	}
	
	public static boolean operand_is_ok(String operand_)
	{
		return operand_is_ok(operand_, false);
	}
	
	public static String check_operand(String operand_)
	{
		return check_operand(operand_, false);
	}
	
	public static boolean link_is_ok(String link_)
	{
		return link_is_ok(link_, false);
	}
	
	public static String check_link(String link_)
	{
		return check_link(link_, false);
	}
	
	private static void ini()
	{
		populate_globals();
	}

	private void populate_main(String key_, Object value_, boolean is_literal_)
	{
		_is_ok = true;
		
		_source = _temp_source;
		_key = key_;
		_operand = _temp_operand;
		_value = value_;	
		_is_literal = is_literal_;
		_link = _temp_link;
	}
	
	private static void populate_globals()
	{
		if (_all_operands.size() > 0) return;
		
		_all_operands.put(EQUAL, new String[] { "=" });
		_all_operands.put(NOT_EQUAL, new String[] { "!=" });
		_all_operands.put(GREATER, new String[] { ">" });
		_all_operands.put(GREATER_EQUAL, new String[] { ">=" });
		_all_operands.put(LESS, new String[] { "<" });
		_all_operands.put(LESS_EQUAL, new String[] { "<=" });
		
		_all_links.put(AND, new String[] { "and" });
		_all_links.put(OR, new String[] { "or" });
	}

	private static boolean is_ok(String source_, String key_, String operand_, Object value_, String link_)
	{
		_temp_source = db.check_source(source_);
		check_link(link_, true);
		
		return 
		( 
			strings.is_ok(_temp_source) &&
			db.field_is_ok(_temp_source, key_) &&
			operand_is_ok(operand_, true) &&
			generic.is_ok(value_)
		);
	}
	
	private static boolean operand_is_ok(String operand_, boolean use_default_)
	{
		check_operand(operand_, use_default_);
		
		return strings.is_ok(_temp_operand);
	}
	
	private static String check_operand(String operand_, boolean use_default_)
	{
		_temp_operand = types.check_multiple(operand_, _all_operands);
		
		if (!strings.is_ok(_temp_operand) && use_default_) _temp_operand = DEFAULT_OPERAND;
		
		return _temp_operand;
	}

	private static boolean link_is_ok(String link_, boolean use_default_)
	{
		check_link(link_, use_default_);
		
		return strings.is_ok(_temp_link);
	}
	
	private static String check_link(String link_, boolean use_default_)
	{
		_temp_link = types.check_multiple(link_, _all_links);
		
		if (!strings.is_ok(_temp_link) && use_default_) _temp_link = DEFAULT_LINK;
		
		return _temp_link;
	}
}