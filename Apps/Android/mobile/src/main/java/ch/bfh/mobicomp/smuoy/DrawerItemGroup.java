package ch.bfh.mobicomp.smuoy;

import ch.bfh.mobicomp.smuoy.entities.Smuoy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by chris on 05.12.14.
 */
public class DrawerItemGroup {
    final int groupName;
    final List<DrawerItem> groupItems;

    public DrawerItemGroup(int groupName, DrawerItem... groupItems) {
        this.groupName = groupName;
        this.groupItems = Arrays.asList(groupItems);
    }

    public DrawerItemGroup(int groupName, List<Smuoy> smuoys) {
        this.groupName = groupName;
        this.groupItems = new ArrayList<>(smuoys.size());
        for (Smuoy smuoy : smuoys) {
            groupItems.add(new DrawerItem(smuoy));
        }
    }
}
