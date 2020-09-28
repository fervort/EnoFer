package com.fervort.enofer.preferences;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.ListEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class PropertyPreferences extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public PropertyPreferences()
	{
		super(GRID);
		setPreferenceStore(com.fervort.enofer.Activator.getDefault().getPreferenceStore());
	}
	
	@Override
	public void init(IWorkbench arg0) {
		setDescription("add properties in key value format. Key and value should be seprated by equal sign.");
	}

	@Override
	protected void createFieldEditors() {
		
		addField(new ListEditor( "com.fervort.enofer.preferencesstore.properties","EnoFer Properties",getFieldEditorParent()) {
			
			@Override
	        protected String createList(String[] items) {
				String strReturn ="";
				int i=0;
				for (String string : items) {
					if(i==0)
					{
						strReturn=string;
					}else
					{
						strReturn= strReturn+","+string;
					}
					i++;
				}
	            return strReturn;
	        }

	        @Override
	        protected String getNewInputObject() {
	            InputDialog d = new InputDialog(getShell(), "New Property", "Add a property seprated by = sign like (Logging=true)", "",
	                    new IInputValidator() {

	                        @Override
	                        public String isValid(String newText) {
	                            if (newText.indexOf(' ') != -1 || newText.indexOf(',') != -1) {
	                                return "The input cannot have spaces and commas (,) ";
	                            }
	                            return null;
	                        }
	                    });

	            int retCode = d.open();
	            if (retCode == InputDialog.OK) {
	                return d.getValue();
	            }
	            return null;
	        }

	        @Override
	        protected String[] parseString(String stringList) {
	            if(stringList.isEmpty())
	            {
	            	return new String[0];
	            }
	        	return stringList.split(",");
	        }

	        @Override
	        protected void doFillIntoGrid(Composite parent, int numColumns) {
	            super.doFillIntoGrid(parent, numColumns);
	            List listControl = getListControl(parent);
	            GridData layoutData = (GridData) listControl.getLayoutData();
	            layoutData.heightHint = 400;
	        }
			
		}); 
		
	}

}
