package com.zwk.sacnner;

import com.zwk.ClassDecryptor;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.context.annotation.ScannedGenericBeanDefinition;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.util.StreamUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zwk
 * @version 1.0
 * @date 2023/2/24 14:01
 */

public class MyScanner extends ClassPathBeanDefinitionScanner {
    public MyScanner(BeanDefinitionRegistry registry, boolean useDefaultFilters) {
        super(registry, useDefaultFilters);
    }

    @Override
    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        try {
            Resource[] resources = getResources(basePackage);
            CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();
            boolean traceEnabled = logger.isTraceEnabled();
            boolean debugEnabled = logger.isDebugEnabled();
            for (Resource resource : resources) {
                if (traceEnabled) {
                    logger.trace("Scanning " + resource);
                }
                try {
                    MetadataReader metadataReader = readerFactory.getMetadataReader(resource);
                    if (isCandidateComponent(metadataReader)) {
                        ScannedGenericBeanDefinition sbd = new ScannedGenericBeanDefinition(metadataReader);
                        sbd.setSource(resource);
                        if (isCandidateComponent(sbd)) {
                            if (debugEnabled) {
                                logger.debug("Identified candidate component class: " + resource);
                            }
                            candidates.add(sbd);
                        } else {
                            if (debugEnabled) {
                                logger.debug("Ignored because not a concrete top-level class: " + resource);
                            }
                        }
                    } else {
                        if (traceEnabled) {
                            logger.trace("Ignored because not matching any filter: " + resource);
                        }
                    }
                } catch (FileNotFoundException ex) {
                    if (traceEnabled) {
                        logger.trace("Ignored non-readable " + resource + ": " + ex.getMessage());
                    }
                } catch (Throwable ex) {
                    throw new BeanDefinitionStoreException(
                            "Failed to read candidate component class: " + resource, ex);
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }

    private Resource[] getResources(String basePackage) throws IOException {
        String resolveBasePackage = resolveBasePackage(basePackage);
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                resolveBasePackage + "/**/*.class.yj";
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);

        List<ByteArrayResource> byteArrayResources = new ArrayList<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (Resource resource : resources) {
            InputStream inputStream = resource.getInputStream();
            byte[] bytes = StreamUtils.copyToByteArray(inputStream);
            ByteArrayResource byteArrayResource = new ByteArrayResource(bytes, resource.getFilename());
            byteArrayResources.add(byteArrayResource);
            String filename = resource.getURI().toString();
            String className = resolveClassName(filename);
            byte[] byteArray = byteArrayResource.getByteArray();
            ClassDecryptor.defineClass(className, classLoader, byteArray, 0, byteArray.length);
        }
        return byteArrayResources.toArray(new Resource[0]);
    }

    private String resolveClassName(String filename) {
        int end = filename.indexOf(".class.yj");
        if (filename.startsWith("jar:")) {
            int i = filename.lastIndexOf("!");
            return filename.substring(i + 2, end);
        } else if (filename.startsWith("file:")) {
            String s = "/target/classes/";
            int i = filename.indexOf(s);
            return filename.substring(i + s.length(), end);
        }

        return null;
    }
}
