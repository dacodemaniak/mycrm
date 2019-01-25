/**
 * 
 */
package com.crm.ihm;

import java.awt.event.ActionEvent;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.crm.models.CompanyModel;
import com.crm.models.Model;

/**
 * @author jean-
 *
 */
public class AddEntityButton extends Button {
	private Model entity;
	
	private String dialogTitle;
	private String dialogContent;
	
	/**
	 * @param label
	 * @param name
	 */
	public AddEntityButton(String label, String name) {
		super(label, name);
		this.addActionListener(this);
	}

	public AddEntityButton setEntity(Model entity) {
		this.entity = entity;
		
		if (this.entity instanceof CompanyModel) {
			this.dialogContent = "La compagnie a bien �t� cr��e : ";
			this.dialogTitle = "Compagnie cr��e";
		}
		return this;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// D�sactivation du bouton (on �vite que l'utilisateur �nerv� clique 150 fois !)
		this.setEnabled(false);
		
		// Sp�cialiste de l'UX me dit... affiche "En cours..." dans le bouton
		String beforeContent = this.getText(); // Avant, je r�cup�re le texte du bouton
		this.setText("En cours...");
		
		try { // On peut maintenant faire le travail : faire persister le Model transmis
			this.entity.persist();
			// Pour faire �l�gant, une bo�te de message
			JOptionPane.showMessageDialog(
					null, 
					this.dialogContent + " : " + this.entity, 
					this.dialogTitle,
					JOptionPane.INFORMATION_MESSAGE
			);
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			if (this.entity instanceof CompanyModel) {
				this.dialogContent = "Une erreur est survenue lors de la cr�ation de la soci�t�";
				this.dialogTitle = "Erreur";
				JOptionPane.showMessageDialog(
						null, 
						this.dialogContent + " : " + e.getMessage(), 
						this.dialogTitle,
						JOptionPane.ERROR_MESSAGE
				);				
			}
		} finally {
			this.setText(beforeContent);
			this.setEnabled(true);
		}
	}

}
