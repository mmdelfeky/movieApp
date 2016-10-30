package training.elfeky.com.gridviewexample.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;

import org.json.JSONException;

import java.util.List;
import java.util.Objects;
import java.util.zip.Inflater;

import training.elfeky.com.gridviewexample.R;
import training.elfeky.com.gridviewexample.main.MainActivity;

/**
 * Created by f on 13/09/2016.
 */
public abstract class FragmentInterface extends Fragment {
    public abstract void update();
    public abstract void getDataFromJson(String forecastJsonStr)
            throws JSONException;
    protected abstract String urlBuilder(String baseUrl);
    ProgressDialog progressDialog;
    protected boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo!=null&&activeNetworkInfo.isConnected();
    }




}
