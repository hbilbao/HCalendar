package com.hcalendar;

/**
 * Repository for app constants
 * */
public class HCalendarConstants {

	// CONFIGURATION
	public static final String ANUALCONFIGURATION_FILE = "anualConfiguration.xml";
	public static final String HOUR_INPUT_FILE = "hours_input.xml";

	public static final String NULL_COMBO_INPUT = "";

	// EXPORT DATA
	public static final String EXPORT_CSV_TEMP_FILE = "export_temp.csv";
	public static final String EXPORT_PDF_STYLESHEET_FILE = "workInputs2fo.xsl";
	public static final String EXPORT_PDF_TEMP_FILE = "export_temp.pdf";

	public static final char EXPORT_CSV_COLUMN_SEPARATOR = ',';
	public static final char EXPORT_CSV_ROW_SEPARATOR = '\n';

	// UI
	public static final String SUCCES_SAVE_DATA = "Guardado completado";
	public static final String ERROR_SAVE_DATA = "Error al guardar los datos";

	public static final String ACTION_BUTTON_SAVE_TITLE = "Guardar";
	public static final String ACTION_BUTTON_CANCEL_TITLE = "Cancelar";
	public static final String ACTION_BUTTON_CONTINUE_TITLE = "Continuar";
	public static final String ERROR_WINDOW_CREATION = "Error en la creación de la ventana";

	public static final String SUCCES_DELETE = "Borrado completado";
	public static final String ERROR_DELETE = "Error al borrar";
	public static final String ACTION_BUTTON_ACCEPT_TITLE = "Aceptar";

	public static final Integer[] ANUAL_CONFIGURATION_YEAR_OPTIONS = new Integer[] {
			2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021,
			2022, 2023, 2024, 2025, 2026, 2027, 2028, 2029, 2030, 2031, 2032,
			2033, 2034, 2035 };
}
