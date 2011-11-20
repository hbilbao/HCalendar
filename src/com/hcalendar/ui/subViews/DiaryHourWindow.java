package com.hcalendar.ui.subViews;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import com.hcalendar.data.IDateEntity.DateType;
import com.hcalendar.data.orm.ORMEntity;
import com.hcalendar.ui.widgets.IWindowResultListener;

public class DiaryHourWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	JTextField diaryHoursTextField;
	JTextField descTextField;

	IWindowResultListener listener;
	Date date;

	public DiaryHourWindow(IWindowResultListener listener, Date date) {
		this.listener = listener;
		this.date = date;
		// this.setSize(1500, 1000);
		// this.setMaximumSize(new Dimension(1500, 900));
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setTitle("Imputación diaria");
		Container content = this.getContentPane();
		JPanel panel = new JPanel(new GridLayout(1, 1));
		createTextFields(panel);
		content.add(panel);
		createButtons(content);
		pack();
		this.setVisible(true);
	}

	private void createTextFields(Container panel) {
		// Panel actualizable de horas diarias
		JPanel diaryPanel = new JPanel(new GridLayout(4, 2));
		diaryPanel.setBorder(BorderFactory.createTitledBorder("Información diaria"));
		JLabel diaryHoursLabel = new JLabel("Horas trabajadas: ");
		diaryHoursTextField = new JTextField();
		diaryHoursLabel.setLabelFor(diaryHoursTextField);
		diaryPanel.add(diaryHoursLabel, BorderLayout.WEST);
		diaryPanel.add(diaryHoursTextField, BorderLayout.CENTER);
		JLabel desc = new JLabel("Descripción: ");
		descTextField = new JTextField();
		desc.setLabelFor(descTextField);
		diaryPanel.add(desc, BorderLayout.WEST);
		diaryPanel.add(descTextField, BorderLayout.CENTER);
		panel.add(diaryPanel);
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
				if (DiaryHourWindow.this.listener != null) {
					Float hours = Float.valueOf(diaryHoursTextField.getText());
					String description = descTextField.getText();
					DiaryHourWindow.this.listener.windowResult(new ORMEntity(date, DateType.WORK_DAY, hours,
							description));
					DiaryHourWindow.this.dispose();
				}
			}
		});
		buttonPane.add(saveButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		// BOTON CANCELAR
		JButton cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DiaryHourWindow.this.dispose();
			}
		});

		buttonPane.add(cancelButton);
		panel.add(buttonPane, BorderLayout.PAGE_END);
	}

	public static void main(String[] args) {
		new DiaryHourWindow(null, null);
	}

}
