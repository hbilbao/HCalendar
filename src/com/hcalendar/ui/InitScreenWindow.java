package com.hcalendar.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

import com.hcalendar.HCalendarConstants;
import com.hcalendar.data.IHCCallback;
import com.hcalendar.data.crud.CRUDManager;
import com.hcalendar.data.crud.exception.CRUDException;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.orm.impl.ORMHelper;
import com.hcalendar.data.orm.impl.ORMManager;
import com.hcalendar.ui.actions.CreateProfileLauncher;
import com.hcalendar.ui.actions.HourManagerLauncher;
import com.hcalendar.ui.widgets.impl.JWindowUtils;

public class InitScreenWindow extends JFrame {
	private static final long serialVersionUID = 1L;

	JComboBox profilesCombo;
	JComboBox deleteProfilesCombo;

	public InitScreenWindow() {
		try {
			final IORMClient orm = ORMManager.getInstance().getORMClient();
			this.setSize(400, 160);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle("Calendario laboral");
			JPanel panel = new JPanel();
			Border border = BorderFactory.createTitledBorder("Seleccione operación");
			panel.setBorder(border);

			JLabel profilesComboLabel = new JLabel("Gestionar horas del perfil: ");
			List<String> profilesList = ORMHelper.getCurrentProfiles(orm.getAnualConfiguration());
			profilesList.add(HCalendarConstants.NULL_COMBO_INPUT);
			Collections.reverse(profilesList);
			profilesCombo = new JComboBox(profilesList.toArray());
			profilesComboLabel.setLabelFor(profilesCombo);
			panel.add(profilesComboLabel, BorderLayout.WEST);
			panel.add(profilesCombo, BorderLayout.CENTER);
			profilesCombo.addActionListener(new HourManagerLauncher(orm));

			AbstractButton newProfileButton = new JToggleButton("Crear nuevo perfil");
			newProfileButton.setSize(new Dimension(100, 100));
			newProfileButton.addActionListener(new CreateProfileLauncher(orm, new IHCCallback() {

				@Override
				public void itemChanged() {
					try {
						deleteProfilesCombo.removeAllItems();
						profilesCombo.removeAllItems();
						deleteProfilesCombo.addItem(HCalendarConstants.NULL_COMBO_INPUT);
						profilesCombo.addItem(HCalendarConstants.NULL_COMBO_INPUT);
						for (String profile : ORMHelper.getCurrentProfiles(orm.getAnualConfiguration())) {
							deleteProfilesCombo.addItem(profile);
							profilesCombo.addItem(profile);
						}
					} catch (ORMException e) {
						JWindowUtils.showErrorPanel(InitScreenWindow.this,
								"Error al volver de la ventana de creación de perfil");
					}
				}
			}));
			panel.add(newProfileButton);

			JLabel deleteProfilesComboLabel = new JLabel("Borrar perfil: ");
			deleteProfilesCombo = new JComboBox(profilesList.toArray());
			deleteProfilesComboLabel.setLabelFor(deleteProfilesCombo);
			panel.add(deleteProfilesComboLabel, BorderLayout.WEST);
			panel.add(deleteProfilesCombo, BorderLayout.CENTER);
			deleteProfilesCombo.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String profileName = (String) deleteProfilesCombo.getSelectedItem();
					if (profileName == null || profileName.equals(HCalendarConstants.NULL_COMBO_INPUT))
						return;
					try {
						CRUDManager.deleteProfile(orm.getAnualConfiguration(), orm.getAnualHours(),
								profileName);
						deleteProfilesCombo.removeAllItems();
						profilesCombo.removeAllItems();
						deleteProfilesCombo.addItem(HCalendarConstants.NULL_COMBO_INPUT);
						profilesCombo.addItem(HCalendarConstants.NULL_COMBO_INPUT);
						for (String profile : ORMHelper.getCurrentProfiles(orm.getAnualConfiguration())) {
							deleteProfilesCombo.addItem(profile);
							profilesCombo.addItem(profile);
						}
						JWindowUtils.showSuccesPanel(InitScreenWindow.this, "Perfil borrado correctamente");
					} catch (CRUDException crude) {
						JWindowUtils.showErrorPanel(InitScreenWindow.this, "Error al eliminar el perfil");
					} catch (ORMException orme) {
						JWindowUtils.showErrorPanel(InitScreenWindow.this, "Error al eliminar el perfil");
					}

				}
			});

			this.add(panel, BorderLayout.CENTER);
			// Show frame
			this.setVisible(true);
		} catch (ORMException e) {
			JWindowUtils.showErrorPanel(this, "Error al iniciar la ventana");
		}
	}

	public static void main(String[] args) {
		new InitScreenWindow();
	}
}
