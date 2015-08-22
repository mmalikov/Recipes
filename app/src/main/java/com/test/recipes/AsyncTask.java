package com.test.recipes;

import android.content.Context;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by mmalykov on 8/21/15.
 */
abstract public class AsyncTask extends android.os.AsyncTask<JSONObject, Void, JSONObject> {

    private Context mContext;
    private View mView;

    AsyncTask( Context context, View view){
        mContext = context;
        mView = view;
    }

    abstract protected JSONObject do_In_Background(JSONObject... params) throws Exception;

    abstract protected void on_Post_Execute(JSONObject result);

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        JSONObject result = null;
        try {
            result = do_In_Background(params[0]);
        } catch (Exception e) {
            Log.i("RecipeAPI", "doInBackground Problem: " + e.getMessage());
        }
        if (result != null) Log.i("TAG", result.toString());
        else Log.i("TAG", "doInBackground = null");


        return result;
    }

    @Override
    protected void onPostExecute(JSONObject result) {
        if (result == null) {
            Log.i("TAG", "JsonObject result = null");
            return;
        }
        on_Post_Execute(result);
    }
}

