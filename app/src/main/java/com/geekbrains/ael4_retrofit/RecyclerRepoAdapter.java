package com.geekbrains.ael4_retrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.ael4_retrofit.model.RepoUsers;
import com.geekbrains.ael4_retrofit.presenters.RepoPresenter;

public class RecyclerRepoAdapter extends RecyclerView.Adapter<RecyclerRepoAdapter.ViewHolder> {

    RepoPresenter presenter;
    RepoUsers user;

    public RecyclerRepoAdapter(RepoPresenter presenter) {
        this.presenter = presenter;
        user = presenter.getUser();
    }

    @NonNull
    @Override
    public RecyclerRepoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_repo_info, parent, false);
        return new RecyclerRepoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerRepoAdapter.ViewHolder holder, int position) {
        holder.itemName.setText(user.getRepository(position).getRepos());
        holder.itemDescr.setText(user.getRepository(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if (user != null && user.getRepositoryList() != null)
            return user.getRepositoryList().size();
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName, itemDescr;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_repos_name);
            itemDescr = itemView.findViewById(R.id.item_repos_descr);
            itemView.setOnClickListener(v -> presenter.startUrl(user.getRepository(getAdapterPosition()).getUrl()));
        }
    }
}
