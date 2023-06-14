package accessory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

//Use the methods in this class very carefully! Relevant inputs are assumed correct and few validity checks are performed.
public abstract class db_cache
{
	public static final String MAIN = "main";
	public static final String WHERE = "where";
	public static final String ORDER = "order";

	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static final String SELECT = "select";
	public static final String SELECT_COUNT = "select_count";	
	
	public static final int WRONG_ID = arrays.WRONG_I;
	
	private static final String PLACEHOLDER_KEY = "VAL";
	
	private static final String START = misc.REPLACEABLE_START;
	private static final String END = misc.REPLACEABLE_END;
	
	private static String[] _queries = new String[0];
	private static String[] _sources = new String[0];
	private static boolean[] _returns = new boolean[0];
	private static boolean[] _are_quick = new boolean[0];
	
	public static boolean id_is_ok(int id_) { return (id_ >= 0 && id_ < _queries.length); }

	public static String[] get_queries() { return arrays_quick.get_new(_queries); }

	public static String get_query(int id_) { return (id_is_ok(id_) ? _queries[id_] : strings.DEFAULT); }
	
	public static void remove_query(int id_) { remove_queries(new int[] { id_ }); }
	
	public static void remove_queries(int[] ids_)
	{
		if (!arrays.is_ok(ids_)) return;
		
		String[] queries = arrays_quick.get_new(_queries);
		String[] sources = arrays_quick.get_new(_sources);
		boolean[] returns = arrays_quick.get_new(_returns);
		boolean[] are_quick = arrays_quick.get_new(_are_quick);
		
		_queries = new String[0];
		_sources = new String[0];
		_returns = new boolean[0];
		_are_quick = new boolean[0];
		
		for (int i = 0; i < queries.length; i++)
		{
			if (arrays_quick.value_exists(ids_, i)) continue;
			
			_queries = arrays_quick.add(_queries, queries[i]);
			_sources = arrays_quick.add(_sources, sources[i]);
			_returns = arrays_quick.add(_returns, returns[i]);
			_are_quick = arrays_quick.add(_are_quick, are_quick[i]);
		}
	}
	
	public static String get_placeholder(int id_) { return (START + PLACEHOLDER_KEY + Integer.toString(id_) + END); }

	public static String get_variable(String source_, String variable_) { return get_variable(source_, variable_, null); }
	
	public static String get_value(String source_) { return get_value(source_, null); }
	
	public static String get_value(String source_, String value_) { return get_value(source_, value_, null); }
	
	public static HashMap<Integer, String> add_changing_val(int col_id_, String val_) { return add_changing_val(col_id_, val_, null); }
	
	public static HashMap<Integer, String> add_changing_val(int col_id_, String val_, HashMap<Integer, String> changing_vals_) 
	{ 
		HashMap<Integer, String> output = (changing_vals_ != null ? new HashMap<Integer, String>(changing_vals_) : new HashMap<Integer, String>());
		
		output.put(col_id_, val_);
		
		return output; 
	}
	
	public static int get_col_id(String col_, String[] cols_) { return arrays_quick.get_i(cols_, col_); }

	static int add_select_count(String source_, String col_, int col_id_, boolean is_quick_, String quote_value_, String quote_variable_) { return add_select_count(source_, col_, col_id_, null, is_quick_, quote_value_, quote_variable_); }
	
	static int add_select_count(String source_, String col_, int col_id_, String where_, boolean is_quick_, String quote_value_, String quote_variable_) { return add(source_, get_query_select_count(source_, col_, col_id_, where_, quote_variable_), true, is_quick_, quote_value_, quote_variable_); }

	static String get_query_select_count(String source_, String where_col_, int where_col_id_, String where_, String quote_variable_) { return get_query_select_count(source_, get_query_where(source_, where_col_, where_col_id_, where_, false, quote_variable_), quote_variable_); }
	
	static String get_query_select_count(String source_, String where_, String quote_variable_)
	{
		String output = "SELECT COUNT(*) FROM " + get_variable(source_, db.get_table(source_), quote_variable_);
		
		if (strings.is_ok(where_)) output += " WHERE " + where_;
		
		return output;
	}
	
