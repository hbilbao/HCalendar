package com.hcalendar.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
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

import com.hcalendar.HCalendarConstants;
import com.hcalendar.config.ConfigurationUtils;
import com.hcalendar.data.DataServices;
import com.hcalendar.data.IDateEntity;
import com.hcalendar.data.calculator.Calculator;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.IORMClient.ENTITY_TYPE;
import com.hcalendar.data.orm.exception.ORMException;
import com.hcalendar.data.orm.impl.ORMHelper;
import com.hcalendar.data.utils.DateHelper;
import com.hcalendar.data.xml.workedhours.AnualHours.UserInput.WorkedHours;
import com.hcalendar.ui.helper.ModalWindowUtils;
import com.hcalendar.ui.widgets.ICalendarActionProvider;
import com.hcalendar.ui.widgets.ICalendarActionProvider.LIST_TYPE;
import com.hcalendar.ui.widgets.impl.JCalendarPanel;

public class HourManagerWindow extends JFrame implements IWindow {

	private static final long serialVersionUID = 1L;

	private static final String WINDOW_TITLE = "Gesti�n de horas";

	private static final String ERROR_ANUALCONFIGURATION_NOT_EXIST = "No se ha creado ninguno configuraci�n anual. Creala antes de acceder a esta ventana, por favor.";
	private static final String ERROR_OPERATION_DATECHANGE = "Error al completar operaciones de cambio de fecha";
	private static final String ERROR_OPERATION_APPLY_CHANGES = "Error al a�adir los cambios solicitados";

	private static final String SUCCES_EXPORT_DATA = "Exportaci�n correcta";
	private static final String ERROR_EXPORT_DATA = "Error al exportar";
	private static final String ERROR_EXPORT_DATA_FILE_LOCKED = "Error al exportar. El archivo est� siendo usado por otro proceso, cierre el archivo antes de exportar los datos porfavor.";

	private static final String ERROR_CALCULATE_DATA = "Error al calcular horas anules para el a�o ";

	private static final String WINDOW_ITEM_NAME_TITLE = "Nombre : ";
	private static final String WINDOW_ITEM_YEAR_TITLE = "A�o : ";
	private static final String WINDOW_ITEM_NOT_WORKINGDAY_TITLE = "D�as no laborales: ";
	private static final String WINDOW_ITEM_FREEDAYS_TITLE = "D�as libres del calendario: ";
	private static final String WINDOW_ITEM_HOLIDAY_TITLE = "Vacaciones: ";
	private static final String WINDOW_ITEM_YEAR_TOTAL_HOURS_TITLE = "Total horas convenio: ";
	private static final String WINDOW_ITEM_ACTUAL_HOURS_TITLE = "Horas actuales: ";
	private static final String WINDOW_ITEM_PLANNED_HOURS_TITLE = "Horas planificadas: ";
	private static final String WINDOW_ITEM_DAY_HOURS_TITLE = "Horas diarias: ";
	private static final String WINDOW_ITEM_DAY_HOURS_DESC_TITLE = "Descripci�n: ";
	private static final String WINDOW_ITEM_EXPORT_DATA_TITLE = "Exportar datos";

	private static final String WINDOW_PANEL_BORDER_PROFILE_TITLE = "Perfil";
	private static final String WINDOW_PANEL_BORDER_HISTORY_TITLE = "Hist�rico";
	private static final String WINDOW_PANEL_BORDER_ANUAL_INFO = "Informaci�n anual";
	private static final String WINDOW_PANEL_BORDER_DIARY_INFO = "Informaci�n diaria";
	// Exceso de horas
	private static final String WINDOW_PANEL_BORDER_EXCEDEED_HOURS_INFO = "Calculo de vacaciones";
	private static final String WINDOW_ITEM_EXCEDEED_HOURS_TITLE = "Exceso horario";
	private static final String WINDOW_ITEM_EXCEDEED_DAYS_TITLE = "D�as de vacaciones disponibles";

