package com.hcalendar.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.hcalendar.data.IHCCallback;
import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.ui.UserConfigurationWindow;

public class CreateUserProfile implements ActionListener {

	private IHCCallback callback;
	private IORMClient orm;

	public CreateUserProfile(IORMClient orm, IHCCallback callback){
		this.callback = callback;	
		this.orm = orm;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		new UserConfigurationWindow(orm, callback);
	}

}
