package com.bailbots.tg.Framework.bpp;

import com.bailbots.tg.Framework.bpp.container.BotApiMethodContainer;
import org.telegram.telegrambots.meta.api.objects.Update;

public class BotSelectHandle {
    private static BotApiMethodContainer container = BotApiMethodContainer.getInstanse();

    public static void processByUpdate(Update update) {
        String path;
        BotApiMethodController controller = null;

        if (update.hasMessage() && update.getMessage().hasText()) {
            path = update.getMessage().getText();//.split("")[0].trim();
            controller = container.getControllerMap().get(path);
            if (controller == null) controller = container.getControllerMap().get("");
        }
        else if (update.hasCallbackQuery()) {
            if(!update.getCallbackQuery().getData().equals("none")) {
                String botControllerValue = update.getCallbackQuery().getData().split("/")[0].trim();
                String requestMappingValue = update.getCallbackQuery().getData().split("/")[1].trim();

                path = botControllerValue + requestMappingValue;

                controller = container.getControllerMap().get(path);
            }
        }

        if(controller == null) {
            return;
        }

        controller.process(update);
    }
}
