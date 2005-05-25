/*
 * Copyright 2004,2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.axis.tool.service.eclipse.ui;

import org.apache.axis.tool.service.eclipse.plugin.ServiceArchiver;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class WizardPane2 extends WizardPage {
   
    private Text serviceXMLText;
    private Label manualSelectionLabel;
    private Button browseButton;
    private Button selectAutoFileGenerationCheckBox;
    
    private boolean skipNextPage=true;
    private boolean pageComplete;
    
    public WizardPane2(){
        super("page2");
        this.setTitle(ServiceArchiver.getResourceString("page2.title"));
        this.setDescription(ServiceArchiver.getResourceString("page2.welcometext"));
        this.setImageDescriptor(ServiceArchiver.getWizardImageDescriptor());
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        layout.numColumns=3;
        container.setLayout(layout);
               
        manualSelectionLabel = new Label(container,SWT.NULL);
        manualSelectionLabel.setText("Set the service XML file");
		
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		serviceXMLText = new Text(container,SWT.BORDER);
		serviceXMLText.setLayoutData(gd);
		serviceXMLText.addModifyListener(new ModifyListener(){
		    public void modifyText(ModifyEvent e){
		    handleModify();
		    }
		});
		
		browseButton = new Button(container,SWT.PUSH);
		browseButton.setText("Browse...");
		browseButton.addMouseListener(new MouseAdapter(){
		    public void mouseUp(MouseEvent e) {
		        handleBrowse();
		    }
		});
		
		gd = new GridData();
		gd.horizontalSpan = 2;
		selectAutoFileGenerationCheckBox = new Button(container,SWT.CHECK);
		selectAutoFileGenerationCheckBox.setLayoutData(gd);
		selectAutoFileGenerationCheckBox.setText("Generate the service xml automatically");
		selectAutoFileGenerationCheckBox.addSelectionListener(new SelectionListener(){
		    public void widgetSelected(SelectionEvent e){
		        handleSelection();
		    }
		    public void widgetDefaultSelected(SelectionEvent e){}
		});
		setControl(container);

    }
    
    private void handleBrowse(){
        FileDialog fileDialog = new FileDialog(this.getShell());
        fileDialog.setFilterExtensions(new String[]{"service.xml"});
        String returnFileName = fileDialog.open() ;
        if (returnFileName!=null){
            this.serviceXMLText.setText(returnFileName);
        }
    }
    
    private void handleSelection(){
        if (this.selectAutoFileGenerationCheckBox.getSelection()){
            changeManualSelectionStatus(false); 
            this.skipNextPage = false;
            updateMessage(null);
        }else{
            changeManualSelectionStatus(true);
            this.skipNextPage = true;
            handleModify();
            
        }
    }
    
    private void changeManualSelectionStatus(boolean state){
        this.serviceXMLText.setEnabled(state);
        this.browseButton.setEnabled(state);
        this.manualSelectionLabel.setEnabled(state);
    }
    
    private void handleModify(){
        String serviceXMLString =serviceXMLText.getText().trim().toLowerCase(); 
        if (serviceXMLString.equals("")){
           this.updateMessage("Service XML should not be empty"); 
        }else if(!serviceXMLString.endsWith("service.xml")){
            this.updateMessage("Please select a file named service.xml");  
        }else{
            this.updateMessage(null);
        }
    }
    
    private void updateMessage(String str){
        this.setErrorMessage(str);
        this.pageComplete = (str==null);
    }
    
    /**
     * @return Returns the skipNextPage.
     */
    public boolean isSkipNextPage() {
        return skipNextPage;
    }
}
