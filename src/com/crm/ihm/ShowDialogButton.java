/**
 * 
 */
package com.crm.ihm;

import java.awt.event.ActionEvent;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

/**
 * @author jean-
 *
 */
public class ShowDialogButton extends Button {

	private String title;
	private String content;
	private int type;
	
	public ShowDialogButton(String label, String name) {
		super(label, name);
		
		// Spécifiquement, dans le doute... type par défaut
		this.type = JOptionPane.INFORMATION_MESSAGE;
		
		this.addActionListener(this);
	}

	public ShowDialogButton title(String title) {
		this.title = title;
		return this;
	}
	
	public ShowDialogButton content(String content) {
		this.content = content;
		return this;
	}
	
	public ShowDialogButton type(int type) {
		this.type = type;
		return this;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JOptionPane.showMessageDialog(null, this.content, this.title, this.type);
	}

}
