/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 *      Copyright (c) 2014, 
        Paul Aiyetan
        Department of Pathology, 
        Johns Hopkins University,
        Baltimore, MD 21231
        All rights reserved.

        Redistribution and use in source and binary forms, with or without
        modification, are permitted provided that the following conditions are met:
         
            * Redistributions of source code must retain the above copyright
                notice, this list of conditions and the following disclaimer.
            * Redistributions in binary form must reproduce the above copyright
                notice, this list of conditions and the following disclaimer in the
                documentation and/or other materials provided with the distribution.
            * Neither the name of the Johns Hopkins University nor the
                names of its contributors may be used to endorse or promote products
                derived from this software without specific prior written permission.

        THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
        ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
        WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
        DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
        DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
        (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
        LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
        ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
        (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
        SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 
 * 
 * 
 * 
 */
package utilities;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/**
 * @author paiyeta1
 *
 */
@SuppressWarnings("serial")
public class MultiInFileChooser extends JPanel {
	
	//private JPanel fileChooser = new JPanel();
	private String[] inputFiles;
	
	
	public MultiInFileChooser(){
		setInputFiles();
		
	}
        
        public MultiInFileChooser(String dialogDesc){
                setInputFiles(dialogDesc); //dialog decription
        }
        
        public MultiInFileChooser(String dialogDesc, String currentDirPath){
                setInputFiles(dialogDesc, currentDirPath);
        }
        
        

	private void setInputFiles() {
		// TODO Auto-generated method stub
		JFileChooser fc = new JFileChooser();
		
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//fc.setFileHidingEnabled(false);
		fc.setDialogTitle("Select Files");
		fc.setMultiSelectionEnabled(true);
		//fc.showOpenDialog(InputFileChooser.this);
		fc.showDialog(MultiInFileChooser.this, "Select");
		File[] files = fc.getSelectedFiles();
		inputFiles = new String[files.length];
		for(int i = 0; i < inputFiles.length; i++){
			inputFiles[i] = files[i].getAbsolutePath();
		}
		
		//JDialog dialog = fc.createDialog(new JTextField());
		
	}
        
	private void setInputFiles(String diaInstr) {
		// TODO Auto-generated method stub
		JFileChooser fc = new JFileChooser();
		
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//fc.setFileHidingEnabled(false);
		fc.setDialogTitle(diaInstr);
		fc.setMultiSelectionEnabled(true);
		//fc.showOpenDialog(InputFileChooser.this);
		fc.showDialog(MultiInFileChooser.this, "Select");
		File[] files = fc.getSelectedFiles();
		inputFiles = new String[files.length];
		for(int i = 0; i < inputFiles.length; i++){
			inputFiles[i] = files[i].getAbsolutePath();
		}
		
		//JDialog dialog = fc.createDialog(new JTextField());
		
	}
        
        
	private void setInputFiles(String diaInstr,String currentDirPath) {
		// TODO Auto-generated method stub
		JFileChooser fc = new JFileChooser(currentDirPath);
		
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		//fc.setFileHidingEnabled(false);
		fc.setDialogTitle(diaInstr);
		fc.setMultiSelectionEnabled(true);
		//fc.showOpenDialog(InputFileChooser.this);
		fc.showDialog(MultiInFileChooser.this, "Select");
		File[] files = fc.getSelectedFiles();
		inputFiles = new String[files.length];
		for(int i = 0; i < inputFiles.length; i++){
			inputFiles[i] = files[i].getAbsolutePath();
		}
		
		//JDialog dialog = fc.createDialog(new JTextField());		
	}


        
        
        public String[] getInputFiles(){
		return inputFiles;
	}
	

}
