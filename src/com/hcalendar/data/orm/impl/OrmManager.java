package com.hcalendar.data.orm.impl;

import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.exception.ORMException;

public class OrmManager {

	private static OrmManager instance = null;

	private OrmManager() {
	}

	public static OrmManager getInstance() {
		if (instance == null)
			instance = new OrmManager();
		return instance;
	}

	public IORMClient getORMClient() throws ORMException {
		return new ORMClient();
	}
}
