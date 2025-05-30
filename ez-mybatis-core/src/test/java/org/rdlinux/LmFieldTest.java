package org.rdlinux;

import lombok.Getter;
import lombok.Setter;
import org.rdlinux.ezmybatis.core.LmField;

public class LmFieldTest {
    public static void main(String[] args) {
        String field = LmField.of(User::getName);
        System.out.println(field);
        field = LmField.of(User::getFirst_name);
        System.out.println(field);
        field = LmField.of(User::getLastName);
        System.out.println(field);
        field = LmField.of(User::isWoman);
        System.out.println(field);
    }

    @Getter
    @Setter
    public static class User {
        private String name;
        private String first_name;
        private String lastName;
        private boolean woman;
    }
}
