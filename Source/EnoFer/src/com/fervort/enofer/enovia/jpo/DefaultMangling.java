package com.fervort.enofer.enovia.jpo;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class DefaultMangling extends ManglingAbstractStrategy {

	@Override
	public String JPOToJavaSource(String strJPOPath) throws IOException {
		
		File file = getFile(strJPOPath);
		String jpoName = getFileNameWithoutExtension(file);
		//String packageName = getPackageName(strJPOPath, "src");
		String fileContent = readAllFile(file.getAbsolutePath());
		
		return evaluateAllTechnique(fileContent,jpoName);
	}

	@Override
	public String JaveSourceToJPO(String strJavaSource) {
		throw new UnsupportedOperationException();
	}
	
	public String evaluateAllTechnique(String strContent,String jpoName)
	{
		String newContent = evaluateRemovePackage(strContent); 
		
		newContent = evaluateReplaceClassName(newContent,jpoName);
		newContent = evaluateReplaceJPOInvoke(newContent);
		newContent = evaluateReplaceBackslash(newContent);
		newContent = evaluateEscapeDoubleQuote(newContent);
		
		return newContent;
	}
	
	public String evaluateRemovePackage(String strContent)
	{
		return strContent.replaceAll("package\\s+[0-9A-Za-z_\\-\\.]+\\s*;", "");
	}
	
	public String evaluateReplaceClassName(String strContent,String jpoName)
	{
		return strContent.replaceAll(Pattern.quote(jpoName)+"\\b", "\\${CLASSNAME}");
	}

	public String evaluateReplaceJPOInvoke(String strContent)
	{
		return strContent.replaceAll("\\b([0-9A-Za-z_\\-\\.]+)_mxJPO\\b", "\\${CLASS:$1}");
	}
	
	public String evaluateReplaceBackslash(String strContent)
	{
		return strContent.replaceAll("\\\\", "\\\\\\\\");
	}
	
	public String evaluateEscapeDoubleQuote(String strContent)
	{
		//return strContent.replaceAll("\"", "\\\"");
		return strContent.replace("\"", "\\\"");
	}
	
}
