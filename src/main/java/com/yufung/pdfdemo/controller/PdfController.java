package com.yufung.pdfdemo.controller;

import com.yufung.pdfdemo.service.PdfService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/saveFromRemote/local")
    public void saveFromRemote(@RequestParam("companyId") String companyId) throws IOException {
        pdfService.saveFromRemoteToLocal(companyId);
    }

    @GetMapping("/saveFromRemote/s3")
    public void saveFromRemoteToS3(@RequestParam("companyId") String companyId) throws IOException {
        pdfService.saveFromRemoteToS3(companyId);
    }
}