package com.fervort.enofer.preferences;

import org.eclipse.core.runtime.preferences.InstanceScope;
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
		setDescription("Set Host, Username and Password of Enovia \nDon't add https url , it is not supported in this release");
	}

	@Override
	protected void createFieldEditors() {
		
		addField(new StringFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.host", "Enovia Host:", getFieldEditorParent()));
		addField(new StringFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.username", "Enovia Username:", getFieldEditorParent()));
		addField(new StringFieldEditor("com.fervort.enofer.preferencesstore.settings.enovia.password", "Enovia Userpassword:", getFieldEditorParent()));
		
		
		
	}

}
