package org.rdlinux;

import lombok.Getter;
import lombok.Setter;
import org.junit.Assert;
import org.junit.Test;
import org.rdlinux.ezmybatis.core.FnField;

public class FnFieldTest {
    @Test
    public void shouldResolveGetterNamesToFields() {
        Assert.assertEquals("name", FnField.of(User::getName));
        Assert.assertEquals("first_name", FnField.of(User::getFirst_name));
        Assert.assertEquals("lastName", FnField.of(User::getLastName));
        Assert.assertEquals("woman", FnField.of(User::isWoman));
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
