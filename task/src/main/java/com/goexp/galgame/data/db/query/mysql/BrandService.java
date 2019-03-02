package com.goexp.galgame.data.db.query.mysql;

import com.goexp.common.db.mysql.DBOperatorTemplate;
import com.goexp.galgame.data.model.Brand;
import org.apache.commons.dbutils.ResultSetHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrandService extends DBOperatorTemplate {

    private final Logger logger = LoggerFactory.getLogger(BrandService.class);


    public Brand getById(int id) {

        logger.debug("<getById> Brand Id:{}", id);

        try {
            return runner.query("select * from getchu_brand where id=? ", (ResultSetHandler<Brand>) resultSet -> {
                if (resultSet.next()) {
                    return createBrand(resultSet);
                }

                return null;
            }, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Brand> list() {


        var list = new ArrayList<Brand>();

        try {
            return runner.query("select * from getchu_brand where isMain=1 ", (ResultSetHandler<List<Brand>>) resultSet -> {
                while (resultSet.next()) {
                    Brand g = createBrand(resultSet);

                    list.add(g);
                }

                return list;

            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Integer> idsAliveList() {


        var list = new ArrayList<Integer>();

        try {
            return runner.query("select id from getchu_brand where isMain=1 and dead=0", (ResultSetHandler<List<Integer>>) resultSet -> {
                while (resultSet.next()) {
                    list.add(resultSet.getInt("id"));
                }

                return list;

            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Integer> idsList() {


        var list = new ArrayList<Integer>();

        try {
            return runner.query("select id from getchu_brand where isMain=1", (ResultSetHandler<List<Integer>>) resultSet -> {
                while (resultSet.next()) {
                    list.add(resultSet.getInt("id"));
                }

                return list;

            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    private Brand createBrand(ResultSet resultSet) throws SQLException {
        var g = new Brand();

        g.id = resultSet.getInt("id");
        g.name = resultSet.getString("name");
        g.website = resultSet.getString("website");
//        g.isMain = resultSet.getInt("isMain") == 1;
        g.comp = resultSet.getString("comp");
        return g;
    }


}
