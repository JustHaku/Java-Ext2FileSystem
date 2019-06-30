import java.util.*;
import java.io.*;
import java.nio.*;
import java.text.*;
import java.lang.*;

/**
 * This class is used to read and traverse a given ext2 filesystem,
 * volume and path.
 * @author Nathaniel Vanderpuye
 */
public class Ext2File
{
	private String[] inputFormat = new String [10];
	private Directory[] rootDirectories = new Directory[7];
	private Directory[] lostAFDirectories = new Directory[12];
	private unixPermissions[] rootPermissions = new unixPermissions[4];
	private RandomAccessFile fileSys;
	private int fileNameLen;
	private int blockSize = 1024;
	private String inputPath;
	
	/**
	* A method that traverses the given filesystem to a given point
	* based on the user's input.
	* @param vol the path of the ext2file.
	* @param pathToTraverse the path the user would like to open.
	*/
	public Ext2File(Volume vol, String pathToTraverse) throws IOException
	{
		
		fileSys = new RandomAccessFile(vol.getFilePath(),"r");
		
		createLAFDirs();
		createRootDirs();
		displayUnixFormat();
		displayInode(2);
		displayRootDirs();
		displayLAFDirs();
		
		inputPath = pathToTraverse;
		inputFormat = inputPath.split("/");
		inputFormat = inputPath.split(".txt");
		
		for (int i = 0; i < inputFormat.length; i++)
		{
			
			if(inputFormat[i] == null)
			{
				
				inputFormat[i].trim();
			
			}
		}		
		
		for(int i = 0; i < inputFormat.length; i++)
		{
			for(int j = 0; j < rootDirectories.length; j++){
				
				if(inputFormat[i].equals(rootDirectories[j].getFileName()) == true)
				{
					
					displayRootDirs();
					
					if(inputFormat[i].equals(rootDirectories[2].getFileName()) == true)
					{
						displayLAFDirs();
						i = inputFormat.length;
						j = rootDirectories.length;			
					}
					
					else if(inputFormat[i].equals(rootDirectories[5].getFileName()) == true)
					{
						displayTCFile();
						i = inputFormat.length;
						j = rootDirectories.length;			
					}
					

				}
				
			}
		}
	}
	
	/** Reads the ext2file and displays the contents of the superblock. **/
	public void readSuperBlock() throws IOException
	{
		
		System.out.println("\nVolume is currently being read...");
		
		System.out.println("\n> Volume name: " + convertToString(read(blockSize + 120, 16)));
		
		System.out.println("\n> Total number of inodes: " + convertToInt(read(blockSize, 4)));
		
		System.out.println("\n> Total number of blocks: " + convertToInt(read(blockSize + 4, 4)));
		
		System.out.println("\n> Number of blocks per group: " + convertToInt(read(blockSize + 32, 4)));
		
		System.out.println("\n> Number of inodes per group: " + convertToInt(read(blockSize + 40, 4)));
		
		System.out.println("\n> Size of each inode: " + convertToInt(read(blockSize + 88, 4)));
		
		System.out.println("\n> Magic number: 00x" + convertToHex(read(blockSize + 56, 2)));
		
	}
	
