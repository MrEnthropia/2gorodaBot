package ru.dvagoroda.MrEntropy.gorodaBot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.dvagoroda.MrEntropy.gorodaBot.config.BotConfig;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;

    final String HELP_TEXT= """
                            Это демонстративная демо-версия бота для сайта 2goroda.ru
                            """;

    String sorry = "Sorry, i don't understand this command";

    public TelegramBot(BotConfig config) {
        this.config = config;
        List <BotCommand> listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand("/start","Начать работу с ботом"));
        listOfCommands.add(new BotCommand("/mydata","Вывести данные о пользователе"));
        listOfCommands.add(new BotCommand("/delete","Удалить данные о пользователе"));
        listOfCommands.add(new BotCommand("/help","Вывести подсказки по работе"));
        listOfCommands.add(new BotCommand("/settings","Вывести данные о пользователе"));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(),null));
        } catch (TelegramApiException e) {
            log.error("Error occurred:" + e.getMessage());
            System.out.println();
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText= update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            switch (messageText){
                case "/start": startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                break;
                case "/help": sendMessage(chatId,HELP_TEXT);

                default: sendMessage(chatId,sorry);
            }
        }
    }

    private void startCommandReceived(long chatId, String name){

        String answer = "Hi, " + name + ". Nice to meet you.";
        sendMessage(chatId,answer);
        log.info("Replied to user "+name);
        System.out.println("Replied to user "+name);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

       try {
           execute(message);
       }catch (TelegramApiException e) {
           log.error("Error occurred:" + e.getMessage());
       }


    }
}
