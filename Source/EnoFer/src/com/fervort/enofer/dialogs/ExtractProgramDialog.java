package com.fervort.enofer.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.fervort.enofer.enovia.EnoviaUtility;
import com.fervort.enofer.log.Logger;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;



public class ExtractProgramDialog extends Dialog{

	public ExtractProgramDialog(Shell parentShell) {
		super(parentShell);
	}
	
	
	private String strSourceFolderPath;
	
	public String getSourceFolderPath() {
		return strSourceFolderPath;
	}

	public void setSourceFolderPath(String strSourceFolderPath) {
		this.strSourceFolderPath = strSourceFolderPath;
	}

	protected Control createDialogArea(Composite parent) {
		
		Composite composite = (Composite)super.createDialogArea(parent);
		GridLayout layout = (GridLayout)composite.getLayout();
		
		layout.marginLeft = 3;
		layout.marginRight = 3;
		layout.marginTop = 3;
		layout.marginBottom = 3; 

		GridData labelData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		lProgramToSelectLabel = new Label(composite, 1);
		lProgramToSelectLabel.setText("Programs Filter");
		lProgramToSelectLabel.setLayoutData(labelData);

		GridData fieldData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		tProgramToSelect = new Text(composite, SWT.BORDER);
		tProgramToSelect.setLayoutData(fieldData);

		lShowProgramsLabel = new Label(composite, 1);
		lShowProgramsLabel.setText("Matched Programs");
		lShowProgramsLabel.setLayoutData(labelData);

		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		ltShowProgramsList = new List(composite, 2562);
		ltShowProgramsList.setLayoutData(data);

		try {
			
			alPrograms = new ArrayList<String>();
			
			alPrograms = EnoviaUtility.getEnoviaPrograms("*");
			Iterator<String> iter = alPrograms.listIterator();
			while (iter.hasNext()) {
				String progName = (String)iter.next();
				ltShowProgramsList.add(progName);
			}
			
		} catch (Exception ex) {
			String message = ex.getMessage();
			Status status = new Status(IStatus.ERROR, "com.fervort.enofer", 0, message, ex);
			ErrorDialog.openError(getShell(),"Failed","Couldn't retrieve program from Enovia database", status);
		}

		tProgramToSelect.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				System.out.println("called modify ");
				ltShowProgramsList.removeAll();
				filterPrograms();
			}
		});
		
		GridData chkBoxData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		
		bSelectAll = new Button(composite, SWT.CHECK);
		bSelectAll.setText("Select All Programs");
		bSelectAll.setLayoutData(chkBoxData);
		bSelectAll.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			Button chkBx = (Button)e.getSource();
			if (chkBx.getSelection()) {
				ltShowProgramsList.selectAll();
			} else {
				ltShowProgramsList.deselectAll();
			}
			}
		});
		
		lShowProgramsLabel = new Label(composite, 1);
		lShowProgramsLabel.setText("Source folder where programs will be exported");
		lShowProgramsLabel.setLayoutData(labelData);
		
		GridData textData = new GridData(SWT.FILL, SWT.FILL, true, true,1,1);
		tSourceFolderPath = new Text(composite, SWT.BORDER);
		tSourceFolderPath.setLayoutData(textData);
		tSourceFolderPath.setText(getSourceFolderPath());
		tSourceFolderPath.setSize(30,80);
		//Point p = tSourceFolderPath.getSize();
		//System.out.println(p.x+" mmmmm "+p.y);
		return composite;
    }
	
	protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("EnoFer Extract Program Dialog");
    }
	
	// TODO add regular expression to support * in program name
	private void filterPrograms() {
		
		boolean selectAllCBSelected = bSelectAll.getSelection();
		String enteredText = tProgramToSelect.getText().toLowerCase();


				for (int i = 0; i < alPrograms.size(); i++) {
					String progName = (String)alPrograms.get(i);
					if (progName.toLowerCase().startsWith(enteredText)) {
						ltShowProgramsList.add(progName);
					}
				}

		if (selectAllCBSelected) {
			if (ltShowProgramsList.getItemCount() > 0) {
				ltShowProgramsList.selectAll();
			}
		} else {
			ltShowProgramsList.deselectAll();
		}
	}

	@Override
	protected void okPressed() {
		String[] selectedPrograms = this.ltShowProgramsList.getSelection();
		super.okPressed();
	}
	/*
    @Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }
	*/
	private Label lProgramToSelectLabel;
	private Label lShowProgramsLabel;
	private List ltShowProgramsList 		= null;
	private Text tProgramToSelect 			= null;
	private Text tSourceFolderPath 			= null;
	private ArrayList<String> alPrograms 	= null;	
	private Button bSelectAll 				= null;

}
