package com.noobwire.pokedex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PokemonStatsActivity extends AppCompatActivity {
    private LinearLayout card;
    private ImageView imageView;
    private TextView nameTextView, weightTextNumber, heightTextNumber, hpTextNumber, atkTextNumber, defTextNumber, spAtkTextNumber, spDefTextNumber, spdTextNumber, aboutText, baseStatsText, idTextView, movesTextView;
    private ProgressBar hpBar, atkBar, defBar, spAtkBar, spDefBar, spdBar;
    private ChipGroup typeChipGroup;

    private ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_stats);

        movesTextView = findViewById(R.id.pokemon_moves);
        idTextView = findViewById(R.id.pokemon_id);
        card = findViewById(R.id.pokemon_details);
        aboutText = findViewById(R.id.about_text);
        baseStatsText = findViewById(R.id.base_stats_text);
        backArrow = findViewById(R.id.arrow_back);
        imageView = findViewById(R.id.pokemon_image);
        nameTextView = findViewById(R.id.pokemon_name);
        hpBar = findViewById(R.id.hp_bar);
        atkBar = findViewById(R.id.atk_bar);
        defBar = findViewById(R.id.def_bar);
        spAtkBar = findViewById(R.id.satk_bar);
        spDefBar = findViewById(R.id.sdef_bar);
        spdBar = findViewById(R.id.spd_bar);
        weightTextNumber = findViewById(R.id.pokemon_weight);
        heightTextNumber = findViewById(R.id.pokemon_height);
        typeChipGroup = findViewById(R.id.pokemon_type_group);
        hpTextNumber = findViewById(R.id.hp_number);
        atkTextNumber = findViewById(R.id.atk_number);
        defTextNumber = findViewById(R.id.def_number);
        spAtkTextNumber = findViewById(R.id.satk_number);
        spDefTextNumber = findViewById(R.id.sdef_number);
        spdTextNumber = findViewById(R.id.spd_number);

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

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            int id = intent.getIntExtra("id", 0);
            String imageUrl = intent.getStringExtra("imageUrl");
            List<String> types = intent.getStringArrayListExtra("types");
            String weight = intent.getStringExtra("weight");
            String height = intent.getStringExtra("height");
            List<String> stats = intent.getStringArrayListExtra("stats");
            List<String> moves = intent.getStringArrayListExtra("moves");

            int typeColorId = colorMap.get(types.get(0));
            int typeColor = ContextCompat.getColor(this, typeColorId);

            Pokemon pokemon = new Pokemon.Builder()
                    .name(name)
                    .id(id)
                    .imageUrl(imageUrl)
                    .types(types)
                    .weight(weight)
                    .height(height)
                    .stats(stats)
                    .moves(moves)
                    .build();

            if (pokemon != null) {
                Glide.with(this)
                        .load(pokemon.getImageUrl())
                        .placeholder(R.drawable.silhouette)
                        .into(imageView);
                idTextView.setText(String.format("#%03d", pokemon.getId()));
                nameTextView.setText(pokemon.getName());
                weightTextNumber.setText(pokemon.getWeight());
                heightTextNumber.setText(pokemon.getHeight());

                String moveString = "";
                for(String move : pokemon.getMoves()){
                    if (moveString.equals("")) {
                        moveString = move;
                    } else {
                        moveString = moveString.concat("\n" + move);
                    }
                }

                movesTextView.setText(moveString);

                for (String type : types) {
                    Chip chip = new Chip(this);
                    chip.setText(type);
                    chip.setChipBackgroundColorResource(colorMap.get(type));
                    chip.setTextColor(Color.WHITE);
                    typeChipGroup.addView(chip);
                }

                card.setBackgroundColor(typeColor);
                aboutText.setTextColor(typeColor);
                baseStatsText.setTextColor(typeColor);

                hpBar.setProgress(Integer.parseInt(stats.get(0)));
                atkBar.setProgress(Integer.parseInt(stats.get(1)));
                defBar.setProgress(Integer.parseInt(stats.get(2)));
                spAtkBar.setProgress(Integer.parseInt(stats.get(3)));
                spDefBar.setProgress(Integer.parseInt(stats.get(4)));
                spdBar.setProgress(Integer.parseInt(stats.get(5)));

                hpBar.getProgressDrawable().setColorFilter(typeColor, PorterDuff.Mode.MULTIPLY);
                atkBar.getProgressDrawable().setColorFilter(typeColor, PorterDuff.Mode.MULTIPLY);
                defBar.getProgressDrawable().setColorFilter(typeColor, PorterDuff.Mode.MULTIPLY);
                spAtkBar.getProgressDrawable().setColorFilter(typeColor, PorterDuff.Mode.MULTIPLY);
                spDefBar.getProgressDrawable().setColorFilter(typeColor, PorterDuff.Mode.MULTIPLY);
                spdBar.getProgressDrawable().setColorFilter(typeColor, PorterDuff.Mode.MULTIPLY);


                hpTextNumber.setText(stats.get(0));
                atkTextNumber.setText(stats.get(1));
                defTextNumber.setText(stats.get(2));
                spAtkTextNumber.setText(stats.get(3));
                spDefTextNumber.setText(stats.get(4));
                spdTextNumber.setText(stats.get(5));

            }
        }

        backArrow.setOnClickListener(v -> {
            finish();
        });
    }
}