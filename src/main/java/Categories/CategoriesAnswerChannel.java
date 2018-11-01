package Categories;

import inlineMessages.BasicAnsw;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoriesAnswerChannel {
    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();


    Map<String,String> structure = Arrays.stream(new Object[][]{
            {"E-mail", "setMail"},
            {"Телефон", "setPhone"},
            {"Telegram", "setContact"},
    }).collect(Collectors.toMap(kv -> (String) kv[0], kv -> (String) kv[1]));

    public CategoriesAnswerChannel(){
        structure.forEach((key, value) -> {
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new BasicAnsw().getButtonCustom(key,value));
            rowsInline.add(rowInline);
        });
        // Set the keyboard to the markup

        // Add it to the message
        markupInline.setKeyboard(rowsInline);
    }

    public InlineKeyboardMarkup getMarkupInline() {
        return markupInline;
    }
}
