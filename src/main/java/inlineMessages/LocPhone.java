package inlineMessages;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class LocPhone {
   public ReplyKeyboardMarkup getInfoButtonGroup() {
       KeyboardButton kbL = new KeyboardButton();
       kbL.setRequestLocation(true);
       kbL.setText("Я здесь");

       KeyboardButton kbPh = new KeyboardButton();
       kbPh.setRequestContact(true);
       kbPh.setText("Мой номер");

       List<KeyboardButton> keybuttons = new ArrayList<KeyboardButton>();
       keybuttons.add(kbL);
       keybuttons.add(kbPh);

       KeyboardRow kr = new KeyboardRow();
       kr.addAll(keybuttons);

       List<KeyboardRow> kl = new ArrayList<KeyboardRow>();
       kl.add(kr);

       ReplyKeyboardMarkup rk = new ReplyKeyboardMarkup();
       rk.setKeyboard(kl);

       return rk;
   }
}