	static String get_query_update(String source_, String[] cols_ids_, String where_, String quote_variable_)
	{
		int tot = arrays.get_size(cols_ids_);
		if (tot == 0) return strings.DEFAULT;
		
		String output = "UPDATE " + get_variable(source_, db.get_table(source_), quote_variable_) + " SET ";
		
		boolean first_time = true;
		
		for (int i = 0; i < tot; i++)
		{
			if (first_time) first_time = false;
			else output += ", ";
				
			output += get_variable(source_, cols_ids_[i], quote_variable_) + "=" + get_placeholder(i);
		}
		
		if (strings.is_ok(where_)) output += " WHERE " + where_;

		return output;
	}

	static String get_query_insert(String source_, String[] cols_ids_, String quote_variable_)
	{
		int tot = arrays.get_size(cols_ids_);
		if (tot == 0) return strings.DEFAULT;
		
		String output = "INSERT INTO " + get_variable(source_, db.get_table(source_), quote_variable_) + "(";

		int count = 0;
		
		while (count < 2)
		{
			count++;
			
			boolean first_time = true;
			
			for (int i = 0; i < tot; i++) 
			{ 
				if (first_time) first_time = false;
				else output += ", ";
					
				output += (count == 1 ? get_variable(source_, cols_ids_[i], quote_variable_) : get_placeholder(i));
			}
			
			output += ")";
			
			if (count == 1) output += " VALUES (";
		}

		return output;
	}

	static String get_query_where(String source_, String col_, int col_id_, String where_, boolean add_where_, String quote_variable_)
	{
		if (!strings.is_ok(col_) || col_id_ <= WRONG_ID) return strings.DEFAULT;
		
		String output = (add_where_ ? "WHERE " : "");

		output += get_variable(source_, col_, quote_variable_) + "=" + get_placeholder(col_id_);
		
		if (strings.is_ok(where_)) output += " AND " + where_;
		
		return output;
	}
	
	static int add(String source_, String query_, boolean return_data_, boolean is_quick_, String quote_value_, String quote_variable_)
	{
		int output = WRONG_ID;
		if (!strings.are_ok(new String[] { source_, query_, })) return output;

		String query = get_query_add(source_, query_, quote_value_, quote_variable_);
		if (!strings.is_ok(query)) return output;
		
		_queries = arrays_quick.add(_queries, query);
		_sources = arrays_quick.add(_sources, source_);
		_returns = arrays_quick.add(_returns, return_data_);
		_are_quick = arrays_quick.add(_are_quick, is_quick_);
		
		return _queries.length - 1;	
	}
	
	static String add_placeholders(String source_, String query_, String[] cols_, String quote_value_, String quote_variable_)
	{
		if (!db.source_is_ok(source_) || !strings.is_ok(query_) || !arrays.is_ok(cols_)) return strings.DEFAULT;
		
		String output = query_;
		
		for (int i = 0; i < cols_.length; i++)
		{
			String col = cols_[i];
			if (!strings.is_ok(col)) continue;
			
			output = add_placeholders(source_, output, col, i, quote_value_, quote_variable_);
		}
		
		return output;
	}
	
	static String add_placeholders(String source_, String query_, String col_, int id_, String quote_value_, String quote_variable_)
	{
		if (id_ <= WRONG_ID || !db.source_is_ok(source_) || !strings.are_ok(new String[] { query_, col_ })) return strings.DEFAULT;
		
		String output = query_;
		
		String placeholder = get_placeholder(id_);
	
		int start_i = get_table_i(source_, query_, quote_value_, quote_variable_);
		if (start_i <= strings.WRONG_I) return output;
		
		while (true)
		{
			int i = get_i_end_variable(quote_variable_ + col_ + quote_variable_, output, start_i, quote_value_);			
			if (i <= strings.WRONG_I) break;
			
			int[] is = get_is(output, i + 1, quote_value_);
			if (is == null) break;
			
			String[] before_after = strings.get_before_after(output, is);
			
			output = before_after[strings.BEFORE_I] + placeholder + before_after[strings.AFTER_I];
		
			start_i = i;
		}
		
		return output;
	}
	
	static boolean exists_simple(int id_select_count_query_, HashMap<Integer, String> changing_vals_, String type_, String quote_value_, String select_count_col_) { return (select_count_simple(id_select_count_query_, changing_vals_, type_, quote_value_, select_count_col_) > 0); }
	
	static int select_count_simple(int id_, HashMap<Integer, String> changing_vals_, String type_, String quote_value_, String select_count_col_) 
	{ 
		int output = 0;

		ArrayList<HashMap<String, String>> temp = execute_simple(id_, null, changing_vals_, type_, quote_value_);

		if (temp != null && temp.size() > 0) output = Integer.parseInt(temp.get(0).get(select_count_col_));

		return output;
	}

