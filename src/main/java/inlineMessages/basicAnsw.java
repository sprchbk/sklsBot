package inlineMessages;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class basicAnsw {
    public InlineKeyboardButton getButtonYes()
    {
       return new InlineKeyboardButton().setText("Да");
    }

    public InlineKeyboardButton getButtonNo()
    {
       return new InlineKeyboardButton().setText("Нет");
    }
}
