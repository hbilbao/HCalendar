package com.hcalendar.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.Border;

import com.hcalendar.data.DataServices;
import com.hcalendar.data.IDateEntity;
import com.hcalendar.data.IHCCallback;
import com.hcalendar.data.calculator.Calculator;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.orm.impl.ORMHelper;
import com.hcalendar.data.orm.impl.OrmManager;
import com.hcalendar.data.xml.userconfiguration.UserConfiguration;
import com.hcalendar.data.xml.workedhours.AnualHours;
import com.hcalendar.ui.widgets.ICalendarActionProvider;
import com.hcalendar.ui.widgets.ICalendarActionProvider.LIST_TYPE;
import com.hcalendar.ui.widgets.impl.JUserCalendarPanel;
import com.hcalendar.ui.widgets.impl.JWindowUtils;

public class UserConfigurationWindow extends JFrame {

	private final String DATE_SEPARATOR = "\n";
	private static final long serialVersionUID = 1L;

	JTextArea diasLibresTextField;
	JTextField nameTextField;
	JTextField anualHours;
	JComboBox yearCombo;

	JCheckBox lunes;
	JCheckBox martes;
	JCheckBox miercoles;
	JCheckBox jueves;
	JCheckBox viernes;
	JCheckBox sabado;
	JCheckBox domingo;

	JTextField horasLunes;
	JTextField horasMartes;
	JTextField horasMiercoles;
	JTextField horasJueves;
	JTextField horasViernes;
	JTextField horasSabado;
	JTextField horasDomingo;

	JUserCalendarPanel jCalendarPanel;
	private IORMClient orm;
	private IHCCallback callback;

	public UserConfigurationWindow(IORMClient orm, IHCCallback callback) {
		this.callback = callback;
		this.orm = orm;
		this.setSize(1000, 600);
		this.setMaximumSize(new Dimension(1500, 900));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Creación de perfil");
		Container content = this.getContentPane();
		JPanel panel = new JPanel(new GridLayout(2, 2));
		createTextFields(panel);
		createDaysChecks(panel);
		createCalendar(panel);
		content.add(panel);
		createButtons(content);
		this.setVisible(true);
		pack();
	}

	private void createCalendar(Container panel) {
		// Calendar
		jCalendarPanel = new JUserCalendarPanel(new ICalendarEventListener() {

			@Override
			public void onDateChanged(Date date, Boolean selected, ICalendarActionProvider actionProvider) {
				calendarOnDateChangedActions(date, selected, actionProvider);
			}

			@Override
			public void onDataInput(IDateEntity entity) {
				try {
					orm.addChange(null, entity);
				} catch (ORMException e) {
					JWindowUtils.showErrorPanel(UserConfigurationWindow.this,
							"Error al añadir el cambio solicitado");
				}
			}
		}, false, true);
		panel.add(jCalendarPanel);

		JPanel diasLibresPanel = new JPanel(new GridLayout(1, 2));
		Border hoursBorder = BorderFactory.createTitledBorder("Lista de dias libres");
		diasLibresPanel.setBorder(hoursBorder);
		diasLibresTextField = new JTextArea();
		diasLibresTextField.setEditable(false);
		JScrollPane scroll = new JScrollPane(diasLibresTextField);
		diasLibresPanel.add(scroll, BorderLayout.CENTER);
		panel.add(diasLibresPanel);
	}

