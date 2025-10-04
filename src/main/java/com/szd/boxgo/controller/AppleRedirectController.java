package com.szd.boxgo.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/.well-known/apple-app-site-association")
public class AppleRedirectController {
    @GetMapping
    public ResponseEntity<String> getAppleAppSiteAssociation() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("""
                        {
                          "applinks": {
                          	"apps": [],
                            "details": [
                              {
                                "appID": "UAN3JPTQZ2.com.szd.sueta",
                                "paths": [ "*", "/*", "/listing/*" ]
                              }
                            ]
                          }
                        }
                        """);
    }
}