	static void insert_update_simple(int id_select_count_query_, int id_update_query_, int id_insert_query_, HashMap<Integer, String> changing_vals_, String type_, String quote_value_, String select_count_col_) 
	{	
		if (exists_simple(id_select_count_query_, changing_vals_, type_, quote_value_, select_count_col_)) update_simple(id_update_query_, changing_vals_, type_, quote_value_);
		else insert_simple(id_insert_query_, changing_vals_, type_, quote_value_);
	}

	static void insert_simple(int id_, HashMap<Integer, String> changing_vals_, String type_, String quote_value_) { execute_simple(id_, null, changing_vals_, type_, quote_value_); }

	static void update_simple(int id_, HashMap<Integer, String> changing_vals_, String type_, String quote_value_) { execute_simple(id_, null, changing_vals_, type_, quote_value_); }
	
	static ArrayList<HashMap<String, String>> execute_simple(int id_, String[] cols_, HashMap<Integer, String> changing_vals_, String type_, String quote_value_)
	{
		ArrayList<HashMap<String, String>> output = null;

		try
		{
			String source = _sources[id_];
			
			String query = get_query_execute_simple(source, _queries[id_], quote_value_, changing_vals_);

			output = execute_query(type_, source, query, _returns[id_], cols_, _are_quick[id_]);
		}
		catch (Exception e) { output = null; }

		return output;
	}
	
	static ArrayList<HashMap<String, String>> execute(int id_, String[] cols_, HashMap<String, HashMap<Integer, String>> changing_vals_, String type_, String quote_value_, String quote_variable_)
	{
		ArrayList<HashMap<String, String>> output = null;
		if (!id_is_ok(id_)) return output;

		String source = _sources[id_];
		String query = get_query_execute(source, _queries[id_], changing_vals_, quote_value_, quote_variable_);
		
		if (!strings.are_ok(new String[] { query, source })) return output;
		
		boolean return_data = _returns[id_];
		boolean is_quick = _are_quick[id_];

		return execute_query(type_, source, query, return_data, cols_, is_quick);
	}

	private static ArrayList<HashMap<String, String>> execute_query(String type_, String source_, String query_, boolean return_data_, String[] cols_, boolean is_quick_) { return (is_quick_ ? db_quick.execute_query(type_, source_, query_, return_data_, cols_) : db.execute_query(type_, source_, query_, return_data_, cols_)); }

	private static String get_query_add(String source_, String query_, String quote_value_, String quote_variable_) { return (get_table_i(source_, query_, quote_value_, quote_variable_) > strings.WRONG_I ? query_ : null); }

	private static String get_query_execute(String source_, String query_, HashMap<String, HashMap<Integer, String>> changing_vals_, String quote_value_, String quote_variable_)
	{	
		int table_i = get_table_i(source_, query_, quote_value_, quote_variable_);
		if (table_i <= strings.WRONG_I) return null;
		
		String output = query_;
		if (!arrays.is_ok(changing_vals_)) return output;

		if (changing_vals_.containsKey(MAIN) && changing_vals_.get(MAIN).size() > 0) output = get_query_execute_main(source_, output, table_i, changing_vals_.get(MAIN), quote_value_);
		if (changing_vals_.containsKey(WHERE) && changing_vals_.get(WHERE).size() > 0) output = get_query_execute_where(source_, output, table_i, changing_vals_.get(WHERE), quote_value_);
		if (changing_vals_.containsKey(ORDER) && changing_vals_.get(ORDER).size() > 0) output = get_query_execute_order(source_, output, table_i, changing_vals_.get(ORDER), quote_value_);
			
		return output; 
	}	
	
	private static String get_query_execute_simple(String source_, String query_, String quote_value_, HashMap<Integer, String> changing_vals_)
	{
		String output = query_;
		if (!arrays.is_ok(changing_vals_)) return output;
		
		for (Entry<Integer, String> item: changing_vals_.entrySet()) { output = output.replace(get_placeholder(item.getKey()), quote_value_ + item.getValue() + quote_value_); }
		
		return output;
	}

	private static int get_table_i(String source_, String query_, String quote_value_, String quote_variable_)
	{
		String table = db.get_table(source_);

		return (strings.is_ok(table) ? get_i_end_variable(quote_variable_ + table + quote_variable_, query_, 0, quote_value_) : strings.WRONG_I);
	}
	
