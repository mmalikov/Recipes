package com.test.recipes;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class RecipesListAdapter extends BaseAdapter {

    private String TAG = "RecipesListAdapter";

    private ArrayList<JSONObject> mList;
    private LayoutInflater mInflater;

    RecipesListAdapter(Context context, ArrayList<JSONObject> list) {
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mList = list;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        view = mInflater.inflate(R.layout.list_item, null);

        TextView publisher = (TextView) view.findViewById(R.id.publisher);
        TextView f2f_url = (TextView) view.findViewById(R.id.f2f_url);
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView source_url = (TextView) view.findViewById(R.id.source_url);
        TextView recipe_id = (TextView) view.findViewById(R.id.recipe_id);
        TextView image_url = (TextView) view.findViewById(R.id.image_url);
        TextView social_rank = (TextView) view.findViewById(R.id.social_rank);
        TextView publisherURL = (TextView) view.findViewById(R.id.publisher_url);
        TextView page = (TextView) view.findViewById(R.id.page);

        try {

            Log.i(TAG, mList.get(position).toString());

            publisher.setText(mList.get(position).getString("publisher"));
            publisherURL.setText(mList.get(position).getString("publisher_url"));
            social_rank.setText(mList.get(position).getString("social_rank"));
            f2f_url.setText(mList.get(position).getString("f2f_url"));
            title.setText(mList.get(position).getString("title"));
            source_url.setText(mList.get(position).getString("source_url"));

            Log.i(TAG, "recipe_id: " + mList.get(position).getString("recipe_id")
                    + "\nimage_url: " + mList.get(position).getString("image_url"));

            recipe_id.setText(mList.get(position).getString("recipe_id"));
            image_url.setText(mList.get(position).getString("image_url"));

            page.setText(mList.get(position).getString("page"));


        } catch (JSONException e) {
            Log.e(TAG, "JSONException: " + e.getMessage());
        }
        return view;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }
}
