package com.goexp.galgame.gui.db.mysql.query;

import com.goexp.common.db.mysql.DBQueryTemplate;
import com.goexp.common.db.mysql.ObjectCreator;
import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.gui.db.IBrandQuery;
import com.goexp.galgame.gui.model.Brand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BrandQuery extends DBQueryTemplate<Brand> implements IBrandQuery {

    private static final String SELECT_FROM_GETCHU_BRAND = "select * from getchu_brand where isMain=1";
    private static final String ORDER_BY = "order by `order` desc,`index` asc, name asc";

    private final Logger logger = LoggerFactory.getLogger(BrandQuery.class);

    List<Brand> getList(String sql, Object... params) {
        return getList(new BrandCreator(), sql, params);
    }

    @Override
    public Brand getById(int id) {

        logger.debug("<getById> Brand Id:{}", id);
        return super.get(new BrandCreator(), "select * from getchu_brand where id=? ", id);
    }


    @Override
    public List<Brand> list() {

        return getList(SELECT_FROM_GETCHU_BRAND + " order by `order` desc");
    }


    @Override
    public List<Brand> list(int type) {

        logger.debug("<list> type:{}", type);

        return getList(SELECT_FROM_GETCHU_BRAND + " and isLike=? " + ORDER_BY
                , type
        );
    }

    @Override
    public List<Brand> listByName(String keyword) {

        logger.debug("<listByName> keyword:{}", keyword);

        return getList(SELECT_FROM_GETCHU_BRAND + " and `name` like ? " + ORDER_BY
                , keyword + "%"
        );
    }

    @Override
    public List<Brand> listByComp(String comp) {

        logger.debug("<listByComp> keyword:{}", comp);

        return getList(SELECT_FROM_GETCHU_BRAND + " and `comp` = ? " + ORDER_BY
                , comp
        );
    }

    @Override
    public List<Brand> queryByComp(String keyword) {

        logger.debug("<queryByComp> keyword:{}", keyword);

        return getList(SELECT_FROM_GETCHU_BRAND + " and `comp` like ? " + ORDER_BY
                , keyword + "%"
        );
    }

    public static class BrandCreator implements ObjectCreator<Brand> {
        @Override
        public Brand create(ResultSet resultSet) throws SQLException {
            var g = new Brand();

            g.id = resultSet.getInt("id");
            g.name = resultSet.getString("name");
            g.website = resultSet.getString("website");
//            g.isMain = resultSet.getBoolean("isMain");
            g.isLike = BrandType.from(resultSet.getInt("type"));
            g.comp = resultSet.getString("comp");
//            g.index = resultSet.getString("index");
//            g.dead = resultSet.getBoolean("dead");
            return g;
        }
    }

//    static class Test {
//        public static void main(String[] args) {
//            var b = new BrandQuery().getById(7);
//            System.out.println(b);
//        }
//    }


}
