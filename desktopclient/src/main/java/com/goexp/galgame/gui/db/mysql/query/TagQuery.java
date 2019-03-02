package com.goexp.galgame.gui.db.mysql.query;

import com.goexp.common.db.mysql.DBQueryTemplate;
import com.goexp.galgame.gui.model.Tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagQuery extends DBQueryTemplate<Tag> {

    public List<Tag> types() {


        try {
            return runner.query("SELECT t.id,t.`name`,tt.`name` as type,tt.`order`  FROM `tag` as t  inner join  tag_type as tt on t.`typeid`=tt.`id`  order by tt.`order` asc", resultSet -> {
                var list = new ArrayList<Tag>();
                while (resultSet.next()) {

                    var tag = new Tag();
                    tag.id = resultSet.getInt("id");
                    tag.name = resultSet.getString("name");
                    tag.type = new Tag.TagType();
                    tag.type.type = resultSet.getString("type");
                    tag.type.order = resultSet.getInt("order");
                    list.add(tag);
                }

                return list;
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }
}
