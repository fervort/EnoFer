package com.fervort.enofer.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;

public class EnoFerPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

	public EnoFerPreferences() {
		super(GRID);
		setPreferenceStore(com.fervort.enofer.Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	public void init(IWorkbench workbench) {
		
		//setPreferenceStore(new ScopedPreferenceStore(InstanceScope.INSTANCE, "com.fervort.enofer"));
		setDescription("Set Host, Username and Password of Enovia \nDon't add https url , it is not supported in this release \n'Remote Server JPO Directory' is used only in case Enovia server is installed on another machine.");
	}

	@Override
	protected void createFieldEditors() {
		
		addField(new StringFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.host", "Enovia Host:", getFieldEditorParent()));
		addField(new StringFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.username", "Enovia Username:", getFieldEditorParent()));
		addField(new StringFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.password", "Enovia Userpassword:", getFieldEditorParent()));
		addField(new StringFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.vault", "Enovia Vault:", getFieldEditorParent()));
		addField(new BooleanFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.enablelogs", "Enable Logs", getFieldEditorParent()));
		addField(new DirectoryFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.logpath", "Log Path:", getFieldEditorParent()));
		addField(new StringFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.serverjpodir", "Remote Server JPO Directory:", getFieldEditorParent()));
		
		
	}

}
