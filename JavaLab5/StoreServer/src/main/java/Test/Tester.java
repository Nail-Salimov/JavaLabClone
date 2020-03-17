package Test;

import DB.BdLogical;
import DB.ClientDBLogical;
import DB.GeneralDBLogical;
import Service.ClientService;
import Service.ClientServiceImpl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jwt.Tokenizer;
import org.mindrot.jbcrypt.BCrypt;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Tester {
    public static void main(String[] args) throws IOException {
        ClientService service = new ClientServiceImpl(ClientDBLogical.getClientDBLogical("/home/nail/Progy/JavaLab/JavaLab3/db.properties"),
                GeneralDBLogical.getGeneralDBLogical("/home/nail/Progy/JavaLab/JavaLab3/db.properties"));


    }
}
