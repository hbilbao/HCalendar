package com.hcalendar.ui.widgets.impl;

/*
 * All rights reserved. Software written by Ian F. Darwin and others.
 * $Id: LICENSE,v 1.8 2004/02/09 03:33:38 ian Exp $
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE AUTHOR OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Java, the Duke mascot, and all variants of Sun's Java "steaming coffee
 * cup" logo are trademarks of Sun Microsystems. Sun's, and James Gosling's,
 * pioneering role in inventing and promulgating (and standardizing) the Java 
 * language and environment is gratefully acknowledged.
 * 
 * The pioneering role of Dennis Ritchie and Bjarne Stroustrup, of AT&T, for
 * inventing predecessor languages C and C++ is also gratefully acknowledged.
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hcalendar.data.utils.DateHelper;

/**
 * Calendar panel
 * */
public class JCalendarDatePicker extends JFrame {

	private static final long serialVersionUID = 1L;

	/** The currently-interesting year (not modulo 1900!) */
	protected int yy;

	/** Currently-interesting month and day */
	protected int mm, dd;

	/** The buttons to be displayed */
	protected JButton labs[][];

	/** The number of day squares to leave blank at the start of this month */
	protected int leadGap = 0;

	/** A Calendar object used throughout */
	Calendar calendar = new GregorianCalendar();

	/** Today's year */
	protected final int thisYear = calendar.get(Calendar.YEAR);

	/** Today's month */
	protected final int thisMonth = calendar.get(Calendar.MONTH);

	/** One of the buttons. We just keep its reference for getBackground(). */
	private JButton b0;

	/** The month choice */
	private JComboBox monthChoice;

	/** The year choice */
	private JComboBox yearChoice;

	private int activeDay = -1;

	private Observer observer;

	private static final String DAY_SHORT_NAME_MONDAY = DateHelper.daysOfWeek[0];
	private static final String DAY_SHORT_NAME_TUESDAY = DateHelper.daysOfWeek[1];
	private static final String DAY_SHORT_NAME_WEDNESDAY = DateHelper.daysOfWeek[2];
	private static final String DAY_SHORT_NAME_THURSDAY = DateHelper.daysOfWeek[3];
	private static final String DAY_SHORT_NAME_FRIDAY = DateHelper.daysOfWeek[4];
	private static final String DAY_SHORT_NAME_SATURDAY = DateHelper.daysOfWeek[5];
	private static final String DAY_SHORT_NAME_SUNDAY = DateHelper.daysOfWeek[6];

