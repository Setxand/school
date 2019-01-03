package org.school.telegram.button;


import lombok.Data;
import org.school.telegram.Markup;

import java.util.List;

@Data
public class KeyboardMarkup implements Markup {
	private List<List<KeyboardButton>> keyboard;

	public KeyboardMarkup(List<List<KeyboardButton>> keyboard) {
		this.keyboard = keyboard;
	}
}

