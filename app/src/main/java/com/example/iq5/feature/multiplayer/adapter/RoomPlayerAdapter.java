package com.example.iq5.feature.multiplayer.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iq5.R;
// Giả sử bạn có model NguoiDung ở package này, hoặc import đúng package
// import com.example.iq5.core.model.NguoiDung;
import com.example.iq5.feature.multiplayer.model.Friend; // Dùng tạm model Friend để minh họa
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Adapter cho RecyclerView trong RoomLobbyActivity (Phòng 4 người)
 * Quản lý một danh sách 4 item, NguoiDung (hoặc model tương tự) hoặc null
 */
public class RoomPlayerAdapter extends RecyclerView.Adapter<RoomPlayerAdapter.PlayerViewHolder> {

    // Sử dụng model Friend làm ví dụ, bạn nên thay bằng NguoiDung
    private List<Friend> playerList;
    private int currentUserID; // ID của người dùng xem sảnh này

    // Thay List<Friend> bằng List<NguoiDung>
    public RoomPlayerAdapter(List<Friend> playerList, int currentUserID) {
        this.playerList = playerList;
        this.currentUserID = currentUserID;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_room_player, parent, false);
        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        // Lấy người chơi tại vị trí. Có thể là NULL nếu slot trống.
        Friend player = playerList.get(position);
        holder.bind(player, position == 0, currentUserID); // Giả sử host luôn ở vị trí 0
    }

    @Override
    public int getItemCount() {
        return playerList.size(); // Luôn luôn là 4
    }

    // ViewHolder
    static class PlayerViewHolder extends RecyclerView.ViewHolder {
        LinearLayout layoutEmptySlot, layoutPlayerSlot;
        CircleImageView imgPlayerAvatar;
        TextView tvPlayerName, tvPlayerStatus;

        public PlayerViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutEmptySlot = itemView.findViewById(R.id.layoutEmptySlot);
            layoutPlayerSlot = itemView.findViewById(R.id.layoutPlayerSlot);
            imgPlayerAvatar = itemView.findViewById(R.id.imgPlayerAvatar);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerStatus = itemView.findViewById(R.id.tvPlayerStatus);
        }

        // Thay Friend bằng NguoiDung
        public void bind(Friend player, boolean isHost, int currentUserID) {
            if (player == null) {
                // Slot trống
                layoutEmptySlot.setVisibility(View.VISIBLE);
                layoutPlayerSlot.setVisibility(View.GONE);
            } else {
                // Có người chơi
                layoutEmptySlot.setVisibility(View.GONE);
                layoutPlayerSlot.setVisibility(View.VISIBLE);

                // Dùng getTenNguoiBan() làm ví dụ, thay bằng getHoTen()
                tvPlayerName.setText(player.getTenNguoiBan());
                // TODO: Load ảnh bằng Glide/Picasso
                // Glide.with(itemView.getContext()).load(player.getAnhDaiDien()).into(imgPlayerAvatar);

                // Hiển thị trạng thái
                if (isHost) {
                    tvPlayerStatus.setText("Chủ phòng");
                    tvPlayerStatus.setVisibility(View.VISIBLE);
                } else {
                    // TODO: Thêm logic "Sẵn sàng"
                    tvPlayerStatus.setText("Đang chờ");
                    tvPlayerStatus.setVisibility(View.GONE);
                }

                // Đánh dấu "Bạn"
                // Dùng getUserID2() làm ví dụ, thay bằng getUserID()
                if (player.getUserID2() == currentUserID) {
                    tvPlayerName.setText(player.getTenNguoiBan() + " (Bạn)");
                }
            }
        }
    }
}