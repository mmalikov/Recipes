package com.test.recipes;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by mmalykov on 8/21/15.
 */
public class RecipeAPI {

    private static RecipeAPI instance;
    private View mView;
    private Context mContext;

    private String API_KEY = "487e9fd1f10444651765a4c765352628";
    private String SEARCH_REQUEST_URL = "http://food2fork.com/api/search";
    private String RECIPE_REQUEST_URL = "http://food2fork.com/api/get";

    private String TAG = "RecipeAPI";


    private RecipeAPI(Context context, View view) {
        mContext = context;
        mView = view;
    }

    private RecipeAPI(Context context) {
        mContext = context;
    }

    public static synchronized RecipeAPI getInstance(Context context, View view) {
        if (instance == null) {
            instance = new RecipeAPI(context, view);
        }
        return instance;
    }

    public static synchronized RecipeAPI getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeAPI(context);
        }
        return instance;
    }

    void search(String query, String sortKind, Integer numberOfPage) {
        /*  key: API Key
            q: (optional) Search Query (Ingredients should be separated by commas). If this is omitted top rated recipes will be returned.
            sort: (optional) How the results should be sorted. See Below for details.
            page: (optional) Used to get additional results
        */

        JSONObject postJSON = null;

        try {
            postJSON = new JSONObject();
            postJSON.put("key", API_KEY);
            postJSON.put("q", query);
            postJSON.put("sort", sortKind);
            postJSON.put("page", numberOfPage);
        } catch (JSONException e) {
            Log.e(TAG, "create json to post: exception: " + e.getMessage());
        }

        AsyncTask a = new AsyncTask(mContext, mView) {
            @Override
            protected JSONObject do_In_Background(JSONObject... post) throws Exception {

                String key = "?key=" + post[0].getString("key");
                String q = "&q=shredded%20chicken";
                String sort = "&sort=" + post[0].getString("sort");
                String page = "&page=" + post[0].getString("page");

                String url = SEARCH_REQUEST_URL + key + q + sort + page;

                JSONObject json = getDataFromServer(url);

                return json;
            }

            @Override
            protected void on_Post_Execute(JSONObject result) {
                ListView list = (ListView) mView;

                ArrayList<JSONObject> arrayList = new ArrayList<>();
                try {
                    JSONArray jsonArray = result.getJSONArray("recipes");
                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        arrayList.add(jsonArray.getJSONObject(i));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, e.getMessage());
                }

                RecipesListAdapter adapter = new RecipesListAdapter(mContext, arrayList);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, arrayList);

                list.setAdapter(adapter);

            }
        };

        a.execute(postJSON);
    }

    void getRecipe(View view, String recipe_id) {

        AsyncTask a = new AsyncTask(mContext, view) {
            @Override
            protected JSONObject do_In_Background(JSONObject... params) throws Exception {

                String url = RECIPE_REQUEST_URL + "?key=" + API_KEY + "&rId=" + params[0].getString("recipe_id");
                JSONObject j = getDataFromServer(url);

                return j;

            }

            @Override
            protected void on_Post_Execute(JSONObject result) {

                Log.i("AAAA", mView.getId() == R.id.container ? "OK" : "FAIL");
                JSONObject j = null;
                try {
                    j = result.getJSONObject("recipe");
                } catch (JSONException e) {
                    Log.e(TAG, "onPostExecute: JSON PROBLEM: " + e.getMessage());
                }

                TextView mImageURL = (TextView) mView.findViewById(R.id.image_url);
                TextView mSourceURL = (TextView) mView.findViewById(R.id.source_url);
                TextView mF2fURL = (TextView) mView.findViewById(R.id.f2f_url);
                TextView mTitle = (TextView) mView.findViewById(R.id.title);
                TextView mPublisher = (TextView) mView.findViewById(R.id.publisher);
                TextView mPublisherURL = (TextView) mView.findViewById(R.id.publisher_url);
                TextView mSocialRank = (TextView) mView.findViewById(R.id.social_rank);
                TextView mIngredients = (TextView) mView.findViewById(R.id.ingredients);

                try {
                    Log.i("AAAA", j.getString("image_url")
                            + '\n'
                            + j.getString("source_url")
                            + '\n'
                            + j.getString("f2f_url")
                            + '\n'
                            + j.getString("title")
                            + '\n'
                            + j.getString("publisher")
                            + '\n'
                            + j.getString("publisher_url")
                            + '\n'
                            + j.getString("social_rank")
                            + '\n'
                            + j.getString("ingredients"));

                    JSONArray ingr = j.getJSONArray("ingredients");
                    Log.i("AAAA", ingr.toString());

                    try {
                        if (mImageURL == null) Log.e("AAAA", "mImage: " + " NULL");
                        else if (mSourceURL == null) Log.e("AAAA", "mSourceURL: " + " NULL");
                        else if (mF2fURL == null) Log.e("AAAA", "mF2fURL: " + " NULL");
                        else if (mTitle == null) Log.e("AAAA", "mTitle: " + " NULL");
                        else if (mPublisher == null) Log.e("AAAA", "mPublisher: " + " NULL");
                        else if (mPublisherURL == null) Log.e("AAAA", "mPublisherURL: " + " NULL");
                        else if (mSocialRank == null) Log.e("AAAA", "mSocialRank: " + " NULL");
                        //else if (mIngredients == null) Log.e("AAAA", "mIngredients: " + " NULL");
                        else {
                            mImageURL.setText(j.getString("image_url"));
                            mSourceURL.setText(j.getString("source_url"));
                            mF2fURL.setText(j.getString("f2f_url"));
                            mTitle.setText(j.getString("title"));
                            mPublisher.setText(j.getString("publisher"));
                            mPublisherURL.setText(j.getString("publisher_url"));
                            mSocialRank.setText(j.getString("social_rank"));
                            //mIngredients.setText(ingr.toString());

                            String v = (String) mImageURL.getText()
                                    + '\n'
                                    +  mSourceURL.getText()
                                    + '\n'
                                    +  mF2fURL.getText()
                                    + '\n'
                                    +  mTitle.getText()
                                    + '\n'
                                    +  mPublisher.getText()
                                    + '\n'
                                    +  mPublisherURL.getText()
                                    + '\n'
                                    +  mSocialRank.getText();
                            Log.i("AAAA", "text in text views\n"+ v);
                        }
                    } catch (NullPointerException n) {
                        Log.e("AAAA", "NULL: " + n.getMessage());
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "onPostExecute: JSON PROBLEM: " + e.getMessage());
                }

                Log.i("AAAA", "end of PostExecute");
                return;
            }
        };

        try {
            JSONObject j = new JSONObject();
            j.put("recipe_id", recipe_id);
            a.execute(j);
            Log.i("AAAA", "end of try");
        } catch (JSONException e) {
            Log.e("AAAA", "problem with JSON: " + e.getMessage());
        }
        Log.i("AAAA", "end of AsyncTask");
    }

    JSONObject getDataFromServer(String url) {

        JSONObject json = null;

        URL obj = null;
        try {
            obj = new URL(url);
        } catch (MalformedURLException e) {
            Log.e("MIXSA", "problem with url: \n" + e.getMessage());
        }

        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) obj.openConnection();
        } catch (IOException e) {
            Log.e("MIXSA", "problem with connection: \n" + e.getMessage());
        }

        try {
            // optional default is GET
            connection.setRequestMethod("GET");
        } catch (ProtocolException e) {
            Log.e("MIXSA", "problem with protocol: \n" + e.getMessage());
        }
        //add request header
        // connection.setRequestProperty("User-Agent", USER_AGENT);

        StringBuffer response = null;
        try {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
        } catch (IOException e) {
            Log.e("MIXSA", "problem with READING: \n" + e.getMessage());
        }
        //print result
        if (response != null) {
            Log.i(TAG, response.toString());
            try {
                json = new JSONObject(response.toString());
            } catch (JSONException e) {
                Log.e("MIXSA", "problem with JSON: \n" + e.getMessage());
            }
        }

        return json;
    }
}
