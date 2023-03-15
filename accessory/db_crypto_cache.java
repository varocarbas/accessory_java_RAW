package accessory;

class db_crypto_cache 
{
	private static String[] _cols = null;
	
	public static void reset_ids() { populate(true); }
	
	private static void populate(boolean force_population_)
	{
		if (!force_population_ && _cols != null) return;
		
		_cols = db.get_cols(db_crypto.SOURCE);
		
		add_queries();
	}
	
	private static void add_queries()
	{

	}
}