package com.yufung.pdfdemo.controller;

import com.yufung.pdfdemo.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/pdf")
public class PdfController {
    @Autowired
    PdfService pdfService;

    @GetMapping("/download")
    public void download(HttpServletResponse response) {
        pdfService.download(response);
    }

    @GetMapping("/preview")
    public void preview(HttpServletResponse response) throws IOException {
        pdfService.preview(response);
    }

    @PostMapping("/getFromRemote")
    public void getFromRemote() throws IOException {
        pdfService.getFromRemote();
    }
}