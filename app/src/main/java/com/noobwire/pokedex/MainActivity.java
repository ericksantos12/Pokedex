package com.noobwire.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Pokemon> pokemonList = new ArrayList<>();
    private RecyclerView recyclerView;
    private PokemonAdapter adapter;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        adapter = new PokemonAdapter(pokemonList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        queue = Volley.newRequestQueue(this);
        String url ="https://pokeapi.co/api/v2/pokemon?limit=151";


        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        queue.add(request);

    }

    public void handleResponse(JSONObject response){
        try {
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject pokemonObject = results.getJSONObject(i);
                String name = pokemonObject.getString("name");
                String urlDetail = pokemonObject.getString("url");

                JsonObjectRequest requestDetail = new JsonObjectRequest
                        (Request.Method.GET, urlDetail, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        handleDetailResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
                queue.add(requestDetail);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleDetailResponse(JSONObject response){
        try {
            int id = response.getInt("id");
            String weight = String.valueOf(response.getDouble("weight")) + " kg";
            String height = String.valueOf(response.getDouble("height")) + " m";
            String name = String.valueOf(response.getJSONArray("forms").getJSONObject(0).getString("name"));

            List<String> stats = new ArrayList<>();
            for (int i = 0; i <= 5; i++) {
                stats.add(String.valueOf(response.getJSONArray("stats").getJSONObject(i).getInt("base_stat")));
            }

            String imageUrl = response.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").getString("front_default");

            List<String> types = new ArrayList<>();
            JSONArray typesArray = response.getJSONArray("types");
            for (int j = 0; j < typesArray.length(); j++) {
                types.add(typesArray.getJSONObject(j).getJSONObject("type").getString("name"));
            }

            Pokemon pokemon = new Pokemon.Builder()
                    .name(capitalizeFirstLetter(name))
                    .id(id)
                    .weight(weight)
                    .height(height)
                    .stats(stats)
                    .types(types)
                    .imageUrl(imageUrl)
                    .build();

            pokemonList.add(pokemon);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String capitalizeFirstLetter(String original) {
        if (original == null || original.isEmpty()) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}