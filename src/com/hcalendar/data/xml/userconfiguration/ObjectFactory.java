//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-257 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.11.19 at 02:35:18 PM CET 
//


package com.hcalendar.data.xml.userconfiguration;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.hcalendar.data.userconfiguration package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.hcalendar.data.userconfiguration
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link UserConfiguration }
     * 
     */
    public UserConfiguration createUserConfiguration() {
        return new UserConfiguration();
    }

    /**
     * Create an instance of {@link UserConfiguration.User.YearConf }
     * 
     */
    public UserConfiguration.User.YearConf createUserConfigurationUserYearConf() {
        return new UserConfiguration.User.YearConf();
    }

    /**
     * Create an instance of {@link UserConfiguration.User.YearConf.FreeDays.FreeDay }
     * 
     */
    public UserConfiguration.User.YearConf.FreeDays.FreeDay createUserConfigurationUserYearConfFreeDaysFreeDay() {
        return new UserConfiguration.User.YearConf.FreeDays.FreeDay();
    }

    /**
     * Create an instance of {@link UserConfiguration.User.YearConf.WorkingDays }
     * 
     */
    public UserConfiguration.User.YearConf.WorkingDays createUserConfigurationUserYearConfWorkingDays() {
        return new UserConfiguration.User.YearConf.WorkingDays();
    }

    /**
     * Create an instance of {@link UserConfiguration.User }
     * 
     */
    public UserConfiguration.User createUserConfigurationUser() {
        return new UserConfiguration.User();
    }

    /**
     * Create an instance of {@link UserConfiguration.User.YearConf.FreeDays }
     * 
     */
    public UserConfiguration.User.YearConf.FreeDays createUserConfigurationUserYearConfFreeDays() {
        return new UserConfiguration.User.YearConf.FreeDays();
    }

}