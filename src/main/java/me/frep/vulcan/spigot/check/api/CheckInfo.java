package me.frep.vulcan.spigot.check.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.TYPE})
public @interface CheckInfo {
    public String name();

    public char type();

    public String complexType() default "N/";

    public boolean experimental() default false;

    public boolean punish() default true;

    public String description() default "No description.";
}

