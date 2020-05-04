package com.fervort.enofer.handlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.fervort.enofer.Activator;
import com.fervort.enofer.enovia.EnoviaUtility;
import com.fervort.enofer.log.Logger;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

public class MyCommandHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        if( window != null )
        {
        	String strSelectionType = null;
        	String strSelectionResoruce = null;
            
        	ISelection selection = (ISelection) window.getSelectionService().getSelection();
            
             
            if (selection instanceof IStructuredSelection) {
    			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
    			if(structuredSelection.size()!=1)
    			{
    				MessageDialog.openError(window.getShell(),"EnoFer Error","Wrong Selection. You have selected "+structuredSelection.size()+" Componenets. Please select only one component at a time.");
    			}else
    			{
    				// TODO Implement in future to provide file, if multiples are selected 
    				Object obFirstSelection = structuredSelection.getFirstElement();
    				if(obFirstSelection instanceof org.eclipse.jdt.core.IJavaProject)
    				{
    					strSelectionType = "PROJECT";
    					IResource selectedResource = (IResource)((IAdaptable)obFirstSelection).getAdapter(IResource.class);
    					strSelectionResoruce = selectedResource.getLocation().toOSString();
    					
    				}else if(obFirstSelection instanceof org.eclipse.jdt.core.IPackageFragmentRoot)
    				{
    					strSelectionType = "SOURCE_FOLDER";
    					IResource selectedResource = (IResource)((IAdaptable)obFirstSelection).getAdapter(IResource.class);
    					strSelectionResoruce = selectedResource.getLocation().toOSString();
    					
    				}else if(obFirstSelection instanceof org.eclipse.jdt.core.IPackageFragment)
    				{
    					strSelectionType = "PACKAGE";
    					IResource selectedResource = (IResource)((IAdaptable)obFirstSelection).getAdapter(IResource.class);
    					strSelectionResoruce = selectedResource.getLocation().toOSString();
    				}
    				else if(obFirstSelection instanceof org.eclipse.jdt.core.ICompilationUnit)
    				{
    					strSelectionType = "COMPILATION_UNIT";
    					
    					IResource selectedResource = (IResource)((IAdaptable)obFirstSelection).getAdapter(IResource.class);
    					
    					strSelectionResoruce = selectedResource.getLocation().toOSString();
    					//importProgram(window,selectedResource.getLocation().toOSString());
    					
    				}else if(obFirstSelection instanceof org.eclipse.core.internal.resources.File)
    				{
    					strSelectionType = "RESOURCE_FILE";
    					IResource selectedResource = (IResource)((IAdaptable)obFirstSelection).getAdapter(IResource.class);
    					strSelectionResoruce = selectedResource.getLocation().toOSString();
    				}else if(obFirstSelection instanceof org.eclipse.core.internal.resources.Folder)
    				{
    					strSelectionType = "RESOURCE_FOLDER";
    					IResource selectedResource = (IResource)((IAdaptable)obFirstSelection).getAdapter(IResource.class);
    					strSelectionResoruce = selectedResource.getLocation().toOSString();
    					
    				}
    				else
    				{
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong Selection. Selected uncategories class .\n"+obFirstSelection +" class "+obFirstSelection.getClass());
    					System.out.print("Found uncategories class to cast: "+obFirstSelection.getClass());
    					System.out.print(obFirstSelection.getClass());	
    					// org.eclipse.core.internal.resources.File
    					 // folder org.eclipse.core.internal.resources.File
    					
    				}
    				
    			}
    			
    		}
    		if (selection instanceof ITextSelection) {
    			
    			strSelectionType = "TEXT";

    			
    		}
    		if (selection instanceof IMarkSelection) {
    			
    			strSelectionType = "MARK";
    			
    		}
    		
    		// call exe or batch file.
    		callCommand(window,strSelectionType,strSelectionResoruce);
        }
		
		return null;
	}
	
	void callCommand(IWorkbenchWindow window ,String strSelectionType,String strSelectionResoruce)
	{
		System.out.println("callmethod"+strSelectionType+strSelectionResoruce);
		try {
			String strMyCommand = Activator.getDefault().getPreferenceStore().getString("com.fervort.enofer.preferencesstore.settings.enovia.mycommand");
			
			if(strMyCommand.trim().toLowerCase().startsWith("file:"))
			{
				callExecutable(window,strSelectionType,strSelectionResoruce,strMyCommand.replaceFirst("file:", ""));
				
			}else if(strMyCommand.trim().toLowerCase().startsWith("java:"))
			{
				callJavaMethod(window,strSelectionType,strSelectionResoruce,strMyCommand.replaceFirst("java:", ""));
				
			}else
			{
				MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong command passed in setting 'My Command'. Command should start with file: or java: "
				+"\n For example: file:D:\\MyFolder\\MyCustomCommand.exe"
				+ "or java:com.fervort.commands.MyClass:MyMethod"		
				+strSelectionType+" === "+strSelectionResoruce);	
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Exception "+ex);
		}
	}
	
	void callExecutable(IWorkbenchWindow window ,String strSelectionType,String strSelectionResoruce, String strMyCommand)
	{
		try {
		
			String[] aCommand = strMyCommand.split("\\|");
			System.out.println("length "+aCommand.length);
			String strExecutable = aCommand[0];
			String strExtraInfo = aCommand.length >=2 ? aCommand[1]:"";
			
			Process process = new ProcessBuilder(strExecutable,strSelectionType,strSelectionResoruce,strExtraInfo) .start();
			InputStream processInputStream = process.getInputStream();
			//OutputSteam processOutputStream = process.getOutputStream();
			//BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(processOutputStream));
			BufferedReader reader = new BufferedReader(new InputStreamReader(processInputStream));
			String line;
			StringBuffer sb = new StringBuffer();
			while ((line = reader.readLine()) != null) {  
		        sb.append(line).append(System.lineSeparator()); // System.lineSeparator() should write \n or \r\n
		    }  
			reader.close(); 
			MessageDialog.openInformation(window.getShell(), "EnoFer Info", sb.toString());
		
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in callExecutable() "+e);
		}
	}
	
	void callJavaMethod(IWorkbenchWindow window ,String strSelectionType,String strSelectionResoruce, String strMyCommand)
	{
		String[] aCommand = strMyCommand.split("\\|");
		String strJavaPackageClass = aCommand[0];
		
		String strExtraInfo = aCommand.length >=2 ? aCommand[1]:"";
		
		try {
			String[] aJavaPackageClass = strJavaPackageClass.split(":");
			if(aJavaPackageClass.length==2)
			{
				Class clazz = Class.forName(aJavaPackageClass[0]);
				
		        Method method=clazz.getDeclaredMethod(aJavaPackageClass[1],String.class,String.class,String.class);
		        Object oClassInstance = clazz.newInstance();
		        Object oReturn= method.invoke(oClassInstance,strSelectionType,strSelectionResoruce,strExtraInfo);
		        
		        MessageDialog.openInformation(window.getShell(), "EnoFer Info", oReturn.toString());
		        
			}else
			{
				System.out.println("Java class name package name syntax is wrong. It should be java:full_class_name:methodname . There should be three parameters to method");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in callExecutable() "+e);
		}

		
	}

}
