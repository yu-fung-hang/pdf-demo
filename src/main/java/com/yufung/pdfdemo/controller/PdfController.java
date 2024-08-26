package com.yufung.pdfdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfController
{
    @GetMapping("demo")
    public String demoMethod()
    { return "Success!"; }
}