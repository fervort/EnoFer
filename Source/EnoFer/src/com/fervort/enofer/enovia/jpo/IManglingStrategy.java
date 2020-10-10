package com.fervort.enofer.enovia.jpo;

import java.io.IOException;

interface IManglingStrategy {

	abstract String getJPOName(String filePath);
	
	abstract String getPackageName(String filePath,String filePathAfter) ;
	
	abstract String getJPONameWithPackage(String filePath,String filePathAfter);
	
	abstract String JPOToJavaSource(String strJPOPath) throws Exception;
	
	abstract String JaveSourceToJPO(String strJavaSource);
}
