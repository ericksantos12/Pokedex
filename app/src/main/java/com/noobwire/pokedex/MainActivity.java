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
        // Cria um adaptador para os Pokémon usando a lista de pokémons e o associa à RecyclerView
        adapter = new PokemonAdapter(pokemonList);
        // Define o adaptador da RecyclerView
        recyclerView.setAdapter(adapter);
        // Configura o gerenciador de layout para a RecyclerView, definindo como linear (uma lista vertical)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cria uma fila de requisições usando a biblioteca Volley para gerenciar solicitações HTTP
        queue = Volley.newRequestQueue(this);
        // Define a URL para a API do PokeAPI, solicitando os primeiros 151 Pokémon
        String url = "https://pokeapi.co/api/v2/pokemon?limit=151";

        // Cria uma solicitação de objeto JSON para obter dados da PokeAPI
        JsonObjectRequest request = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Lida com a resposta recebida da PokeAPI
                        handleResponse(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Imprime o rastreamento de pilha do erro
                        error.printStackTrace();
                    }
                });

        // Adiciona a solicitação à fila do Volley para ser executada
        queue.add(request);

    }

    public void handleResponse(JSONObject response) {
        try {
            // Extrai o array 'results' do objeto JSON da resposta
            JSONArray results = response.getJSONArray("results");
            // Itera por cada elemento do array 'results'
            for (int i = 0; i < results.length(); i++) {
                JSONObject pokemonObject = results.getJSONObject(i);
                String name = pokemonObject.getString("name");
                String urlDetail = pokemonObject.getString("url");

                // Cria uma nova solicitação de objeto JSON para obter detalhes específicos do Pokémon
                JsonObjectRequest requestDetail = new JsonObjectRequest
                        (Request.Method.GET, urlDetail, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Lida com a resposta dos detalhes do Pokémon
                                handleDetailResponse(response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });
                // Adiciona a solicitação de detalhes à fila do Volley para execução
                queue.add(requestDetail);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void handleDetailResponse(JSONObject response) {
        try {
            // Extrai o ID, peso, altura e nome do Pokémon da resposta JSON
            int id = response.getInt("id");
            String weight = String.valueOf(response.getDouble("weight")) + " kg";
            String height = String.valueOf(response.getDouble("height")) + " m";
            String name = String.valueOf(response.getJSONArray("forms").getJSONObject(0).getString("name"));

            // Cria uma lista para armazenar as estatísticas base do Pokémon
            List<String> stats = new ArrayList<>();
            for (int i = 0; i <= 5; i++) {
                stats.add(String.valueOf(response.getJSONArray("stats").getJSONObject(i).getInt("base_stat")));
            }

            // Extrai a URL da imagem oficial do Pokémon
            String imageUrl = response.getJSONObject("sprites").getJSONObject("other").getJSONObject("official-artwork").getString("front_default");

            // Cria uma lista para armazenar os tipos do Pokémon
            List<String> types = new ArrayList<>();
            JSONArray typesArray = response.getJSONArray("types");

            // Itera por cada tipo do Pokémon
            for (int j = 0; j < typesArray.length(); j++) {
                types.add(typesArray.getJSONObject(j).getJSONObject("type").getString("name"));
            }

            // Cria um objeto Pokémon usando o padrão Builder
            Pokemon pokemon = new Pokemon.Builder()
                    .name(capitalizeFirstLetter(name)) // Capitaliza a primeira letra do nome
                    .id(id)
                    .weight(weight)
                    .height(height)
                    .stats(stats)
                    .types(types)
                    .imageUrl(imageUrl)
                    .build();

            // Adiciona o objeto Pokémon à lista de Pokémon
            pokemonList.add(pokemon);
            // Notifica o adaptador que os dados mudaram para atualizar a visualização
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