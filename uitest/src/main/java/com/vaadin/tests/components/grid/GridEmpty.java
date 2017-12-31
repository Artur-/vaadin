/*
 * Copyright 2000-2016 Vaadin Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.vaadin.tests.components.grid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.vaadin.annotations.Widgetset;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.tests.components.AbstractTestUI;
import com.vaadin.tests.fieldgroup.ComplexPerson;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;

@Widgetset("com.vaadin.DefaultWidgetSet")
public class GridEmpty extends AbstractTestUI {

    private Grid<ComplexPerson> grid;
    private ListDataProvider<ComplexPerson> provider;

    @Override
    protected void setup(VaadinRequest request) {
        int count = 10;
        if (request.getParameter("count") != null) {
            count = Integer.parseInt(request.getParameter("count"));
        }

        grid = new Grid<>(ComplexPerson.class);
        grid.setEmptyText("There is no data");
        TextField personsInDataProvider = new TextField(
                "Persons in data provider");
        personsInDataProvider.addValueChangeListener(e -> {
            List<ComplexPerson> persons = createPersons(
                    Integer.parseInt(personsInDataProvider.getValue()),
                    new Random(1));
            provider = DataProvider.ofCollection(persons);
            grid.setDataProvider(provider);

        });
        personsInDataProvider.setValue(String.valueOf(count));
        TextField filter = new TextField("Filter");
        filter.addValueChangeListener(e -> {
            provider.setFilter(person -> {
                return person.getFirstName().matches(filter.getValue());
            });
        });
        addComponent(personsInDataProvider);
        addComponent(filter);
        addComponent(grid);
    }

    public static List<ComplexPerson> createPersons(int count, Random r) {
        List<ComplexPerson> c = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            c.add(ComplexPerson.create(r));
        }
        return c;
    }
}
