package com.goexp.galgame.gui.db;

import com.goexp.galgame.gui.model.Brand;

import java.util.List;

public interface IBrandQuery {

    Brand getById(int id);

    List list();

    List<Brand> list(int type);

    List<Brand> listByName(String keyword);

    List<Brand> listByComp(String comp);

    List<Brand> queryByComp(String keyword);


}
