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
			this.dialogContent = "La compagnie a bien été créée : ";
			this.dialogTitle = "Compagnie créée";
		}
		return this;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		// Désactivation du bouton (on évite que l'utilisateur énervé clique 150 fois !)
		this.setEnabled(false);
		
		// Spécialiste de l'UX me dit... affiche "En cours..." dans le bouton
		String beforeContent = this.getText(); // Avant, je récupère le texte du bouton
		this.setText("En cours...");
		
		try { // On peut maintenant faire le travail : faire persister le Model transmis
			this.entity.persist();
			// Pour faire élégant, une boîte de message
			JOptionPane.showMessageDialog(
					null, 
					this.dialogContent + " : " + this.entity, 
					this.dialogTitle,
					JOptionPane.INFORMATION_MESSAGE
			);
		} catch (IllegalArgumentException | IllegalAccessException | SQLException e) {
			if (this.entity instanceof CompanyModel) {
				this.dialogContent = "Une erreur est survenue lors de la création de la société";
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
