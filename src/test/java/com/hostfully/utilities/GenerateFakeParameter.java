package com.hostfully.utilities;


import com.github.javafaker.Faker;

public class GenerateFakeParameter {


    public static String generateName() {


        Faker faker = new Faker();

        return faker.name().fullName().replaceAll("[^a-zA-Z]", "");
    }



}
