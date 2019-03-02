package com.goexp.galgame.gui.view.task;

import com.goexp.galgame.gui.db.mongo.GameDB;
import com.goexp.galgame.gui.model.Game;
import javafx.concurrent.Task;

import java.util.List;

public class ChangeGameTask {

    public static class Like extends Task<Void> {

        private Game game;

        public Like(Game game) {
            this.game = game;
        }

        @Override
        protected Void call() throws Exception {
            new GameDB.StateDB().update(game);
            return null;
        }
    }

    public static class MultiLike extends Task<Void> {

        private GameDB.StateDB gameDB = new GameDB.StateDB();

        private List<Game> games;

        public MultiLike(List<Game> games) {
            this.games = games;
        }

        @Override
        protected Void call() throws Exception {
            gameDB.batchUpdate(games);
            return null;
        }
    }

    public static class Star extends Task<Void> {

        private Game game;

        public Star(Game game) {
            this.game = game;
        }

        @Override
        protected Void call() throws Exception {
            new GameDB.StarDB().update(game);
            return null;
        }
    }
}
