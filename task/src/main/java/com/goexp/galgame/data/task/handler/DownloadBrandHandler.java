package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.client.GetChu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class DownloadBrandHandler extends DefaultMessageHandler<Integer> {

    private final Logger logger = LoggerFactory.getLogger(DownloadBrandHandler.class);

    @Override
    public void process(Message<Integer> message, BlockingQueue<Message> msgQueue) {

        var id = message.entity;

        try {

            logger.info("Down:{}", id);
            GetChu.BrandService.download(id);

            msgQueue.offer(new Message(MesType.Brand, id), 60, TimeUnit.SECONDS);
        } catch (Exception e) {

            try {
                msgQueue.offer(new Message(99, id), 60, TimeUnit.SECONDS);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
