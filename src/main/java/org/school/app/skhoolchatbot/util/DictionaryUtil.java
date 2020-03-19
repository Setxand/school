package org.school.app.skhoolchatbot.util;

import org.school.app.skhoolchatbot.config.DictionaryKeysConfig;

import java.util.ResourceBundle;

public class DictionaryUtil {

	public static String getDictionaryValue(DictionaryKeysConfig key) {
		return ResourceBundle.getBundle("dictionary").getString(key.name());
	}

	public static String getDictionaryValueWithParams(DictionaryKeysConfig key, String... strings) {
		return String.format(ResourceBundle.getBundle("dictionary").getString(key.name()), strings);
	}

}
