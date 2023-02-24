# encrypt-project 加密

### 对Java的class文件加解密，无侵入性，支持springboot
配合上maven插件让加密自动化，无需手动加密
### 使用（一定要先compile才能生成加密文件）
 1. 跟据common包下cpp文件夹下的c++代码生成动态连接库（windows下 .dll linux下 .so） 
 2. 引入maven插件compile阶段自动加密includes标签下的class文件
```xml
<plugin>
  <groupId>org.example</groupId>
  <artifactId>plugin</artifactId>
  <version>1.0-SNAPSHOT</version>
  <configuration>
    <location>${project.build.outputDirectory}</location>
    <includes>
        <include>com.zwk.config</include>
        <include>com.zwk.consumer</include>
        <include>com.zwk.controller</include>
    </includes>
  </configuration>
  <executions>
    <execution>
      <id>hello-id</id>
      <phase>compile</phase>
      <goals>
         <goal>encrypt</goal>
       </goals>
    </execution>
  </executions>
</plugin>
````
 3. 引入encrypter包，在启动类上添加@EnableClassDecryptor注解，实现class文件的解密及自动注册到beanFactory
