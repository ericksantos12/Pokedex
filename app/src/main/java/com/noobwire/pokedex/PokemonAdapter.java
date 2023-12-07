package com.noobwire.pokedex;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PokemonAdapter extends RecyclerView.Adapter<PokemonAdapter.ViewHolder> {
    private List<Pokemon> pokemonList;

    public PokemonAdapter(List<Pokemon> pokemonList) {
        this.pokemonList = pokemonList;
    }

    @NonNull
    @Override
    // Método onCreateViewHolder, responsável por criar uma nova instância de ViewHolder.
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Cria uma nova View. Usa o LayoutInflater para inflar (criar) a View a partir de um layout XML.
        // Neste caso, está inflando o layout 'item_pokemon'.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pokemon, parent, false);

        // Cria um novo ViewHolder, passando a View que acabou de ser inflada.
        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém a posição do item no adaptador.
                int position = viewHolder.getAdapterPosition();

                // Usa a posição para obter o Pokémon correspondente da lista 'pokemonList'.
                Pokemon pokemon = pokemonList.get(position);

                // Inicia uma nova Activity usando a intenção (Intent) que é retornada pelo método 'getIntent' do Pokémon.
                v.getContext().startActivity(pokemon.getIntent(v.getContext()));
            }
        });

        // Retorna o ViewHolder criado. Este ViewHolder será usado pelo RecyclerView para exibir um item.
        return viewHolder;
    }


    @Override
    // Método onBindViewHolder, responsável por vincular os dados a uma View (ViewHolder) em uma posição específica.
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Criação de um Map para mapear os tipos de Pokémon a uma cor específica.
        Map<String, Integer> colorMap = new HashMap<>();
        colorMap.put("bug", R.color.bug);
        colorMap.put("dark_type", R.color.dark_type);
        colorMap.put("dragon", R.color.dragon);
        colorMap.put("electric", R.color.electric);
        colorMap.put("fairy", R.color.fairy);
        colorMap.put("fighting", R.color.fighting);
        colorMap.put("fire", R.color.fire);
        colorMap.put("flying", R.color.flying);
        colorMap.put("ghost", R.color.ghost);
        colorMap.put("normal", R.color.normal);
        colorMap.put("grass", R.color.grass);
        colorMap.put("ground", R.color.ground);
        colorMap.put("ice", R.color.ice);
        colorMap.put("poison", R.color.poison);
        colorMap.put("psychic", R.color.psychic);
        colorMap.put("rock", R.color.rock);
        colorMap.put("steel", R.color.steel);
        colorMap.put("water", R.color.water);

        // Obtém o objeto Pokémon na posição específica da lista.
        Pokemon pokemon = pokemonList.get(position);
        holder.nameTextView.setText(pokemon.getName());
        holder.idTextView.setText(String.format("#%03d", pokemon.getId()));

        // Usa a biblioteca Glide para carregar a imagem do Pokémon de uma URL e exibi-la no ImageView do ViewHolder.
        Glide.with(holder.itemView.getContext())
                .load(pokemon.getImageUrl())
                .into(holder.imageView);

        // Remove todas as visualizações (chips) existentes no ChipGroup do ViewHolder.
        holder.typeChipGroup.removeAllViews();

        // Itera sobre os tipos do Pokémon e para cada tipo:
        for (String type : pokemon.getTypes()) {
            Chip chip = new Chip(holder.itemView.getContext());
            chip.setText(type);
            chip.setChipBackgroundColorResource(colorMap.get(type));
            chip.setTextColor(Color.WHITE);

            // Adiciona o Chip ao ChipGroup no ViewHolder.
            holder.typeChipGroup.addView(chip);
        }
    }

    @Override
    public int getItemCount() {
        return pokemonList.size();
    }

    // Classe ViewHolder, que estende RecyclerView.ViewHolder.
    // ViewHolder fornece uma referência para as views para cada item de dados.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTextView;
        public TextView idTextView;
        public ImageView imageView;
        public ChipGroup typeChipGroup;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            idTextView = itemView.findViewById(R.id.id_text_view);
            imageView = itemView.findViewById(R.id.image_view);
            typeChipGroup = itemView.findViewById(R.id.type_chip_group);
        }
    }
}