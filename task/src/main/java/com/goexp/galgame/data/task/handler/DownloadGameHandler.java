package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.client.GetChu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class DownloadGameHandler extends DefaultMessageHandler<Integer> {

    final private Logger logger = LoggerFactory.getLogger(DownloadGameHandler.class);


    @Override
    public void process(final Message<Integer> message, BlockingQueue<Message> msgQueue) {

        var gid = message.entity;
        logger.debug("Download {}", gid);


        try {
            GetChu.GameService.download(gid);

        } catch (IOException | InterruptedException e) {
            logger.error("Game:{}", gid);

            try {
                msgQueue.offer(new Message<>(MesType.NEED_DOWN_GAME, gid), 60, TimeUnit.SECONDS);
            } catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }


        try {
            msgQueue.offer(new Message<>(MesType.Game, gid), 60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    //    @Override
//    public ExecutorService getWorkerPool() {
//        return Executors.newFixedThreadPool(1);
//    }
}
