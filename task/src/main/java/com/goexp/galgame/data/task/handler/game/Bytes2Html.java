package com.goexp.galgame.data.task.handler.game;

import com.goexp.common.util.WebUtil;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.client.GetChu;
import com.goexp.galgame.data.task.handler.MesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

public class Bytes2Html extends DefaultMessageHandler<Map.Entry<Integer, byte[]>> {

    final private Logger logger = LoggerFactory.getLogger(Bytes2Html.class);


    @Override
    public void process(final Message<Map.Entry<Integer, byte[]>> message, MessageQueueProxy<Message> msgQueue) {
        var entry = message.entity;

        logger.debug("<Bytes2Html> {}", entry.getKey());

        var html = Map.entry(entry.getKey(),
                Objects.requireNonNull(
                        WebUtil.decodeGzip(
                                entry.getValue()
                                , GetChu.DEFAULT_CHARSET)
                )
        );


        msgQueue.offer(new Message<>(MesType.ContentHtml, html));
    }

}
