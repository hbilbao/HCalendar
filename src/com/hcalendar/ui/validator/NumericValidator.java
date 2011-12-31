package com.hcalendar.ui.validator;

import java.awt.Component;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

import com.hcalendar.ui.helper.ModalWindowUtils;

public class NumericValidator extends InputVerifier {

	private static String VALIDATION_ERROR_MESSAGE = "El valor tiene que ser numérico";

	private static String NUMERIC_REGEX = "((-|\\+)?[0-9]+(\\.[0-9]+)?)+";
	private static Pattern p = Pattern.compile(NUMERIC_REGEX);

	private Component component;
	private boolean isNullable;

	public NumericValidator(Component component, boolean isNullable) {
		this.component = component;
		this.isNullable = isNullable;
	}

	@Override
	public boolean verify(JComponent input) {
		JTextField textField = (JTextField) input;
		if (textField.getText() == null || textField.getText().equals(""))
			if (isNullable)
				return true;
			else
				return false;
		Matcher m = p.matcher(textField.getText());
		if (!m.matches())
			ModalWindowUtils
					.showErrorPanel(component, VALIDATION_ERROR_MESSAGE);
		return m.matches();
	}
}
