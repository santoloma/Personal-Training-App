package com.trainchatapp.utils;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomTags {

    public static Random rand = new Random();

    public static String[]get3Tags(){


        List<String> allTs = Arrays.asList(new String[]{
                "Lower Body",
                "Squat",
                "Pelvis",
                "Squat",
                "Weight loss",
                "Post-pregnancy training",
                "General shape-up",
                "Revenge body",
                "+10 years experience",
                "Beginner course",
                "Full body",
                "Convicted rapist",
                "Certified",
                "Yoga",
                "Coronavirus immunity",
                "Immortality",
                "Penis aerobics",
                "Strength training",
                "Not to Hot Program",
                "Obesity reduction",
                "Fatphobic routine",
                "Military School Program"
        });
        ArrayList<String> allTags = new ArrayList<>(allTs);

        String tag1 = allTags.remove(rand.nextInt(allTags.size()));
        String tag2 = allTags.remove(rand.nextInt(allTags.size()));
        String tag3 = allTags.remove(rand.nextInt(allTags.size()));
        return new String[]{tag1, tag2, tag3};
    }
}
