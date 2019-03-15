package com.goexp.galgame.gui.view.search.frombrand.brand.task;

import com.goexp.galgame.common.model.BrandType;
import com.goexp.galgame.gui.db.mongo.query.BrandQuery;
import com.goexp.galgame.gui.model.Brand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.TreeItem;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class BrandSearchTask {
    public static class ByName extends Task<ObservableList<TreeItem<Brand>>> {

        private String name;

        public ByName(String name) {
            this.name = name;
        }

        @Override
        protected ObservableList<TreeItem<Brand>> call() {


            var list = BrandQuery.tlp.query()
                    .where(regex("name", "^" + name))
                    .list();

            return FXCollections.observableArrayList(makeTree(list));
        }

        private List<TreeItem<Brand>> makeTree(List<Brand> newValue) {
            return newValue.stream()
                    .collect(Collectors.groupingBy(b -> b.comp != null && b.comp.length() > 0 ? b.comp : ""))
                    .entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(entry -> {
                        var comp = new Brand();
                        comp.comp = entry.getKey();


                        var rootItem = new TreeItem<Brand>();
                        rootItem.setValue(comp);
                        rootItem.setExpanded(true);


                        var brandNodes = entry.getValue().stream()
                                .map(brand -> {
                                    var item = new TreeItem<Brand>();
                                    item.setValue(brand);
                                    return item;
                                }).collect(Collectors.toUnmodifiableList());


                        rootItem.getChildren().setAll(brandNodes);

                        return rootItem;
                    }).collect(Collectors.toUnmodifiableList());
        }
    }

    public static class ByType extends Task<ObservableList<TreeItem<Brand>>> {

        private BrandType type;

        public ByType(BrandType type) {
            this.type = type;
        }

        @Override
        protected ObservableList<TreeItem<Brand>> call() {


            List<Brand> list;
            if (type == BrandType.ALL) {
                list = BrandQuery.tlp.query().list();
            } else {
                list = BrandQuery.tlp.query()
                        .where(eq("type", type.getValue()))
                        .list();

            }

            return FXCollections.observableArrayList(makeTree(list));
        }

        private List<TreeItem<Brand>> makeTree(List<Brand> newValue) {
            return newValue.stream()
                    .collect(Collectors.groupingBy(b -> b.comp != null && b.comp.length() > 0 ? b.comp : ""))
                    .entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(entry -> {
                        var comp = new Brand();
                        comp.comp = entry.getKey();


                        var rootItem = new TreeItem<Brand>();
                        rootItem.setValue(comp);
                        rootItem.setExpanded(true);


                        var brandNodes = entry.getValue().stream()
                                .map(brand -> {
                                    var item = new TreeItem<Brand>();
                                    item.setValue(brand);
                                    return item;
                                }).collect(Collectors.toUnmodifiableList());


                        rootItem.getChildren().setAll(brandNodes);

                        return rootItem;
                    }).collect(Collectors.toUnmodifiableList());
        }
    }

    public static class ByComp extends Task<ObservableList<TreeItem<Brand>>> {

        private String name;

        public ByComp(String name) {
            this.name = name;
        }

        @Override
        protected ObservableList<TreeItem<Brand>> call() {


            var list = BrandQuery.tlp.query()
                    .where(regex("comp", name))
                    .list();

            return FXCollections.observableArrayList(makeTree(list));
        }

        private List<TreeItem<Brand>> makeTree(List<Brand> newValue) {
            return newValue.stream()
                    .collect(Collectors.groupingBy(b -> b.comp != null && b.comp.length() > 0 ? b.comp : ""))
                    .entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getKey))
                    .map(entry -> {
                        var comp = new Brand();
                        comp.comp = entry.getKey();


                        var rootItem = new TreeItem<Brand>();
                        rootItem.setValue(comp);
                        rootItem.setExpanded(true);


                        var brandNodes = entry.getValue().stream()
                                .map(brand -> {
                                    var item = new TreeItem<Brand>();
                                    item.setValue(brand);
                                    return item;
                                }).collect(Collectors.toUnmodifiableList());


                        rootItem.getChildren().setAll(brandNodes);

                        return rootItem;
                    }).collect(Collectors.toUnmodifiableList());
        }
    }


}
