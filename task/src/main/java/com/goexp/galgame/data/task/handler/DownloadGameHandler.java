package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.client.GetChu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownloadGameHandler extends DefaultMessageHandler<Integer> {

    final private Logger logger = LoggerFactory.getLogger(DownloadGameHandler.class);


    @Override
    public void process(final Message<Integer> message, MessageQueueProxy<Message> msgQueue) {

        var gid = message.entity;
        logger.debug("Download {}", gid);


        try {
            GetChu.GameService.download(gid);
        } catch (Exception e) {
            logger.error("Re-down:{}", gid);
            msgQueue.offer(new Message<>(MesType.NEED_DOWN_GAME, gid));
            e.printStackTrace();
        }

        msgQueue.offer(new Message<>(MesType.Game, gid));

    }

}
