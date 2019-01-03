package org.school.telegram.button;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.school.telegram.Markup;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class InlineKeyboardMarkup implements Markup {
	@JsonProperty("inline_keyboard")
	private List<List<InlineKeyboardButton>> inlineKeyBoard;

	public InlineKeyboardMarkup(List<List<InlineKeyboardButton>> inlineKeyBoard) {
		this.inlineKeyBoard = inlineKeyBoard;
	}

}
