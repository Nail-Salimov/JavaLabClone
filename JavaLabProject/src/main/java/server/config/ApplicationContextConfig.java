package server.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import server.services.MailConfirmationService;
import server.services.MailConfirmationServiceImpl;

import javax.sql.DataSource;

@Configuration
@PropertySource({"classpath:application.properties",
                "classpath:mail_configuration.properties"
})
@ComponentScan(basePackages = "server")
public class ApplicationContextConfig {

    @Autowired
    private Environment environment;

    @Bean
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(hikariDataSource());
    }

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(environment.getProperty("db.url"));
        config.setUsername(environment.getProperty("db.user"));
        config.setPassword(environment.getProperty("db.password"));
        config.setDriverClassName(environment.getProperty("db.driver"));
        return config;
    }

    @Bean
    public DataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    public MailConfig mailConfig(){
        MailConfig mailConfig = new MailConfig();
        mailConfig.setAuth(environment.getProperty("db.smtpAuth"), environment.getProperty("db.smtpAuthState"));
        mailConfig.setStarttls(environment.getProperty("db.smtpStarttlsEnable"), environment.getProperty("db.smtpStarttlsEnableState"));
        mailConfig.setHost(environment.getProperty("db.smtpHost"), environment.getProperty("db.smtpHostState"));
        mailConfig.setPort(environment.getProperty("db.smtpPort"), environment.getProperty("db.smtpPortNumber"));
        mailConfig.createProperties();
        return mailConfig;
    }


    @Bean
    public ViewResolver viewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("templates/");
        viewResolver.setSuffix(".ftl");
        return viewResolver;
    }


}
