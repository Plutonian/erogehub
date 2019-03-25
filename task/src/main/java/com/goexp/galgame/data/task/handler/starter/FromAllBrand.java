package com.goexp.galgame.data.task.handler.starter;

import com.goexp.galgame.data.db.query.mongdb.BrandQuery;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FromAllBrand extends DefaultStarter<Integer> {

    private final Logger logger = LoggerFactory.getLogger(FromAllBrand.class);

    @Override
    public void process(MessageQueueProxy<Message> msgQueue) {
        BrandQuery.tlp.query()
                .list()
                .forEach(brand -> {
                    msgQueue.offer(new Message<>(99, brand.id));
                });

        logger.info("All Done!!!");
    }

}