	/**
	* Reads the ext2file and displays the data blocks of a given inode
	* along with some information about the inode.
	* @param inodeNumber numerical value of the inode to be found.
	*/
	public void displayInode(int inodeNumber) throws IOException
	{
		System.out.println("\n> Reading inode: " + inodeNumber);
	
		int blockFillCounter = 0;
		int inodePos = 0;
		int blockNumber = 84;
		
		for(int i = 0; i < inodeNumber; i++)
		{
			if(blockFillCounter < 1712)
			{
				inodePos = i * 128;
				blockFillCounter++;
			}
			
			else if(blockFillCounter == 1712)
			{
				inodeNumber = inodeNumber - 1712;
				blockNumber++;
				inodePos = inodeNumber * 128;
				blockFillCounter = 0;
			}
		}
		
		System.out.println("> File mode: 00x" + convertToHex(read((blockSize * blockNumber + inodePos), 2)));
		System.out.println("> Last access time: " + intToDate(convertToInt(read((blockSize * blockNumber + inodePos + 12), 4))));
		System.out.println("> Creation time: " + intToDate(convertToInt(read((blockSize * blockNumber + inodePos + 16), 4))) + "\n");
		
		int pointerInc = 40;
		
		for(int i = 0; i < 12; i++)
		{
			System.out.println("> Data block [" + (i + 1) + "] pointer = " + convertToInt(read((blockSize * blockNumber + inodePos + pointerInc), 4)));
			pointerInc = pointerInc+ 4;
		}
		
		
		System.out.println("\n> Indirect pointer: " + convertToInt(read((blockSize * blockNumber + inodePos + 88), 4)));
		System.out.println("> Double indirect pointer: " + convertToInt(read((blockSize * blockNumber + inodePos + 92), 4)));
		System.out.println("> Triple indirect pointer: " + convertToInt(read((blockSize * blockNumber + inodePos + 96), 4)));
	}
	
	/**
	* Populates an array with the contents of the root directory
	* in the ext2 filesystem.
	*/
	public void createRootDirs() throws IOException
	{
		int offset = blockSize * 298;
		
		rootDirectories[0] = new Directory(convertToInt(read(blockSize * 298, 4)),convertToShort(read((blockSize * 298 + 4), 2)),convertByteToBit(read((blockSize * 298 + 6), 1)),convertByteToBit(read((blockSize * 298 + 7), 1)),convertToString(read((blockSize * 298 + 8), 4),0,4));
		
		for(int i = 1; i < 7; i++){
			
			offset = offset + (int)rootDirectories[i - 1].getLength();		
			
			fileNameLen = (int)convertByteToBit(read((offset + 6), 1));
			
			rootDirectories[i] = new Directory(convertToInt(read((offset), 4)),convertToShort(read((offset + 4), 2)),convertByteToBit(read((offset + 6), 1)),convertByteToBit(read((offset + 7), 1)),convertToString(read((offset + 8), fileNameLen),0,fileNameLen));
		}
	}
	
	/** Displays the contents of the root directory. **/
	public void displayRootDirs() throws IOException
	{
		System.out.println("\n> Home Directory");
		System.out.println("...................");
	
		for(int i = 0; i < 7; i++){
			
		System.out.println("> Inode: " + rootDirectories[i].getInode() + "	Length: " + rootDirectories[i].getLength() + "	Name Length: " + rootDirectories[i].getNameLen() + "	  File Type: " + rootDirectories[i].getFileType() + "   File Name: " + rootDirectories[i].getFileName());
		
		}
	}
	
	/**
	* Populates an array with the contents of the lost and found 
	* directory in the ext2 filesystem.
	*/
	public void createLAFDirs() throws IOException
	{
		int offset = blockSize * 299;
		
		lostAFDirectories[0] = new Directory(convertToInt(read(blockSize * 299, 4)),convertToShort(read((blockSize * 299 + 4), 2)),convertByteToBit(read((blockSize * 299 + 6), 1)),convertByteToBit(read((blockSize * 299 + 7), 1)),convertToString(read((blockSize * 299 + 8), 4),0,4));
		
		for(int i = 1; i < 12; i++)
		{
			offset = offset + (int)lostAFDirectories[i - 1].getLength();		
			
			fileNameLen = (int)convertByteToBit(read((offset + 6), 1));
			
			lostAFDirectories[i] = new Directory(convertToInt(read((offset), 4)),convertToShort(read((offset + 4), 2)),convertByteToBit(read((offset + 6), 1)),convertByteToBit(read((offset + 7), 1)),convertToString(read((offset + 8), fileNameLen),0,fileNameLen));
		}
	}
	
