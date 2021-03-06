package ch.bfh.mobicomp.smuoy;


import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import ch.bfh.mobicomp.smuoy.entities.Smuoy;

import java.util.Collection;

import static ch.bfh.mobicomp.smuoy.services.SmuoyService.SmuoyLoadedListener;
import static ch.bfh.mobicomp.smuoy.services.SmuoyService.smuoyService;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * private NavigationDrawerCallbacks fragmentAttacher;
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle drawerToggle;

    private FragmentAttacher fragmentAttacher;

    private DrawerLayout drawerLayout;
    private ListView drawerListView;
    private View fragmentContainerView;

    private int currentSelectedPosition = 0;
    private boolean fromSavedInstanceState;
    private boolean userLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            currentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            fromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        selectItem(currentSelectedPosition);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View drawerView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        drawerListView = (ListView) drawerView.findViewById(R.id.smuoy_list);
        drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position);
            }
        });
        final DrawerItem.Listener<Smuoy> smuoyListener = new DrawerItem.Listener<Smuoy>() {
            @Override
            public void drawerItemSelected(Smuoy smuoy) {
                fragmentAttacher.display(SmuoyDetailFragment.newInstance(smuoy), smuoy.id);
            }
        };
        final DrawerItemGroup mapGroup = new DrawerItemGroup(0, new DrawerItem<>(getString(R.string.map_view), R.drawable.ic_action_map, new DrawerItem.Listener<Void>() {
            @Override
            public void drawerItemSelected(Void ignore) {
                if (fragmentAttacher != null) {
                    fragmentAttacher.display(null, null);
                }
            }
        }));
        drawerListView.setAdapter(new DrawerAdapter(getActivity(), mapGroup));
        smuoyService.loadSmuoys(new SmuoyLoadedListener() {
            @Override
            public void onSmuoyListLoaded(Collection<Smuoy> smuoys) {
                drawerListView.setAdapter(new DrawerAdapter(getActivity(),
                        mapGroup,
                        new DrawerItemGroup(R.string.smuoys, smuoys, smuoyListener)));
            }
        });
        drawerListView.setItemChecked(currentSelectedPosition, true);
        return drawerView;
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        fragmentContainerView = getActivity().findViewById(fragmentId);
        this.drawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        this.drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        drawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.drawable.ic_drawer, R.drawable.ic_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!userLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    userLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().invalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!userLearnedDrawer && !fromSavedInstanceState) {
            this.drawerLayout.openDrawer(fragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        this.drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

        this.drawerLayout.setDrawerListener(drawerToggle);
    }

    private void selectItem(int position) {
        if (drawerListView != null) {
            drawerListView.setItemChecked(currentSelectedPosition, false);
            drawerListView.setItemChecked(position, true);

            currentSelectedPosition = position;
            if (((DrawerItem) drawerListView.getAdapter().getItem(position)).select()) {
                drawerLayout.closeDrawer(fragmentContainerView);
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentAttacher = (FragmentAttacher) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        fragmentAttacher = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, currentSelectedPosition);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }


    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public interface FragmentAttacher {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void display(Fragment fragment, String title);
    }
}
