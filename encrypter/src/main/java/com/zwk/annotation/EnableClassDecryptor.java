package com.zwk.annotation;

import com.zwk.processor.MyBeanFactoryPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MyBeanFactoryPostProcessor.class)
public @interface EnableClassDecryptor {
}
