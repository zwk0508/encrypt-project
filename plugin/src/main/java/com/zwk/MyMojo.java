package com.zwk;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileFilter;
import java.nio.file.Files;
import java.util.List;

@Mojo(name = "encrypt", defaultPhase = LifecyclePhase.PROCESS_CLASSES)
public class MyMojo
        extends AbstractMojo {

    @Parameter(name = "location")
    private File location;
    @Parameter(name = "includes")
    private List<String> includes;
    @Parameter(name = "excludes")
    private List<String> excludes;

    public void execute()
            throws MojoExecutionException {
        if (location == null) {
            return;
        }
        if (includes == null) {
            return;
        }
        File[] files = location.listFiles(filter);
        if (files == null) {
            return;
        }
        for (File file : files) {
            try {
                handleFile(file, null, false);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    FileFilter filter = file -> file.isDirectory() || file.getName().endsWith(".class");

    private void handleFile(File f, String p, boolean include) throws Exception{
        String name = f.getName();
        p = p == null ? name : p + "." + name;
        for (String s : includes) {
            if (p.startsWith(s)) {
                include = true;
                break;
            }
        }
        if (f.isFile()) {
            if (include) {
                File dest = new File(f.getParent(), name + ".yj");
                byte[] bytes = Files.readAllBytes(f.toPath());
                Encryptor.encrypt(bytes);
                Files.write(dest.toPath(), bytes);
                f.delete();
            }
        }else {
            File[] files = f.listFiles(filter);
            if (files == null) {
                return;
            }
            for (File file : files) {
                handleFile(file, p, include);
            }
        }
    }
}
