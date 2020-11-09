package main.services;

import main.api.responses.BlogInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiService {

    private final BlogInfo info;

    public ApiService(BlogInfo info) {
        this.info = info;
    }

    public ResponseEntity<BlogInfo> getBlogInfo(){
        return new ResponseEntity<>(info, HttpStatus.OK);
    }

}
