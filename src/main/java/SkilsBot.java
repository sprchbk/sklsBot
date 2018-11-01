import inlineMessages.BasicAnsw;
import inlineMessages.States;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

public class SkilsBot extends AbilityBot {

    private static String BOT_USER = "@sbrbnk_skilsbot" /* proxy user */;
    private static String BOT_PASSWORD = "699831604:AAEcQZc5CPT8wFI02vrj9NNiIptVlIOzu2g" /* proxy password */;

    protected SkilsBot(DefaultBotOptions botOptions) {
        super(BOT_USER, BOT_PASSWORD, botOptions);
    }

    public int creatorId() {
        return 0;
    }

    @Override
    public void onUpdateReceived(Update update) {
        States state = null;
        String name = "";

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
            SendMessage message = null;
            if(update.getMessage().getText().equalsIgnoreCase("/start"))
            {
                 message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getMessage().getChatId())
                        .setText("Добрый день!\n" +
                                "\n" +
                                "Как я могу к Вам обращаться?");
                 state = States.getName;
            }
            else {
                if(state!=null && state.equals(States.getName)) {
                    name = update.getMessage().getText();

                    message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setText(name + ",вы находитесь в отделении Сбербанка?");

                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();

                    rowInline.add(new BasicAnsw().getButtonYes().setCallbackData("clientInOffice"));
                    rowInline.add(new BasicAnsw().getButtonNo().setCallbackData("clientNotInOffice"));

                    message.setReplyMarkup(markupInline.setKeyboard(rowsInline));
                }

            }

            try {
                if(message!=null)
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
