package com.hcalendar.data.orm.impl;

import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.exception.ORMException;

public class ORMManager {

	private static ORMManager instance = null;

	private ORMManager() {
	}

	public static ORMManager getInstance() {
		if (instance == null)
			instance = new ORMManager();
		return instance;
	}

	public IORMClient getORMClient() throws ORMException {
		return new ORMClient();
	}
}
