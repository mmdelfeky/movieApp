package training.elfeky.com.gridviewexample.customs;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import training.elfeky.com.gridviewexample.R;
import training.elfeky.com.gridviewexample.main.MainActivity;

/**
 * Created by f on 11/09/2016.
 */
public class ReviewAdapter extends BaseAdapter{
    List<Review> reviews;
    Activity activity;
    public  ReviewAdapter (List<Review> reviews,Activity activity)
    {
        this.reviews = reviews;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView author;
        TextView content;
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.review, parent, false);
            author =(TextView) view.findViewById(R.id.authorTextView);
            content = (TextView)view.findViewById(R.id.reviewTextView);
            view.setTag(R.id.authorTextView,view.findViewById(R.id.authorTextView));
            view.setTag(R.id.reviewTextView,view.findViewById(R.id.reviewTextView));
        }
        else
        {
            author = (TextView)convertView.getTag(R.id.authorTextView);
            content = (TextView)convertView.getTag(R.id.reviewTextView);
        }
        author.setText(reviews.get(position).author);
        content.setText(reviews.get(position).content);
        return view;
    }


}

