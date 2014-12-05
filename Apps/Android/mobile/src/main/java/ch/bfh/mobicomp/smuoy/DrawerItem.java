package ch.bfh.mobicomp.smuoy;

import ch.bfh.mobicomp.smuoy.entities.Smuoy;

public class DrawerItem<T> {
    private String itemName;
    private int image;
    private Object data;
    private Listener<T> listener;

    public DrawerItem(String groupTitle) {
        this.itemName = groupTitle;
    }

    public DrawerItem(Smuoy smuoy, Listener<T> listener) {
        this.itemName = smuoy.name;
        this.image = R.drawable.ic_list_smuoy;
        this.data = smuoy;
        this.listener = listener;
    }

    public DrawerItem(String itemName, int imageResourceId, Listener<T> listener) {
        this.itemName = itemName;
        this.image = imageResourceId;
        this.listener = listener;
    }

    @SuppressWarnings("unchecked")
    public boolean select() {
        if (listener == null)
            return false;

        listener.drawerItemSelected((T) data);
        return true;
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

    public static interface Listener<T> {
        void drawerItemSelected(T data);
    }
}