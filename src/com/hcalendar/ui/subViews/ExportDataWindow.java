package com.hcalendar.ui.subViews;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.hcalendar.data.utils.DateHelper;
import com.hcalendar.ui.validator.NumericValidator;
import com.hcalendar.ui.widgets.impl.JCalendarDatePicker;

/**
 * Window to export data
 * */
public class ExportDataWindow extends JFrame {

	private static final String WINDOW_TITLE = "Exportación de datos";
	private static final String WINDOW_PANEL_BORDER_DATE_FILTER = "Filtrar por fechas";

	private static final String WINDOW_ITEM_ENABLE_FILTER = "Habilitar filtro";

	public static final Integer EXPORT_PDF_OPTION_DAY = 0;
	public static final Integer EXPORT_PDF_OPTION_MONTH = 1;
	public static final Integer EXPORT_CSV_OPTION_DAY = 3;
	public static final Integer EXPORT_CSV_OPTION_MONTH = 4;

	private static final long serialVersionUID = 1L;

	private JTextField fromDateTextField;
	private JTextField toDateTextField;
	private JRadioButton pdfOption;
	private JRadioButton csvOption;

	private Observer observer;

	public ExportDataWindow(Observer observer) {
		this.observer = observer;
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setTitle(WINDOW_TITLE);
		Container content = this.getContentPane();
		JPanel panel = new JPanel(new GridLayout(1, 1));
		createWidgets(panel);
		content.add(panel);
		createButtons(content);
		pack();
		this.setVisible(true);
	}

	private void createWidgets(Container panel) {
		// Panel actualizable de horas diarias
		JPanel widgetPanel = new JPanel(new GridLayout(2, 1));
		widgetPanel.setBorder(BorderFactory
				.createTitledBorder(WINDOW_PANEL_BORDER_DATE_FILTER));

		JPanel checkPanel = new JPanel(new GridLayout(1, 1));
		final JCheckBox enableFilter = new JCheckBox(WINDOW_ITEM_ENABLE_FILTER);
		checkPanel.add(enableFilter, BorderLayout.WEST);

		pdfOption = new JRadioButton("pdf");
		csvOption = new JRadioButton("excel");
		ButtonGroup pdfOrCsvOptionGroup = new ButtonGroup();
		pdfOrCsvOptionGroup.add(csvOption);
		pdfOption.setSelected(true);
		pdfOrCsvOptionGroup.add(pdfOption);
		checkPanel.add(csvOption);
		checkPanel.add(pdfOption);

		JPanel datePanel = new JPanel(new GridLayout(1, 4));
		JLabel diaryHoursLabel = new JLabel("Desde: ");
		fromDateTextField = new JTextField();
		diaryHoursLabel.setLabelFor(fromDateTextField);
		datePanel.add(diaryHoursLabel, BorderLayout.WEST);
		datePanel.add(fromDateTextField, BorderLayout.CENTER);
		final JButton fromButton = new JButton();
		datePanel.add(fromButton);
		fromButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new JCalendarDatePicker(new Observer() {

					@Override
					public void update(Observable arg0, Object arg1) {
						Date date = ((GregorianCalendar) arg1).getTime();
						fromDateTextField.setText(DateHelper.DATE_FORMAT
								.format(date));
					}
				});
			}
		});

		fromDateTextField.setInputVerifier(new NumericValidator(this, true));
		JLabel desc = new JLabel("Hasta: ");
		toDateTextField = new JTextField();
		desc.setLabelFor(toDateTextField);
		datePanel.add(desc, BorderLayout.WEST);
		datePanel.add(toDateTextField, BorderLayout.CENTER);
		final JButton toButton = new JButton();
		toButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				new JCalendarDatePicker(new Observer() {

					@Override
					public void update(Observable arg0, Object arg1) {
						Date date = ((GregorianCalendar) arg1).getTime();
						toDateTextField.setText(DateHelper.DATE_FORMAT
								.format(date));
					}
				});
			}
		});

		enableFilter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (enableFilter.isSelected()) {
					fromDateTextField.setEditable(true);
					toDateTextField.setEditable(true);
					fromButton.setEnabled(true);
					toButton.setEnabled(true);
				} else {
					fromDateTextField.setText(null);
					toDateTextField.setText(null);
					fromDateTextField.setEditable(false);
					toDateTextField.setEditable(false);
					fromButton.setEnabled(false);
					toButton.setEnabled(false);
				}
			}
		});

		// SET DEFAULT VALUES TO DISABLE
		fromDateTextField.setText(null);
		toDateTextField.setText(null);
		fromDateTextField.setEditable(false);
		toDateTextField.setEditable(false);
		fromButton.setEnabled(false);
		toButton.setEnabled(false);

		datePanel.add(toButton);
		widgetPanel.add(checkPanel);
		widgetPanel.add(datePanel);
		panel.add(widgetPanel);
	}

	private void createButtons(Container panel) {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		// BOTON desglose por días
		JButton dayOption = new JButton("Desglosado por días");
		dayOption.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Object> result = new ArrayList<Object>();
				result.add(pdfOption.isSelected() ? EXPORT_PDF_OPTION_DAY
						: EXPORT_CSV_OPTION_DAY);
				result.add(fromDateTextField.getText());
				result.add(toDateTextField.getText());
				ExportDataWindow.this.observer.update(null, result);
				ExportDataWindow.this.dispose();
			}
		});
		buttonPane.add(dayOption);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		// BOTON DESGLOSE POR MESES
		JButton cancelButton = new JButton("Desglosado por meses");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<Object> result = new ArrayList<Object>();
				result.add(pdfOption.isSelected() ? EXPORT_PDF_OPTION_MONTH
						: EXPORT_CSV_OPTION_MONTH);
				result.add(fromDateTextField.getText());
				result.add(toDateTextField.getText());
				ExportDataWindow.this.observer.update(null, result);
				ExportDataWindow.this.dispose();
			}
		});

		buttonPane.add(cancelButton);
		panel.add(buttonPane, BorderLayout.PAGE_END);
	}

	public static void main(String[] args) {
		new ExportDataWindow(null);
	}
}
