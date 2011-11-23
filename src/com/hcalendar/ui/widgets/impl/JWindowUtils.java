package com.hcalendar.ui.widgets.impl;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Window utils. Show various modal windows
 * */
public class JWindowUtils {

	private static final String INFO_MESSAGE = "Información";
	private static final String ERROR_MESSAGE = "Error";
	private static final String SELECT_OPTION_MESSAGE = "Seleccione una opción";

	/**
	 * Show modal success panel
	 * 
	 * @param component
	 *            component on which show modal window
	 * @param message
	 *            Message to show
	 * */
	public static void showSuccesPanel(Component component, String message) {
		JOptionPane.showMessageDialog(component, message, INFO_MESSAGE,
				JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Show modal error panel
	 * 
	 * @param component
	 *            component on which show modal window
	 * @param message
	 *            Message to show
	 * */
	public static void showErrorPanel(Component component, String message) {
		JOptionPane.showMessageDialog(component, message, ERROR_MESSAGE,
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Show modal option panel
	 * 
	 * @param component
	 *            component on which show modal window
	 * @param message
	 *            Message to show
	 * @param options
	 *            options to show
	 * */
	public static int showOptionPanel(Component component, String message,
			Object[] options) {
		return JOptionPane.showOptionDialog(component, message,
				SELECT_OPTION_MESSAGE, 0, JOptionPane.INFORMATION_MESSAGE,
				null, options, null);
	}
}
