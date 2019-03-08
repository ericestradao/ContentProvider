package com.example.user.wordlistsqliwithcontentprovider.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.user.wordlistsqliwithcontentprovider.MainActivity;
import com.example.user.wordlistsqliwithcontentprovider.model.MyButtonOnClickListener;
import com.example.user.wordlistsqliwithcontentprovider.R;
import com.example.user.wordlistsqliwithcontentprovider.model.Contract;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        Button delete_button;
        Button edit_button;

        public WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = (TextView) itemView.findViewById(R.id.word);
            delete_button = (Button)itemView.findViewById(R.id.delete_button);
            edit_button = (Button)itemView.findViewById(R.id.edit_button);
        }
    }

    private static final String TAG = WordListAdapter.class.getSimpleName();

    public static final String EXTRA_ID = "ID";
    public static final String EXTRA_WORD = "WORD";
    public static final String EXTRA_POSITION = "POSITION";

    private String queryUri = Contract.CONTENT_URI.toString(); // base uri
    private static final String[] projection = new String[] {Contract.CONTENT_PATH}; //table
    private String selectionClause = null;
    private String selectionArgs[] = null;
    private String sortOrder = "ASC";

    private final LayoutInflater mInflater;
    Context mContext;

    public WordListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.wordlist_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        String word = "";
        int id = -1;

        Cursor cursor = mContext.getContentResolver().query(Uri.parse(
                queryUri), null, null, null, sortOrder);

        if (cursor != null) {
            if (cursor.moveToPosition(position)) {
                int indexWord = cursor.getColumnIndex(Contract.WordList.KEY_WORD);
                word = cursor.getString(indexWord);
                holder.wordItemView.setText(word);
                int indexId = cursor.getColumnIndex(Contract.WordList.KEY_ID);
                id = cursor.getInt(indexId);
            } else {
                holder.wordItemView.setText(R.string.error_no_word);
            }

            cursor.close();

        } else {
            Log.e (TAG, "onBindViewHolder: Cursor is null.");
        }
        final WordViewHolder h = holder;

        holder.delete_button.setOnClickListener(new MyButtonOnClickListener(
                id, null)  {


            @Override
            public void onClick(View v ) {
                selectionArgs = new String[]{Integer.toString(id)};
                int deleted = mContext.getContentResolver().delete(
                        Contract.CONTENT_URI, Contract.CONTENT_PATH,selectionArgs);
                if (deleted > 0) {
                    notifyItemRemoved(h.getAdapterPosition());
                    notifyItemRangeChanged(
                            h.getAdapterPosition(), getItemCount());
                } else {
                    Log.d (TAG, mContext.getString(R.string.not_deleted) + deleted);
                }
            }
        });

        holder.edit_button.setOnClickListener(new MyButtonOnClickListener(
                id, word) {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditWordActivity.class);

                intent.putExtra(EXTRA_ID, id);
                intent.putExtra(EXTRA_POSITION, h.getAdapterPosition());
                intent.putExtra(EXTRA_WORD, word);

                ((Activity) mContext).startActivityForResult(intent, MainActivity.WORD_EDIT);
            }
        });
    }

    @Override
    public int getItemCount() {

        Cursor cursor = mContext.getContentResolver().query(
                Contract.ROW_COUNT_URI, new String[] {"count(*) AS count"},
                selectionClause, selectionArgs, sortOrder);
        try {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count;
        } catch (Exception e){
            Log.d(TAG, "EXCEPTION getItemCount: " + e);
            return -1;
        }
    }
}