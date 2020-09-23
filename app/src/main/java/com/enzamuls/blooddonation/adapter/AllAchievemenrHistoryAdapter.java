package com.enzamuls.blooddonation.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.enzamuls.blooddonation.R;
import com.enzamuls.blooddonation.model.userachievements;

public class AllAchievemenrHistoryAdapter extends FirestoreRecyclerAdapter<userachievements, AllAchievemenrHistoryAdapter.AchievementViewHolder> {

    private OnAchievementClickListener onAchievementClickListener;

    public AllAchievemenrHistoryAdapter(@NonNull FirestoreRecyclerOptions<userachievements> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull AchievementViewHolder holder, final int position, @NonNull userachievements model) {
        holder.donatedPatientName.setText("Donated To: "+model.getPatientName());
        holder.donatedLocation.setText("Location: "+model.getGivenLocation());
        holder.donatedDate.setText("Date: "+model.getDonateDate());

        holder.editAchievement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position != RecyclerView.NO_POSITION && onAchievementClickListener != null){
                    onAchievementClickListener.OnAchivementClickEdit(getSnapshots().getSnapshot(position), position);
                }
            }
        });
    }

    @NonNull
    @Override
    public AchievementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.achievement_history_view, parent,false);
        return new AchievementViewHolder(view);
    }

    public class AchievementViewHolder extends RecyclerView.ViewHolder{

        TextView donatedPatientName, donatedLocation, donatedDate, editAchievement;

        public AchievementViewHolder(@NonNull View itemView) {
            super(itemView);

            donatedPatientName = itemView.findViewById(R.id.achDonatedPatientName);
            donatedLocation = itemView.findViewById(R.id.achDonatedLocation);
            donatedDate = itemView.findViewById(R.id.achMyDonatedDate);
            editAchievement = itemView.findViewById(R.id.achievementEdit);
        }
    }

    public interface OnAchievementClickListener{
        void OnAchivementClickEdit(DocumentSnapshot documentSnapshot, int position);
    }
    public void setOnAchievementClickListener(OnAchievementClickListener onAchievementClickListener){
        this.onAchievementClickListener = onAchievementClickListener;
    }
}
