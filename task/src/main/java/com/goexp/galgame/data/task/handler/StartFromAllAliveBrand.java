package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.data.db.importor.Config;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.handler.DefaultStarter;
import com.goexp.galgame.data.task.client.GetChu;
import com.goexp.galgame.data.task.download.provider.IdsProvider;
import com.goexp.galgame.data.task.download.provider.brand.DBIdsProvider;
import com.goexp.galgame.data.task.download.provider.brand.ErrorIDSProvider;

import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class StartFromAllAliveBrand extends DefaultStarter<Integer> {


    @Override
    public void process(BlockingQueue<Message> msgQueue) {
        IdsProvider idsProvider = new DBIdsProvider();
        var ids = idsProvider.getIds();
        down(ids, msgQueue);

        while (Files.exists(Config.BRAND_ERROR_FILE)) {
            down(new ErrorIDSProvider().getIds(), msgQueue);
        }

        System.out.println("All Done!!!");
    }

    private void down(List<Integer> ids, BlockingQueue<Message> msgQueue) {
        ids.parallelStream()
                .forEach(id -> {

                    try {

                        System.out.println("Down:" + id);
                        GetChu.BrandService.download(id);

//                        msgQueue.offer(new Message(MesType.Brand, id), 60, TimeUnit.SECONDS);
                    } catch (Exception e) {
//                        e.printStackTrace();
                    }

                });
    }
}
