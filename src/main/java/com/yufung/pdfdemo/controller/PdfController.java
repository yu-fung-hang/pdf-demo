package com.yufung.pdfdemo.controller;

import com.yufung.pdfdemo.model.FileDto;
import com.yufung.pdfdemo.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
@RequestMapping("/pdf")
public class PdfController {
    @Autowired
    PdfService pdfService;

//    @GetMapping("/download")
//    public void download(HttpServletResponse response) {
//        pdfService.download(response);
//    }
//
//    @GetMapping("/preview")
//    public void preview(HttpServletResponse response) throws IOException {
//        pdfService.preview(response);
//    }

    @PostMapping("/saveFromRemote/local")
    public void saveFromRemote(@RequestBody FileDto dto) throws IOException {
        pdfService.saveFromRemoteToLocal(dto);
    }

    @PostMapping("/saveFromRemote/s3")
    public void saveFromRemoteToS3(@RequestBody FileDto dto) throws IOException {
        pdfService.saveFromRemoteToS3(dto);
    }
}