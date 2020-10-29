package me.koply.sorustore.commands.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {
    String value();

    // Value içerisinde konsolda kullanılacak olan komut girilir.

    //@Retention(RetentionPolicy.RUNTIME)
    //@Target(ElementType.TYPE)
    // Bu iki annotation ise bu annotation'un compile edilmesini ve uygulamada aktif olarak kullanılabilmesini sağlar.
}