package com.fervort.enofer.handlers;

import java.io.File;

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
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
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

public class ImportAndCompilerProgramHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

        if( window != null )
        {
            ISelection selection = (ISelection) window.getSelectionService().getSelection();
            
             
            if (selection instanceof IStructuredSelection) {
    			IStructuredSelection structuredSelection = (IStructuredSelection) selection;
    			if(structuredSelection.size()!=1)
    			{
    				MessageDialog.openError(window.getShell(),"EnoFer Error","Wrong Selection. You have selected "+structuredSelection.size()+" Componenets. Please select only one component at a time.");
    			}else
    			{
    				Object obFirstSelection = structuredSelection.getFirstElement();
    				if(obFirstSelection instanceof org.eclipse.jdt.core.IJavaProject)
    				{
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong Selection. This is project. Select program and then click this option.");
    				}else if(obFirstSelection instanceof org.eclipse.jdt.core.IPackageFragmentRoot)
    				{
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong Selection. This is Source Folder. Select program and then click this option.");
    				}else if(obFirstSelection instanceof org.eclipse.jdt.core.IPackageFragment)
    				{
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong Selection. This is package. Select program and then click this option.");
    				}
    				else if(obFirstSelection instanceof org.eclipse.jdt.core.ICompilationUnit)
    				{
    					//MessageDialog.openInformation(window.getShell(), "EnoFer Info", "This is file");
    					
    					IResource selectedResource = (IResource)((IAdaptable)obFirstSelection).getAdapter(IResource.class);
    					//MessageDialog.openInformation(window.getShell(), "EnoFer Info","This is file "+selectedResource.getLocation());
    					importAndCompileProgram(window,obFirstSelection,selectedResource.getLocation().toOSString());
    				}else
    				{
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong Selection. Select program and then click this option.\n"+obFirstSelection);
    					System.out.print("Found uncategories class to cast: "+obFirstSelection);
    					System.out.print(obFirstSelection);	
    				}
    				
    			}
    			
    		}
    		if (selection instanceof ITextSelection) {
    			MessageDialog.openError(window.getShell(),"EnoFer Error","Wrong Selection. You have selected text .You have to select Program to import.");
    			//ITextSelection ts  = (ITextSelection) selection;
    			//System.out.println(ts.getText());
    			//System.out.println("ITextSelection "+ts.getText());
    		}
    		if (selection instanceof IMarkSelection) {
    			MessageDialog.openError(window.getShell(),"EnoFer Error","Wrong Selection. You have selected text .You have to select Program to import.");
    			/*
    			IMarkSelection ms = (IMarkSelection) selection;
    			System.out.println("IMarkSelection "+ms.getDocument());
    			try {
    			    System.out.println(ms.getDocument().get(ms.getOffset(), ms.getLength()));
    			    
    			} catch (BadLocationException ble) { }
    			*/
    		}
    		
        }
		
		return null;
	}
	
	void importAndCompileProgram(IWorkbenchWindow window,Object obFirstSelection,String strFullPath) 
	{
		
		// TODO Implement this feature
		// throws JavaModelException
		// String strSouceFolderName = CommonHandlerUtilities.getSourceFolderName(obFirstSelection);
		try {
			String strServerJPOPath = Activator.getDefault().getPreferenceStore().getString("com.fervort.enofer.preferencesstore.settings.enovia.serverjpodir");
			
			//strFullPath
			
			if(strServerJPOPath.trim().length()!=0)
			{
				String strFileName = new File(strFullPath).getName();
				strFullPath=strServerJPOPath+"/"+strFileName;
			}
			Logger.write("Insert full file path "+strFullPath);
			
			EnoviaUtility.executeMQL("insert program "+strFullPath);
			Logger.write("File inserted ");
			//MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Program inserted sucessfully !");
			
			String strFileName = new File(strFullPath).getName();
			String strJPOName = strFileName.replaceAll("_mxJPO.java", "");
			
			Logger.write("JPO Name "+strJPOName);
				
			EnoviaUtility.executeMQL("compile program '"+strJPOName+"' force update");
    		Logger.write("Program compiled ");
			MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Program inserted and compiled sucessfully !");
				
		} catch (Exception ex) {
			MessageDialog.openError(window.getShell(), "EnoFer Error", ""+ex);
			Logger.write("insert and compiled program failed "+strFullPath);
			String message = ex.getMessage();
			Logger.write("Exception "+message+" trace "+ex);
			Status status = new Status(IStatus.ERROR, "com.fervort.enofer", 0, message, ex);
			//ErrorDialog.openError(getShell(),"Failed","Couldn't export program from Enovia database", status);
		}
	}
	
	
}
