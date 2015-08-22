package com.test.recipes;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by mmalykov on 8/21/15.
 */
public class ShowRecipeActivity extends ActionBarActivity {

    private LinearLayout mRoot;

    private String TAG = "ShowRecipeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_recipe_layout);

        mRoot = (LinearLayout) findViewById(R.id.container);

        TextView mImageURL = (TextView) findViewById(R.id.image_url);
        TextView mSourceURL = (TextView) findViewById(R.id.source_url);
        TextView mF2fURL = (TextView) findViewById(R.id.f2f_url);
        TextView mTitle = (TextView) findViewById(R.id.title);
        TextView mPublisher = (TextView) findViewById(R.id.publisher);
        TextView mPublisherURL = (TextView) findViewById(R.id.publisher_url);
        TextView mSocialRank = (TextView) findViewById(R.id.social_rank);
        TextView mIngredients = (TextView) findViewById(R.id.ingredients);

        mImageURL.setText("BLA LAV");
        mSourceURL.setText("BLA LAV");
        mF2fURL.setText("BLA LAV");
        mTitle.setText("BLA LAV");
        mPublisher.setText("BLA LAV");
        mPublisherURL.setText("BLA LAV");
        mSocialRank.setText("BLA LAV");
        //mIngredients.setText(ingr.toString());

        String recipe_id = getIntent().getStringExtra(MainActivity.RECIPE);

        RecipeAPI api = RecipeAPI.getInstance(getApplicationContext());
        api.getRecipe(mRoot, recipe_id);
        Log.i("AAAA","after getRecipe()");
    }
}
