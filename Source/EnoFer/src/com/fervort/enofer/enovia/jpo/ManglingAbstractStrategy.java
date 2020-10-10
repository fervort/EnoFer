package com.fervort.enofer.enovia.jpo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public abstract class ManglingAbstractStrategy implements IManglingStrategy {

	public File getFile(String strPath)
	{
		return new File(strPath);
	}
	
	public String getFileNameWithoutExtension(File file) {
		
		return file.getName().replaceFirst("[.][^.]+$", "");
		
	}
	
	protected String readAllFile(String strPath) throws IOException
	{
		return new String(Files.readAllBytes(Paths.get(strPath)));
	}
	
	public String getJPOName(String filePath)
	{
		return getFileNameWithoutExtension(getFile(filePath)).replace("_mxJPO", "");
	}
	/***
	 * replace("\\", ".") might not work on linux as path is /opt/abc/pqr
	 * 
	 */
	public String getPackageName(String filePath,String filePathAfter) 
	{
		File file =getFile(filePath);
		String absolutePath =file.getParentFile().getAbsolutePath();
		String[] parentPathPart = absolutePath.split(Pattern.quote(filePathAfter),2);
		
		if(parentPathPart.length==2)
		{
			String packageName = parentPathPart[1].replace("\\", ".").trim();
			// after soliting by src it generates .com.fer.abc.pqr
			if(packageName.startsWith("."))
				return packageName.substring(1);
		}

		return "";
		
		//return parentPathPart.length==2?parentPathPart[1].replace("\\", "."):"";
		
	}
	
	public String getJPONameWithPackage(String filePath,String filePathAfter) 
	{
		String packageName = getPackageName(filePath, filePathAfter);
		if(packageName.isEmpty())
		{
			return getJPOName(filePath);
		}
		return packageName+"."+getJPOName(filePath);
	}
}
