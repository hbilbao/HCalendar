package com.hcalendar.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import com.hcalendar.HCalendarConstants;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.ui.HourManagerWindow;

public class HourManagerLauncher implements ActionListener {

	private IORMClient orm;

	public HourManagerLauncher(IORMClient orm) {
		this.orm = orm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String userName = (String) ((JComboBox) e.getSource()).getSelectedItem();
		if (userName == null || userName.equals(HCalendarConstants.NULL_COMBO_INPUT))
			return;
		new HourManagerWindow(this.orm, userName);
	}

}
