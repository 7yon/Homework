package study.contentprovider;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by simen on 25.07.2017.
 */

public class Adapter extends CursorRecyclerViewAdapter {

    public Adapter(Context context, Cursor cursor) {
        super(context, cursor);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, Cursor cursor) {
        ItemHolder holder = (ItemHolder) viewHolder;
        cursor.moveToPosition(cursor.getPosition());
        holder.setData(cursor);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item, parent, false);
        return new ItemHolder(v);
    }

    private class ItemHolder extends RecyclerView.ViewHolder{
        TextView textView;

        public ItemHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.item_text);
        }


        public void setData(Cursor c) {

            if((c.getPosition() == -1 && c.getCount() != 0) || (c.getPosition() != c.getCount() - 1)){
                c.moveToNext();
            }
            int index = c.getColumnIndex("text");
            String string = c.getString(index);
            textView.setText(string);
        }
    }
}
