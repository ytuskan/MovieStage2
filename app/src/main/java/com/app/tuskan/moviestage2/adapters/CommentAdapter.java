package com.app.tuskan.moviestage2.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.tuskan.moviestage2.R;
import com.app.tuskan.moviestage2.models.Comment;

import java.util.ArrayList;

/**
 * Created by Yakup on 29.6.2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<Comment> comments;

    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    public static class commentHolder extends RecyclerView.ViewHolder {

        TextView textView;
        TextView textView1;

        public commentHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.comment_author_textview);
            textView1 = itemView.findViewById(R.id.comment_textview);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return new CommentAdapter.commentHolder(layoutInflater.inflate(R.layout.comment_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        final Comment item = comments.get(position);

        final CommentAdapter.commentHolder container = (commentHolder) holder;

        container.textView.setText(item.getName());
        container.textView1.setText(item.getComment());
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

}
