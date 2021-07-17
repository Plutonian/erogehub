/*
 * Copyright (c) 2010, 2020, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package com.goexp.galgame.gui.view.common.control.listview;

import javafx.beans.InvalidationListener;
import javafx.beans.WeakInvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Skin;
import javafx.scene.control.skin.VirtualContainerBase;
import javafx.scene.control.skin.VirtualFlow;

import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Default skin implementation for the {@link ListView} control.
 *
 * @see ListView
 * @since 9
 */
public class ReadOnlyListViewSkin<T> extends VirtualContainerBase<ListView<T>, ListCell<T>> {

    /***************************************************************************
     *                                                                         *
     * Static Fields                                                           *
     *                                                                         *
     **************************************************************************/

    // RT-34744 : IS_PANNABLE will be false unless
    // javafx.scene.control.skin.ListViewSkin.pannable
    // is set to true. This is done in order to make ListView functional
    // on embedded systems with touch screens which do not generate scroll
    // events for touch drag gestures.
    private static final boolean IS_PANNABLE =
            AccessController.doPrivileged((PrivilegedAction<Boolean>) () -> Boolean.getBoolean("javafx.scene.control.skin.ListViewSkin.pannable"));


    /***************************************************************************
     *                                                                         *
     * Internal Fields                                                         *
     *                                                                         *
     **************************************************************************/


    private final MyVirtualFlow flow;


    private ObservableList<T> listViewItems;

    //    private boolean needCellsRebuilt = true;
    private boolean needCellsReconfigured = false;

    private int itemCount = -1;

    /***************************************************************************
     *                                                                         *
     * Listeners                                                               *
     *                                                                         *
     **************************************************************************/


    private final ListChangeListener<T> listViewItemsListener = c -> {
        while (c.next()) {
            if (!c.wasReplaced()) {
                if (c.getRemovedSize() == itemCount) {
                    // RT-22463: If the user clears out an items list then we
                    // should reset all cells (in particular their contained
                    // items) such that a subsequent addition to the list of
                    // an item which equals the old item (but is rendered
                    // differently) still displays as expected (i.e. with the
                    // updated display, not the old display).
                    itemCount = 0;
                    break;
                }
            }
        }

        markItemCountDirty();
        getSkinnable().requestLayout();
    };

    private final WeakListChangeListener<T> weakListViewItemsListener = new WeakListChangeListener<T>(listViewItemsListener);


    private final InvalidationListener itemsChangeListener = observable -> updateListViewItems();

    private final WeakInvalidationListener weakItemsChangeListener = new WeakInvalidationListener(itemsChangeListener);


    /***************************************************************************
     *                                                                         *
     * Constructors                                                            *
     *                                                                         *
     **************************************************************************/


    public ReadOnlyListViewSkin(final ListView<T> control) {
        super(control);


        updateListViewItems();

        // init the VirtualFlow
        flow = (MyVirtualFlow) getVirtualFlow();
        flow.setId("virtual-flow");
        flow.setPannable(IS_PANNABLE);
        flow.setVertical(control.getOrientation() == Orientation.VERTICAL);
        flow.setCellFactory(flow -> createCell());
        flow.setFixedCellSize(control.getFixedCellSize());
        getChildren().add(flow);

        updateItemCount();

        control.itemsProperty().addListener(weakItemsChangeListener);

        // Register listeners
        registerChangeListener(control.itemsProperty(), o -> updateListViewItems());
        registerChangeListener(control.orientationProperty(), o ->
                flow.setVertical(control.getOrientation() == Orientation.VERTICAL)
        );
        registerChangeListener(control.cellFactoryProperty(), o -> flow.recreateCells());
        registerChangeListener(control.parentProperty(), o -> {
            if (control.getParent() != null && control.isVisible()) {
                control.requestLayout();
            }
        });
        registerChangeListener(control.fixedCellSizeProperty(), o ->
                flow.setFixedCellSize(control.getFixedCellSize())
        );
    }


    /***************************************************************************
     *                                                                         *
     * Public API                                                              *
     *                                                                         *
     **************************************************************************/

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
        if (getSkinnable() == null) return;


