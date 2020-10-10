package com.fervort.enofer.enovia.jpo;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.jface.dialogs.MessageDialog;

import com.fervort.enofer.log.Logger;
import com.fervort.enofer.preferences.EnoProperties;

public class ExternalMangling extends ManglingAbstractStrategy {
	
	/***
	 * TODO: Duplicate code . Create common code to invoke java command
	 * @throws Exception 
	 * 
	 */
	@Override
	public String JPOToJavaSource(String strJPOPath) throws Exception {
		
			
		String strJavaDirClassMethod = EnoProperties.getPropertyValue("ExternalManglingJavaConnector");
		
		if(strJavaDirClassMethod==null)
		{
			Logger.write("ExternalManglingJavaConnector property is not set.");
			throw new Exception("ExternalManglingJavaConnector property is not set.");
		}
		try {
			
			String strClassDirName = null;
			if(strJavaDirClassMethod.contains("{") && strJavaDirClassMethod.contains("}"))
			{
				// TODO improve this logic
				strClassDirName = strJavaDirClassMethod.substring(strJavaDirClassMethod.indexOf("{") + 1, strJavaDirClassMethod.indexOf("}"));
				// Remove everything between curly braces and curly braces as well
				strJavaDirClassMethod = strJavaDirClassMethod.replaceAll("\\{.*?\\}:", "");
				
			}
			
			String[] aJavaPackageClassMethod = strJavaDirClassMethod.split(":");
			if(aJavaPackageClassMethod.length==2)
			{
				Class clazz = null;
				
				if(strClassDirName==null)
				{
					clazz = Class.forName(aJavaPackageClassMethod[0]);
				}
				else
				{
					File dir = new File(strClassDirName);
					URL[] cp = {dir.toURI().toURL()}; // construct URL from URI
					URLClassLoader urlCl = new URLClassLoader(cp);
					clazz = urlCl.loadClass(aJavaPackageClassMethod[0]);
				}
				
		        Method method=clazz.getDeclaredMethod(aJavaPackageClassMethod[1],String.class,String.class,String.class);
		        Object oClassInstance = clazz.newInstance();
		        Object oReturn= method.invoke(oClassInstance,strJPOPath,"","");
		        
		        return oReturn.toString();
		        
			}
			else
			{
				Logger.write("Java class name package name syntax is wrong. It should be java:full_class_name:methodname or java:{dir_path}:full_class_name:methodname . There should be three parameters to method");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in callJavaMethod() "+e);
			Logger.write("Exception in callJavaMethod() "+e);
		}

		
		
		return null;
	}

	@Override
	public String JaveSourceToJPO(String strJavaSource) {
		// TODO Auto-generated method stub
		return null;
	}

}
