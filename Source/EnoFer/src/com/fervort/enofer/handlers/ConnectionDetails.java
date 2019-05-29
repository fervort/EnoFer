package com.fervort.enofer.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import com.fervort.enofer.Activator;

public class ConnectionDetails extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		
		MessageDialog.openInformation(
				window.getShell(),
				"EnoFer",
				"You are connected to "+Activator.getDefault().getPreferenceStore()
                 .getString("com.fervort.enofer.preferencesstore.settings.enovia.host"));
                 
		
		return null;
	}
}
