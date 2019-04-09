package com.goexp.galgame.data.task.handler;

import com.goexp.galgame.common.model.GameState;
import com.goexp.galgame.data.db.importor.mongdb.GameDB;
import com.goexp.galgame.data.db.query.mongdb.GameQuery;
import com.goexp.galgame.data.piplline.core.Message;
import com.goexp.galgame.data.piplline.core.MessageQueueProxy;
import com.goexp.galgame.data.piplline.handler.DefaultMessageHandler;
import com.goexp.galgame.data.task.download.contentprovider.brand.LocalProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Projections.include;

public class ProcessGameList extends DefaultMessageHandler<Integer> {

    final private Logger logger = LoggerFactory.getLogger(ProcessGameList.class);

    final private GameDB importor = new GameDB();

    @Override
    public void process(final Message<Integer> message, MessageQueueProxy<Message> msgQueue) {

        var brandId = message.entity;
        logger.debug("<Brand> {}", brandId);
        try {

            var parseGameList = LocalProvider.getList(brandId);

            final var indbList = GameQuery.fullTlp.query()
                    .select(include("_id"))
                    .where(eq("brandId", brandId)).list().stream()
                    .map(game -> game.id)
                    .collect(Collectors.toUnmodifiableSet());


            if (parseGameList.size() != indbList.size()) {
                logger.debug("Brand:{},RemoteCount:{},LocalCount:{}", brandId, parseGameList.size(), indbList.size());
                parseGameList.stream()
                        .filter(g -> !indbList.contains(g.id))
                        //New Game
                        .forEach(newGame -> {
                            newGame.brandId = brandId;
                            newGame.state = GameState.UNCHECKED;

                            logger.info("<Insert> {}", newGame.simpleView());
                            importor.insert(newGame);

                            msgQueue.offer(new Message<>(MesType.NEED_DOWN_GAME, newGame.id));
                        });

            }

        } catch (Exception e) {
            logger.error("Brand:{}", brandId);

            e.printStackTrace();
        }

    }

}
