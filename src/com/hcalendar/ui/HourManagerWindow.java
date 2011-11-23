package com.hcalendar.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.hcalendar.config.ConfigurationUtils;
import com.hcalendar.data.DataServices;
import com.hcalendar.data.IDateEntity;
import com.hcalendar.data.calculator.Calculator;
import com.hcalendar.data.exception.BusinessException;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.IORMClient.ENTITY_TYPE;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.orm.impl.ORMHelper;
import com.hcalendar.ui.widgets.ICalendarActionProvider;
import com.hcalendar.ui.widgets.ICalendarActionProvider.LIST_TYPE;
import com.hcalendar.ui.widgets.impl.JUserCalendarPanel;
import com.hcalendar.ui.widgets.impl.JWindowUtils;

//	TODO Poner constantes en todas las ventanas
public class HourManagerWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	JTextArea diasLibresTextField;

	JUserCalendarPanel jCalendarPanel;

	JTextField horasLunes;
	JTextField horasMartes;
	JTextField horasMiercoles;
	JTextField horasJueves;
	JTextField horasViernes;
	JTextField horasSabado;
	JTextField horasDomingo;

	JTextField convTextField;
	JTextField actualHoursTextField;
	JTextField plannedHoursTextField;
	JTextField descTextField;
	JTextField diaryHoursTextField;

	private IORMClient orm;
	private String username;

	public HourManagerWindow(IORMClient orm, String username) {
		this.orm = orm;
		this.username = username;
		try {
			if (!ConfigurationUtils.existAnualConfigurationFile()) {
				JWindowUtils
						.showOptionPanel(
								this,
								"No se ha creado ninguno configuraci�n anual. Creala antes de acceder a esta ventana, por favor.",
								new Object[] { "Aceptar" });
				this.dispose();
				return;
			}
			this.setSize(1000, 600);
			this.setMaximumSize(new Dimension(1500, 900));
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Gesti�n de horas");
			Container content = this.getContentPane();
			JPanel panel4 = new JPanel(new GridLayout(1, 2));
			JPanel mainPanel = new JPanel(new GridLayout(1, 1));
			createUserAndCalendar(panel4);
			createTextFields(panel4);

			mainPanel.add(panel4);
			// content.add(generalPanel);
			createButtons(content);
			content.add(mainPanel);

			// Paint with green the calendar free days
			List<Date> freeDays = ORMHelper.getCalendarFreeDays(orm.getAnualConfiguration(), this.username,
					jCalendarPanel.getSelectedYear());
			for (Date date : freeDays)
				jCalendarPanel.addDayToList(date, LIST_TYPE.CALENDAR_FREEDAY);
			// Paint with red the anual not working days
			List<Date> calendarNotWorkingDays = ORMHelper.getCalendarNotWorkingDays(
					orm.getAnualConfiguration(), this.username, jCalendarPanel.getSelectedYear());
			for (Date date : calendarNotWorkingDays)
				jCalendarPanel.addDayToList(date, LIST_TYPE.USER_NOT_WORKINGDAY);

			List<Date> userHolidays = ORMHelper.getUserHolidays(orm.getAnualHours(), this.username);
			for (Date date : userHolidays)
				jCalendarPanel.addDayToList(date, LIST_TYPE.USER_HOLIDAYS);

			this.setVisible(true);
			pack();
		} catch (Exception e) {
			e.printStackTrace();
			JWindowUtils.showErrorPanel(this, "Error en la creaci�n de la ventana");
		}
	}

	// private final String DATE_SEPARATOR = "\n";

	private void createUserAndCalendar(Container panel) throws ORMException {
		// Poner en el calendar el a�o actual, y si no tiene configuraci�n,
		// el m�s cercano hacia arriba y luego para abajo.
		int yearToShow = Calculator.calculateYearConfigForProfile(orm.getAnualConfiguration(), this.username,
				Calendar.getInstance().get(Calendar.YEAR));

		JPanel combPanel = new JPanel(new GridLayout(2, 1));
		JPanel profileAndHist = new JPanel(new GridLayout(2, 1));
		// Anyadimos botones para el hist�rico de colores utilizados
		JPanel profilePanel = new JPanel(new GridLayout(2, 1));
		JLabel shownProfile = new JLabel("Nombre : " + this.username);
		JLabel shownYear = new JLabel("A�o : " + yearToShow);
		profilePanel.setBorder(BorderFactory.createTitledBorder("Perfil"));
		profilePanel.add(shownProfile);
		profilePanel.add(shownYear);

		// Anyadimos botones para el hist�rico de colores utilizados
		JPanel histPanel = new JPanel();
		histPanel.setBorder(BorderFactory.createTitledBorder("Hist�rico"));
		JLabel colorHist = new JLabel("D�as no laborales: ");
		JButton redButton = new JButton();
		redButton.setEnabled(false);
		redButton.setBackground(Color.red);
		colorHist.setLabelFor(redButton);
		histPanel.add(colorHist);
		histPanel.add(redButton);
		colorHist = new JLabel("D�as libres del calendario: ");
		JButton greenButton = new JButton();
		greenButton.setEnabled(false);
		greenButton.setBackground(Color.green);
		colorHist.setLabelFor(greenButton);
		histPanel.add(colorHist);
		histPanel.add(greenButton);
		colorHist = new JLabel("Vacaciones: ");
		JButton blueButton = new JButton();
		blueButton.setEnabled(false);
		blueButton.setBackground(Color.blue);
		colorHist.setLabelFor(blueButton);
		histPanel.add(colorHist);
		histPanel.add(blueButton);
		profileAndHist.add(profilePanel, BorderLayout.CENTER);
		profileAndHist.add(histPanel, BorderLayout.CENTER);

		// Calendar
		jCalendarPanel = new JUserCalendarPanel(yearToShow, 0, 0, new ICalendarEventListener() {

			@Override
			public void onDateChanged(Date date, Boolean selected, ICalendarActionProvider actionProvider) {
				try {
					calendarOnDateChangedActions(date, selected, actionProvider);
				} catch (ORMException e) {
					JWindowUtils.showErrorPanel(HourManagerWindow.this,
							"Error al completar operaciones de cambio de fecha");
					e.printStackTrace();
				}
			}

			@Override
			public void onDataInput(IDateEntity entity) {
				jCalendarPanel.dataInputOcurred(entity);
				try {
					orm.addChange(HourManagerWindow.this.username, entity);
				} catch (ORMException e) {
					JWindowUtils.showErrorPanel(HourManagerWindow.this,
							"Error al a�adir los cambios solicitados");
					e.printStackTrace();
				}
			}
		}, true, false);
		combPanel.add(profileAndHist);
		combPanel.add(jCalendarPanel);
		panel.add(combPanel);
	}

	private void createTextFields(JPanel parentPanel) throws ORMException {
		JPanel panel = new JPanel(new GridLayout(3, 1));

		JPanel separatorPanel = new JPanel();
		// Panel de horas generales
		JPanel genHoursPanel = new JPanel(new GridLayout(3, 2));
		genHoursPanel.setBorder(BorderFactory.createTitledBorder("Informaci�n anual"));
		JLabel genLabel = new JLabel("Total horas convenio: ");
		convTextField = new JTextField();
		// Obtener horas anuales desde el xml
		convTextField.setEditable(false);
		int year = jCalendarPanel.getSelectedYear();
		if (ORMHelper.getCalendarHours(orm.getAnualConfiguration(), this.username, year) != null) {
			String yearStr = String.valueOf(ORMHelper.getCalendarHours(orm.getAnualConfiguration(),
					this.username, year));
			convTextField.setText(yearStr);
		}
		genLabel.setLabelFor(convTextField);
		genHoursPanel.add(genLabel, BorderLayout.WEST);
		genHoursPanel.add(convTextField, BorderLayout.CENTER);

		// Calcular las horas desde los datos del XML
		JLabel actualHours = new JLabel("Horas actuales: ");
		actualHoursTextField = new JTextField();
		actualHoursTextField.setEditable(false);
		actualHours.setLabelFor(actualHoursTextField);
		genHoursPanel.add(actualHours, BorderLayout.WEST);
		genHoursPanel.add(actualHoursTextField, BorderLayout.CENTER);

		JLabel plannedHours = new JLabel("Horas planificadas: ");
		plannedHoursTextField = new JTextField();
		plannedHoursTextField.setEditable(false);
		String plannedH = String.valueOf(Calculator.calculateAnualPlannedHours(orm.getAnualHours(), year, this.username));
		plannedHoursTextField.setText(plannedH);
		plannedHours.setLabelFor(plannedHoursTextField);
		genHoursPanel.add(plannedHours, BorderLayout.WEST);
		genHoursPanel.add(plannedHoursTextField, BorderLayout.CENTER);

		// Panel actualizable de horas diarias
		JPanel diaryPanel = new JPanel(new GridLayout(2, 2));
		diaryPanel.setBorder(BorderFactory.createTitledBorder("Informaci�n diaria"));
		JLabel diaryHoursLabel = new JLabel("Horas diarias: ");
		diaryHoursTextField = new JTextField();
		diaryHoursTextField.setEditable(false);
		diaryHoursLabel.setLabelFor(diaryHoursTextField);
		diaryPanel.add(diaryHoursLabel, BorderLayout.WEST);
		diaryPanel.add(diaryHoursTextField, BorderLayout.CENTER);

		JLabel desc = new JLabel("Descripci�n: ");
		descTextField = new JTextField();
		descTextField.setEditable(false);
		desc.setLabelFor(descTextField);
		diaryPanel.add(desc, BorderLayout.WEST);
		diaryPanel.add(descTextField, BorderLayout.CENTER);

		// Anyadir los dos paneles secundarios
		panel.add(diaryPanel);
		panel.add(separatorPanel);
		panel.add(genHoursPanel);
		// Anyadir el panel al panel principal
		parentPanel.add(panel);
	}

	private void createButtons(Container panel) {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());

		// BOTON EXPORTAR
		JButton exportButton = new JButton("Exportar datos");
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if (DataServices.exportToCSV(orm, jCalendarPanel.getSelectedYear(),
							HourManagerWindow.this.username, HourManagerWindow.this) == 0)
						JWindowUtils.showSuccesPanel(HourManagerWindow.this, "Exportaci�n correcta");
				} catch (BusinessException e1) {
					JWindowUtils.showErrorPanel(HourManagerWindow.this, "Error al exportar");
				}
			}
		});
		buttonPane.add(exportButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		// BOTON CANCELAR
		JButton cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HourManagerWindow.this.dispose();
			}
		});

		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

		// BOTON GUARDAR
		JButton saveButton = new JButton("Guardar");
		buttonPane.add(saveButton);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					orm.persist(ENTITY_TYPE.ANUALHOURS);
					orm.persist(ENTITY_TYPE.USERCONFIGURATION);
					JWindowUtils.showSuccesPanel(HourManagerWindow.this, "Guardado completado");
				} catch (ORMException e1) {
					JWindowUtils.showSuccesPanel(HourManagerWindow.this,
							"Error al guardar los datos de las horas");
					e1.printStackTrace();
				}
			}
		});

		panel.add(buttonPane, BorderLayout.PAGE_END);
	}

	// Acciones ha realizar cuando cambia la fecha seleccionada
	@SuppressWarnings("deprecation")
	private void calendarOnDateChangedActions(Date date, Boolean selected,
			ICalendarActionProvider actionProvider) throws ORMException {
		String yearConvHoues;
		try {
			yearConvHoues = String.valueOf(ORMHelper.getCalendarHours(orm.getAnualConfiguration(), this.username,
					1900 + date.getYear()));
			convTextField.setText(yearConvHoues);
		} catch (Throwable e) {
			JWindowUtils.showErrorPanel(HourManagerWindow.this, "Error al calcular horas anules para el a�o "
					+ date.getYear());
		}

		String plannedH;
		try {
			plannedH = String.valueOf(Calculator.calculateAnualPlannedHours(orm.getAnualHours(), date.getYear() + 1900,
					this.username));
			plannedHoursTextField.setText(plannedH);
			// Calcular horas imputadas hasta ahora
			actualHoursTextField.setText(String.valueOf(Calculator.calculateHoursUntilDate(
					orm.getAnualHours(), date, this.username)));
		} catch (Throwable e) {
			JWindowUtils.showErrorPanel(HourManagerWindow.this, "Error al calcular horas anules para el a�o "
					+ date.getYear());
		}

		Map<Float, String> inputHours;
		try {
			inputHours = ORMHelper.getInputHours(orm.getAnualHours(), date, this.username);
			Set<Float> keys = inputHours.keySet();
			Float tHours = new Float(0);
			String desc = new String();
			for (Float f : keys) {
				tHours = tHours + f;
				desc = desc + inputHours.get(f);
			}
			diaryHoursTextField.setText(String.valueOf(tHours));
			descTextField.setText(desc);
		} catch (Throwable e) {
			JWindowUtils.showErrorPanel(HourManagerWindow.this, "Error al calcular horas anules para el a�o "
					+ date.getYear());
		}
	}
}
