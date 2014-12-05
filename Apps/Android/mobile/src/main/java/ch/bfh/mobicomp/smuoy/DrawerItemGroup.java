package ch.bfh.mobicomp.smuoy;

import ch.bfh.mobicomp.smuoy.entities.Smuoy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Holds a group of drawer items with a title and some items.
 */
public class DrawerItemGroup {
    final int groupName;
    final List<DrawerItem> groupItems;

    public DrawerItemGroup(int groupName, DrawerItem... groupItems) {
        this.groupName = groupName;
        this.groupItems = Arrays.asList(groupItems);
    }

    public DrawerItemGroup(int groupName, Collection<Smuoy> smuoys, DrawerItem.Listener<Smuoy> listener) {
        this.groupName = groupName;
        this.groupItems = new ArrayList<>(smuoys.size());
        for (Smuoy smuoy : smuoys) {
            groupItems.add(new DrawerItem<>(smuoy, listener));
        }
    }
}
