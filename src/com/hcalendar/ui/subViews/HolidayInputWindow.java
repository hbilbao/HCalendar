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
import javax.swing.JTextArea;
import javax.swing.WindowConstants;

import com.hcalendar.HCalendarConstants;
import com.hcalendar.data.IDateEntity.DateType;
import com.hcalendar.data.orm.ORMEntity;
import com.hcalendar.ui.widgets.IWindowResultListener;

/**
 * Window to input holiday data
 * */
public class HolidayInputWindow extends JFrame {

	private static final String WINDOW_TITLE = "Vacaciones";
	private static final String WINDOW_PANEL_BORDER_COMMENTS = "Comentarios sobre las vacaciones";
	private static final String WINDOW_LABEL_COMMENTS = "Comentarios: ";

	private static final long serialVersionUID = 1L;

	JTextArea descTextArea;

	IWindowResultListener listener;
	Date date;

	public HolidayInputWindow(IWindowResultListener listener, Date date) {
		this.listener = listener;
		this.date = date;
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		this.setTitle(WINDOW_TITLE);
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
		diaryPanel.setBorder(BorderFactory
				.createTitledBorder(WINDOW_PANEL_BORDER_COMMENTS));
		JLabel diaryHoursLabel = new JLabel(WINDOW_LABEL_COMMENTS);
		descTextArea = new JTextArea();
		descTextArea.setLineWrap(true);
		diaryHoursLabel.setLabelFor(descTextArea);
		diaryPanel.add(diaryHoursLabel, BorderLayout.WEST);
		diaryPanel.add(descTextArea, BorderLayout.CENTER);
		panel.add(diaryPanel);
	}

	private void createButtons(Container panel) {
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
		buttonPane.add(Box.createHorizontalGlue());
		// BOTON GUARDAR
		JButton saveButton = new JButton(
				HCalendarConstants.ACTION_BUTTON_SAVE_TITLE);
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (HolidayInputWindow.this.listener != null) {
					String description = descTextArea.getText();
					HolidayInputWindow.this.listener
							.windowResult(new ORMEntity(date,
									DateType.HOLIDAYS, null, description));
					HolidayInputWindow.this.dispose();
				}
			}
		});
		buttonPane.add(saveButton);
		buttonPane.add(Box.createRigidArea(new Dimension(10, 0)));
		// BOTON CANCELAR
		JButton cancelButton = new JButton(
				HCalendarConstants.ACTION_BUTTON_CANCEL_TITLE);
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
