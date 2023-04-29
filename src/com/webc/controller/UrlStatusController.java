package com.webc.controller;

import com.webc.service.UrlStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:8080")
@RestController
@RequestMapping("/")
public class UrlStatusController {

    @Autowired
    private UrlStatusService urlStatusService;

    @PostMapping("crawl/")
    public ResponseEntity<HttpStatus> getUrlStatus(@RequestParam String url) throws Exception {

        try {
            urlStatusService.generateStatusCodes(url);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }

}
