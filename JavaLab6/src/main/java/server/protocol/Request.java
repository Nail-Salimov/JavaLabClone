package server.protocol;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Request {

    private String header;
    private String payload;
    private String verifySignature;

    private ObjectMapper mapper;

    public Request(String json) {
        mapper = new ObjectMapper();
        setParameter(json);
    }

    public String getHeader() {
        return header;
    }

    public String getVerifySignature() {
        return verifySignature;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setVerifySignature(String verifySignature) {
        this.verifySignature = verifySignature;
    }

    private void setParameter(String json) {
        try {
            JsonNode rootNode = mapper.readTree(json);
            this.header = rootNode.path("header").asText();
            this.payload = rootNode.path("payload").toString();
            this.verifySignature = rootNode.path("verifySignature").asText();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public <T> T getPayloadParameter(String parameter, Class<T> parameterClass) {
        try {
            JsonNode node = mapper.readTree(payload);

            if (parameterClass.getName().equals("java.lang.String")) {
                String nodeValue = node.path(parameter).asText();
                return parameterClass.cast(nodeValue);

            } else if (parameterClass.getName().equals("java.lang.Integer")) {
                Integer nodeValue = node.path(parameter).asInt();
                return parameterClass.cast(nodeValue);

            } else if (parameterClass.getName().equals("java.lang.Long")) {
                Long nodeValue = node.path(parameter).asLong();
                return parameterClass.cast(nodeValue);
            }

            throw new IllegalStateException();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
