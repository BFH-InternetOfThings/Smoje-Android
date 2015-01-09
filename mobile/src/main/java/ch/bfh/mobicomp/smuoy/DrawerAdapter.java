package ch.bfh.mobicomp.smuoy;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DrawerAdapter extends BaseAdapter {
    private Activity activity;
    private final List<DrawerItem> drawerItems = new ArrayList<>();

    public DrawerAdapter(Activity activity, DrawerItemGroup... groups) {
        // Apparently there are situations where this occurs,
        // probably when the app is about to close:
        if (activity == null)
            return;

        this.activity = activity;
        for (DrawerItemGroup group : groups) {
            if (group.groupName != 0) {
                drawerItems.add(new DrawerItem(activity.getString(group.groupName)));
            }
            drawerItems.addAll(group.groupItems);
        }
    }

    @Override
    public int getCount() {
        return drawerItems.size();
    }

    @Override
    public DrawerItem getItem(int position) {
        return drawerItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DrawerItem item = drawerItems.get(position);
        View view;
        if (item.getImage() != 0) {
            view = activity.getLayoutInflater().inflate(R.layout.drawer_list_item, parent, false);
            ((TextView) view.findViewById(R.id.text)).setText(item.getItemName());
            ((ImageView) view.findViewById(R.id.icon)).setImageResource(item.getImage());
        } else if (item.getItemName() != null) {
            view = activity.getLayoutInflater().inflate(R.layout.drawer_list_subheader, parent, false);
            ((TextView) view.findViewById(R.id.text)).setText(item.getItemName());
        } else {
            view = activity.getLayoutInflater().inflate(R.layout.drawer_list_separator, parent, false);
        }
        return view;
    }
}
