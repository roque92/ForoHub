package org.example.forohub.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jmx.support.MBeanServerFactoryBean;

@Configuration
public class JmxConfiguration {

    @Bean
    public MBeanServerFactoryBean jmxrmi() {
        MBeanServerFactoryBean factoryBean = new MBeanServerFactoryBean();
        // You can customize the MBean server here if needed
        return factoryBean;
    }

}
