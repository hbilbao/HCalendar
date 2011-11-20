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

public class HolidayInputWindow extends JFrame {

	private static final long serialVersionUID = 1L;

	JTextField descTextField;

	IWindowResultListener listener;
	Date date;

	public HolidayInputWindow(IWindowResultListener listener, Date date) {
		this.listener = listener;
		this.date = date;
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setTitle("Vacaciones");
		Container content = this.getContentPane();
		JPanel panel = new JPanel(new GridLayout(1, 1));
		createTextFields(panel);
		content.add(panel);
		createButtons(content);
		pack();
		this.setVisible(true);
	}

	private void createTextFields(Container panel) {
		JPanel diaryPanel = new JPanel(new GridLayout(2, 2));
		diaryPanel.setBorder(BorderFactory.createTitledBorder("Comentarios sobre las vacaciones"));
		JLabel diaryHoursLabel = new JLabel("Comentarios: ");
		descTextField = new JTextField();
		diaryHoursLabel.setLabelFor(descTextField);
		diaryPanel.add(diaryHoursLabel, BorderLayout.WEST);
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
				if (HolidayInputWindow.this.listener != null) {
					String description = descTextField.getText();
					HolidayInputWindow.this.listener.windowResult(new ORMEntity(date, DateType.HOLIDAYS,
							null, description));
					HolidayInputWindow.this.dispose();
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
				HolidayInputWindow.this.dispose();
			}
		});

		buttonPane.add(cancelButton);
		panel.add(buttonPane, BorderLayout.PAGE_END);
	}

	public static void main(String[] args) {
		new HolidayInputWindow(null, null);
	}

}
