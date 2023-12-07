package com.noobwire.pokedex;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

public class Pokemon {
    private String name;
    private int id;
    private String weight;
    private String height;
    private List<String> stats;
    private String imageUrl;
    private List<String> types;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public List<String> getStats() {
        return stats;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getTypes() {
        return types;
    }

    private Pokemon(Builder builder) {
        this.name = builder.name;
        this.id = builder.id;
        this.weight = builder.weight;
        this.height = builder.height;
        this.stats = builder.stats;
        this.imageUrl = builder.imageUrl;
        this.types = builder.types;
    }


    public static class Builder {
        private String name;
        private int id;
        private String weight;
        private String height;
        private List<String> stats;
        private String imageUrl;
        private List<String> types;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder weight(String weight) {
            this.weight = weight;
            return this;
        }

        public Builder height(String height) {
            this.height = height;
            return this;
        }

        public Builder stats(List<String> stats) {
            this.stats = stats;
            return this;
        }

        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder types(List<String> types) {
            this.types = types;
            return this;
        }

        public Pokemon build() {
            return new Pokemon(this);
        }
    }

    public Intent getIntent(Context context) {
        Intent intent = new Intent(context, PokemonStatsActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("id", id);
        intent.putExtra("imageUrl", imageUrl);
        intent.putStringArrayListExtra("types", new ArrayList<String>(types));
        intent.putStringArrayListExtra("stats", new ArrayList<String>(stats));
        intent.putExtra("weight", weight);
        intent.putExtra("height", height);

        return intent;
    }
}
