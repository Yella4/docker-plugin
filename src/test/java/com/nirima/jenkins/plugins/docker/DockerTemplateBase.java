package com.nirima.jenkins.plugins.docker;

import com.github.dockerjava.api.model.HostConfig;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.io.Serializable;

public class DockerTemplateBase implements Serializable {

    private String image;
    private String dnsString;

    public DockerTemplateBase(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getDnsString() {
        return dnsString;
    }

    public void setDnsString(String dnsString) {
        this.dnsString = dnsString;
    }

    /**
     * Return DNS host list as an array, splitting on whitespace.
     */
    @SuppressFBWarnings("UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR")
    public String[] getDnsHosts() {
        if (dnsString == null || dnsString.trim().isEmpty()) {
            return new String[0];
        }
        return dnsString.trim().split("\\s+");
    }

    /**
     * Build HostConfig for docker-java
     */
    public HostConfig buildHostConfig() {
        HostConfig config = HostConfig.newHostConfig();
        String[] dns = getDnsHosts();
        if (dns.length > 0) {
            config.withDns(dns);
        }
        return config;
    }
}
