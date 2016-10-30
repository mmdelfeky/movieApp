package training.elfeky.com.gridviewexample.customs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import training.elfeky.com.gridviewexample.R;
import training.elfeky.com.gridviewexample.main.MovieDetails;

/**
 * Created by f on 20/08/2016.
 */
public class ImageAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    Context mContext;
   List<MovieDetails> mThumbIds;
    public ImageAdapter(Context context ,List<MovieDetails> mThumbIds  ) {
        mContext = context;
        this.mThumbIds = mThumbIds;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mThumbIds.size();
    }

    @Override
    public MovieDetails getItem(int position) {
        return mThumbIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ImageView imageView;
        if(v == null)
        {
            v = inflater.inflate(R.layout.gridview_item, parent, false);
            v.setTag(R.id.picture, v.findViewById(R.id.picture));
        }
        imageView = (ImageView)v.getTag(R.id.picture);
        Picasso.with(mContext).load(mThumbIds.get(position).getPosterLink()).into(imageView);
        return v;


    }
}