	private JCalendarPanel jCalendarPanel;

	private JTextField convTextField;
	private JTextField actualHoursTextField;
	private JTextField plannedHoursTextField;
	private JTextArea descTextArea;
	private JTextField diaryHoursTextField;
	private JTextField exceededHoursTextField;
	private JTextField exceededDaysTextField;

	private IORMClient orm;
	private String username;

	public HourManagerWindow(IORMClient orm, String username) {
		this.orm = orm;
		this.username = username;
		try {
			if (!ConfigurationUtils.existAnualConfigurationFile()) {
				ModalWindowUtils
						.showOptionPanel(
								this,
								ERROR_ANUALCONFIGURATION_NOT_EXIST,
								new Object[] { HCalendarConstants.ACTION_BUTTON_ACCEPT_TITLE });
				this.dispose();
				return;
			}
			this.setSize(1000, 600);
			this.setMaximumSize(new Dimension(1500, 900));
			this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle(WINDOW_TITLE);
			Container content = this.getContentPane();
			JPanel panel4 = new JPanel(new GridLayout(1, 2));
			JPanel mainPanel = new JPanel(new GridLayout(1, 1));
			createUserAndCalendar(panel4);
			createTextFields(panel4);

			mainPanel.add(panel4);
			createButtons(content);
			content.add(mainPanel);

			// Paint with green the calendar free days
			List<Date> freeDays = ORMHelper.getCalendarFreeDaysDate(
					orm.getAnualConfiguration(), this.username,
					jCalendarPanel.getSelectedYear());
			for (Date date : freeDays)
				jCalendarPanel.addDayToList(date, LIST_TYPE.CALENDAR_FREEDAY);
			// Paint with red the anual not working days
			List<Date> calendarNotWorkingDays = ORMHelper
					.getCalendarNotWorkingDays(orm.getAnualConfiguration(),
							this.username, jCalendarPanel.getSelectedYear());
			for (Date date : calendarNotWorkingDays)
				jCalendarPanel
						.addDayToList(date, LIST_TYPE.USER_NOT_WORKINGDAY);
			// Add user holidays working days
			List<WorkedHours> calendarWorkingDays = ORMHelper
					.getUsersWorkedHourList(orm.getAnualHours(), this.username);
			for (WorkedHours wh : calendarWorkingDays)
				jCalendarPanel.addDayToList(
						DateHelper.xmlGregorianCalendar2Date(wh.getDate()),
						LIST_TYPE.USER_WORKINGDAY);

			// Add user holidays
			List<Date> userHolidays = ORMHelper.getUserHolidays(
					orm.getAnualHours(), this.username);
			for (Date date : userHolidays)
				jCalendarPanel.addDayToList(date, LIST_TYPE.USER_HOLIDAYS);

			this.setVisible(true);
			pack();
		} catch (Exception e) {
			e.printStackTrace();
			ModalWindowUtils.showErrorPanel(this,
					HCalendarConstants.ERROR_WINDOW_CREATION);
		}
	}

	// private final String DATE_SEPARATOR = "\n";

