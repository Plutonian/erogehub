package com.goexp.galgame.data.task;

import com.goexp.galgame.data.piplline.core.MesType;
import com.goexp.galgame.data.piplline.core.Piplline;
import com.goexp.galgame.data.task.handler.DownloadGameHandler;
import com.goexp.galgame.data.task.handler.ProcessGameList;
import com.goexp.galgame.data.task.handler.StartFromAllAliveBrand;
import com.goexp.galgame.data.task.handler.game.*;
import com.goexp.galgame.common.util.Network;

import java.io.IOException;

public class FromAliveBrandTask {

    public static void main(String[] args) throws IOException {

        Network.initProxy();


        var pipl = new Piplline(new StartFromAllAliveBrand());

        pipl.registryCPUTypeMessageHandler(MesType.Brand, new ProcessGameList());
//        pipl.registryIOTypeMessageHandler(MesType.NEED_DOWN_GAME, new DownloadGameHandler());
//        pipl.registryCPUTypeMessageHandler(MesType.Game, new LocalGameHandler());
//        pipl.registryCPUTypeMessageHandler(MesType.ContentBytes, new Bytes2Html());
//        pipl.registryCPUTypeMessageHandler(MesType.ContentHtml, new Html2GameOK());
//        pipl.registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameOK());
//        pipl.registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameCharOK());
//        pipl.registryCPUTypeMessageHandler(MesType.GAME_OK, new ProcessGameImgOK());

        pipl.start();

    }


}
