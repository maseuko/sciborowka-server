package pl.scb.models;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

public class ResponseMessage {
    private final HttpStatus statusCode;

    public ResponseMessage(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public ResponseEntity<Object> sendMessage(String msg){
        HashMap<String, Object> response = new HashMap<>();
        response.put("statusCode", this.statusCode);
        response.put("msg", msg);
        return new ResponseEntity<>(response,this.statusCode);
    }

    public ResponseEntity<Object> sendMessage(Object data){
        HashMap<String, Object> response = new HashMap<>();
        response.put("statusCode", this.statusCode);
        response.put("data", data);
        return new ResponseEntity<>(response,this.statusCode);
    }

    public ResponseEntity<Object> sendMessage(String msg, Object data){
        HashMap<String, Object> response = new HashMap<>();
        response.put("statusCode", this.statusCode);
        response.put("msg", msg);
        response.put("data", data);
        return new ResponseEntity<>(response,this.statusCode);
    }
}
