package com.fervort.enofer.enovia.jpo;

import java.io.IOException;

import com.fervort.enofer.preferences.EnoProperties;

public class ManglingContext {

	IManglingStrategy manglingStrategy;
	String filePath;
	
	public ManglingContext(String filePath) {
		
		if(EnoProperties.getPropertyValue("JPOMangling")!=null && EnoProperties.getPropertyValue("JPOMangling").equalsIgnoreCase("ExternalMangling"))
		{
			this.manglingStrategy= new ExternalMangling();
		}else
		{
			this.manglingStrategy= new DefaultMangling();
		}
		
		this.filePath= filePath;
	}

	public ManglingContext(IManglingStrategy manglingStrategy,String filePath) {
		
		this.manglingStrategy= manglingStrategy;
		this.filePath= filePath;
	}
	
	public String translateJPOToValidJavaClass() throws Exception
	{
		return this.manglingStrategy.JPOToJavaSource(this.filePath);
	}
	
	public String getJPOName() {
		return this.manglingStrategy.getJPOName(this.filePath);
	}
	
	public String getPackageName(String filePathAfter) {
		return this.manglingStrategy.getPackageName(this.filePath,filePathAfter);
	}
	
	public String getJPONameWithPackage(String filePathAfter) {
		return this.manglingStrategy.getJPONameWithPackage(this.filePath,filePathAfter);
	}
	
}
