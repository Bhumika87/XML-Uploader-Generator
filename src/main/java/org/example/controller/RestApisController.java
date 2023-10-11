package org.example.controller;

import org.example.service.DownloadXmlProcessor;
import org.example.service.GetTableListForDropdown;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestApisController {

    @GetMapping("/table-list")
    public List<String> getTableList() {
        List<String> tables = new GetTableListForDropdown().getTableList();
        return tables;
    }

    @PostMapping("/download-xml")
    public ResponseEntity<?> downloadFile(@RequestBody String selectedValue) {
        String tableName = selectedValue.split("=")[1];
        String xml = new DownloadXmlProcessor().generateXml(tableName);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=table.xml");
        return ResponseEntity.ok()
                .headers(headers)
                .body(xml);
    }
}
