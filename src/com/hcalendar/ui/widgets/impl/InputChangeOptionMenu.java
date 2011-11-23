package com.hcalendar.ui.widgets.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.hcalendar.data.IDateEntity;
import com.hcalendar.data.IDateEntity.DateType;
import com.hcalendar.data.orm.ORMEntity;
import com.hcalendar.ui.subViews.DiaryHourWindow;
import com.hcalendar.ui.subViews.HolidayInputWindow;
import com.hcalendar.ui.widgets.IWindowResultListener;

/**
 * Menu class with the following options
 * - CHANGE TO LABORAL
 * - CHANGE TOP NOT WORKING
 * - CHANGE INPUTS OF DAY
 * - CHANGE TO HOLIDAY
 * */
public class InputChangeOptionMenu extends JPopupMenu {
	private static final long serialVersionUID = 1L;

	JMenuItem anItem;
	String[] changeInputsValues = new String[] { CHANGE_VALUES_LABORAL, CHANGE_VALUES_NOT_WORKING,
			CHANGE_VALUES_CHANGE_INPUTS, CHANGE_VALUES_HOLIDAY };

	private static final String CHANGE_VALUES_LABORAL = "Marcar laboral";
	private static final String CHANGE_VALUES_NOT_WORKING = "Marcar festivo";
	private static final String CHANGE_VALUES_CHANGE_INPUTS = "Cambiar imputaciones";
	private static final String CHANGE_VALUES_HOLIDAY = "Vacaciones";

	public InputChangeOptionMenu(final IWindowResultListener listener, final Date date) {
		JMenuItem laboralChoice = new JMenuItem(CHANGE_VALUES_LABORAL);
		JMenuItem notWorkingChoice = new JMenuItem(CHANGE_VALUES_NOT_WORKING);
		JMenuItem changeInChoice = new JMenuItem(CHANGE_VALUES_CHANGE_INPUTS);
		JMenuItem holidayChoice = new JMenuItem(CHANGE_VALUES_HOLIDAY);
		add(laboralChoice);
		add(notWorkingChoice);
		add(changeInChoice);
		add(holidayChoice);

		laboralChoice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new DiaryHourWindow(new IWindowResultListener() {

					@Override
					public void windowResult(IDateEntity entity) {
						listener.windowResult(entity);
					}
				}, date);
			}
		});

		notWorkingChoice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Mark date to not working
				listener.windowResult(new ORMEntity(date, DateType.FREE_DAY, null, null));
			}
		});

		changeInChoice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new DiaryHourWindow(new IWindowResultListener() {

					@Override
					public void windowResult(IDateEntity entity) {
						listener.windowResult(entity);
					}
				}, date);

			}
		});

		holidayChoice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Mark date to holiday
				new HolidayInputWindow(new IWindowResultListener() {

					@Override
					public void windowResult(IDateEntity entity) {
						listener.windowResult(entity);
					}
				}, date);
			}
		});
	}
}
