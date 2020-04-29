package com.fervort.enofer.handlers;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

import com.fervort.enofer.log.Logger;

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
		if(SOURCE_FOLDER_NAME==null)
		{
			ICompilationUnit compilationUnit = (ICompilationUnit)iSelected; 
			
			IJavaProject javaProject = compilationUnit.getJavaProject();
			IPackageFragmentRoot packageFragements[] = javaProject.getAllPackageFragmentRoots();
			
			for(int i=0;i<packageFragements.length;i++)
			{
				IPackageFragment packageFragment = (IPackageFragment) packageFragements[i];
				if(packageFragment.getKind()==IPackageFragmentRoot.K_SOURCE)
				{
					SOURCE_FOLDER_NAME = packageFragment.getElementName();
					Logger.write("Generated Source Folder Name = "+SOURCE_FOLDER_NAME);
				}
			}
		}
		Logger.write("Returned Source Folder Name = "+SOURCE_FOLDER_NAME);
		return SOURCE_FOLDER_NAME;
	}
}
