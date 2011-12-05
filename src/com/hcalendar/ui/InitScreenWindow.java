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
import com.hcalendar.data.crud.CRUDManager;
import com.hcalendar.data.crud.exception.CRUDException;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.orm.impl.ORMHelper;
import com.hcalendar.data.orm.impl.ORMManager;
import com.hcalendar.ui.actions.CreateProfileLauncher;
import com.hcalendar.ui.actions.HourManagerLauncher;
import com.hcalendar.ui.helper.ModalWindowUtils;

public class InitScreenWindow extends JFrame  implements IWindow{
	private static final long serialVersionUID = 1L;

	private static final String WINDOW_TITLE ="Calendario laboral";
	
	private static final String ERROR_RETURNTO_WINDOW ="Error al volver de la ventana de creación de perfil";
		
	private static final String WINDOW_PANEL_BORDER_SELECT_TITLE ="Seleccione operación";
	
	private static final String ACTION_BUTTON_MANAGE_PROFILE_TITLE ="Gestionar horas del perfil: ";
	private static final String ACTION_BUTTON_CREATE_PROFILE_TITLE ="Crear nuevo perfil";
	private static final String ACTION_BUTTON_DELETE_PROFILE_TITLE ="Borrar perfil: ";
	
	JComboBox profilesCombo;
	JComboBox deleteProfilesCombo;
	IORMClient orm;
	
	public InitScreenWindow() {
		try {
			orm = ORMManager.getInstance().getORMClient();
			this.setSize(400, 160);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setTitle(WINDOW_TITLE);
			JPanel panel = new JPanel();
			Border border = BorderFactory.createTitledBorder(WINDOW_PANEL_BORDER_SELECT_TITLE);
			panel.setBorder(border);

			JLabel profilesComboLabel = new JLabel(ACTION_BUTTON_MANAGE_PROFILE_TITLE);
			List<String> profilesList = ORMHelper.getCurrentProfiles(orm.getAnualConfiguration());
			profilesList.add(HCalendarConstants.NULL_COMBO_INPUT);
			Collections.reverse(profilesList);
			profilesCombo = new JComboBox(profilesList.toArray());
			profilesComboLabel.setLabelFor(profilesCombo);
			panel.add(profilesComboLabel, BorderLayout.WEST);
			panel.add(profilesCombo, BorderLayout.CENTER);
			profilesCombo.addActionListener(new HourManagerLauncher(orm));

			AbstractButton newProfileButton = new JToggleButton(ACTION_BUTTON_CREATE_PROFILE_TITLE);
			newProfileButton.setSize(new Dimension(100, 100));
			newProfileButton.addActionListener(new CreateProfileLauncher(orm, this));
			panel.add(newProfileButton);

			JLabel deleteProfilesComboLabel = new JLabel(ACTION_BUTTON_DELETE_PROFILE_TITLE);
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
						setData();
						ModalWindowUtils.showSuccesPanel(InitScreenWindow.this, HCalendarConstants.SUCCES_DELETE);
					} catch (CRUDException crude) {
						ModalWindowUtils.showErrorPanel(InitScreenWindow.this, HCalendarConstants.ERROR_DELETE);
					} catch (ORMException orme) {
						ModalWindowUtils.showErrorPanel(InitScreenWindow.this, HCalendarConstants.ERROR_DELETE);
					}

				}
			});

			this.add(panel, BorderLayout.CENTER);
			// Show frame
			this.setVisible(true);
		} catch (ORMException e) {
			ModalWindowUtils.showErrorPanel(this, HCalendarConstants.ERROR_WINDOW_CREATION);
		}
	}

	@Override
	public void setData() {
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
			ModalWindowUtils.showErrorPanel(InitScreenWindow.this,
					ERROR_RETURNTO_WINDOW);
		}
	}
	
	@Override
	public void addParentWindow(IWindowDataHanlder window) {
		childWindows.add(window);
	}

	@Override
	public void notifyDataChange() {
		for (IWindowDataHanlder w: childWindows){
			w.setData();
		}
	}
	
	public static void main(String[] args) {
		new InitScreenWindow();
	}

}
