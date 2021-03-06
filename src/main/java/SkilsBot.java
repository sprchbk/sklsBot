import Categories.CategoriesAnswerChannel;
import Categories.CategoriesNotInOffice;
import Categories.FeedBack;
import callBacks.Clbks;
import inlineMessages.BasicAnsw;
import inlineMessages.States;
import model.Vsp;
import modelApi.VspApi;
import org.telegram.abilitybots.api.bot.AbilityBot;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendContact;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Contact;
import org.telegram.telegrambots.meta.api.objects.Location;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SkilsBot extends AbilityBot {
//    private static String BOT_USER = "@username" /* proxy user */;
//    private static String BOT_PASSWORD = "pass" /* proxy password */;

    private static String BOT_USER = "@sbrbnk_skilsbot";
    private static String BOT_PASSWORD = "699831604:AAEcQZc5CPT8wFI02vrj9NNiIptVlIOzu2g";

    States state = States.getName;
    String name = "";
    String mail = "";
    String cat = "";
    VspApi vspApi = new VspApi();
    String userMessage = "";
    Boolean inOffice = false;
    Long chatId = null;

    Long messId = null;

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
/*
            DeleteMessage dM = new DeleteMessage()
                    .setChatId(update.getMessage().getChatId())
                    .setMessageId(update.getMessage().getMessageId());
            try {
                execute(dM);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
*/
            Contact contact = update.getMessage().getContact();
            System.out.println(contact.toString());
            sendContactToAdmin(contact);


            String messageText = "Ваше обращение отправлено.\n" +
                    "В ближайшее время с Вами свяжется наш сотрудник.";
            if(!inOffice)
                messageText = "Ваше обращение отправлено.\n" +
                        "Наш сотрудник свяжется с Вами в ближайшее время.";


            SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                    .setChatId(update.getMessage().getChatId())
                    .setText(messageText);

            sendMessW(message);
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
                chatId = update.getMessage().getChatId();
            }
            else {
                if(state.equals(States.getName)) {
                    name = update.getMessage().getText();

                    message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setText(name + ", вы находитесь в отделении Сбербанка?");

                    InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
                    List<InlineKeyboardButton> rowInline = new ArrayList<>();
                    rowInline.add(new BasicAnsw().getButtonYes().setCallbackData("setStatesInOffices"));
                    rowInline.add(new BasicAnsw().getButtonNo().setCallbackData("setStatesNotInOffices"));
                    // Set the keyboard to the markup
                    rowsInline.add(rowInline);
                    // Add it to the message
                    markupInline.setKeyboard(rowsInline);
                    message.setReplyMarkup(markupInline);

                }
                else if(state.equals(States.getOfice)) {
                    String officeNum = update.getMessage().getText();
                    Vsp office = vspApi.getVspById(officeNum);
                    if (office == null) {
                        message = new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText("Нет данных о подразделении № "+officeNum+"\n"+
                                        "Выберите интересующий вас раздел")
                                .setReplyMarkup(new CategoriesNotInOffice().getMarkupInline());
                        state = States.notinOffice;
                    } else {
                        message = new SendMessage()
                                .setChatId(update.getMessage().getChatId())
                                .setText("Чем я могу Вам помочь?");
                        state = States.setMess;
                    }
                }  else if(state == States.notinOffice)
                {
                    message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setText("Выберите интересующий вас раздел")
                            .setReplyMarkup(new CategoriesNotInOffice().getMarkupInline());

                   // sendMessW(message);
                    state = States.setCat;
                }else if(state == States.getMail) {
                    mail = update.getMessage().getText();
                    sendMailToAdmin();
                    String messageText = "Ваше обращение отправлено.\n" +
                            "В ближайшее время с Вами свяжется наш сотрудник.";
                    if(!inOffice)
                        messageText = "Ваше обращение отправлено.\n" +
                                "Наш сотрудник свяжется с Вами в ближайшее время.";

                    message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getMessage().getChatId())
                            .setText(messageText);

                }
                else if(state == States.setMess){
                    userMessage = update.getMessage().getText();



                    if(!inOffice) {
                        message = new SendMessage() // Create a SendMessage object with mandatory fields
                                .setChatId(update.getMessage().getChatId())
                                .setText("Выберите удобный для Вас способ связи")
                                .setReplyMarkup(new CategoriesAnswerChannel().getMarkupInline());

                        state = States.setCat;
                    }
                    else
                        state = States.setAnswChannel;
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
            String msgTxt= update.getCallbackQuery().getMessage().getText();
            Clbks callBack = new Clbks(call_data);
            System.out.println("call_data:"+call_data);
            if(call_data.equalsIgnoreCase("closeTicket"))
             System.out.println("txt:"+msgTxt);
            state =  callBack.getStt()==null?States.getName:callBack.getStt();

            if(state == States.notinOffice)
            {
                DeleteMessage dm = new DeleteMessage()
                        .setChatId(update.getCallbackQuery().getMessage().getChatId())
                        .setMessageId(Math.toIntExact(message_id));
                try{
                    execute(dm);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                //clearInlineButtons(chat_id,Math.toIntExact(message_id),"Вы не находитесь в офисе ");
                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getCallbackQuery().getMessage().getChatId())
                        .setText("Выберите интересующий вас раздел")
                        .setReplyMarkup(new CategoriesNotInOffice().getMarkupInline());

                sendMessW(message);

                state = States.setCat;
            }
            else if(state == States.setCat)
            {
                cat = callBack.getCat();
                clearInlineButtons(chat_id,Math.toIntExact(message_id),"Регистрируем сообщение по категории: " + cat);

                if ("Качество услуг отделения Сбербанка".equalsIgnoreCase(cat)) {
                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setText("Введите номер подразделения, в котором вы находитесь");
                    sendMessW(message);
                    state = States.getOfice;
                } else {
                    SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                            .setChatId(update.getCallbackQuery().getMessage().getChatId())
                            .setText("Чем я могу Вам помочь?");
                    sendMessW(message);
                    state = States.setMess;
                }
            }
            else if(state == States.inOffice)
            {
                clearInlineButtons(chat_id,Math.toIntExact(message_id),"Определяем номер подразделения Сбербанка");
                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(update.getCallbackQuery().getMessage().getChatId())
                        .setText("Введите номер подразделения, в котором вы находитесь");
                sendMessW(message);
                state = States.getOfice;
            }
            else if(state == States.setMess) {

            }
            else if(state == States.setAnswChannel) {
                if (callBack.getCat() != null) {
                    if (callBack.getCat().equalsIgnoreCase("setMail")) {
                        clearInlineButtons(chat_id,Math.toIntExact(message_id),"Введите адрес электронный почты");
                        state = States.getMail;
                    } else {
                        clearInlineButtons(chat_id,Math.toIntExact(message_id),"Способ связи: " + (callBack.getCat().equalsIgnoreCase("setPhone")?"Звонок сотрудника":"Telegram"));
                        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                                .setChatId(update.getCallbackQuery().getMessage().getChatId())
                                .setText("Нажмите 'Отправить номер телефона'")
                                .setReplyMarkup(getPhoneKeyEnd());
                        sendMessW(message);

                        state = (callBack.getCat().equalsIgnoreCase("setPhone")?States.finalPhone:States.finalChat);
                    }
                }
                else{
                    System.out.println("NullCat");
                }
            }else if(state == States.getFeedBack) {
                Long chId = chatId;

                SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                        .setChatId(chatId)
                        .setText("Уважаемый клиент!\n" +
                                "Оцените, пожалуйста, качество обслуживания по шкале от 1 до 5.\n" +
                                "Это улучшит нашу работу в дальнейшем")
                        .setReplyMarkup(new FeedBack().getMarkupInline());
                sendMessW(message);
                state = States.gotFeedBack;
            }else if(state == States.gotFeedBack) {
                clearInlineButtons(chat_id, Math.toIntExact(message_id), "Спасибо");
            }
        }
    }

    public void sendContactToAdmin(Contact cnt) {
        //Long admId = new Long(222966961);
        Long admId = new Long(351291881);
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(admId)
                .setText("Зарегистрировано новое обращение\n" +
                        "Клиент находится "+(inOffice?"вне офиса":"в отделении")+"\n" +
                        "Имя для обращения: " + name +"\n"+
                        //"System: " + chatId.toString() +":System\n"+
                        "Способ связи: " + (state == States.finalPhone?"Звонок":"Telegram") +"\n"+
                        "Категория: " + (inOffice?"Обращение внутри отделения": cat) +"\n"+
                        "Сообщение:\n" +
                        userMessage
                )
                .setReplyMarkup(getInlineCloseTicketButton());

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
    public void sendMailToAdmin() {
        //Long admId = new Long(222966961);
        Long admId = new Long(351291881);
        SendMessage message = new SendMessage() // Create a SendMessage object with mandatory fields
                .setChatId(admId)
                .setText("Зарегистрировано новое обращение\n" +
                        //"System: " + chatId.toString() +":System\n"+
                        "Клиент находится "+(inOffice?"вне офиса":"в отделении")+"\n" +
                        "Имя для обращения: " + name +"\n"+
                        "E-mail для ответа: " + mail +"\n"+
                        "Сообщение:\n" +
                        userMessage
                );

        sendMessW(message);
    }

    public void sendCategories(Long chatId){


    }

    public void sendMessW(SendMessage mes){
        try {
            if(mes!=null)
                execute(mes); // Call method to send the message
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void clearInlineButtons(Long chatId, Integer mesId, String changedText){
        EditMessageText new_message = new EditMessageText()
                .setChatId(chatId)
                .setMessageId(Math.toIntExact(mesId))
                .setText(changedText);
        try {
            execute(new_message);
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

    public InlineKeyboardMarkup getInlineCloseTicketButton(){
        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();
        rowInline.add(new BasicAnsw().getButtonCustom("Проблема решена","closeTicket"));
        // Set the keyboard to the markup
        rowsInline.add(rowInline);
        // Add it to the message
        markupInline.setKeyboard(rowsInline);
        return markupInline;
    }

    public ReplyKeyboardMarkup getPhoneKeyEnd(){
        KeyboardButton kbPh = new KeyboardButton();
        kbPh.setRequestContact(true);
        kbPh.setText("Отправить номер телефона");

        List<KeyboardButton> keybuttons = new ArrayList<KeyboardButton>();
        keybuttons.add(kbPh);

        KeyboardRow kr = new KeyboardRow();
        kr.addAll(keybuttons);

        List<KeyboardRow> kl = new ArrayList<KeyboardRow>();
        kl.add(kr);

        ReplyKeyboardMarkup kM = new ReplyKeyboardMarkup();

        kM.setKeyboard(kl);
        kM.setOneTimeKeyboard(true);

        return kM;
    }
}
