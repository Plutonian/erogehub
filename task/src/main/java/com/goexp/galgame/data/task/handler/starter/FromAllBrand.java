package com.goexp.galgame.data.task.handler.starter;

import com.goexp.galgame.data.db.query.mongdb.BrandQuery;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FromAllBrand extends DefaultStarter<Integer> {

    private final Logger logger = LoggerFactory.getLogger(FromAllBrand.class);

    @Override
    public void process(BlockingQueue<Message> msgQueue) {
        BrandQuery.tlp.query()
                .list()
                .forEach(brand -> {
                    try {
                        msgQueue.offer(new Message(99, brand.id), 60, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });

        logger.info("All Done!!!");
    }

}
