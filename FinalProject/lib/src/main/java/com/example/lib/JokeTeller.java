package com.example.lib;

import java.util.Random;

public class JokeTeller {

    private String[] jokes;
    private Random random;

    public JokeTeller() {
        jokes = new String[5];
        jokes[0] = "Santa asked to Ramdev Baba- Baba I want to learn such Yoga\n" +
                "Which makes me owner of Patanjali Ayurved.";
        jokes[1] = "HELLO meri aavaj aa rahi hai.. Hello hello..?\n" +
                ". \n" +
                "kya aavaj nhi aa rhi hai?\n" +
                ".\n" +
                "ab.. ab bhi nahi.\n" +
                ".\n" +
                "abe dhaKKan ye SMS hai awaz kaha se aayegi.";
        jokes[2] = "Once der was a fight between Me and a Tiger...\n" +
                ".\n" +
                "I Ran away...\n" +
                "Why? \n" +
                ".\n" +
                "To Save d Tiger...\n" +
                "\n" +
                "Only 1411 r left!!! ;)\n" +
                "Otherwise u know me..";
        jokes[3]="A very small love story.. Boy:excuse me.! Girl-Yes, bro.!";
        jokes[4]="Sardar: Aap kitna padhe ho?\n" +
                "Friend: B.A.\n" +
                "Sardar: kamal karte ho yaar, sirf do word padhe aur woh bhi ulte.";
        random = new Random();
    }

    public String[] getJokes() {
        return jokes;
    }

    public String getRandomJoke() {
        return jokes[random.nextInt(jokes.length)];
    }

}
