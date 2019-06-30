public class unixPermissions{

	private int[] inodeFileModes = new int[10];
	String userRead;
	String userWrite;
	String userExecute;
	String groupRead;
	String groupWrite;
	String groupExecute;
	String othersRead;
	String othersWrite;
	String othersExecute;
	String dirOF;
	
	public unixPermissions(int fileMode)
	{
		
		inodeFileModes[0] = 0x0100;
		inodeFileModes[1] = 0x0080;
		inodeFileModes[2] = 0x0040;
		inodeFileModes[3] = 0x0020;
		inodeFileModes[4] = 0x0010;
		inodeFileModes[5] = 0x0008;
		inodeFileModes[6] = 0x0004;
		inodeFileModes[7] = 0x0002;
		inodeFileModes[8] = 0x0001;
		inodeFileModes[9] = 0x4000;
		
		
			
		if((inodeFileModes[0] & fileMode) > 0 )
		{
				
			userRead = "r";
				
		}
		else
		{
				
			userRead = "-";
				
		}
		
		if((inodeFileModes[1] & fileMode) > 0 )
		{
				
			userWrite = "w";
				
		}
		else
		{
				
			userWrite= "-";
				
		}
		
		if((inodeFileModes[2] & fileMode) > 0 )
		{
				
			userExecute = "x";
				
		}
		else
		{
				
			userExecute = "-";
				
		}
		
		if((inodeFileModes[3] & fileMode) > 0 )
		{
				
			groupRead= "r";
				
		}
		else
		{
				
			groupRead= "-";
				
		}
		
		if((inodeFileModes[4] & fileMode) > 0 )
		{
				
			groupWrite = "w";
				
		}
		else
		{
				
			groupWrite = "-";
				
		}
		
		if((inodeFileModes[5] & fileMode) > 0 )
		{
				
			groupExecute = "x";
				
		}
		else
		{
				
			groupExecute = "-";
				
		}
		
		if((inodeFileModes[6] & fileMode) > 0 )
		{
				
			othersRead = "r";
				
		}
		else
		{
				
			othersRead = "-";
				
		}	
			
		if((inodeFileModes[7] & fileMode) > 0 )
		{
				
			othersWrite = "w";
				
		}
		else
		{
				
			othersWrite = "-";
				
		}	
		
		if((inodeFileModes[8] & fileMode) > 0 )
		{
				
			othersExecute = "x";
				
		}
		else
		{
				
			othersExecute = "-";
				
		}
		
		if((inodeFileModes[9] & fileMode) > 0 )
		{
				
			dirOF = "d";
				
		}
		else
		{
				
			dirOF = "-";
				
		}
		
	}

	public String getDirOF(){
		
		return dirOF;
		
	}
	
	public String getUserRead(){
		
		return userRead;
		
	}
	
	public String getUserWrite(){
		
		return userWrite;
		
	}
	
	public String getUserExecute(){
		
		return userExecute;
		
	}
	
	public String getGroupRead(){
		
		return groupRead;
		
	}
	
	public String getGroupWrite(){
		
		return groupWrite;
		
	}
	
	public String getGroupExecute(){
		
		return groupExecute;
		
	}
	
	public String getOtherRead(){
		
		return othersRead;
		
	}
	
	public String getOtherWrite(){
		
		return othersWrite;
		
	}
	
	public String getOtherExecute(){
		
		return othersExecute;
		
	}
	
}