package com.vondeny.demo.dagger2demo;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wendh1 on 2016/8/26.
 */
@Module
public class PersonModule {
    @Provides
    public Person providerPerson(Book book) {
        Person person = new Person();
        person.setName(book.getAuthor());
        person.setAge(45);
        return person;
    }
}
