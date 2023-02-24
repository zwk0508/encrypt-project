package com.zwk.processor;

import com.zwk.sacnner.MyScanner;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

/**
 * @author zwk
 * @version 1.0
 * @date 2023/2/24 14:02
 */

public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (beanFactory instanceof BeanDefinitionRegistry) {
            new MyScanner((BeanDefinitionRegistry) beanFactory, true).scan("com.zwk");
        }
    }
}
