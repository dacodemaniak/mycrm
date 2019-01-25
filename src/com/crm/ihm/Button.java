/**
 * 
 */
package com.crm.ihm;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * @author jean-
 *
 */
public abstract class Button extends JButton implements ActionListener {
	public Button(String label, String name) {
		super();
		this.setText(label);
		this.setPreferredSize(new Dimension(200, 100));
		this.validate();
		
		this.setName(name);
		
	}
}
