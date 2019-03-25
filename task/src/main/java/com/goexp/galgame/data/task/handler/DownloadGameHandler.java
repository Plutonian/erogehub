package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.client.GetChu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class DownloadGameHandler extends DefaultMessageHandler<Integer> {

    final private Logger logger = LoggerFactory.getLogger(DownloadGameHandler.class);


    @Override
    public void process(final Message<Integer> message, MessageQueueProxy<Message> msgQueue) {

        var gid = message.entity;
        logger.debug("Download {}", gid);


        try {
            GetChu.GameService.download(gid);

        } catch (IOException | InterruptedException e) {
            logger.error("Game:{}", gid);

            msgQueue.offer(new Message<>(MesType.NEED_DOWN_GAME, gid));
        }

        msgQueue.offer(new Message<>(MesType.Game, gid));
    }


    //    @Override
//    public ExecutorService getWorkerPool() {
//        return Executors.newFixedThreadPool(1);
//    }
}
