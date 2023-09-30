package com.example.hibernatetest2.tables.manytomany.rest;

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

import com.example.hibernatetest2.tables.manytomany.dto.Table4Dto;
import com.example.hibernatetest2.tables.manytomany.entities.Table4;
import com.example.hibernatetest2.tables.manytomany.service.Table4Service;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/api/v1/manytomany") // [ ] URL for this table
@RequiredArgsConstructor
public class Table4Resource {

    private final Table4Service table2Service;



    @GetMapping
    public ResponseEntity<Page<Table4Dto>> search(
                                                  @RequestParam(defaultValue = "0") //? this variable will be in the URL itself after ? (like http://website.com/bands?pageNumber=1)
                                                  Integer pageNumber,
                                                  @RequestParam(defaultValue = "2")
                                                  Integer numberOfRowsInOnePage,
                                                  @RequestParam(defaultValue = "id")
                                                  String sortedBy,
                                                  @RequestParam(defaultValue = "")
                                                  String searchPhrase) {
        return ResponseEntity.ok()
                             .body(table2Service.search(pageNumber,
                                                        numberOfRowsInOnePage,
                                                        sortedBy,
                                                        searchPhrase));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Table4Dto> findById(
                                              @PathVariable
                                              String id) {
        return ResponseEntity.ok().body(table2Service.findById(Long.parseLong(id)));
    }

    @PostMapping("/add")
    public ResponseEntity<Table4Dto> createNewRow(
                                                  @RequestBody
                                                  @Valid
                                                  Table4 creatingForm) {
        return ResponseEntity.created(URI.create(ServletUriComponentsBuilder.fromCurrentContextPath()
                                                                            .path("/api/v1/table2/<name>")
                                                                            .toUriString()))
                             .body(table2Service.createNewRow(creatingForm));
    }

    @PutMapping("/change/{id}")
    public ResponseEntity<Table4Dto> changeRow(
                                               @RequestBody
                                               String newName,
                                               @PathVariable
                                               String id) {
        return ResponseEntity.ok().body(table2Service.changeRow(newName, Long.parseLong(id)));
    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<?> deleteRow(
                                       @PathVariable
                                       String name) {
        return table2Service.deleteRow(name) == 1 ?//
                ResponseEntity.noContent().build() ://
                ResponseEntity.notFound().build(); //? NotFound 404
    }
}
