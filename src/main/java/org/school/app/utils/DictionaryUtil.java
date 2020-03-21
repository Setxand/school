package org.school.app.utils;

import org.school.app.config.DictionaryKeysConfig;
import org.school.app.model.TestProcess;

import java.util.ResourceBundle;

public class DictionaryUtil {

	public static String getDictionaryValue(DictionaryKeysConfig key) {
		return ResourceBundle.getBundle("dictionary").getString(key.name());
	}

	public static String getDictionaryValueWithParams(DictionaryKeysConfig key, String... strings) {
		return String.format(ResourceBundle.getBundle("dictionary").getString(key.name()), strings);
	}

	public static String createTestConclusion(TestProcess testProcess, String userName) {
		String messasge = String.format(DictionaryUtil.getDictionaryValue(
				DictionaryKeysConfig.TEST_CONCLUSION),
				userName, testProcess.getMark(), testProcess.getCreationTime(), testProcess.getEndTime());
		return messasge;
	}
}
