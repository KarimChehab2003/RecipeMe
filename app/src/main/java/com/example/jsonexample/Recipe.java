package com.example.jsonexample;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class Recipe implements Serializable {
    public Integer id ;
    public String name;
    public String time;
    public String description;
    public List<String> instructions;
    public String videoURL;
    public String imageURL;
    public Map<String,Integer> nutritionFacts;
    public Float score;

    public Recipe(String name, Integer id) {
        this.name = name;
        this.id = id;
    }

    public Recipe(Integer id, String name, String time, String description, List<String> instructions, String videoURL, String imageURL, Map<String, Integer> nutritionFacts, Float score) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.description = description.replace("â€™","'");
        this.instructions = instructions;
        this.videoURL = videoURL;
        this.imageURL = imageURL;
        this.nutritionFacts = nutritionFacts;
        this.score = score;
    }

}
