package com.hcalendar.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.ui.IWindowDataHanlder;
import com.hcalendar.ui.UserConfigurationWindow;

/**
 * Create a new window of type anual configuration
 */
public class CreateProfileLauncher implements ActionListener {

	private IWindowDataHanlder callback;
	private IORMClient orm;

	public CreateProfileLauncher(IORMClient orm, IWindowDataHanlder callback) {
		this.callback = callback;
		this.orm = orm;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		new UserConfigurationWindow(orm, callback);
	}

}
