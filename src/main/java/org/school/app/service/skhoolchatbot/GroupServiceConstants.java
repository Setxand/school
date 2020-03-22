package org.school.app.service.skhoolchatbot;

import org.school.app.config.DictionaryKeysConfig;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import telegram.Message;

import static org.school.app.utils.DictionaryUtil.getDictionaryValue;

public interface GroupServiceConstants {

	static final Pageable PAGEABLE = new PageRequest(0, 4);
	static final String INVALID_UGROUP_ID = "Invalid User group ID";


	default String getTextMessage(DictionaryKeysConfig key, Message message) {
		return getDictionaryValue(key, message.getFrom().getLanguageCode());
	}

}
