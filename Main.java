public class Main
{

	public static void main(String [] args)
	{
		
		try
		{
			Volume  vol = new Volume("ext2fs");
			Ext2File  f = new Ext2File (vol,"w");
			byte buf[ ] = f.read(0L, f.size());
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
