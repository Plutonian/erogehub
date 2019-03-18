package com.goexp.galgame.data.task;

import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.data.piplline.core.Piplline;
import com.goexp.galgame.data.task.handler.DownloadGameHandler;
import com.goexp.galgame.data.task.handler.MesType;
import com.goexp.galgame.data.task.handler.PreProcessGame;
import com.goexp.galgame.data.task.handler.game.Bytes2Html;
import com.goexp.galgame.data.task.handler.game.Html2GameOK;
import com.goexp.galgame.data.task.handler.game.LocalGameHandler;
import com.goexp.galgame.data.task.handler.game.ProcessGameOK;
import com.goexp.galgame.data.task.handler.starter.FromDateRange;

import java.time.LocalDate;

public class FromDateRangeTask {

    public static void main(String[] args) {
        Network.initProxy();


        var start = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        var end = LocalDate.now().withMonth(12).withDayOfMonth(31);

        var pipl = new Piplline(new FromDateRange(start, end));
        pipl.registryCPUTypeMessageHandler(MesType.PRE_GAME, new PreProcessGame());
        pipl.registryIOTypeMessageHandler(MesType.NEED_DOWN_GAME, new DownloadGameHandler());
        pipl.registryCPUTypeMessageHandler(MesType.Game, new LocalGameHandler());
        pipl.registryCPUTypeMessageHandler(MesType.ContentBytes, new Bytes2Html());
        pipl.registryCPUTypeMessageHandler(MesType.ContentHtml, new Html2GameOK());
        pipl.registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameOK());
        pipl.start();
    }

}
