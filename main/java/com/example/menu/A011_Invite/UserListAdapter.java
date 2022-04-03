package com.example.menu.A011_Invite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.R;
import com.example.menu.classes.Tracking;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class UserListAdapter extends FirebaseRecyclerAdapter<Tracking,UserListAdapter.viewHolder> {
    private Context mCtx;

    public UserListAdapter(Context mCtx , FirebaseRecyclerOptions<Tracking> options) {
        super(options);
        this.mCtx = mCtx;

    }






    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.a11_invite_listitem, parent, false);

        return new viewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull viewHolder holder, int position, @NonNull Tracking model) {

        if(FirebaseAuth.getInstance().getCurrentUser()!=null&& FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(model.getEmail())){
            holder.layout.setVisibility(View.GONE);
        }else{
            holder.email.setText(model.getEmail());
            holder.uid.setText(this.getRef(position).getKey());
        }

    }

    class viewHolder extends RecyclerView.ViewHolder{
        TextView email,uid; LinearLayout layout;
        public viewHolder(View view){
            super(view);
            layout = view.findViewById(R.id.inviteitem);
            email = view.findViewById(R.id.inviteitem);

        }
    }

}
