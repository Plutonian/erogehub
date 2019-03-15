package com.goexp.galgame.data.task.handler.starter;

import com.goexp.galgame.data.Config;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import com.goexp.galgame.data.task.client.GetChu;
import com.goexp.galgame.data.task.download.provider.IdsProvider;
import com.goexp.galgame.data.task.download.provider.brand.DBIdsProvider;
import com.goexp.galgame.data.task.download.provider.brand.ErrorIDSProvider;
import com.goexp.galgame.data.task.handler.MesType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class FromAllBrand extends DefaultStarter<Integer> {

    private final Logger logger = LoggerFactory.getLogger(FromAllBrand.class);

    @Override
    public void process(BlockingQueue<Message> msgQueue) {
        IdsProvider idsProvider = new DBIdsProvider();
        var ids = idsProvider.getIds();
        down(ids, msgQueue);

        while (Files.exists(Config.BRAND_ERROR_FILE)) {
            down(new ErrorIDSProvider().getIds(), msgQueue);
        }

        logger.info("All Done!!!");
    }

    private void down(List<Integer> ids, BlockingQueue<Message> msgQueue) {
        ids.parallelStream()
                .forEach(id -> {

                    try {

                        logger.info("Down:{}", id);
                        GetChu.BrandService.download(id);

                        msgQueue.offer(new Message(MesType.Brand, id), 60, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                });
    }
}
