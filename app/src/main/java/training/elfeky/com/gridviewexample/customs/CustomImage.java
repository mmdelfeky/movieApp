package training.elfeky.com.gridviewexample.customs;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CustomImage extends ImageView
{
    public CustomImage(Context context)
    {
        super(context);
    }

    public CustomImage(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public CustomImage(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int)(getMeasuredHeight()*1.5)); //Snap to width
    }
}