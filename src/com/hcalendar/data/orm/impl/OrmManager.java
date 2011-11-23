package com.hcalendar.data.orm.impl;

import com.hcalendar.data.orm.IORMClient;
import com.hcalendar.data.orm.exception.ORMException;

/**
 * ORMManager that allow to create new orm instances. Each orm instance contains
 * a instance and changes tracking of each persistable entities
 * */
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
