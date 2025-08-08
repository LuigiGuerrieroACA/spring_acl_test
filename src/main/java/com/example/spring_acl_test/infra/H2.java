package com.example.spring_acl_test.infra;

import org.h2.tools.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class H2 {

//    private org.h2.tools.Server webServer;
//
//    private org.h2.tools.Server tcpServer;
//
//    @EventListener(org.springframework.context.event.ContextRefreshedEvent.class)
//    public void start() throws java.sql.SQLException {
//        this.webServer = org.h2.tools.Server.createWebServer("-webPort", "8082", "-tcpAllowOthers").start();
//        this.tcpServer = org.h2.tools.Server.createTcpServer("-tcpPort", "9092", "-tcpAllowOthers").start();
//    }
//
//    @EventListener(org.springframework.context.event.ContextClosedEvent.class)
//    public void stop() {
//        this.tcpServer.stop();
//        this.webServer.stop();
//    }

    // Start internal H2 server so can query from IDE
    @Bean(initMethod = "start", destroyMethod = "stop")
    public Server h2Server() throws SQLException {
        return Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092");
    }

}
