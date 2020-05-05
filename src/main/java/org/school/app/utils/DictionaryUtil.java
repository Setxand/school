package org.school.app.utils;

import org.school.app.config.DictionaryKeysConfig;
import org.school.app.model.TestProcess;

import java.util.Locale;
import java.util.ResourceBundle;

public class DictionaryUtil {

	private static final Locale localeToGetMessage = new Locale("ua");
	private static final Locale enLocale = new Locale("en");

	public static String getDictionaryValue(DictionaryKeysConfig key, String languageCode) {
		boolean lanCondition = languageCode != null && languageCode.equals(enLocale.getCountry());

		return ResourceBundle.getBundle("dictionary1", lanCondition ? enLocale :
				localeToGetMessage).getString(key.name());
	}

	public static String getDictionaryValue(DictionaryKeysConfig key) {
		return ResourceBundle.getBundle("dictionary1", localeToGetMessage).getString(key.name());
	}

	public static String getDictionaryValueWithParams(
			DictionaryKeysConfig key, String languageCode, String... strings) {
		return String.format(getDictionaryValue(key, languageCode), strings);
	}

	public static String createTestConclusion(TestProcess testProcess, String userName, String languageCode) {
		String messasge = String.format(DictionaryUtil.getDictionaryValue(
				DictionaryKeysConfig.TEST_CONCLUSION, languageCode),
				userName, testProcess.getMark(), testProcess.getCreationTime(), testProcess.getEndTime());
		return messasge;
	}
}
