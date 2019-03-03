package com.goexp.galgame.data.task.handler.starter;

import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import com.goexp.galgame.data.task.client.GetChu;
import com.goexp.galgame.data.task.handler.MesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FromDateRange extends DefaultStarter<Integer> {

    private final Logger logger = LoggerFactory.getLogger(FromDateRange.class);

    private final LocalDate start;

    private final LocalDate end;

    public FromDateRange(LocalDate start, LocalDate end) {
        this.start = start;
        this.end = end;
    }


    @Override
    public void process(BlockingQueue<Message> msgQueue) {


//        for (var i = 2015; i < 2019; i++) {

//            System.out.println("Year:" + i);


//        var start = LocalDate.now().withMonth(3).withDayOfMonth(1);
//        var end = LocalDate.now().withMonth(12).withDayOfMonth(1);

//        var start = LocalDate.of(2007, 1, 1);
//        var end = LocalDate.of(2007, 12, 31);

        logger.info("Start:" + start + ",End:" + end);

        var list = GetChu.BrandService.gamesFrom(start, end);

        logger.info("{}", list.size());

        list.forEach(game -> {
            try {
                msgQueue.offer(new Message<>(MesType.PRE_GAME, game), 60, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
//        }


    }

}
