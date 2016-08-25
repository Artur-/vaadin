package com.vaadin.tests.containers.sqlcontainer;

import com.vaadin.server.LegacyApplication;
import com.vaadin.ui.LegacyWindow;
import com.vaadin.v7.shared.ui.combobox.FilteringMode;
import com.vaadin.v7.ui.ComboBox;

/**
 * See http://dev.vaadin.com/ticket/9155 .
 */
public class ComboBoxUpdateProblem extends LegacyApplication {
    private final DatabaseHelper databaseHelper = new DatabaseHelper();

    @Override
    public void init() {
        setMainWindow(new LegacyWindow("Test window"));

        ComboBox combo = new ComboBox("Names",
                databaseHelper.getTestContainer());
        combo.setItemCaptionPropertyId("FIELD1");
        combo.setFilteringMode(FilteringMode.CONTAINS);
        combo.setImmediate(true);

        getMainWindow().addComponent(combo);
    }

}
