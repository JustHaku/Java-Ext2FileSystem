import java.util.*;
import java.io.*;
import java.nio.*;

/**
 * This class is used to create helper objects to be
 * referenced in the Ext2File class.
 * @author Nathaniel Vanderpuye
 */
public class Helper
{
	
	/**
	* Takes an array of bytes and converts it to
	* a block of formatted hex text.
	* @param bytes An array of read bytes.
	*/
	public Helper(byte[] bytes)
	{
		
		StringBuilder sb = new StringBuilder();
		int i = 0;
		
		System.out.println("\nHex dump: ");
		
		for (byte b : bytes)
		{
			if(i < 8)
			{
				sb.append(String.format("%02X ", b));
				i++;
			}
			
			else
			{
				sb.append(String.format("\n", b));
				i = 0;
			}
		}
		System.out.println(sb.toString());	
	}
	
}