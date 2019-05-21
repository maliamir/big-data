package com.maaksoft.cassandra.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.core.env.Environment;

import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

import org.springframework.data.cassandra.core.CassandraOperations;
import org.springframework.data.cassandra.core.CassandraTemplate;

import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;

import org.springframework.data.cassandra.core.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@PropertySource(value = { "classpath:cassandra.properties" })
@EnableCassandraRepositories(basePackages = "com.maaksoft.cassandra.repository")
public class CassandraConfig extends AbstractCassandraConfiguration {

    private static final Log logger = LogFactory.getLog(CassandraConfig.class);

    /*
    @Bean
    public CassandraOperations operations() throws Exception {
        return new CassandraTemplate(session().getObject(), new MappingCassandraConverter(new BasicCassandraMappingContext()));
    }
    */

    @Autowired
    private Environment environment;


    @Override
    protected String getKeyspaceName() {
        return environment.getProperty("cassandra.keyspace");
    }

    @Override
    @Bean
    public CassandraClusterFactoryBean cluster() {

        CassandraClusterFactoryBean cluster = null;

        try {

            cluster = new CassandraClusterFactoryBean();
            cluster.setContactPoints(environment.getProperty("cassandra.contactpoints"));
            cluster.setPort(Integer.parseInt(environment.getProperty("cassandra.port")));

            logger.info("Cluster created with contact points [" + environment.getProperty("cassandra.contactpoints") + "] " + "& port [" + Integer.parseInt(environment.getProperty("cassandra.port")) + "].");
        } catch (Throwable throwable) {
            System.out.println("Cassandra Cluster Setup Error:\n" + throwable);
        }

        return cluster;

    }

    @Override
    @Bean
    public CassandraMappingContext cassandraMapping() throws ClassNotFoundException {
        return new BasicCassandraMappingContext();
    }

}