package com.example.menu.SQLite;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.menu.A07_RestaurantDetail.RestaurantDetailView;
import com.example.menu.R;
import com.squareup.picasso.Picasso;

import xyz.hanks.library.bang.SmallBangView;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private Context mContext;
    private Cursor mCursor;
    private MyDB myDB;
    public SmallBangView like_heart;

    public ItemAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
        myDB =new MyDB(mContext);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder  {

        public TextView nameText , noteText;
        public ImageView imagename;
        public SmallBangView like_heart;
        CardView cardView;

        private ItemViewHolder(View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.title_content);
            noteText = itemView.findViewById(R.id.subtitle_content);
            imagename = (ImageView) itemView.findViewById(R.id.image_content);
            like_heart = itemView.findViewById(R.id.like_heart);
            cardView = itemView.findViewById(R.id.search_bar);
            //heart = itemView.findViewById(R.id.image_heart);
//            timeText = itemView.findViewById(R.id.Time);
//            vidText = itemView.findViewById(R.id.vid);
//            realsec = itemView.findViewById(R.id.realsec);

        }




    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.a10_fav_listitem, parent, false);
        return new ItemViewHolder(view);
        //return null;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String name = mCursor.getString(mCursor.getColumnIndex(DBitem.KEY_NAME));
        String note = mCursor.getString(mCursor.getColumnIndex(DBitem.KEY_NOTE));
       String VID = mCursor.getString(mCursor.getColumnIndex(DBitem.KEY_PID));
        int id= mCursor.getInt(mCursor.getColumnIndex(DBitem.KEY_ID));

        //Log.d("BBBA",""+id);

       holder.nameText.setText(name);
       holder.noteText.setText(note);
       Picasso.get().load(VID).into(holder.imagename);
       //holder.imagename;
       holder.itemView.setTag(id);
       holder.like_heart.setSelected(true);


       holder.like_heart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               holder.like_heart.setSelected(false);
               if (holder.like_heart.isSelected()){

               }else{
                   holder.like_heart.likeAnimation(new AnimatorListenerAdapter() {
                       @Override
                       public void onAnimationEnd(Animator animation) {
                           super.onAnimationEnd(animation);
                           removeItem(id);
                           ItemAdapter.this.notifyDataSetChanged();
                       }
                   });
               }

           }
       });
       holder.cardView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(v.getContext(), RestaurantDetailView.class);
               i.putExtra("name",name);
               i.putExtra("note",note);
               i.putExtra("imgname",VID);
               mContext.startActivity(i);
           }
       });

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
    private void removeItem(long id) {

        myDB.deleteData(id);
        ItemAdapter.this.swapCursor(myDB.getData());
    }



}
