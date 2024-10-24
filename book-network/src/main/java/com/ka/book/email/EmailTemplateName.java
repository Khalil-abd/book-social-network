package com.ka.book.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATE_ACOUNT("activate_account");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
