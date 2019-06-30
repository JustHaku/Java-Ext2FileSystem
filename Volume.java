/**
 * This class is used to create volume objects to be
 * referenced in the Ext2File class.
 * @author Nathaniel Vanderpuye
 */
public class Volume
{	
	private String filePath;

	/**
	* Takes a string variable that represents the path
	* of the ext2file.
	* @param fileSystem Path of the ext2file.
	*/
	public Volume(String fileSystem)
	{
		filePath = fileSystem;
	}
	
	/** @return Returns the file path. **/
	public String getFilePath()
	{
		return filePath;
	}

}
