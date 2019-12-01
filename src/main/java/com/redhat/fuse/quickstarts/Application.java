/*
 * Copyright (C) 2018 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.fuse.quickstarts;

import oracle.jdbc.xa.client.OracleXADataSource;
import oracle.jms.AQjmsFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.messaginghub.pooled.jms.JmsPoolXAConnectionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.XAConnectionFactory;
import javax.transaction.TransactionManager;
import java.sql.SQLException;
import java.util.Properties;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean(name = "jms-component")
    public JmsComponent jmsComponent(ConnectionFactory xaJmsConnectionFactory,
                                     PlatformTransactionManager jtaTransactionManager) {
        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(xaJmsConnectionFactory);
        jms.setTransactionManager(jtaTransactionManager);
        jms.setTransacted(true);

        return jms;
    }

    @Bean(name = "oracleaq")
    JmsComponent oracleAQJmsComponent(PlatformTransactionManager transactionManager,
                                      TransactionManager jtaTransactionManager)
            throws JMSException, SQLException {
        OracleXADataSource oracleXADataSource = new OracleXADataSource();
        oracleXADataSource.setURL("jdbc:oracle:thin:@localhost:1521:ORCLCDB");
        oracleXADataSource.setUser("scott");
        oracleXADataSource.setPassword("tiger");

        // Now we've created the XA datasource, we need something that will generate an XAConnectionFactory
        // Oracle's example just gets a connection; we want a connection FACTORY.
        XAConnectionFactory oracleXACF = AQjmsFactory.getXAConnectionFactory(oracleXADataSource);

        // Presumably this is a non-enlisting XAConnectionFactory.
        // So we need to wrap it in an enlisting connection factory?
        // Similar to: https://access.redhat.com/documentation/en-us/red_hat_fuse/7.2/html-single/apache_karaf_transaction_guide/index#manual-deployment-connection-factories
        JmsPoolXAConnectionFactory pooledJmsXACF = new JmsPoolXAConnectionFactory();
        pooledJmsXACF.setConnectionFactory(oracleXACF);
        pooledJmsXACF.setTransactionManager(jtaTransactionManager); // Narayana implementing the JTA interface

        JmsComponent jms = new JmsComponent();
        jms.setConnectionFactory(pooledJmsXACF);
        jms.setTransactionManager(transactionManager); // again Narayana, implementing the Spring interface
        jms.setTransacted(false);

        return jms;
    }


}
