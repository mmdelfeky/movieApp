package training.elfeky.com.gridviewexample.helpers;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.KeyEvent;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import training.elfeky.com.gridviewexample.fragments.FragmentInterface;

/**
 * Created by f on 21/08/2016.
 */
public class GetMovies extends AsyncTask<String,Void, String> {

    private ProgressDialog progressDialog;
    FragmentInterface fragment;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(fragment.getActivity(),"","Loading....",true,false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK)
                {
                    if(progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                return false;
            }
        });

    }

    public GetMovies(FragmentInterface fragment,ProgressDialog progressDialog)
    {
        this.fragment = fragment;
        this.progressDialog = progressDialog;
    }
    @Override
        protected  String doInBackground(String... params) {
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;


            try {



                URL url = new URL(params[0]);

                // Create the request to OpenWeatherMap, and open the connection
                try {


                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                }
                catch (Exception e)
                {
                }

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }

        try {
            return forecastJsonStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

        }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

            try {

                try {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                }
                catch (Exception e)
                {

                }
                fragment.getDataFromJson(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        catch (NullPointerException e)
        {
            try {
                Toast.makeText(fragment.getActivity().
                        getApplicationContext(), "There is Internet Connection  error", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e1)
            {

            }
        }
        catch (Exception e)
        {

        }

    }
}

