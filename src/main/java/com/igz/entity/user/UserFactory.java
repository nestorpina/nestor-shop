package com.igz.entity.user;

import com.igzcode.java.gae.pattern.AbstractFactory;

public class UserFactory extends AbstractFactory<UserDto> {

    protected UserFactory() {
        super(UserDto.class);
    }
}
