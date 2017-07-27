package study.contentprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    private Button create;
    private Button update;
    private Button delete;

    private RecyclerView mRecyclerView;
    private Adapter mAdapter;
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        create = (Button) findViewById(R.id.create);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);

        create.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        number = 0;

        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, MyContentProvider.ITEM_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (mAdapter == null) {
            mAdapter = new Adapter(this, data);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.changeCursor(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        switch (id){
            case R.id.create:
                number++;
                ContentValues cv = new ContentValues();
                cv.put(DBHelper.ItemEntry.TEXT, String.valueOf(number));
                Uri newUri = getContentResolver().insert(MyContentProvider.ITEM_CONTENT_URI, cv);
                break;
            case R.id.update:
                ContentValues contentValues = new ContentValues();
                contentValues.put(DBHelper.ItemEntry.TEXT, "update");
                int update = getContentResolver().update(MyContentProvider.ITEM_CONTENT_URI, contentValues, null, null);
                break;
            case R.id.delete:
                number = 0;
                int delete = getContentResolver().delete(MyContentProvider.ITEM_CONTENT_URI, null, null);
                break;
        }
    }
}
