//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.19 at 02:35:30 PM CET 
//

package com.hcalendar.data.xml.workedhours;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the com.hcalendar.data.workedhours package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

	/**
	 * Create a new ObjectFactory that can be used to create new instances of
	 * schema derived classes for package: com.hcalendar.data.workedhours
	 * 
	 */
	public ObjectFactory() {
	}

	/**
	 * Create an instance of {@link AnualHours.UserInput.WorkedHours }
	 * 
	 */
	public AnualHours.UserInput.WorkedHours createAnualHoursUserInputWorkedHours() {
		return new AnualHours.UserInput.WorkedHours();
	}

	/**
	 * Create an instance of {@link AnualHours.UserInput }
	 * 
	 */
	public AnualHours.UserInput createAnualHoursUserInput() {
		return new AnualHours.UserInput();
	}

	/**
	 * Create an instance of {@link AnualHours.UserInput.Holidays }
	 * 
	 */
	public AnualHours.UserInput.Holidays createAnualHoursUserInputHolidays() {
		return new AnualHours.UserInput.Holidays();
	}

	/**
	 * Create an instance of {@link AnualHours }
	 * 
	 */
	public AnualHours createAnualHours() {
		return new AnualHours();
	}

}
