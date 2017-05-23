
Словарная сиситема
==================

__модули словарной системы__

- [core-orm](..\\..\\..\\..\\..\\..\\..\core-orm\src\main\java\com\bivgroup\common\doc-files\readme.md) - базовый провайдер работы с бд
- [core-dictionary](..\\..\\..\\..\\..\\..\\..\core-dictionary\src\main\java\com\bivgroup\core\dictionary\doc-files\readme.md) - экспорт метаданных из структуры бд. типовые ооперации с описанием модуля (чтение, запись, удаление ..)
- [core-dictionary-plugin](..\\..\\..\\..\\..\\..\\..\core-dictionary-plugin\src\main\java\com\bivgroup\dbmodel\plugin\doc-files\readme.md) - плагин генерации модуля.
- [core-aspect-beans](..\\..\\..\\..\\..\\..\\..\core-aspect-beans\src\main\java\com\bivgroup\core\aspect\doc-files\readme.md) - описание аспектов
- [core-aspect](..\\..\\..\\..\\..\\..\\..\core-aspect\src\main\java\com\bivgroup\core\aspect\doc-files\readme.md) - работа с аспектами модуля
- [core-dictionary-editor](..\\..\\..\\..\\..\\..\\..\core-dictionary-editor\src\main\java\com\bivgroup\common\doc-files\readme.md) - визуальный редактор словарной системы
- [core-service](..\\..\\..\\..\\..\\..\\..\core-service\src\main\java\com\bivgroup\core\doc-files\readme.md) - базовые методы платформы (копия системных методов: corews, adminws)
- crm, termination  - примеры модулей

Генерация модулей
=================

для генерации модуляй необходимы следующие условия:

1. наличие описания модуля в структуре бд. описание  в модуле [core-dictionary](..\\..\\..\\..\\..\\..\\..\core-dictionary\src\main\java\com\bivgroup\common\doc-files\readme.md)
2. настроенный сборщик модуля - maven  проект
пример:
      
        <project xmlns="http://maven.apache.org/pom/4.0.0" xmlns:xsi="http://www.w3.org/2001/xmlschema-instance"
                          xsi:schemalocation="http://maven.apache.org/pom/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
                     <modelversion>4.0.0</modelversion>
                     <groupid>com.bivgroup</groupid>
                     <artifactid>crm</artifactid>
                     <packaging>jar</packaging>
                     <version>1.0-snapshot</version>
                     <name>core-crm</name>
                     <properties>
                         <project.build.sourceencoding>utf-8</project.build.sourceencoding>
                         <hibernate-version>5.2.4.final</hibernate-version>
                         <hibernate-tool-version>5.0.2.final</hibernate-tool-version>
                     </properties>    
                     <build>
                         <plugins>
                             <plugin>
                                 <groupid>com.bivgroup</groupid>
                                 <artifactid>core-dictionary-maven-plugin</artifactid>
                                 <version>1.0-snapshot</version>
                                 <executions>
                                     <execution>
                                         <goals>
                                             <goal>buildmodule</goal>
                                         </goals>
                                         <phase>validate</phase>
                                     </execution>
                                 </executions>
                                 <configuration>
                                     <database>
                                         <url>jdbc:oracle:thin:@10.10.100.250:1521:orcd</url>
                                         <login>sber_dev_02</login>
                                         <password>1</password>
                                         <datasourceclass>oracle.jdbc.pool.oracledatasource</datasourceclass>
                                     </database>
                                     <typeoutobject>xml</typeoutobject>
                                 </configuration>
                             <dependencies>
                                 <dependency>
                                     <groupid>org.hibernate</groupid>
                                     <artifactid>hibernate-core</artifactid>
                                     <version>${hibernate-version}</version>
                                 </dependency>
                                 <dependency>
                                     <groupid>org.hibernate</groupid>
                                     <artifactid>hibernate-entitymanager</artifactid>
                                     <version>${hibernate-version}</version>
                                 </dependency>
                             <!--для генерации presistence-->
                                     <dependency>
                                         <groupid>org.hibernate</groupid>
                                         <artifactid>hibernate-tools</artifactid>
                                         <version>${hibernate-tool-version}</version>
                                     </dependency>
                                     <dependency>
                                         <groupid>org.apache.velocity</groupid>
                                         <artifactid>velocity</artifactid>
                                         <version>1.7</version>                        
                                     </dependency>
                                 </dependencies>
                             </plugin>
                         </plugins>
                     </build>
                     <dependencies>
                          <dependency>
                             <groupid>com.bivgroup</groupid>
                             <artifactid>core-orm</artifactid>
                             <version>1.0-snapshot</version>
                             </dependency>
                         <dependency>
                             <groupid>com.bivgroup</groupid>
                             <artifactid>core-dictionary</artifactid>        
                             <version>1.0-snapshot</version>
                         </dependency>
                         <dependency>
                             <groupid>com.bivgroup</groupid>
                             <artifactid>core-aspect-beans</artifactid>
                             <version>1.0-snapshot</version>
                         </dependency>
                         <dependency>
                             <groupid>com.bivgroup</groupid>
                             <artifactid>core-aspect</artifactid>
                             <version>1.0-snapshot</version>
                         </dependency>
                         <dependency>
                             <groupid>org.hibernate</groupid>
                             <artifactid>hibernate-core</artifactid>
                             <version>${hibernate-version}</version>
                            <scope>provided</scope>
                         </dependency>
                         <dependency>
                             <groupid>org.hibernate</groupid>
                             <artifactid>hibernate-entitymanager</artifactid>
                             <version>${hibernate-version}</version>
                             <scope>provided</scope>
                         </dependency>
                         <!--для генерации presistence-->
                         <dependency>
                             <groupid>org.hibernate</groupid>
                             <artifactid>hibernate-tools</artifactid>
                             <version>${hibernate-tool-version}</version>
                         <scope>provided</scope>
                         </dependency>        
                     </dependencies>
                 </project>
Где необходимо описать: 

    - группу  

            <groupid>com.bivgroup</groupid>

    - название модуля 
    
            <artifactid>crm</artifactid>

    - подключение к БД , где находится описание метамодели

            <database>
            <url>jdbc:oracle:thin:@10.10.100.250:1521:orcd</url>
            <login>sber_dev_02</login>
            <password>1</password>
            <datasourceclass>oracle.jdbc.pool.oracledatasource</datasourceclass>
            </database>

    - тип экспорта (xml, class)  
                                   
            <typeoutobject>xml</typeoutobject>

