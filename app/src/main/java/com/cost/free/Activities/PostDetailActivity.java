package com.cost.free.Activities;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.cost.free.Adapter.CommentAdapter;
import com.cost.free.Adapter.PostAdapter;
import com.cost.free.Model.Comment;
import com.cost.free.Model.Post;
import com.cost.free.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostDetailActivity extends BaseActivity {
    //Comment Array From Database
    ArrayList<Comment> commentArrayList;
    CommentAdapter commentAdapter;

    List<Post> postList;
    PostAdapter postAdapter;
    //RecyclerView
    RecyclerView recyclerView, postRecyclerView;
    //post on click
    String postId;
    //progress bar
    ProgressDialog progressDialog;
    //Edit comment view
    EditText commentEt;
    ImageButton sendBtn;
    CircularImageView cAvatarIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        //Check current user status
        checkUserStatus();
        //Action bar
        setupActionBar();
        //Initial field
        InitField();
        //Init View
        initView();
        //Comment list recycler view
        initCommentRecyclerView();
        //Load current post
        loadClickedPost();
        //Load current user on comment prompt
        loadUserInfo();
        //Load Comments
        loadComments();
        //On click handler
        initOnClick();
    }

    private void setupActionBar() {
        setActionBarTitle("Post Detail","SignedIn as "+ currentUser.getEmail());
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void initView() {
        //user view
        cAvatarIv = findViewById(R.id.cAvatarIv);

        //comment post
        commentEt = findViewById(R.id.commentEt);
        sendBtn = findViewById(R.id.sendBtn);
    }

    private void InitField() {
        this.postId = this.getIntent().getStringExtra("postId");
        this.commentArrayList = new ArrayList<>();
        postList = new ArrayList<>();
    }

    private void initCommentRecyclerView() {

        postRecyclerView = findViewById(R.id.post_comment_recyclerView);
        postRecyclerView.setHasFixedSize(true);

        recyclerView = (RecyclerView)findViewById(R.id.comment_recycler_view);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        postRecyclerView.setLayoutManager(layoutManager);
        postRecyclerView.addItemDecoration(dividerItemDecoration);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        recyclerView.setLayoutManager(layoutManager1);
    }

    private void loadComments() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Comments");
        DatabaseReference postRef = ref.child(postId);
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                commentArrayList.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

                    Comment comment = ds.getValue(Comment.class);

                    commentArrayList.add(comment);

                    commentAdapter = new CommentAdapter(commentArrayList, PostDetailActivity.this);

                    recyclerView.setAdapter(commentAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PostDetailActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadClickedPost() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = ref.child("Posts");
        postRef.child(postId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post clickedPost = dataSnapshot.getValue(Post.class);

                postList.add(clickedPost);

                postAdapter = new PostAdapter(PostDetailActivity.this, postList);

                postRecyclerView.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                makeToast("Fail loading clicked post...");

            }
        });

    }

    private void loadUserInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String currentUserPhoto = (String) snapshot.child("image").getValue();
                try{
                    Picasso.get().load(currentUserPhoto).placeholder(R.drawable.ic_default).into(cAvatarIv);
                }
                catch(Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initOnClick() {

        //Send button on comment post bar
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });
    }


    private void postComment() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding new comment..");

        Comment comment = new Comment();
        //Current user
        comment.setUserId(this.currentUser.getUid());
        //Time
        comment.setTime(String.valueOf(System.currentTimeMillis()));
        //Comment id
        comment.setCommentId(String.format("%s%s", comment.getTime(), comment.getUserId()));
        //Comment description
        String commentDescription = commentEt.getText().toString().trim();
        if(commentDescription.isEmpty()){
            makeToast("Comment is empty..");
            return;
        } else {
            comment.setDescription(commentDescription);
        }
        //Parent post id
        comment.setPostId(postId);

        //Add comment to database
        DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference("Comments").child(postId);
        commentRef.child(comment.getCommentId()).setValue(comment.toMap())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        makeToast("Comment added");
                        updateCommentsCount(commentRef);
                    }
                })
                .addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        makeToast(""+e.getMessage());
                    }
                });

        progressDialog.dismiss();
        commentEt.setText("");
    }

    private void updateCommentsCount(DatabaseReference commentRef) {
        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        commentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String comments = ""+ snapshot.getChildrenCount();
                postRef.child("commentCount").setValue(""+comments);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.findItem(R.id.action_post).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_create_group).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }


}