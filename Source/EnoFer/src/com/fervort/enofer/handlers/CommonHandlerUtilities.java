package com.fervort.enofer.handlers;

import java.util.Arrays;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;

import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;

import com.fervort.enofer.log.Logger;
import com.fervort.enofer.preferences.EnoProperties;
import com.fervort.enofer.preferences.PropertyPreferences;

public class CommonHandlerUtilities {

	public static String SOURCE_FOLDER_NAME = null;
	/**
	 *  All Selectables this should be same
	 *  
	 *  TODO : If two project will have different src folder names then this function will return false result.
	 *  Improve the function so that it will return correct src folder for respective project
	 */
	public static String getSourceFolderName(Object iSelected) throws JavaModelException
	{
		String strCustomSourceFolder = EnoProperties.getPropertyValue("SourceFolder");
		if(strCustomSourceFolder!=null)
		{
			return strCustomSourceFolder.trim();
		}
		
		if(SOURCE_FOLDER_NAME==null)
		{
			ICompilationUnit compilationUnit = (ICompilationUnit)iSelected; 
			
			IJavaProject javaProject = compilationUnit.getJavaProject();
			IPackageFragmentRoot packageFragements[] = javaProject.getAllPackageFragmentRoots();
			
			for(int i=0;i<packageFragements.length;i++)
			{
				// If jars then kind will be 1 (K_BINARY) and if source folder then kind=1 (K_SOURCE)
				// So ignore all jars
				if(packageFragements[i].getKind()==IPackageFragmentRoot.K_SOURCE)
				{
					//java.lang.ClassCastException: org.eclipse.jdt.internal.core.PackageFragmentRoot cannot be cast to org.eclipse.jdt.core.IPackageFragment
					PackageFragmentRoot pkgFrRoot = (PackageFragmentRoot) packageFragements[i];
					//IPackageFragment packageFragment = (IPackageFragment) packageFragements[i];
					SOURCE_FOLDER_NAME = pkgFrRoot.getElementName();
					Logger.write("Generated Source Folder Name = "+SOURCE_FOLDER_NAME);

				}
			}
		}
		Logger.write("Returned Source Folder Name = "+SOURCE_FOLDER_NAME);
		return SOURCE_FOLDER_NAME;
	}
	
	/*
	 * Split the path based on source folder
	 * 
	 * If in package
	 * D:\runtime-EclipseApplication\EnoviaJPOs\src\com\fervort\enoferapp\TestFromPackage_mxJPO.java
	 * Output should be: \com\fervort\enoferapp\TestFromPackage_mxJPO.java
	 * 
	 * If not inside the package
	 * D:\runtime-EclipseApplication\EnoviaJPOs\src\Test_mxJPO.java
	 * Output should be : \Test_mxJPO.java
	 * 
	 */
	public static String getFilePathFromPackage(String strFullFilePath,String strSourceFolder)
	{
		String[] aFullFilePath = strFullFilePath.split(strSourceFolder);
		Logger.write("After split: "+Arrays.toString(aFullFilePath));
		return aFullFilePath[1];
	}
	// TODO Might need changes for remote Windows OS
	public static String processDirPath(String strPath)
	{
		String strRemoteServerOS = EnoProperties.getPropertyValue("RemoteServerOS");
		if(strRemoteServerOS!=null)
		{
			if(strRemoteServerOS.trim().equalsIgnoreCase("Linux"))
			{
				return strPath.replace("\\", "/");
			}
		}
		return strPath;
	}
	
	
}
