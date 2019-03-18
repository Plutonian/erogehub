package com.goexp.galgame.data.task;

import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.data.piplline.core.Piplline;
import com.goexp.galgame.data.task.handler.DownloadGameHandler;
import com.goexp.galgame.data.task.handler.MesType;
import com.goexp.galgame.data.task.handler.ProcessGameList;
import com.goexp.galgame.data.task.handler.game.Bytes2Html;
import com.goexp.galgame.data.task.handler.game.Html2GameOK;
import com.goexp.galgame.data.task.handler.game.LocalGameHandler;
import com.goexp.galgame.data.task.handler.game.ProcessGameOK;
import com.goexp.galgame.data.task.handler.starter.FromAllBrand;

public class FromAliveBrandTask {

    public static void main(String[] args) {

        Network.initProxy();


        var pipl = new Piplline(new FromAllBrand());

        pipl.registryCPUTypeMessageHandler(MesType.Brand, new ProcessGameList());
        pipl.registryIOTypeMessageHandler(MesType.NEED_DOWN_GAME, new DownloadGameHandler());
        pipl.registryCPUTypeMessageHandler(MesType.Game, new LocalGameHandler());
        pipl.registryCPUTypeMessageHandler(MesType.ContentBytes, new Bytes2Html());
        pipl.registryCPUTypeMessageHandler(MesType.ContentHtml, new Html2GameOK());
        pipl.registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameOK());

        pipl.start();

    }


}
