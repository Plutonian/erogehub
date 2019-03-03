package com.goexp.galgame.data.task.handler.game;

import com.goexp.common.util.WebUtil;
import com.goexp.galgame.data.task.handler.MesType;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.client.GetChu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Bytes2Html extends DefaultMessageHandler<Map.Entry<Integer, byte[]>> {

    final private Logger logger = LoggerFactory.getLogger(Bytes2Html.class);


    @Override
    public void process(final Message<Map.Entry<Integer, byte[]>> message, BlockingQueue<Message> msgQueue) {
        var entry = message.entity;

        logger.debug("<Bytes2Html> {}", entry.getKey());

        var html = Map.entry(entry.getKey(),
                Objects.requireNonNull(
                        WebUtil.decodeGzip(
                                entry.getValue()
                                , GetChu.DEFAULT_CHARSET)
                )
        );


        try {
            msgQueue.offer(new Message<>(MesType.ContentHtml, html), 60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
