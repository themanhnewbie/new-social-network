package com.cost.free.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cost.free.Activities.GroupActivity;
import com.cost.free.Activities.PeopleActivity;
import com.cost.free.Model.Group;
import com.cost.free.Model.User;
import com.cost.free.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyHolder> {

    Context context;
    List<Group> groupList;

    public GroupAdapter(Context context, List<Group> groupList) {
        this.context = context;
        this.groupList = groupList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_user, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String groupImage = groupList.get(position).getImage();
        String groupName = groupList.get(position).getName();
        String adminId = groupList.get(position).getAdminId();
        String groupId = groupList.get(position).getGroupId();

        DatabaseReference adminRef = FirebaseDatabase.getInstance().getReference();
        adminRef.child("Users").child(adminId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String adminEmail = (String) snapshot.child("email").getValue();
                holder.adminEmail.setText(adminEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.groupName.setText(groupName);

        try {
            Picasso.get().load(groupImage).placeholder(R.drawable.ic_default_img).into(holder.groupImage);
        }
        catch (Exception e) {

        }

        holder.groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, GroupActivity.class);
                intent.putExtra("groupId", groupId);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        ImageView groupImage;
        TextView groupName, adminEmail;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            groupImage = itemView.findViewById(R.id.avatar_fri);
            groupName = itemView.findViewById(R.id.name_fri);
            adminEmail = itemView.findViewById(R.id.email_fri);

        }
    }
}
