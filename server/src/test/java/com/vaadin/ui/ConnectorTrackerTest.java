package com.vaadin.ui;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.tests.util.AlwaysLockedVaadinSession;

public class ConnectorTrackerTest {

    @Test
    public void unregisterDoesNotUseInifiniteMemory() throws Exception {
        final VerticalLayout verticalLayout = new VerticalLayout();

        UI uI = new UI() {
            @Override
            protected void init(VaadinRequest request) {
            }
        };
        uI.setSession(new AlwaysLockedVaadinSession(
                Mockito.mock(VaadinService.class)));
        uI.setContent(verticalLayout);

        ConnectorTracker ct = uI.getConnectorTracker();
        for (int i = 0; i < 100000; i++) {
            Button b = new Button();
            verticalLayout.addComponent(b);
        }
        uI.setContent(null);
        TreeMap<Integer, List<String>> syncIdToUnregisteredConnectorIds = getSyncIdToUnregisteredConnectorIds(
                ct);
        Assert.assertEquals(1, syncIdToUnregisteredConnectorIds.size());
        Assert.assertEquals(100, syncIdToUnregisteredConnectorIds.firstEntry()
                .getValue().size());
        ct.cleanConcurrentlyRemovedConnectorIds(123);
        Assert.assertEquals(0, syncIdToUnregisteredConnectorIds.size());
    }

    private TreeMap<Integer, List<String>> getSyncIdToUnregisteredConnectorIds(
            ConnectorTracker ct) throws Exception {
        Field f;
        f = ConnectorTracker.class
                .getDeclaredField("syncIdToUnregisteredConnectorIds");
        f.setAccessible(true);
        return (TreeMap<Integer, List<String>>) f.get(ct);
    }

    private int mapSize(TreeMap<Integer, List<String>> map) {
        int size = 0;
        for (Entry<Integer, List<String>> entry : map.entrySet()) {
            size += entry.getValue().size();
        }
        return size;
    }
}
