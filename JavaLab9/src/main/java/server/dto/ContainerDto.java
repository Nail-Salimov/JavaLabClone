package server.dto;

public class ContainerDto implements Dto {
    private String header;
    private Payload payload;

    private ContainerDto(String header, Payload payload) {
        this.header = header;
        this.payload = payload;
    }

    public static ContainerDto createContent(String header, Payload payload){
        return new ContainerDto(header, payload);
    }

    public String getHeader() {
        return header;
    }

    public Payload getPayload() {
        return payload;
    }
}
