package com.nirima.jenkins.plugins.docker;

import io.jenkins.docker.connector.DockerComputerJNLPConnector;
import com.nirima.jenkins.plugins.docker.DockerTemplateBase;
import com.nirima.jenkins.plugins.docker.DockerTemplate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DockerTemplateTest {

    /** Convenience helper that returns a template whose DockerTemplateBase
     *  has the supplied DNS host string. */
    private DockerTemplate createTemplateWithDnsHosts(String dnsHosts) {

        // 1. Base image & DNS configuration
        DockerTemplateBase base = new DockerTemplateBase("alpine:3.20");
        base.setDnsString(dnsHosts);                  // <-- replaces legacy ctor arg

        // 2. Use the simplest connector (JNLP) for unit tests
        DockerComputerJNLPConnector connector = new DockerComputerJNLPConnector();

        /* 3. Build the DockerTemplate.
         *    Constructor signature:
         *    DockerTemplate(DockerTemplateBase base,
         *                   DockerComputerConnector connector,
         *                   String labelString,
         *                   String remoteFs,
         *                   String instanceCapStr)
         */
        return new DockerTemplate(base,
                                  connector,
                                  "test-label",
                                  "/home/jenkins",
                                  "1");
    }

    @Test
    public void testDnsHostsParsing() {

        // Empty string  →  no DNS hosts
        DockerTemplate template = createTemplateWithDnsHosts("");
        assertEquals(0, template.getDockerTemplateBase().getDnsHosts().length);

        // Single host
        template = createTemplateWithDnsHosts("8.8.8.8");
        assertArrayEquals(new String[]{"8.8.8.8"},
                          template.getDockerTemplateBase().getDnsHosts());

        // Two hosts separated by space
        template = createTemplateWithDnsHosts("8.8.8.8 8.8.4.4");
        assertArrayEquals(new String[]{"8.8.8.8", "8.8.4.4"},
                          template.getDockerTemplateBase().getDnsHosts());
    }
}
