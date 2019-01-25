package com.crm.ihm;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.layout.GridLayout;
import swing2swt.layout.BoxLayout;
import org.eclipse.swt.widgets.Label;

import java.sql.SQLException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Text;

import com.crm.models.CompanyModel;
import com.crm.models.Repository;

import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;

public class CreateCompany {
	private Text name;
	private Text address;
	private Text zipcode;
	private Text city;
	private Button btnNewButton;
	
	private ArrayList<Control> controls = new ArrayList<Control>();
	/** 
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			CreateCompany window = new CreateCompany();
			window.open();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		Shell shlCrerUneCompagnie = new Shell();
		shlCrerUneCompagnie.setSize(662, 467);
		shlCrerUneCompagnie.setText("Cr\u00E9er une compagnie");
		shlCrerUneCompagnie.setLayout(new RowLayout(SWT.VERTICAL));
		
		Label lblNom = new Label(shlCrerUneCompagnie, SWT.NONE);
		lblNom.setText("Nom :");
		
		name = new Text(shlCrerUneCompagnie, SWT.BORDER);
		// Pool les contrôles... pour gérer le bouton de sélection
		controls.add(name);

		this.keyListener(name);
		name.setLayoutData(new RowData(243, SWT.DEFAULT));
		
		Label lblAdresse = new Label(shlCrerUneCompagnie, SWT.NONE);
		lblAdresse.setText("Adresse :");
		
		address = new Text(shlCrerUneCompagnie, SWT.BORDER);
		controls.add(address);

		this.keyListener(address);
		address.setLayoutData(new RowData(233, 49));
		
		Label lblCodePostal = new Label(shlCrerUneCompagnie, SWT.NONE);
		lblCodePostal.setText("Code postal :");
		
		zipcode = new Text(shlCrerUneCompagnie, SWT.BORDER);
		controls.add(zipcode);
		this.keyListener(zipcode);
		
		zipcode.setLayoutData(new RowData(115, SWT.DEFAULT));
		
		Label lblVille = new Label(shlCrerUneCompagnie, SWT.NONE);
		lblVille.setText("Ville :");
		
		city = new Text(shlCrerUneCompagnie, SWT.BORDER);
		controls.add(city);
		this.keyListener(city);
		city.setLayoutData(new RowData(245, SWT.DEFAULT));
		
		btnNewButton = new Button(shlCrerUneCompagnie, SWT.NONE);
		btnNewButton.setEnabled(false);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				CompanyModel company;
				try {
					company = new CompanyModel(new Repository());
					company
						.name(name.getText())
						.address(address.getText())
						.zipcode(zipcode.getText())
						.city(city.getText());
					try {
						company.persist();
						resetForm();
					} catch (IllegalArgumentException | IllegalAccessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			private void resetForm() {
				name.setText("");
				address.setText("");
				zipcode.setText("");
				city.setText("");
				btnNewButton.setEnabled(false);
			}
		});
		btnNewButton.setLayoutData(new RowData(257, SWT.DEFAULT));
		btnNewButton.setText("Ajouter");

		shlCrerUneCompagnie.open();
		shlCrerUneCompagnie.layout();
		while (!shlCrerUneCompagnie.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private void keyListener(Control obj) {
		CreateCompany self = this;
		obj.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				boolean allFilled = true;
				for(Control control : self.controls) {
					if (((Text)control).getText().equals("")) {
						allFilled = false;
						break;
					}
				}
				self.btnNewButton.setEnabled(allFilled);
			}
		});
	}
}
