
import afishaParsers.movie.MovieParser;
import afishaParsers.teatre.TheatreParser;
import birthdayReminder.BirthdayReminder;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;


public class Bot extends TelegramLongPollingBot{
    private final static String TOKEN = "";

    public Bot(){}

    public Bot(DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if(message != null && message.hasText()){
            if(message.getText().equals("/start")){
                sendMsg(message, "Приветсвую тебя, я бот сообщества Алатырь!", makeKeyboard());
                //sendMsg(message, "hz", keyboardMarkup);
            }
            else if(message.getText().equals("/weather") || message.getText().equals("⛅ Погода")
                    || message.getText().equals("/weather@AlatirsBot")){
                String weather = WeatherParser.parseCurrentWeatherJson();
                sendMsg(message, weather);
            }
            else if(message.getText().equals("\uD83E\uDD14 Инфо")){
                sendMsg(message, "Создал и разработал @Veniks версия 0.2.2\n" +
                        "- Теперь бот показывает театральную афишу\n" +
                        "" +
                        "" +
                        "");
            }
            else if(message.getText().equals("/help")){
                //ReplyKeyboardMarkup keyboardMarkup = makeKeyboard();
                //sendMsg(message, "test", keyboardMarkup);
            }
            else if(message.getText().equals("/др") || message.getText().equals("\uD83D\uDCC5 Ближайшее ДР")){
                sendMsg(message, BirthdayReminder.getBirthday());
            }
            else if(message.getText().equals("\uD83D\uDCB1 Курс валют")){
                sendMsg(message, ExchangeRatesParser.parseExchangeRates());
            }
            else if(message.getText().equals("\uD83D\uDCC3 Афиша")){
                InlineKeyboardMarkup markup = makeChoiceKeyboard();
                sendMsg(message, "Театр или Кино?", markup);
            }

            else if(message.getText().equals("\uD83D\uDCFD Сегодня в Кино")){
                InlineKeyboardMarkup markup = makeMovieInlineKeyboard();
                sendMsg(message, "Пожайлуста выберите кинотеатр", markup);
            }
            else if(message.getText().equals("\uD83D\uDCF0 Новости")){
                sendMsg(message, NewsRSSReader.readRSS());
            }
            else {
                sendMsg(message, "-");
            }
        }
        else if(update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            switch (data) {
                case "Teatr":
                    sendCallbackMessage(callbackQuery, TheatreParser.getTheatreAfisha());
                    break;
                case "Kino":
                    InlineKeyboardMarkup markup = makeMovieInlineKeyboard();
                    sendCallbackMessage(callbackQuery, "Пожайлуста выберите кинотеатр", markup);
                    break;
                default:
                    String answer = MovieParser.parseMovieShow(data);
                    sendCallbackMessage(callbackQuery, answer);
                    break;
            }
                //всплываещее уведомление
                /*AnswerCallbackQuery answer = new AnswerCallbackQuery();
                answer.setCallbackQueryId(callbackQuery.getId());
                answer.setText("один два три");
                //answer.setShowAlert(true);
                try {
                    answerCallbackQuery(answer);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }*/
            }
    }

    private void sendMsg(Message message, String string) {
        SendMessage sendMessage = new SendMessage();

        //sendMessage.setReplyMarkup(makeKeyboard());

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(string);

        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Message message, String string, ReplyKeyboard keyboardMarkup) {
        SendMessage sendMessage = new SendMessage();

        sendMessage.setReplyMarkup(keyboardMarkup);

        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(string);

        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendCallbackMessage(CallbackQuery callbackQuery, String string){
        long message_id = callbackQuery.getMessage().getMessageId();
        long chat_id = callbackQuery.getMessage().getChatId();
        EditMessageText new_message = new EditMessageText().
                    setChatId(chat_id)
                    .setMessageId(toIntExact(message_id))
                    .setText(string);
        try {
            execute(new_message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendCallbackMessage(CallbackQuery callbackQuery, String string, InlineKeyboardMarkup keyboardMarkup){
        long message_id = callbackQuery.getMessage().getMessageId();
        long chat_id = callbackQuery.getMessage().getChatId();
        EditMessageText new_message = new_message = new EditMessageText()
                .setChatId(chat_id)
                .setMessageId(toIntExact(message_id))
                .setText(string)
                .setReplyMarkup(keyboardMarkup);
        try {
            execute(new_message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static ReplyKeyboardMarkup makeKeyboard(){
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        // Create a keyboard
        List<KeyboardRow> keyboard = new ArrayList<>();
        // Create a keyboard row
        KeyboardRow row = new KeyboardRow();
        // Set each button, you can also use KeyboardButton objects if you need something else than text
        row.add("⛅ Погода");
        row.add("\uD83D\uDCC5 Ближайшее ДР");
        row.add("\uD83D\uDCB1 Курс валют");
        // Set the keyboard to the markup
        keyboard.add(row);
        row = new KeyboardRow();
        // Set each button for the second line
        row.add("\uD83D\uDCF0 Новости");
        row.add("\uD83D\uDCC3 Афиша");
        row.add("\uD83E\uDD14 Инфо");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        //keyboardMarkup.setOneTimeKeyboard(true);
        return keyboardMarkup;
    }

    private static InlineKeyboardMarkup makeMovieInlineKeyboard(){
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();
        List<InlineKeyboardButton> row4 = new ArrayList<>();
        List<InlineKeyboardButton> row5 = new ArrayList<>();
        List<InlineKeyboardButton> row6 = new ArrayList<>();
        List<InlineKeyboardButton> row7 = new ArrayList<>();
        List<InlineKeyboardButton> row8 = new ArrayList<>();

        row1.add(new InlineKeyboardButton().setText("Тетерин Фильм").setCallbackData("Кинотеатр \"Тетерин Фильм\""));
        row2.add(new InlineKeyboardButton().setText("ДК Химик").setCallbackData("Кинозал в ДК \"Химик\""));
        row3.add(new InlineKeyboardButton().setText("Синема 5").setCallbackData("Кинотеатр \"Синема 5\""));
        row4.add(new InlineKeyboardButton().setText("Три пингвина").setCallbackData("Кинотеатр \"Три пингвина\""));
        row5.add(new InlineKeyboardButton().setText("Волжский").setCallbackData("Кинотеатр \"Волжский\""));
        row6.add(new InlineKeyboardButton().setText("Мир Луксор").setCallbackData("Кинотеатр \"Мир Луксор\""));
        row7.add(new InlineKeyboardButton().setText("Киногалактика").setCallbackData("Кинотеатр \"Киногалактика\""));
        row8.add(new InlineKeyboardButton().setText("Сеспель").setCallbackData("Кинозал \"Сеспель\""));

        buttons.add(row1);
        buttons.add(row2);
        buttons.add(row3);
        buttons.add(row4);
        buttons.add(row5);
        buttons.add(row6);
        buttons.add(row7);
        buttons.add(row8);

        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    private static InlineKeyboardMarkup makeChoiceKeyboard(){
        InlineKeyboardMarkup markupKeyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();

        row1.add(new InlineKeyboardButton().setText("\uD83C\uDFAD Театральная афиша").setCallbackData("Teatr"));
        row2.add(new InlineKeyboardButton().setText("\uD83D\uDCFD Киноафиша").setCallbackData("Kino"));
        buttons.add(row1);
        buttons.add(row2);

        markupKeyboard.setKeyboard(buttons);
        return markupKeyboard;
    }

    @Override
    public String getBotUsername() {
        return "AlatirsBot";
    }

    @Override
    public String getBotToken() {
        return TOKEN;
    }
}
