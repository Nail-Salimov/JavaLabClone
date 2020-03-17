package server.protocol;

public class Response<T> {
    private T data;

    private Response(T data) {
        this.data = data;
    }

    public static <E> Response<E> build(E data){
        return new Response<>(data);
    }
}
