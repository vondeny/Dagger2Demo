package com.vondeny.demo.dagger2demo;

import javax.inject.Inject;

/**
 * Created by wendh1 on 2016/8/26.
 */
public class Book {
    private String name;
    private String author;
    private int price;

    @Inject
    public Book() {
        name = "Java";
        author = "Owen Smith";
        price = 78;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
