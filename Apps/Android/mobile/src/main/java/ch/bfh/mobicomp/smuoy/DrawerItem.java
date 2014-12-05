package ch.bfh.mobicomp.smuoy;

import ch.bfh.mobicomp.smuoy.entities.Smuoy;

public class DrawerItem {

    private String itemName;
    private int image;
    private Object data;

    public DrawerItem(String groupTitle) {
        this.itemName = groupTitle;
    }

    public DrawerItem(Smuoy smuoy) {
        this.itemName = smuoy.name;
        this.image = R.drawable.ic_list_smuoy;
        this.data = smuoy;
    }

    public DrawerItem(String itemName, int imageResourceId) {
        this.itemName = itemName;
        this.image = imageResourceId;
    }

    public String getItemName() {
        return itemName;
    }

    public int getImage() {
        return image;
    }

    public Object getData() {
        return data;
    }
}