package ch.bfh.mobicomp.smuoy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


/**
 * An activity representing a list of Smuoys. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SmuoyDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p/>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link SmuoyListFragment} and the item details
 * (if present) is a {@link SmuoyDetailFragment}.
 * <p/>
 * This activity also implements the required
 * {@link SmuoyListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class SmuoyListActivity extends ActionBarActivity
        implements SmuoyListFragment.Callbacks {

    public static String SMUOY_ID = "smuoyId";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean twoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smuoy_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.smuoy_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((SmuoyListFragment) getFragmentManager()
                    .findFragmentById(R.id.smuoy_list))
                    .setActivateOnItemClick(true);
        }

        if (getIntent().hasExtra(SMUOY_ID)) {
            String smuoyId = getIntent().getStringExtra(SMUOY_ID);
            if (smuoyId != null) {
                onItemSelected(smuoyId);
            }
        }
    }

    /**
     * Callback method from {@link SmuoyListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (twoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(SmuoyDetailFragment.ARG_ITEM_ID, id);
            SmuoyDetailFragment fragment = new SmuoyDetailFragment();
            fragment.setArguments(arguments);
            getFragmentManager().beginTransaction()
                    .replace(R.id.smuoy_detail_container, fragment)
                    .commit();

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, SmuoyDetailActivity.class);
            detailIntent.putExtra(SmuoyDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_show_map:
//                ActivityOptionsCompat options = ActivityOptionsCompat.make
//                startActivity(new Intent(this, MapActivity.class), options);
                startActivity(new Intent(this, MapActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
