package Categories;

import inlineMessages.BasicAnsw;
import inlineMessages.States;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoriesNotInOffice {


    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();


    Map<String,String> structure = Arrays.stream(new Object[][]{
            {"Качество услуг отделения Сбербанка", "setCatVSP"},
            {"Продукты Сбербанка", "setCatProducts"},
            {"Другое", "setCatAnother"},
    }).collect(Collectors.toMap(kv -> (String) kv[0], kv -> (String) kv[1]));


    public CategoriesNotInOffice(){
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
