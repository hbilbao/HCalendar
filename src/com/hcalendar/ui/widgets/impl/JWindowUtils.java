package com.hcalendar.ui.widgets.impl;

import java.awt.Component;

import javax.swing.JOptionPane;

public class JWindowUtils {

	public static void showSuccesPanel(Component component, String string) {
		JOptionPane.showMessageDialog(component, string, "Información", JOptionPane.INFORMATION_MESSAGE);
	}

	public static void showErrorPanel(Component component, String string) {
		JOptionPane.showMessageDialog(component, string, "Error", JOptionPane.ERROR_MESSAGE);
	}

	public static int showOptionPanel(Component component, String message, Object[] options) {
		return JOptionPane.showOptionDialog(component, message, "Seleccione una opción", 0,
				JOptionPane.INFORMATION_MESSAGE, null, options, null);
	}
}
