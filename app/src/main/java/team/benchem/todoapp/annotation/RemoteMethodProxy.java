package team.benchem.todoapp.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import team.benchem.todoapp.lang.RequestType;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RemoteMethodProxy {

    String serviceKey();

    String path();

    RequestType type() default RequestType.GET;

    boolean enableCache() default false;

    int cacheExpireSeconds() default 60;

}