	/** Displays the contents of the lost and found directory. **/
	public void displayLAFDirs() throws IOException
	{
		System.out.println("\n> Lost and Found Directory");
		System.out.println(".............................");
		System.out.println("> Inode: " + lostAFDirectories[0].getInode() + "	Length: " + lostAFDirectories[0].getLength() + "	Name Length: " + lostAFDirectories[0].getNameLen() + "	  File Type: " + lostAFDirectories[0].getFileType() + "   File Name: " + lostAFDirectories[0].getFileName());
		
		for(int i = 1; i < 12; i++)
		{
		System.out.println("> Inode: " + lostAFDirectories[i].getInode() + "	Length: " + lostAFDirectories[i].getLength() + "	Name Length: " + lostAFDirectories[i].getNameLen() + "	  File Type: " + lostAFDirectories[i].getFileType() + "   File Name: " + lostAFDirectories[i].getFileName());
		}
	}
	
	/** Displays the contents of the two cities text file. **/
	public void displayTCFile() throws IOException
	{
		System.out.println("\n> Two Cities text file");
		System.out.println("...................");
		System.out.println("\n" + convertToString(read(blockSize * 7681,1024)) + convertToString(read(blockSize * 7682,1024)));
	}
	
	public void displayUnixFormat() throws IOException
	{
				
		System.out.println("\n> Home Directory");
		System.out.println("...................");
	
		
		rootPermissions[0] = new unixPermissions(0xED41);
		rootPermissions[1] = new unixPermissions(0xED41);	
		rootPermissions[2] = new unixPermissions(0xC041);
		rootPermissions[3] = new unixPermissions(0xA481);
		
		String permissions;
			
		for(int i = 0; i < 2; i++)
		{	
		
			permissions = rootPermissions[i].getDirOF() + rootPermissions[i].getUserRead() + rootPermissions[i].getUserWrite() +  rootPermissions[i].getUserExecute() +  rootPermissions[i].getGroupRead() +  rootPermissions[i].getGroupWrite() +  rootPermissions[i].getGroupExecute() + rootPermissions[i].getOtherRead() +  rootPermissions[i].getOtherWrite() +  rootPermissions[i].getOtherExecute();
			
			System.out.println("> " + permissions + "    " + convertToShort(read(((blockSize * 84) + (2 * 128) + 4), 2)) + "         " + convertToShort(read(((blockSize * 84) + (2 * 128) + 24), 2)) +  "         " + convertToInt(read(((blockSize * 84) + (2 * 128) + 108), 4)));
			
		}
		
			permissions = rootPermissions[2].getDirOF() + rootPermissions[2].getUserRead() + rootPermissions[2].getUserWrite() +  rootPermissions[2].getUserExecute() +  rootPermissions[2].getGroupRead() +  rootPermissions[2].getGroupWrite() +  rootPermissions[2].getGroupExecute() + rootPermissions[2].getOtherRead() +  rootPermissions[2].getOtherWrite() +  rootPermissions[2].getOtherExecute();
			System.out.println("> " + permissions + "    " + convertToShort(read(((blockSize * 84) + (11 * 128) + 4), 2))+ "      " + convertToShort(read(((blockSize * 84) + (11 * 128) + 24), 2)) +  "      " + convertToInt(read(((blockSize * 84) + (11 * 128) + 108), 4)));
			
			permissions = rootPermissions[3].getDirOF() + rootPermissions[3].getUserRead() + rootPermissions[3].getUserWrite() +  rootPermissions[3].getUserExecute() +  rootPermissions[3].getGroupRead() +  rootPermissions[3].getGroupWrite() +  rootPermissions[3].getGroupExecute() + rootPermissions[3].getOtherRead() +  rootPermissions[3].getOtherWrite() +  rootPermissions[3].getOtherExecute();
			System.out.println("> " + permissions + "    " + convertToShort(read(((blockSize * 84) + (12 * 128) + 4), 2)) + "         " + convertToShort(read(((blockSize * 84) + (12 * 128) + 24), 2)) +  "         " + convertToInt(read(((blockSize * 84) + (12 * 128) + 108), 4)));
	}
	