	private void createUserAndCalendar(Container panel) throws ORMException {
		// Poner en el calendar el a�o actual, y si no tiene configuraci�n,
		// el m�s cercano hacia arriba y luego para abajo.
		int yearToShow = Calculator.calculateYearConfigForProfile(orm
				.getAnualConfiguration(), this.username, Calendar.getInstance()
				.get(Calendar.YEAR));

		JPanel combPanel = new JPanel(new GridLayout(2, 1));
		JPanel profileAndHist = new JPanel(new GridLayout(2, 1));
		// Anyadimos botones para el hist�rico de colores utilizados
		JPanel profilePanel = new JPanel(new GridLayout(2, 1));
		JLabel shownProfile = new JLabel(WINDOW_ITEM_NAME_TITLE + this.username);
		JLabel shownYear = new JLabel(WINDOW_ITEM_YEAR_TITLE + yearToShow);
		profilePanel.setBorder(BorderFactory
				.createTitledBorder(WINDOW_PANEL_BORDER_PROFILE_TITLE));
		profilePanel.add(shownProfile);
		profilePanel.add(shownYear);

		// Anyadimos botones para el hist�rico de colores utilizados
		JPanel histPanel = new JPanel();
		histPanel.setBorder(BorderFactory
				.createTitledBorder(WINDOW_PANEL_BORDER_HISTORY_TITLE));
		JLabel colorHist = new JLabel(WINDOW_ITEM_NOT_WORKINGDAY_TITLE);
		JButton redButton = new JButton();
		redButton.setEnabled(false);
		redButton.setBackground(Color.red);
		colorHist.setLabelFor(redButton);
		histPanel.add(colorHist);
		histPanel.add(redButton);
		colorHist = new JLabel(WINDOW_ITEM_FREEDAYS_TITLE);
		JButton greenButton = new JButton();
		greenButton.setEnabled(false);
		greenButton.setBackground(Color.green);
		colorHist.setLabelFor(greenButton);
		histPanel.add(colorHist);
		histPanel.add(greenButton);
		colorHist = new JLabel(WINDOW_ITEM_HOLIDAY_TITLE);
		JButton blueButton = new JButton();
		blueButton.setEnabled(false);
		blueButton.setBackground(Color.blue);
		colorHist.setLabelFor(blueButton);
		histPanel.add(colorHist);
		histPanel.add(blueButton);
		profileAndHist.add(profilePanel, BorderLayout.CENTER);
		profileAndHist.add(histPanel, BorderLayout.CENTER);

		// Calendar
		jCalendarPanel = new JCalendarPanel(yearToShow, 0, 0,
				new ICalendarEventListener() {

					@Override
					public void onDateChanged(Date date, Boolean selected,
							ICalendarActionProvider actionProvider) {
						try {
							calendarOnDateChangedActions(date, selected,
									actionProvider);
						} catch (ORMException e) {
							ModalWindowUtils.showErrorPanel(
									HourManagerWindow.this,
									ERROR_OPERATION_DATECHANGE);
							e.printStackTrace();
						}
					}

					@Override
					public void onDataInput(IDateEntity entity) {
						jCalendarPanel.dataInputOcurred(entity);
						try {
							orm.addChange(HourManagerWindow.this.username,
									entity);
						} catch (ORMException e) {
							ModalWindowUtils.showErrorPanel(
									HourManagerWindow.this,
									ERROR_OPERATION_APPLY_CHANGES);
							e.printStackTrace();
						}
					}
				}, true, false);
		combPanel.add(profileAndHist);
		combPanel.add(jCalendarPanel);
		panel.add(combPanel);
	}

