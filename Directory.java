import java.util.*;
import java.io.*;
import java.nio.*;

/**
 * This class is used to create directory objects to be 
 * referenced in the Ext2File class.
 * @author Nathaniel Vanderpuye
 */
public class Directory
{
	private int inode;
	private short length;
	private byte nameLen;
	private byte fileType;
	private String fileName;
	
	/**
	* Takes various variables that are then attributed to each
	* directory.
	* @param inodeIn The inode of the directory.
	* @param lengthIn The length of the directory.
	* @param nameLenIn The length of the name of the directory.
	* @param fileTypeIn The Type of the directory.
	* @param fileNameIn The file name of the directory
	*/
	public Directory(int inodeIn,short lengthIn,byte nameLenIn,byte fileTypeIn,String fileNameIn)
	{
		inode = inodeIn;
		length = lengthIn;
		nameLen = nameLenIn;
		fileType = fileTypeIn;
		fileName = fileNameIn;
	}
	
	/** @return Returns the inode of the directory. **/
	public int getInode()
	{
		return inode;
	}
	
	/** @return Returns the length of the directory. **/
	public short getLength()
	{
		return length;
	}
	
	/** @return Returns the name length of the directory. **/
	public byte getNameLen()
	{
		return nameLen;
	}
	
	/** @return Returns the type of the directory. **/
	public byte getFileType()
	{
		return fileType;
	}
	
	/** @return Returns the name of the directory. **/
	public String getFileName()
	{
		return fileName;
	}
	
}