	private void createDaysChecks(Container panel) {
		JPanel genPanel = new JPanel(new GridLayout(1, 2));
		// Dias laborales
		JPanel daysPanel = new JPanel(new GridLayout(7, 1));
		Border daysBorder = BorderFactory.createTitledBorder("Seleccione dias laborales");
		daysPanel.setBorder(daysBorder);
		lunes = new JCheckBox("Lunes");
		lunes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (lunes.isSelected())
					horasLunes.setEditable(true);
				else {
					horasLunes.setText(null);
					horasLunes.setEditable(false);
				}
			}
		});
		martes = new JCheckBox("Martes");
		martes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (martes.isSelected())
					horasMartes.setEditable(true);
				else {
					horasMartes.setText(null);
					horasMartes.setEditable(false);
				}
			}
		});
		miercoles = new JCheckBox("Miercoles");
		miercoles.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (miercoles.isSelected())
					horasMiercoles.setEditable(true);
				else {
					horasMiercoles.setText(null);
					horasMiercoles.setEditable(false);
				}
			}
		});
		jueves = new JCheckBox("Jueves");
		jueves.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (jueves.isSelected())
					horasJueves.setEditable(true);
				else {
					horasJueves.setText(null);
					horasJueves.setEditable(false);
				}
			}
		});
		viernes = new JCheckBox("Viernes");
		viernes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (viernes.isSelected())
					horasViernes.setEditable(true);
				else {
					horasViernes.setText(null);
					horasViernes.setEditable(false);
				}
			}
		});
		sabado = new JCheckBox("Sabado");
		sabado.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (sabado.isSelected())
					horasSabado.setEditable(true);
				else {
					horasSabado.setText(null);
					horasSabado.setEditable(false);
				}
			}
		});
		domingo = new JCheckBox("Domingo");
		domingo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (domingo.isSelected())
					horasDomingo.setEditable(true);
				else {
					horasDomingo.setText(null);
				}
			}
		});
		daysPanel.add(lunes);
		daysPanel.add(martes);
		daysPanel.add(miercoles);
		daysPanel.add(jueves);
		daysPanel.add(viernes);
		daysPanel.add(sabado);
		daysPanel.add(domingo);

		JPanel hoursPanel = new JPanel(new GridLayout(7, 1));
		Border hoursBorder = BorderFactory.createTitledBorder("Horas por día");
		hoursPanel.setBorder(hoursBorder);
		horasLunes = new JTextField();
		horasLunes.setEditable(false);
		horasMartes = new JTextField();
		horasMartes.setEditable(false);
		horasMiercoles = new JTextField();
		horasMiercoles.setEditable(false);
		horasJueves = new JTextField();
		horasJueves.setEditable(false);
		horasViernes = new JTextField();
		horasViernes.setEditable(false);
		horasSabado = new JTextField();
		horasSabado.setEditable(false);
		horasDomingo = new JTextField();
		horasDomingo.setEditable(false);
		hoursPanel.add(horasLunes);
		hoursPanel.add(horasMartes);
		hoursPanel.add(horasMiercoles);
		hoursPanel.add(horasJueves);
		hoursPanel.add(horasViernes);
		hoursPanel.add(horasSabado);
		hoursPanel.add(horasDomingo);

		genPanel.add(daysPanel);
		genPanel.add(hoursPanel);
		panel.add(genPanel);
	}

	private void createTextFields(Container panel) {
		JPanel namePanel = new JPanel(new GridLayout(3, 2));
		JLabel nameLabel = new JLabel("Nombre: ");
		nameTextField = new JTextField();
		nameLabel.setLabelFor(nameTextField);
		namePanel.add(nameLabel, BorderLayout.WEST);
		namePanel.add(nameTextField, BorderLayout.CENTER);

		JLabel convHours = new JLabel("Horas por convenio: ");
		anualHours = new JTextField();
		convHours.setLabelFor(anualHours);
		namePanel.add(convHours, BorderLayout.WEST);
		namePanel.add(anualHours, BorderLayout.CENTER);

		Integer[] years = new Integer[50];
		int k = 0;
		for (int i = 2011; i < 2061; i++) {
			years[k] = i;
			k++;
		}

		JLabel year = new JLabel("Año: ");
		yearCombo = new JComboBox(years);
		yearCombo.setSelectedIndex(0);
		year.setLabelFor(yearCombo);
		namePanel.add(year, BorderLayout.WEST);
		namePanel.add(yearCombo, BorderLayout.CENTER);

		panel.add(namePanel);
	}

	private void createButtons(Container panel) {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		// BOTON GUARDAR
		JButton saveButton = new JButton("Guardar");
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				saveUserConfiguration();
				UserConfigurationWindow.this.callback.itemChanged();
			}
		});
		buttonPane.add(saveButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		// BOTON CANCELAR
		JButton cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				UserConfigurationWindow.this.dispose();
			}
		});

		buttonPane.add(cancelButton);
		panel.add(buttonPane, BorderLayout.PAGE_END);
	}

	protected void saveUserConfiguration() {
		try {
			// name
			String name = nameTextField.getText();
			// calendarHours
			String aHours = anualHours.getText();
			// year
			Integer year = (Integer) yearCombo.getSelectedItem();
			// workingsdays
			Map<Integer, String> listaDiasLaborales = new HashMap<Integer, String>();
			if (horasLunes.getText() != null && horasLunes.getText().length() > 0)
				listaDiasLaborales.put(Calendar.MONDAY, horasLunes.getText());
			if (horasMartes.getText() != null && horasMartes.getText().length() > 0)
				listaDiasLaborales.put(Calendar.TUESDAY, horasMartes.getText());
			if (horasMiercoles.getText() != null && horasMiercoles.getText().length() > 0)
				listaDiasLaborales.put(Calendar.WEDNESDAY, horasMiercoles.getText());
			if (horasJueves.getText() != null && horasJueves.getText().length() > 0)
				listaDiasLaborales.put(Calendar.THURSDAY, horasJueves.getText());
			if (horasViernes.getText() != null && horasViernes.getText().length() > 0)
				listaDiasLaborales.put(Calendar.FRIDAY, horasViernes.getText());
			if (horasSabado.getText() != null && horasSabado.getText().length() > 0)
				listaDiasLaborales.put(Calendar.SATURDAY, horasSabado.getText());
			if (horasDomingo.getText() != null && horasDomingo.getText().length() > 0)
				listaDiasLaborales.put(Calendar.SUNDAY, horasDomingo.getText());
			// freedays
			List<String> dLibresList = new ArrayList<String>();
			String dLibres = diasLibresTextField.getText();
			String[] splitted = dLibres.split(DATE_SEPARATOR);
			for (String dateStr : splitted) {
				if (dateStr != null && dateStr.length() > 0)
					dLibresList.add(dateStr);
			}
			// Verify if profile exists
			boolean ovewriteProfile = false;
			if (ORMHelper.getCurrentProfiles(orm.getUserConfiguration()).contains(name)) {
				int result = JWindowUtils.showOptionPanel(this,
						"El perfil que intenta guardar la existe, quiere sobreescribirlo?", new String[] {
								"Si", "No" });
				if (result == 1)
					return;
				ovewriteProfile = true;
			}
			UserConfiguration userConfig = DataServices.createAnualConfigurationProfile(orm, name, year,
					aHours, listaDiasLaborales, dLibresList, ovewriteProfile);
			AnualHours anualHours = Calculator.calculatePlannedHoursOfYear(orm, name, year,
					listaDiasLaborales, dLibresList, ovewriteProfile);
			ORMHelper.persistAnualHours(anualHours);
			ORMHelper.persistUserConfiguration(userConfig);
			JWindowUtils.showSuccesPanel(this, "Configuración guardada correctamente");
		} catch (Exception e) {
			JWindowUtils.showErrorPanel(this, "Error al guardar la configuración");
		}
	}

	// Acciones ha realizar cuando cambia la fecha seleccionada
	private void calendarOnDateChangedActions(Date date, Boolean selected,
			ICalendarActionProvider actionProvider) {
		// Add color to the date onCalendar
		if (!selected)
			jCalendarPanel.addDayToList(date, LIST_TYPE.USER_NOT_WORKINGDAY);
		else
			jCalendarPanel.removeDayFromList(date, LIST_TYPE.USER_NOT_WORKINGDAY);

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDate = df.format(date);
		// Lista vacia
		if (diasLibresTextField.getText() == null || diasLibresTextField.getText().length() == 0)
			diasLibresTextField.setText(formattedDate);
		// El dia existe en la lista
		else if (diasLibresTextField.getText().contains(formattedDate) && selected) {
			StringBuffer strBuff = new StringBuffer();
			String text = diasLibresTextField.getText();
			String[] splitted = text.split(DATE_SEPARATOR);
			for (String dateStr : splitted) {
				if ((dateStr != null && dateStr.length() > 0) && (!dateStr.equals(formattedDate)))
					strBuff.append(dateStr).append(DATE_SEPARATOR);
			}
			// int startIndex = text.indexOf(formattedDate);
			// String finalStr =
			// text.substring(0,startIndex).concat(text.substring(startIndex+formattedDate.length()));
			diasLibresTextField.setText(strBuff.toString());
			// El dia no existe en la lista
		} else if (!diasLibresTextField.getText().contains(formattedDate))
			diasLibresTextField.setText(diasLibresTextField.getText() + DATE_SEPARATOR + formattedDate);

	}
}
