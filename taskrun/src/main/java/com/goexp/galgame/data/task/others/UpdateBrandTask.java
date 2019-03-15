package com.goexp.galgame.data.task.others;

import com.goexp.galgame.common.util.Network;
import com.goexp.galgame.common.website.GetchuURL;
import com.goexp.galgame.data.db.importor.mongdb.BrandDB;
import com.goexp.galgame.data.db.query.mongdb.BrandQuery;
import com.goexp.galgame.data.parser.GetchuBrandParser;
import com.goexp.galgame.data.task.client.GetChu;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

public class UpdateBrandTask {

    public static void main(String[] args) {

        var logger = LoggerFactory.getLogger(UpdateBrandTask.class);

        Network.initProxy();

        var localBrandMap = BrandQuery.tlp.query()
                .list()
                .stream()
                .collect(Collectors.toMap(b -> b.id, b -> b));

        logger.info("Local:{}", localBrandMap.size());

        var request = GetchuURL.RequestBuilder
                .create("http://www.getchu.com/all/brand.html?genre=pc_soft")
                .adaltFlag()
                .build();

        var html = GetChu.getHtml(request);

        var remoteBrandList = new GetchuBrandParser()
                .parse(html);

        logger.info("Remote:{}", remoteBrandList.size());


        var brandDb = new BrandDB();

        for (var remoteBrand : remoteBrandList) {
            // Update

            var localBrand = localBrandMap.get(remoteBrand.id);

            if (localBrand != null) {

                if (localBrand.website.isEmpty() && !remoteBrand.website.isEmpty()) {
                    logger.info("Local: {},Remote: {}", localBrand, remoteBrand);
                    brandDb.updateWebsite(remoteBrand);
                }
                //Insert
            } else {

                logger.info("<Insert> {}", remoteBrand);
                brandDb.insert(remoteBrand);
            }
        }


    }
}
