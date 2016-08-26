package com.vondeny.demo.dagger2demo;

import dagger.Module;
import dagger.Provides;

/**
 * Created by wendh1 on 2016/8/26.
 */
@Module
public class BookModule {
    @Provides
    public Book provideBook() {
        Book book = new Book();
        book.setName("C/C++");
        book.setAuthor("Jordon");
        book.setPrice(10);
        return book;
    }
}