        getSkinnable().itemsProperty().removeListener(weakItemsChangeListener);
        if (listViewItems != null) {
            listViewItems.removeListener(weakListViewItemsListener);
            listViewItems = null;
        }
        // flow related cleanup
        // leaking without nulling factory
        flow.setCellFactory(null);
        super.dispose();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void layoutChildren(final double x, final double y,
                                  final double w, final double h) {
        super.layoutChildren(x, y, w, h);

//        if (needCellsRebuilt) {
//            flow.rebuildCells();
//        } else


        if (needCellsReconfigured) {
            flow.reconfigureCells();
        }

//        needCellsRebuilt = false;
        needCellsReconfigured = false;

        if (getItemCount() > 0) {
            flow.resizeRelocate(x, y, w, h);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double computePrefWidth(double height, double topInset, double rightInset, double bottomInset, double leftInset) {
//        _checkState();

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double computePrefHeight(double width, double topInset, double rightInset, double bottomInset, double leftInset) {
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected int getItemCount() {
        return itemCount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateItemCount() {
        if (flow == null) return;

        int oldCount = itemCount;
        int newCount = listViewItems == null ? 0 : listViewItems.size();

        itemCount = newCount;

        flow.setCellCount(newCount);

        if (newCount == oldCount) {
            needCellsReconfigured = true;
        } else if (oldCount == 0) {
            flow.rebuildCells();
        }
    }

//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
//        switch (attribute) {
//            case FOCUS_ITEM: {
//                FocusModel<?> fm = getSkinnable().getFocusModel();
//                int focusedIndex = fm.getFocusedIndex();
//                if (focusedIndex == -1) {
//                    if (placeholderRegion != null && placeholderRegion.isVisible()) {
//                        return placeholderRegion.getChildren().get(0);
//                    }
//                    if (getItemCount() > 0) {
//                        focusedIndex = 0;
//                    } else {
//                        return null;
//                    }
//                }
//                return flow.getPrivateCell(focusedIndex);
//            }
//            case ITEM_COUNT:
//                return getItemCount();
//            case ITEM_AT_INDEX: {
//                Integer rowIndex = (Integer) parameters[0];
//                if (rowIndex == null) return null;
//                if (0 <= rowIndex && rowIndex < getItemCount()) {
//                    return flow.getPrivateCell(rowIndex);
//                }
//                return null;
//            }
//            case SELECTED_ITEMS: {
//                MultipleSelectionModel<T> sm = getSkinnable().getSelectionModel();
//                ObservableList<Integer> indices = sm.getSelectedIndices();
//                List<Node> selection = new ArrayList<>(indices.size());
//                for (int i : indices) {
//                    ListCell<T> row = flow.getPrivateCell(i);
//                    if (row != null) selection.add(row);
//                }
//                return FXCollections.observableArrayList(selection);
//            }
//            case VERTICAL_SCROLLBAR:
//                return flow.getVbar();
//            case HORIZONTAL_SCROLLBAR:
//                return flow.getHbar();
//            default:
//                return super.queryAccessibleAttribute(attribute, parameters);
//        }
//    }
//
//    /**
//     * {@inheritDoc}
//     */
//    @Override
//    protected void executeAccessibleAction(AccessibleAction action, Object... parameters) {
//        switch (action) {
//            case SHOW_ITEM: {
//                Node item = (Node) parameters[0];
//                if (item instanceof ListCell) {
//                    @SuppressWarnings("unchecked")
//                    ListCell<T> cell = (ListCell<T>) item;
//                    flow.scrollTo(cell.getIndex());
//                }
//                break;
//            }
//            case SET_SELECTED_ITEMS: {
//                @SuppressWarnings("unchecked")
//                ObservableList<Node> items = (ObservableList<Node>) parameters[0];
//                if (items != null) {
//                    MultipleSelectionModel<T> sm = getSkinnable().getSelectionModel();
//                    if (sm != null) {
//                        sm.clearSelection();
//                        for (Node item : items) {
//                            if (item instanceof ListCell) {
//                                @SuppressWarnings("unchecked")
//                                ListCell<T> cell = (ListCell<T>) item;
//                                sm.select(cell.getIndex());
//                            }
//                        }
//                    }
//                }
//                break;
//            }
//            default:
//                super.executeAccessibleAction(action, parameters);
//        }
//    }


    /***************************************************************************
     *                                                                         *
     * Private implementation                                                  *
     *                                                                         *
     **************************************************************************/

    /**
     * {@inheritDoc}
     */
    private ListCell<T> createCell() {
        ListCell<T> cell;
        if (getSkinnable().getCellFactory() != null) {
            cell = getSkinnable().getCellFactory().call(getSkinnable());
        } else {
            cell = createDefaultCellImpl();
        }

        cell.updateListView(getSkinnable());

        return cell;
    }

    private void updateListViewItems() {
        if (listViewItems != null) {
            listViewItems.removeListener(weakListViewItemsListener);
        }

        this.listViewItems = getSkinnable().getItems();

        if (listViewItems != null) {
            listViewItems.addListener(weakListViewItemsListener);
        }

        markItemCountDirty();
        getSkinnable().requestLayout();
    }


    private static <T> ListCell<T> createDefaultCellImpl() {

        class DefaultListCell<T> extends ListCell<T> {

            @Override
            protected Skin<?> createDefaultSkin() {
                return new ReadOnlyCellSkin<>(this);
            }

            @Override
            public void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setStyle(null);
                } else if (item instanceof Node) {
                    setText(null);
                    Node currentNode = getGraphic();
                    Node newNode = (Node) item;
                    if (currentNode == null || !currentNode.equals(newNode)) {
                        setGraphic(newNode);
                    }
                } else {
                    /**
                     * This label is used if the item associated with this cell is to be
                     * represented as a String. While we will lazily instantiate it
                     * we never clear it, being more afraid of object churn than a minor
                     * "leak" (which will not become a "major" leak).
                     */
                    setText(item == null ? "null" : item.toString());
                    setGraphic(null);
                }
            }
        }

        return new DefaultListCell<T>();
    }

    @Override
    protected VirtualFlow<ListCell<T>> createVirtualFlow() {
        return new MyVirtualFlow();
    }


    /**
     * Custom VirtualFlow to grant access to protected methods.
     */
    private class MyVirtualFlow extends VirtualFlow<ListCell<T>> {

        public void recreateCells() {
            super.recreateCells();
        }

        public void rebuildCells() {
            super.rebuildCells();
        }

        public void reconfigureCells() {
            super.reconfigureCells();
        }
    }
}