	private void createTextFields(JPanel parentPanel) throws ORMException {
		JPanel panel = new JPanel(new GridLayout(4, 1));

		JPanel separatorPanel = new JPanel();
		// Panel de horas generales
		JPanel genHoursPanel = new JPanel(new GridLayout(3, 2));
		genHoursPanel.setBorder(BorderFactory
				.createTitledBorder(WINDOW_PANEL_BORDER_ANUAL_INFO));
		JLabel genLabel = new JLabel(WINDOW_ITEM_YEAR_TOTAL_HOURS_TITLE);
		convTextField = new JTextField();
		// Obtener horas anuales desde el xml
		convTextField.setEditable(false);
		int year = jCalendarPanel.getSelectedYear();
		if (ORMHelper.getCalendarHours(orm.getAnualConfiguration(),
				this.username, year) != null) {
			String yearStr = String.valueOf(ORMHelper.getCalendarHours(
					orm.getAnualConfiguration(), this.username, year));
			convTextField.setText(yearStr);
		}

		genLabel.setLabelFor(convTextField);
		genHoursPanel.add(genLabel, BorderLayout.WEST);
		genHoursPanel.add(convTextField, BorderLayout.CENTER);

		// Calcular las horas desde los datos del XML
		JLabel actualHours = new JLabel(WINDOW_ITEM_ACTUAL_HOURS_TITLE);
		actualHoursTextField = new JTextField();
		actualHoursTextField.setEditable(false);
		actualHours.setLabelFor(actualHoursTextField);
		genHoursPanel.add(actualHours, BorderLayout.WEST);
		genHoursPanel.add(actualHoursTextField, BorderLayout.CENTER);

		JLabel plannedHours = new JLabel(WINDOW_ITEM_PLANNED_HOURS_TITLE);
		plannedHoursTextField = new JTextField();
		plannedHoursTextField.setEditable(false);
		String plannedH = String.valueOf(Calculator.calculateAnualPlannedHours(
				orm.getAnualHours(), year, this.username));
		plannedHoursTextField.setText(plannedH);
		plannedHours.setLabelFor(plannedHoursTextField);
		genHoursPanel.add(plannedHours, BorderLayout.WEST);
		genHoursPanel.add(plannedHoursTextField, BorderLayout.CENTER);

		// Panel de horas restantes
		JPanel excedeedHourPanel = new JPanel(new GridLayout(2, 2));
		excedeedHourPanel.setBorder(BorderFactory
				.createTitledBorder(WINDOW_PANEL_BORDER_EXCEDEED_HOURS_INFO));
		JLabel label = new JLabel(WINDOW_ITEM_EXCEDEED_HOURS_TITLE);
		exceededHoursTextField = new JTextField();
		exceededHoursTextField.setEditable(false);
		label.setLabelFor(exceededHoursTextField);
		excedeedHourPanel.add(label, BorderLayout.WEST);
		excedeedHourPanel.add(exceededHoursTextField, BorderLayout.CENTER);

		label = new JLabel(WINDOW_ITEM_EXCEDEED_DAYS_TITLE);
		exceededDaysTextField = new JTextField();
		exceededDaysTextField.setEditable(false);
		label.setLabelFor(exceededDaysTextField);
		excedeedHourPanel.add(label, BorderLayout.WEST);
		excedeedHourPanel.add(exceededDaysTextField, BorderLayout.CENTER);

		// Panel actualizable de horas diarias
		JPanel diaryPanel = new JPanel(new GridLayout(2, 2));
		diaryPanel.setBorder(BorderFactory
				.createTitledBorder(WINDOW_PANEL_BORDER_DIARY_INFO));
		JLabel diaryHoursLabel = new JLabel(WINDOW_ITEM_DAY_HOURS_TITLE);
		diaryHoursTextField = new JTextField();
		diaryHoursTextField.setEditable(false);
		diaryHoursLabel.setLabelFor(diaryHoursTextField);
		diaryPanel.add(diaryHoursLabel, BorderLayout.WEST);
		diaryPanel.add(diaryHoursTextField, BorderLayout.CENTER);

		JLabel desc = new JLabel(WINDOW_ITEM_DAY_HOURS_DESC_TITLE);
		descTextArea = new JTextArea();
		descTextArea.setEditable(false);
		// Poner el mismo borde y color que al textfield ya que el area se
		// comporta distinto...
		descTextArea.setBackground(diaryHoursTextField.getBackground());
		descTextArea.setBorder(diaryHoursTextField.getBorder());
		descTextArea.setLineWrap(true);

		desc.setLabelFor(descTextArea);
		diaryPanel.add(desc, BorderLayout.WEST);
		diaryPanel.add(descTextArea, BorderLayout.CENTER);

		// Anyadir los paneles secundarios
		panel.add(diaryPanel);
		panel.add(separatorPanel);
		panel.add(excedeedHourPanel);
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
		JButton exportButton = new JButton(WINDOW_ITEM_EXPORT_DATA_TITLE);
		exportButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DataServices.exportInfo(orm, jCalendarPanel.getSelectedYear(),
						HourManagerWindow.this.username, new Observer() {

							@Override
							public void update(Observable o, Object arg) {
								Integer result = (Integer) arg;
								if (result.equals(0))
									ModalWindowUtils.showSuccesPanel(
											HourManagerWindow.this,
											SUCCES_EXPORT_DATA);
								else if (result.equals(-2))
									ModalWindowUtils.showErrorPanel(
											HourManagerWindow.this,
											ERROR_EXPORT_DATA_FILE_LOCKED);
								else
									ModalWindowUtils.showErrorPanel(
											HourManagerWindow.this,
											ERROR_EXPORT_DATA);
							}
						});

			}
		});
		buttonPane.add(exportButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		// BOTON CANCELAR
		JButton cancelButton = new JButton(
				HCalendarConstants.ACTION_BUTTON_CANCEL_TITLE);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HourManagerWindow.this.dispose();
			}
		});

		buttonPane.add(cancelButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));

		// BOTON GUARDAR
		JButton saveButton = new JButton(
				HCalendarConstants.ACTION_BUTTON_SAVE_TITLE);
		buttonPane.add(saveButton);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					orm.persist(ENTITY_TYPE.ANUALHOURS);
					orm.persist(ENTITY_TYPE.USERCONFIGURATION);
					ModalWindowUtils.showSuccesPanel(HourManagerWindow.this,
							HCalendarConstants.SUCCES_SAVE_DATA);
				} catch (ORMException e1) {
					ModalWindowUtils.showSuccesPanel(HourManagerWindow.this,
							HCalendarConstants.ERROR_SAVE_DATA);
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
		try {
			//Horas planificadas y del convenio
			Float yearRequiredHours = ORMHelper.getCalendarHours(orm.getAnualConfiguration(), this.username,1900 + date.getYear());
			convTextField.setText(String.valueOf(yearRequiredHours));
			Float yearPlannedHours = Calculator.calculateAnualPlannedHours(orm.getAnualHours(), date.getYear() + 1900, this.username);
			plannedHoursTextField.setText(String.valueOf(yearPlannedHours));
			
			// Calcular horas imputadas hasta ahora
			actualHoursTextField.setText(String.valueOf(Calculator
					.calculateHoursUntilDate(orm.getAnualHours(), date,
							this.username)));

			Map<Float, String> inputHours;
			inputHours = ORMHelper.getInputHours(orm.getAnualHours(), date,
					this.username);
			Set<Float> keys = inputHours.keySet();
			Float tHours = new Float(0);
			String desc = new String();
			for (Float f : keys) {
				tHours = tHours + f;
				desc = desc + inputHours.get(f);
			}
			diaryHoursTextField.setText(String.valueOf(tHours));
			descTextArea.setText(desc);
			
			//	Calculate hours exceed
			float exHours = Calculator.calculateExceededHours(yearRequiredHours, yearPlannedHours);
			float exDays = Calculator.calculateExceededDays(orm.getAnualConfiguration(), this.username, yearRequiredHours, yearPlannedHours, date.getYear()+1900);
			BigDecimal scaledExDays = new BigDecimal(exDays).setScale(2, RoundingMode.HALF_DOWN);
			exceededHoursTextField.setText(exHours +" horas");
			exceededDaysTextField.setText(String.valueOf(scaledExDays));
		} catch (Throwable e) {
			e.printStackTrace();
			ModalWindowUtils.showErrorPanel(HourManagerWindow.this,
					ERROR_CALCULATE_DATA + date.getYear());
		}
	}

	@Override
	public void addParentWindow(IWindowDataHanlder window) {
		childWindows.add(window);
	}

	@Override
	public void setData() {
		// TODO De momento no hace falta rellenarlo. Nadie escucha cambios
	}

	@Override
	public void notifyDataChange() {
		for (IWindowDataHanlder w : childWindows) {
			w.setData();
		}
	}
}
