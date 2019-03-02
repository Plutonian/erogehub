package com.goexp.galgame.data.task.others;

import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.data.db.importor.mongdb.TagDB;
import com.goexp.galgame.data.parser.GetchuTagParser;
import com.goexp.galgame.data.task.client.GetChu;
import org.slf4j.LoggerFactory;

public class ImportOnceTagTask {

    public static void main(String[] args) {

        var logger = LoggerFactory.getLogger(ImportOnceTagTask.class);

        Network.initProxy();

//        var brandService = new BrandQuery();
//        var localBrandMap = brandService.list()
//                .stream()
//                .collect(Collectors.toMap(b -> b.id, b -> b));
//
//        logger.info("Local:{}", localBrandMap.size());

        /**
         * download page from getchu
         */

        var request = GetchuURL.RequestBuilder
                .create("http://www.getchu.com/pc/genre.html")
                .adaltFlag()
                .build();

        var html = GetChu.getHtml(request);

//        var html = ContentLoader.loadFromFile(Path.of("C:\\Users\\K\\Downloads\\カテゴリー一覧 - Getchu.com.html"), GetChu.DEFAULT_CHARSET);

        var remoteTagList = new GetchuTagParser()
                .parse(html);

        logger.info("Remote:{}", remoteTagList.size());


        var tagDb = new TagDB();
        tagDb.insert(remoteTagList);


    }
}