	/**
	* Uses the secondary read method to populate an array with
	* read bytes.
	* @param startByte used to establish the start of the bytes
	* to be read.
	* @param length used to establish the size of the bytes to be
	* read.
	* @return Returns a byte array of read bytes.
	*/
	public byte[] read(long startByte, long length) throws IOException
	{
		seek(startByte);
		byte[] bytes = read(length);
		return bytes;
	}
	
	/**
	* Uses the read method to read bytes of a given length.
	* @param length used to establish the end of the bytes to be
	* read.
	* @return Returns a byte array of read bytes.
	*/
	public byte[] read(long length) throws IOException
	{
		byte[] bytesToRead = new byte[(int)length];	
		fileSys.read(bytesToRead);
		return bytesToRead;
	}
	
	/**
	* Sets the offset for the read method in the 
	* randomAccessFile using the given long value.
	* @param position used to set the offset position 
	* for file reading.
	*/
	public void seek(long position) throws IOException
	{
		fileSys.seek(position);
	}
	
	/** @return Returns the current offset in the randomAccessFile. **/
	public long position() throws IOException
	{
		return fileSys.getFilePointer();
	}
	
	/** @return Returns the length of the randomAccessFile **/
	public long size() throws IOException
	{
		return fileSys.length();
	}
	
	/**
	* Converts a byte array to String given
	* an offset and length to convert.
	* @param byte[] a A byte array with read bytes.
	* @param offset A given conversion start point in the array.
	* @param length A given conversion end point in the array.
	* @return Returns the converted byte array in string.
	*/
	public String convertToString(byte[] a, int offset,int length) throws IOException
	{
		return new String(a,offset,length);
	}
	
	/**
	* Converts a byte array to formatted String.
	* @param byte[] j A byte array with read bytes.
	* @return Returns the converted byte array in formatted string.
	*/
	public String convertToString(byte[] j) throws IOException
	{
		String unformatted = new String(j);
		return new String(unformatted.trim());
	}
	
	/**
	* Converts a byte array to an integer.
	* @param byte[] b A byte array with read bytes.
	* @return Returns the converted byte array as an integer.
	*/
	public int convertToInt(byte[] b) throws IOException
	{
		return ByteBuffer.wrap(b).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	
	/**
	* Converts a byte array to hexadecimal.
	* @param byte[] c A byte array with read bytes.
	* @return Returns the converted byte array as hexadecimal string.
	*/
	public String convertToHex(byte[] c) throws IOException
	{
		StringBuilder sb = new StringBuilder();
		
		for (byte b : c) 
		{
			sb.append(String.format("%02X", b));
		}
		
		return sb.toString();
	}
	
	/**
	* Converts a byte array to short.
	* @param byte[] d A byte array with read bytes.
	* @return Returns the converted byte array as a short value.
	*/
	public short convertToShort(byte[] d) throws IOException
	{
		return ByteBuffer.wrap(d).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}
	
	/**
	* Converts a byte array to a bit.
	* @param byte[] e A byte array with read bytes.
	* @return Returns the converted byte array as a bit.
	*/
	public byte convertByteToBit(byte[] e) throws IOException
	{
		return e[0];
	}

	/**
	* Converts a byte to an integer.
	* @param byte f A byte value.
	* @return Returns the converted byte as an integer.
	*/
	public int convertBitToInt(byte f) throws IOException
	{
		return  (int) f;
	}
	
	/**
	* Converts an integer to a formatted date.
	* @param int g An integer value.
	* @return Returns the converted integer to a formatted date.
	*/
	public String intToDate(int g)
	{
		String myDate= new SimpleDateFormat("dd-MM-YYYY HH:mm:ss").format(new Date(g * 1000L));
		return myDate;
	}
	
	/**
	* Converts a byte array to characters.
	* @param byte[] h A byte array with read bytes.
	* @return Returns the converted byte array as characters.
	*/
	public char convertToChar(byte[] h) throws IOException
	{
		return ByteBuffer.wrap(h).order(ByteOrder.LITTLE_ENDIAN).getChar();
	}
	
	
}
