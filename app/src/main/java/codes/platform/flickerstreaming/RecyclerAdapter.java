package codes.platform.flickerstreaming;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ahmed on 23/03/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.PhotoHolder> {

    private ArrayList<Image> mPhotos;

    private static Context context;

    public RecyclerAdapter(Context context ,ArrayList<Image> photos) {
        mPhotos = photos;
        this.context =context;
    }

    @Override
    public RecyclerAdapter.PhotoHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_layout, parent, false);



        return new PhotoHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.PhotoHolder holder, int position) {


        Image itemPhoto = mPhotos.get(position);
        holder.bindPhoto(itemPhoto);
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }



    public static class PhotoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mItemImage;
        private TextView mItemDate;

        private Image mPhoto;

        public PhotoHolder(View v) {
            super(v);

            mItemImage = (ImageView) v.findViewById(R.id.imageview);
            mItemDate = (TextView) v.findViewById(R.id.text_title);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }


        public void bindPhoto(Image image) {

            Drawable drawable = image.getDrawable(context);

            mItemImage.setImageDrawable(drawable);
            mItemDate.setText(image.getTitle());
        }

    }
}
