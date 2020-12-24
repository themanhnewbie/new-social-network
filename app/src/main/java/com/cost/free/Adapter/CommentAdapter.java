package com.cost.free.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.cost.free.Model.Comment;
import com.cost.free.Model.User;
import com.cost.free.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyHolder>{
    ArrayList<Comment> commentArrayList;
    Context context;

    public CommentAdapter(ArrayList<Comment> commentArrayList, Context context) {
        this.commentArrayList = commentArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.row_comment, parent, false);
        return new MyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.cCommentTv.setText(commentArrayList.get(position).getDescription());

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(commentArrayList.get(position).getTime()));
        String postTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();
        holder.cDateTv.setText(postTime);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference userRef = ref.child(commentArrayList.get(position).getUserId());
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    String userName = (String) user.getName();
                    if (userName.equals("")) userName = user.getEmail();
                    holder.cNameTv.setText(userName);
                    try {
                        Picasso.get().load(user.getImage()).placeholder(R.drawable.ic_default_img).into(holder.cAvatarIv);
                    } catch (NullPointerException npe) {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        CircularImageView cAvatarIv;
        TextView cNameTv;
        TextView cDateTv;
//        TextView cEmailTv;
        TextView cCommentTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            //comment : c ; field : Field ; text view : Tv ; image view : Iv
            cAvatarIv = (CircularImageView) itemView.findViewById(R.id.avatar_fri);
            cNameTv = (TextView) itemView.findViewById(R.id.name_fri);
            cDateTv = (TextView) itemView.findViewById(R.id.date_fri);
//            cEmailTv = (TextView) itemView.findViewById(R.id.email_fri);
            cCommentTv = (TextView) itemView.findViewById(R.id.comment_fri);
        }
    }

}
