/**
 * 
 */
package com.crm.ihm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.crm.models.CompanyModel;
import com.crm.models.Model;
import com.crm.models.Repository;

/**
 * @author jean-
 *
 */
public class MainIHM extends JFrame implements ActionListener {

	/**
	 * @param args
	 */
	private Repository companiesRepo;
	
	public static void main(String[] args) {
		MainIHM window = new MainIHM();
		
		// D�finit la taille de la fen�tre
		Toolkit toolKit = Toolkit.getDefaultToolkit();
		Dimension dimensions = toolKit.getScreenSize();
		int width = (int) (dimensions.width * 0.95);
		int height = (int) (dimensions.height * 0.95);
		
		window.setSize(width, height);
		
		// D�finit le titre de la barre de titre
		window.setTitle("CRM");
		

		// D�finir un composant d'agencement "layout"
		window.getContentPane().setLayout(new BorderLayout(20, 20));
		
		// Ajouter le bouton � la fen�tre
		window.getContentPane().add(
				new ShowDialogButton("Clique sur moi !", "one")
					.title("Titre param�trable")
					.content("Contenu Param�trable")
					.type(JOptionPane.ERROR_MESSAGE),
				BorderLayout.SOUTH
		);
		
		// Un bouton qui va cr�er une compagnie
		window.getContentPane().add(window._createCompanyButton(), BorderLayout.SOUTH);
		
		//window.getContentPane().add(new Button("Autre bouton", "two"));
		
		
		// Un JTable ?
        List<String> columns = new ArrayList<String>();
        List<String[]> values = new ArrayList<String[]>();

        columns.add("id");
        columns.add("Nom");
        columns.add("Ville");

        
        for (Model company : window.companiesRepo.getRepository().values()) {
            values.add(new String[] {String.valueOf(((CompanyModel) company).id()),((CompanyModel) company).name(),((CompanyModel) company).city()});
        }
		
        
        TableModel tableModel = new DefaultTableModel(values.toArray(new Object[][] {}), columns.toArray());
        JTable table = new JTable(tableModel);
        window.setLayout(new BorderLayout());
        window.add(new JScrollPane(table), BorderLayout.CENTER);
        
		// Rend la fen�tre visible
		window.setVisible(true);
		

		
		// D�finit le mode de fermeture de la fen�tre
		window.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	
	private AddEntityButton _createCompanyButton() {
		AddEntityButton button = new AddEntityButton("Ajout d'une soci�t�", "three");
		this.companiesRepo = new Repository();
		try {
					
			CompanyModel company = new CompanyModel(this.companiesRepo);
			this.companiesRepo.setModel(company);
			company.name("App'in Move")
				.address("12, rue Louis Courtois de Vi�ose")
				.zipcode("31000")
				.city("Toulouse");
			button.setEntity(company);
			System.out.println(this.companiesRepo);
			return button;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * Premier composant : un bouton de s�lection
	 * @return JButton
	 */
	private JButton button() {
		JButton button = new JButton("Click on me");
		
		button.addActionListener(this);
		
		button.setName("my_button");
		
		button.setSize(200, 100);
		return button;
	}





	@Override
	public void actionPerformed(ActionEvent event) {
		System.out.println("Et donc : " + event.toString());
		JButton source = (JButton) event.getSource();
		System.out.println(source.getName());
		source.setText("Tu as cliqu� sur moi");
	}

}
