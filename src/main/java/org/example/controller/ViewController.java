package org.example.controller;

import org.example.service.UploadXmlParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ViewController {

    @GetMapping("/upload-form")
    public String showUploadForm() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            new UploadXmlParser().parse(file.getInputStream());
            model.addAttribute("message", "XML file uploaded and processed successfully");
        } catch (Exception e) {
            model.addAttribute("message", "Error processing XML file: " + e.getMessage());
        }
        return "upload-success";
    }

    @GetMapping("/generate-xml")
    public String generateXml() {
        return "generate-xml";
    }
}
