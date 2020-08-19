package com.trainchatapp.utils;

import java.util.Random;

public class RandomQuotesGenerator {

    static  Random r = new Random();

    static String[] quotes = new String[]{
            "“All our dreams can come true, if we have the courage to pursue them.”\n – Walt Disney",
            "“The secret of getting ahead is getting started.”\n – Mark Twain",
            "“If people are doubting how far you can go, go so far that you can’t hear them anymore.”\n – Michele Ruiz",
            "“Happiness is not something ready made. It comes from your own actions.”\n ― Dalai Lama XIV",
            "“Magic is believing in yourself. If you can make that happen, you can make anything happen.”\n – Johann Wolfgang Von Goethe",
            "“Hold the vision, trust the process.”",
            "“Don’t be afraid to give up the good to go for the great.”\n – John D. Rockefeller",
            "“People who wonder if the glass is half empty or full miss the point. The glass is refillable.”",
            "“One day or day one. You decide.”",
            "“All progress takes place outside the comfort zone.”\n — Michael John Bobak",
            "“The only place where success comes before work is in the dictionary.”\n — Vidal Sassoon",
            "“Whether you think you can, or you think you can’t, you’re right.”\n — Henry Ford",
            "“You must expect great things of yourself before you can do them.”\n — Michael Jordan",
            "“What hurts today makes you stronger tomorrow.”\n — Jay Cutler",
            "“Things work out best for those who make the best of how things work out.”\n — John Wooden",
            "“If something stands between you and your success, move it. Never be denied.”\n— Dwayne ‘The Rock’ Johnson"
    };

    public static String getQuote(){
        return quotes[r.nextInt(quotes.length)];
    }
}
