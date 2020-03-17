package server.config;

import lombok.Data;

import java.util.Properties;

@Data
public class MailConfig {
    private Properties properties;

    private String smtpAuth;
    private String smtpAuthState;

    private String smtpStarttlsEnable;
    private String smtpStarttlsEnableState;

    private String smtpHost;
    private String smtpHostState;

    private String smtpPort;
    private String smtpPortNumber;

    public void setAuth(String auth, String state){
        this.smtpAuth = auth;
        this.smtpAuthState = state;
    }

    public void setStarttls(String host, String state){
        this.smtpStarttlsEnable = host;
        this.smtpStarttlsEnableState = state;
    }

    public void setHost(String host, String hostState){
        this.smtpHost = host;
        this.smtpHostState = hostState;
    }

    public void setPort(String port, String number){
        this.smtpPort = port;
        this.smtpPortNumber = number;
    }

    public void createProperties(){
        this.properties = new Properties();
        properties.put(getSmtpAuth(), getSmtpAuthState());
        properties.put(getSmtpStarttlsEnable(), getSmtpStarttlsEnableState());
        properties.put(getSmtpHost(), getSmtpHostState());
        properties.put(getSmtpPort(), getSmtpPortNumber());
    }

}
