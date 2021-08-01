package ru.softvillage.onlineseller.ui.recyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import ru.softvillage.onlineseller.R;
import ru.softvillage.onlineseller.dataBase.entity.LocalUser;
import ru.softvillage.onlineseller.presenter.AuthPresenter;

@RequiredArgsConstructor
public class UserItemAdapter extends RecyclerView.Adapter<UserViewHolder> {

    private final LayoutInflater inflater;
    private final List<LocalUser> itemList = new ArrayList<>();
    private final List<UserViewHolder> viewHolderList = new ArrayList<>();
    private final itemClickInterface callback;

    @NonNull
    @NotNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new UserViewHolder(inflater.inflate(R.layout.item_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserViewHolder holder, int position) {
        Long presenterLastSelectUserUuid = AuthPresenter.getInstance().getLastSelectUserId().getValue();
        Long currentUserUuid = itemList.get(position).getUserUuid();
        if (currentUserUuid.equals(presenterLastSelectUserUuid)) {
            holder.setSelect();
            AuthPresenter.getInstance().setLastSelectUser(itemList.get(position));
        }

        viewHolderList.add(holder);
        holder.bind(itemList.get(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(v -> {
            for (UserViewHolder userViewHolder : viewHolderList) {
                userViewHolder.setUnSelect();
            }
            AuthPresenter.getInstance().setLastSelectUser(itemList.get(position));
            AuthPresenter.getInstance().setLastSelectUserId(itemList.get(position).getUserUuid());

            holder.setSelect();
            callback.clickOnElement(itemList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItems(List<LocalUser> entityList) {
        itemList.clear();
        itemList.addAll(entityList);

        viewHolderList.clear();
        notifyDataSetChanged();
    }

    public interface itemClickInterface {
        void clickOnElement(LocalUser user);
    }
}
