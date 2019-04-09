package com.goexp.galgame.data.task.others;

import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.data.db.importor.mongdb.TagDB;
import com.goexp.galgame.data.parser.GetchuTagParser;
import com.goexp.galgame.data.task.client.GetChu;
import org.slf4j.LoggerFactory;

public class ImportOnceTagTask {

    public static void main(String[] args) {

        final var logger = LoggerFactory.getLogger(ImportOnceTagTask.class);

        Network.initProxy();

        /**
         * download page from getchu
         */

        var request = GetchuURL.RequestBuilder
                .create("http://www.getchu.com/pc/genre.html")
                .adaltFlag()
                .build();

        var html = GetChu.getHtml(request);

        /**
         * parse html
         */

        var remoteTagList = new GetchuTagParser().parse(html);

        logger.info("Remote:{}", remoteTagList.size());

        /**
         * save to db
         */

        var tagDb = new TagDB();
        tagDb.insert(remoteTagList);


    }
}
