package com.example.hibernatetest2.tables.onetomany.rest;

import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.hibernatetest2.tables.onetomany.dto.Table1Dto;
import com.example.hibernatetest2.tables.onetomany.entities.Table1;
import com.example.hibernatetest2.tables.onetomany.service.Table1Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/table1") // [ ] URL for this table
@RequiredArgsConstructor
public class Table1Resource {

    private final Table1Service table1Service;



    @GetMapping
    public ResponseEntity<Page<Table1Dto>> search(
                                                  @RequestParam(defaultValue = "0") //? this variable will be in the URL itself after ? (like http://website.com/bands?pageNumber=1)
                                                  Integer pageNumber,
                                                  @RequestParam(defaultValue = "2")
                                                  Integer numberOfRowsInOnePage,
                                                  @RequestParam(defaultValue = "id")
                                                  String sortedBy,
                                                  @RequestParam(defaultValue = "")
                                                  String searchPhrase) {
        return ResponseEntity.ok()
                             .body(table1Service.search(pageNumber,
                                                        numberOfRowsInOnePage,
                                                        sortedBy,
                                                        searchPhrase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Table1Dto> findById(
                                              @PathVariable("id")
                                              String id) {
        return ResponseEntity.ok().body(table1Service.findById(Long.parseLong(id)));
    }

    @PostMapping("/add")
    public ResponseEntity<Table1Dto> createNewRow(
                                                  @RequestBody
                                                  @Valid
                                                  Table1 creatingForm) {
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                                                                            .path("/api/v1/table1/<name>")
                                                                            .toUriString()))
                             .body(table1Service.createNewRow(creatingForm));
    }

    @PutMapping("/change/{id}")
    public ResponseEntity<Table1Dto> changeRow(
                                               @RequestBody
                                               String newName,
                                               @PathVariable("id")
                                               String id) {
        return ResponseEntity.ok().body(table1Service.changeRow(newName, Long.parseLong(id)));
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteRow(
                                       @PathVariable
                                       String name) {
        return table1Service.deleteRow(name) == 1 ?//
                ResponseEntity.noContent().build() ://
                ResponseEntity.notFound().build(); //? NotFound 404
    }
}
