package com.vondeny.demo.dagger2demo;

import dagger.Component;

/**
 * Created by wendh1 on 2016/8/25.
 */
@Component(modules = {PersonModule.class, BookModule.class})
public interface MainActivityComponent {
    void inject(MainActivity mainActivity);
}
