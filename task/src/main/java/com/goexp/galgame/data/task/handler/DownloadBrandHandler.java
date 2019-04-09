package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.client.GetChu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadBrandHandler extends DefaultMessageHandler<Integer> {

    private final Logger logger = LoggerFactory.getLogger(DownloadBrandHandler.class);

    @Override
    public void process(Message<Integer> message, MessageQueueProxy<Message> msgQueue) {

        var id = message.entity;
        logger.info("Down:{}", id);

        try {
            GetChu.BrandService.download(id);
        } catch (Exception e) {
            logger.error("ReDown:{}", id);
            msgQueue.offer(new Message<>(99, id));
            e.printStackTrace();
        }

        msgQueue.offer(new Message<>(MesType.Brand, id));
    }
}
