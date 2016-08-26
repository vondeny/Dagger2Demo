package com.vondeny.demo.dagger2demo;

import javax.inject.Inject;

/**
 * Created by wendh1 on 2016/8/25.
 */
public class Person {
    private String name;
    private int age;

    @Inject
    public Person() {
        name = "Owen";
        age = 18;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
