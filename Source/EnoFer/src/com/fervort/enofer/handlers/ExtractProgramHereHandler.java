package com.fervort.enofer.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.IMarkSelection;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import com.fervort.enofer.dialogs.ExtractProgramDialog;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;

public class ExtractProgramHereHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell parent = HandlerUtil.getActiveShell(event).getShell();
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
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong Selection. This is project. Select Source Folder and then click this option.");
    				}else if(obFirstSelection instanceof org.eclipse.jdt.core.IPackageFragmentRoot)
    				{
    					IResource selectedResource = (IResource)((IAdaptable)obFirstSelection).getAdapter(IResource.class);
    					ExtractProgramDialog extractProgramDialog = new ExtractProgramDialog(parent);
    					extractProgramDialog.setSourceFolderPath(selectedResource.getLocation().toOSString());
    					extractProgramDialog.open();
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info","This is file"+selectedResource.getLocation());
    					
    				}else if(obFirstSelection instanceof org.eclipse.jdt.core.IPackageFragment)
    				{
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong Selection. This is package. Select Source Folder and then click this option.");
    				}
    				else if(obFirstSelection instanceof org.eclipse.jdt.core.ICompilationUnit)
    				{
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong Selection. This is is file. Select Source Folder and then click this option.");
    					
    				}else
    				{
    					MessageDialog.openInformation(window.getShell(), "EnoFer Info", "Wrong Selection. Select Source Folder and then click this option.\n"+obFirstSelection);
    					System.out.print("Found uncategories class to cast: "+obFirstSelection);
    					System.out.print(obFirstSelection);	
    				}
    				
    			}
    			
    		}
    		if (selection instanceof ITextSelection) {
    			MessageDialog.openError(window.getShell(),"EnoFer Error","Wrong Selection. You have selected text. Select Source Folder and then click this option.");
    			//ITextSelection ts  = (ITextSelection) selection;
    			//System.out.println(ts.getText());
    			//System.out.println("ITextSelection "+ts.getText());
    		}
    		if (selection instanceof IMarkSelection) {
    			MessageDialog.openError(window.getShell(),"EnoFer Error","Wrong Selection. You have selected text. Select Source Folder and then click this option.");
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
	
	

	void extractProgram()
	{
		
	}

}
