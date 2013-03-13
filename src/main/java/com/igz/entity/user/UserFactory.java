package com.igz.entity.user;

import com.igz.java.gae.pattern.AbstractFactoryPlus;

public class UserFactory extends AbstractFactoryPlus<UserDto> {

    protected UserFactory() {
        super(UserDto.class);
    }
}