	/**
	 * Construct a Cal, starting with today.
	 * 
	 * @param observer
	 */
	public JCalendarDatePicker(Observer observer) {
		super();
		this.observer = observer;
		setYYMMDD(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH));
		buildGUI();
		recompute();
	}

	/**
	 * Construct a Cal, given the leading days and the total days
	 * 
	 * @exception IllegalArgumentException
	 *                If year out of range
	 */
	public JCalendarDatePicker(int year, int month, int today, Observer observer) {
		super();
		this.observer = observer;
		if (month == 0)
			month = Calendar.MONTH;
		if (today == 0)
			today = Calendar.DAY_OF_MONTH;
		setYYMMDD(year, month, today);
		buildGUI();
		recompute();
	}

	private void setYYMMDD(int year, int month, int today) {
		yy = year;
		mm = month;
		dd = today;
	}

	/** Build the GUI. Assumes that setYYMMDD has been called. */
	private void buildGUI() {
		Container c = getContentPane();
		JPanel panel = new JPanel();
		c.add(panel);
		panel.setLayout(new FlowLayout());
		panel.getAccessibleContext().setAccessibleDescription(
				"Calendar not accessible yet. Sorry!");
		panel.setBorder(BorderFactory.createEtchedBorder());

		panel.setLayout(new BorderLayout());

		JPanel tp = new JPanel();
		tp.add(monthChoice = new JComboBox());
		for (int i = 0; i < DateHelper.months.length; i++)
			monthChoice.addItem(DateHelper.months[i]);
		monthChoice.setSelectedItem(DateHelper.months[mm]);
		monthChoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				int i = monthChoice.getSelectedIndex();
				if (i >= 0) {
					mm = i;
					// System.out.println("Month=" + mm);
					recompute();
				}
			}
		});
		monthChoice.getAccessibleContext().setAccessibleName("Months");
		monthChoice.getAccessibleContext().setAccessibleDescription(
				"Selecciona un mes");

		tp.add(yearChoice = new JComboBox());
		for (int i = yy - 5; i < yy + 5; i++)
			yearChoice.addItem(Integer.toString(i));
		yearChoice.setSelectedItem(Integer.toString(yy));
		yearChoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				int i = yearChoice.getSelectedIndex();
				if (i >= 0) {
					yy = Integer.parseInt(yearChoice.getSelectedItem()
							.toString());
					// System.out.println("Year=" + yy);
					recompute();
				}
			}
		});
		panel.add(BorderLayout.CENTER, tp);

		JPanel bp = new JPanel();
		bp.setLayout(new GridLayout(7, 7));
		labs = new JButton[6][7]; // first row is days

		bp.add(b0 = new JButton(DAY_SHORT_NAME_MONDAY));
		bp.add(new JButton(DAY_SHORT_NAME_TUESDAY));
		bp.add(new JButton(DAY_SHORT_NAME_WEDNESDAY));
		bp.add(new JButton(DAY_SHORT_NAME_THURSDAY));
		bp.add(new JButton(DAY_SHORT_NAME_FRIDAY));
		bp.add(new JButton(DAY_SHORT_NAME_SATURDAY));
		bp.add(new JButton(DAY_SHORT_NAME_SUNDAY));

		ActionListener dateSetter = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String num = e.getActionCommand();

				if (!num.equals("")) {
					setDayActive(Integer.parseInt(num));
					// Date selected
					notifyDateSelection(new GregorianCalendar(yy, mm, dd));
					disposePanel();
				}
			}
		};

		// Construct all the buttons, and add them.
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 7; j++) {
				labs[i][j] = new JButton("");
				bp.add(labs[i][j]);
				labs[i][j].addActionListener(dateSetter);
			}

		panel.add(BorderLayout.SOUTH, bp);
		this.setVisible(true);
		pack();
	}

	private void notifyDateSelection(GregorianCalendar date) {
		if (this.observer != null)
			this.observer.update(null, date);
	}

	private void disposePanel() {
		this.dispose();
	}

	/** Compute which days to put where, in the Cal panel */
	private void recompute() {
		// System.out.println("Cal::recompute: " + yy + ":" + mm + ":" + dd);
		if (mm < 0 || mm > 11)
			throw new IllegalArgumentException("Month " + mm
					+ " bad, must be 0-11");
		calendar = new GregorianCalendar(yy, mm, dd);

		// Compute how much to leave before the first.
		// getDay() must returns 0 for the first day (Monday).
		leadGap = calculateLeapGap(new GregorianCalendar(yy, mm, 1));

		int daysInMonth = DateHelper.daysOnMonth[mm];
		if (DateHelper.isLeap(calendar.get(Calendar.YEAR)) && mm == 1)
			++daysInMonth;

		// Blank out the labels before 1st day of month
		for (int i = 0; i < leadGap; i++) {
			labs[0][i].setText("");
		}

		// Fill in numbers for the day of month.
		for (int i = 1; i <= daysInMonth; i++) {
			JButton b = labs[(leadGap + i - 1) / 7][(leadGap + i - 1) % 7];
			b.setText(Integer.toString(i));
		}

		// 7 days/week * up to 6 rows
		for (int i = leadGap + daysInMonth; i < 6 * 7; i++) {
			labs[(i) / 7][(i) % 7].setText("");
		}

		// Say we need to be drawn on the screen
		repaint();
	}

	/**
	 * Calculate leap day. First day is Monday
	 * */
	private int calculateLeapGap(GregorianCalendar date) {
		int day = date.get(Calendar.DAY_OF_WEEK) == 1 ? 8 : date
				.get(Calendar.DAY_OF_WEEK);
		int leadGap = day - 2;
		return leadGap;
	}

	/** Set the year, month, and day */
	public void setDate(int yy, int mm, int dd) {
		// System.out.println("Cal::setDate");
		this.yy = yy;
		this.mm = mm; // starts at 0, like Date
		this.dd = dd;
		yearChoice.setSelectedItem(Integer.toString(this.yy));
		monthChoice.setSelectedItem(DateHelper.months[mm]);
		recompute();
	}

	/**
	 * Set just the day, on the current month
	 * 
	 * @param color
	 */
	public void setDayActive(int newDay, Color color) {
		// Set the new one
		if (newDay <= 0)
			dd = new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
		else
			dd = newDay;
		activeDay = newDay;
	}

	public void setDayActive(int newDay) {
		// Set the new one
		if (newDay <= 0)
			dd = new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
		else
			dd = newDay;
		activeDay = newDay;
	}

	public int getSelectedYear() {
		return yy;
	}

	/** For testing, a main program */
	public static void main(String[] av) {
		JFrame f = new JFrame("Cal");
		Container c = f.getContentPane();
		c.setLayout(new FlowLayout());

		// for this test driver, hardcode 1995/02/10.
		c.add(new JCalendarDatePicker(1995, 2 - 1, 10, null));

		// and beside it, the current month.
		c.add(new JCalendarDatePicker(null));

		f.pack();
		f.setVisible(true);
	}
}