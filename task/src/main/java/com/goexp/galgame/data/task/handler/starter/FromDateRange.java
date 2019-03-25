package com.goexp.galgame.data.task.handler.starter;

import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import com.goexp.galgame.data.task.client.GetChu;
import com.goexp.galgame.data.task.handler.MesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class FromDateRange extends DefaultStarter<Integer> {

    private final Logger logger = LoggerFactory.getLogger(FromDateRange.class);

    private final LocalDate start;

    private final LocalDate end;

    public FromDateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }


    @Override
    public void process(MessageQueueProxy<Message> msgQueue) {

        logger.info("Start:" + start + ",End:" + end);

        var list = GetChu.BrandService.gamesFrom(start, end);

        logger.info("{}", list.size());

        list.forEach(game -> {
            msgQueue.offer(new Message<>(MesType.PRE_GAME, game));
        });

    }

}
