package accessory;

public abstract class parent_ini_first
{
	protected boolean _populated = false;
			
	protected abstract void populate_internal_vals();
	
	protected void populate_internal()
	{
		if (_populated) return;
		
		populate_internal_vals();
		
		_populated = true;
	}
}
