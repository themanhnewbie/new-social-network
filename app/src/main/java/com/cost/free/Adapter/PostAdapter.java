package com.cost.free.Adapter;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cost.free.Model.Post;
import com.cost.free.Activities.PostActivity;
import com.cost.free.R;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class PostAdapter  extends RecyclerView.Adapter<PostAdapter.MyHolder>{

    Context context;
    List<Post> postList;

    public PostAdapter(Context context, List<Post> postList) {
        this.context = context;
        this.postList = postList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_post, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        String uid = postList.get(position).getUid();
        String uEmail = postList.get(position).getuEmail();
        String uName = postList.get(position).getuName();
        String uDp = postList.get(position).getuDp();
        String pId = postList.get(position).getpId();
        String pTitle = postList.get(position).getpTitle();
        String pDescr = postList.get(position).getpDescr();
        String pImage = postList.get(position).getpImage();
        String pTimeStamp = postList.get(position).getpTime();

        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String postTime = DateFormat.format("dd/MM/yyyy hh:mm aa", calendar).toString();

        holder.usName.setText(uName);
        holder.postTime.setText(postTime);
        holder.postTitle.setText(pTitle);
        holder.postDes.setText(pDescr);

        try {
            Picasso.get().load(uDp).placeholder(R.drawable.ic_default_img).into(holder.usPic);
        }
        catch (Exception e) {

        }

        if (pImage.equals("noImage")) {
            holder.postImage.setVisibility(View.GONE);
        }
        else {

            try {
                Picasso.get().load(pImage).into(holder.postImage);
            }
            catch (Exception e) {

            }
        }


        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "More", Toast.LENGTH_SHORT).show();

            }
        });

        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Comment", Toast.LENGTH_SHORT).show();

            }
        });

        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show();

            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder{

        ImageView usPic, postImage;
        TextView usName, postTime, postTitle, postDes, postLike;
        ImageButton moreBtn;
        Button likeBtn, commentBtn, shareBtn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            usPic = itemView.findViewById(R.id.uPic);
            postImage = itemView.findViewById(R.id.pImage);
            usName = itemView.findViewById(R.id.uName);
            postTime = itemView.findViewById(R.id.pTime);
            postTitle = itemView.findViewById(R.id.pTitle);
            postDes = itemView.findViewById(R.id.pDes);
            postLike = itemView.findViewById(R.id.pLike);
            moreBtn = itemView.findViewById(R.id.more_btn);
            likeBtn = itemView.findViewById(R.id.like_btn);
            commentBtn = itemView.findViewById(R.id.comment_btn);
            shareBtn = itemView.findViewById(R.id.share_btn);

        }
    }
}
