package com.geekbrains.ael4_retrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.presenters.MainPresenterInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerUserAdapter extends RecyclerView.Adapter<RecyclerUserAdapter.ViewHolder> {

    private List<RepoUsers> users;
   private MainPresenterInterface presenter;


    public RecyclerUserAdapter(MainPresenterInterface presenter) {
        this.presenter = presenter;
    }

    @NonNull
    @Override
    public RecyclerUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_info, parent, false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerUserAdapter.ViewHolder holder, int position) {
        presenter.getUsersList();
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        if (users!=null)return users.size();
        return 0;
    }


    public void update(){
        users=presenter.getUsersList();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.item_user_img);
            textView=itemView.findViewById(R.id.item_user_name);
            itemView.setOnClickListener(v -> presenter.requestUserRepoList(users.get(getAdapterPosition()).getUser()));
        }


        public void update(int position){
            textView.setText(users.get(position).getUser());
            Picasso.get()
                    .load(users.get(position).getImg_url())
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imageView);
        }
    }
}
