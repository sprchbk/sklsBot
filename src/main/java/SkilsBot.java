import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class SkilsBot extends AbilityBot {
    private static String BOT_USER = "@username" /* proxy user */;
    private static String BOT_PASSWORD = "pass" /* proxy password */;

    protected SkilsBot(DefaultBotOptions botOptions) {
        super(BOT_USER, BOT_PASSWORD, botOptions);
    }

    public int creatorId() {
        return 0;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().isCommand())
        {
            System.out.println(update.getMessage().getText());
        }
        if(update.hasMessage() && update.getMessage().hasLocation())
        {
            Location loc = update.getMessage().getLocation();
            System.out.println(loc.toString());
        }
        if(update.hasMessage() && update.getMessage().hasContact())
        {
            Contact contact = update.getMessage().getContact();
            System.out.println(contact.toString());
            System.out.println(update.getMessage().getChatId().toString());
            sendContactToAdmin(contact);
        }

        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println(update.getMessage().toString());

            KeyboardButton kbL = new KeyboardButton();
            kbL.setRequestLocation(true);
            kbL.setText("Location");


            KeyboardButton kbPh = new KeyboardButton();
            kbPh.setRequestContact(true);
            kbPh.setText("Phone");

            KeyboardButton kbCtg = new KeyboardButton();
            kbCtg.setText("Categories");


            List<KeyboardButton> keybuttons = new ArrayList<KeyboardButton>();
            keybuttons.add(kbL);
            keybuttons.add(kbPh);
            keybuttons.add(kbCtg);
            keybuttons.add(kbCtg);

            //INLINE
            KeyboardRow kr = new KeyboardRow();
            kr.addAll(keybuttons);

            List<KeyboardRow> kl = new ArrayList<KeyboardRow>();
            kl.add(kr);

            ReplyKeyboardMarkup rk = new ReplyKeyboardMarkup();
            rk.setKeyboard(kl);
            rk.setKeyboard(kl);

/*
            InlineKeyboardButton ikbL = new InlineKeyboardButton();
            ikbL.setText("Оставить жалобу").setCallbackData("zalobaDetect");
            InlineKeyboardButton ikbL2 = new InlineKeyboardButton();
            ikbL.setText("Сберпомощ").setCallbackData("sberHelp");



            List<InlineKeyboardButton> ikeybuttons1 = new ArrayList<>();
            ikeybuttons1.add(ikbL);
            List<InlineKeyboardButton> ikeybuttons2 = new ArrayList<>();
            ikeybuttons2.add(ikbL2);



            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            rowsInline.add(ikeybuttons1);
            rowsInline.add(ikeybuttons2);

            InlineKeyboardMarkup ikM = new InlineKeyboardMarkup();
            ikM.setKeyboard(rowsInline);

*/
            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
            List<InlineKeyboardButton> rowInline = new ArrayList<>();
            rowInline.add(new InlineKeyboardButton().setText("Update message text").setCallbackData("update_msg_text"));
            // Set the keyboard to the markup
            rowsInline.add(rowInline);
            // Add it to the message
            markupInline.setKeyboard(rowsInline);


            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText("Now I need location");
            //.setReplyMarkup(ikM);
            //.setReplyMarkup(rk);
            //.setReplyMarkup(ikeybuttons);

            message.setReplyMarkup(markupInline);

            try {
                execute(message); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            String call_data = update.getCallbackQuery().getData();
            long message_id = update.getCallbackQuery().getMessage().getMessageId();
            long chat_id = update.getCallbackQuery().getMessage().getChatId();

            if (call_data.equals("update_msg_text")) {
                String answer = "Updated message text";
                EditMessageText new_message = new EditMessageText()
                        .setChatId(chat_id)
                        .setMessageId(Math.toIntExact(message_id))
                        .setText(answer);
                try {
                    execute(new_message);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendContactToAdmin(Contact cnt) {
        Long admId = new Long(222966961);
        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(admId)
                .setText("We have an question from user");
        SendContact sndCntc = new SendContact()
                .setChatId(admId)
                .setPhoneNumber(cnt.getPhoneNumber())
                .setFirstName(cnt.getFirstName())
                .setLastName(cnt.getLastName());
        try {
            execute(message);
            execute(sndCntc);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }


    @Override
    public String getBotUsername() {
        return BOT_USER;
    }

    @Override
    public String getBotToken() {
        return BOT_PASSWORD;
    }

}
