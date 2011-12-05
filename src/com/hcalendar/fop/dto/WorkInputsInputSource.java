/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* $Id: ProjectTeamInputSource.java 679326 2008-07-24 09:35:34Z vhennebert $ */

package com.hcalendar.fop.dto;

import org.xml.sax.InputSource;

import com.hcalendar.data.dto.WorkInputsDTO;

/**
 * This class is a special InputSource decendant for using ProjectTeam instances
 * as XML sources.
 */
public class WorkInputsInputSource extends InputSource {

	private WorkInputsDTO workInputs;

	/**
	 * Constructor for the ProjectTeamInputSource
	 * 
	 * @param projectTeam
	 *            The ProjectTeam object to use
	 */
	public WorkInputsInputSource(WorkInputsDTO workInputs) {
		this.workInputs = workInputs;
	}

	public WorkInputsDTO getWorkInputs() {
		return workInputs;
	}

	public void setWorkInputs(WorkInputsDTO workInputs) {
		this.workInputs = workInputs;
	}

}
