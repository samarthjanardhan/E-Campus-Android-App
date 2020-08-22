package com.example.samarth.ecampus.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.samarth.ecampus.R;
import com.example.samarth.ecampus.model.UploadNote;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private RecyclerView mRecyclerView;
    private Context mContext;
    private List<UploadNote> mUploads;


    public NoteAdapter(RecyclerView mRecyclerView,Context context, List<UploadNote> uploads) {
        this.mRecyclerView=mRecyclerView;
        mContext = context;
        mUploads = uploads;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.note_item, parent, false);
        return new NoteAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        UploadNote uploadCurrent = mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewName;


        public ViewHolder (View itemView) {      //represents individuals list items
            super(itemView);

            textViewName = itemView.findViewById(R.id.nameofFile);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = mRecyclerView.getChildLayoutPosition(view);

                    UploadNote uploadCurrent = mUploads.get(position);
                    Intent intent = new Intent();
                    intent.setType(Intent.ACTION_VIEW);  //denotes that we are going to view something
                    String urls = uploadCurrent.getpdfUrl();
                    intent.setData(Uri.parse(uploadCurrent.getpdfUrl()));
                    mContext.startActivity(intent);
                }
            });

        }
    }
}
