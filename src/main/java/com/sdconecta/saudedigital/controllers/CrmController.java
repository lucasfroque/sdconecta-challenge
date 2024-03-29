package com.sdconecta.saudedigital.controllers;

import com.sdconecta.saudedigital.dto.CrmDTO;
import com.sdconecta.saudedigital.models.Crm;
import com.sdconecta.saudedigital.models.User;
import com.sdconecta.saudedigital.services.CrmService;
import com.sdconecta.saudedigital.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CrmController {

    @Autowired
    private CrmService crmService;

    @Autowired
    private UserService userService;

    @PostMapping("/users/{userId}/crms")
    public ResponseEntity<Crm> insert(@PathVariable Long userId, @RequestBody CrmDTO obj){
        User user = userService.findById(userId);
        Crm response = crmService.create(obj, user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/crms/{id}")
    public  ResponseEntity<Crm> update(@PathVariable Long id, @RequestBody CrmDTO obj){
        Crm response = crmService.update(id, obj);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = "/crms")
    public ResponseEntity<List<Crm>> findAll(){
        List<Crm> list= crmService.findAll();
        return ResponseEntity.ok().body(list);
    }

    @DeleteMapping(value = "/crms/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        crmService.delete(id);
        return ResponseEntity.noContent().build();

    }

    @GetMapping(value = "crms/{id}")
    public ResponseEntity<Crm> findById(@PathVariable Long id){
        Crm obj = crmService.findById(id);
        return ResponseEntity.ok().body(obj);

    }
}