	private static String get_query_execute_main(String source_, String output_, int table_i_, HashMap<Integer, String> changing_vals_, String quote_value_)
	{
		String output = output_;
		
		int temp = get_i_end_string("insert into ", output, 0, quote_value_);
		boolean is_insert = (temp > strings.WRONG_I && temp < table_i_);
		
		if (is_insert) output = get_query_execute_insert(source_, output, changing_vals_);
		else 
		{
			int[] is = get_is(source_, output, table_i_, MAIN, quote_value_);
			
			if (is != null) output = get_query_execute_common(source_, output, is, changing_vals_, quote_value_);			
		}
	
		return output;
	}
	
	private static String get_query_execute_insert(String source_, String output_, HashMap<Integer, String> changing_vals_)
	{
		String output = output_;
		
		return output;
	}

	private static String get_query_execute_where(String source_, String output_, int table_i_, HashMap<Integer, String> changing_vals_, String quote_value_) { return get_query_execute_common(source_, output_, get_is(source_, output_, table_i_, WHERE, quote_value_), changing_vals_, quote_value_); }

	private static String get_query_execute_order(String source_, String output_, int table_i_, HashMap<Integer, String> changing_vals_, String quote_value_) { return get_query_execute_common(source_, output_, get_is(source_, output_, table_i_, ORDER, quote_value_), changing_vals_, quote_value_); }
	
	private static String get_query_execute_common(String source_, String query_, int[] is_, HashMap<Integer, String> changing_vals_, String quote_value_)
	{
		String output = query_;
		if (!strings.is_are_ok(is_)) return output;
		
		String haystack = strings.substring(output, is_[0], (is_[1] - is_[0] + 1));
		if (!strings.is_ok(haystack)) return output;

		for (Entry<Integer, String> item: changing_vals_.entrySet()) { haystack = haystack.replace(get_placeholder(item.getKey()), get_value(source_, item.getValue(), quote_value_)); }
		
		return strings.substring(output, 0, is_[0]) + haystack + strings.substring(output, is_[1] + 1);
	}
	
	private static String get_variable(String source_, String variable_, String quote_variable_) { return (quote_variable_ == null ? db.get_variable(source_, variable_) : (quote_variable_ + variable_ + quote_variable_)); }
	
	private static String get_value(String source_, String value_, String quote_value_) 
	{
		String value = (value_ == null ? "placeholder" : value_);
		
		return (quote_value_ == null ? db.get_value(source_, value) : (quote_value_ + value + quote_value_)); 
	}

	private static int[] get_is(String source_, String haystack_, int table_i_, String what_, String quote_value_)
	{
		String[] items = null;
		
		if (what_.equals(MAIN)) items = new String[] { null, db.get_keyword_where(source_), db.get_keyword_order(source_), db.get_keyword_max_rows(source_) };
		else if (what_.equals(WHERE)) items = new String[] { db.get_keyword_where(source_), db.get_keyword_order(source_), db.get_keyword_max_rows(source_) };
		else if (what_.equals(ORDER)) items = new String[] { db.get_keyword_order(source_), db.get_keyword_max_rows(source_) };

		int[] is = get_is(items, haystack_, true, table_i_ + 1, false, quote_value_);
	
		return is;
	}

	private static int[] get_is(String haystack_, int start_i_, String quote_value_) { return get_is(new String[] { quote_value_, quote_value_ }, haystack_, false, start_i_, true, quote_value_); }
	
	public static int[] get_is(String[] needles_, String haystack_, boolean normalise_, int start_i_, boolean find_include_end_, String quote_value_) { return strings.get_is(needles_, haystack_, normalise_, quote_value_, quote_value_, start_i_, find_include_end_); }
	
	private static int get_i_end_variable(String needle_, String haystack_, int start_i_, String quote_value_) { return get_i(needle_, haystack_, false, start_i_, false, quote_value_); }

	private static int get_i_end_string(String needle_, String haystack_, int start_i_, String quote_value_) { return get_i(needle_, haystack_, true, start_i_, false, quote_value_); }

	private static int get_i(String needle_, String haystack_, boolean normalise_, int start_i_, boolean return_start_, String quote_value_) 
	{ 
		int output = strings.get_i(needle_, haystack_, normalise_, quote_value_, quote_value_, start_i_);
		
		if (output > strings.WRONG_I && !return_start_) output = strings.get_end_i(output, needle_.length());
		
		return output; 
	}
}