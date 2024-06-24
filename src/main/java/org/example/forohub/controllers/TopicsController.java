package org.example.forohub.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/topics")
public class TopicsController {

    //List all
    @GetMapping("/")
public ResponseEntity<?> listAllTopics() {
    // The JWT validation is now handled by the JwtValidations filter
    // You can directly proceed to fetch and return topics
    // ... 
    return ResponseEntity.ok("List of topics..."); 
}

    //Create
    @PostMapping("/")
    public ResponseEntity<?> newTopic(@RequestBody String entity) {
        //TODO: process POST request
        
        return ResponseEntity.ok("Topic Created");
    }
    

    //Update

    //Delete

    //Topic Details
    

}
