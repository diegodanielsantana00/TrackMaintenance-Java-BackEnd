package com.danieldiego.trackMaintenance.crosscutting.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DatabaseInitializerConfig {

    @Bean(name = "databaseInitializer")
    public Object databaseInitializer(Environment env) {
        String url = env.getProperty("spring.datasource.url");
        String username = env.getProperty("spring.datasource.username");
        String password = env.getProperty("spring.datasource.password");

        String dbName = extractDatabaseName(url);
        String baseUrl = url.substring(0, url.lastIndexOf('/')) + "/postgres";

        try (Connection connection = DriverManager.getConnection(baseUrl, username, password);
             Statement stmt = connection.createStatement()) {

            ResultSet rs = stmt.executeQuery(
                    "SELECT 1 FROM pg_database WHERE datname = '" + dbName.replace("'", "''") + "'"
            );

            if (!rs.next()) {
                stmt.execute("CREATE DATABASE \"" + dbName.replace("\"", "\"\"") + "\"");
                System.out.println("Banco de dados '" + dbName + "' criado com sucesso.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar/criar o banco de dados: " + dbName, e);
        }

        return new Object();
    }

    @Bean
    public static BeanFactoryPostProcessor dataSourceDependsOnDatabaseInitializer() {
        return beanFactory -> {
            if (beanFactory.containsBeanDefinition("dataSource")) {
                BeanDefinition bd = beanFactory.getBeanDefinition("dataSource");
                List<String> deps = new ArrayList<>();
                if (bd.getDependsOn() != null) {
                    deps.addAll(Arrays.asList(bd.getDependsOn()));
                }
                deps.add("databaseInitializer");
                bd.setDependsOn(deps.toArray(new String[0]));
            }
        };
    }

    private String extractDatabaseName(String url) {
        String withoutParams = url.split("\\?")[0];
        return withoutParams.substring(withoutParams.lastIndexOf('/') + 1);
    }
}
