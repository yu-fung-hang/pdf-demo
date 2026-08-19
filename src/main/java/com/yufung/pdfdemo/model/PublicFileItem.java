package com.yufung.pdfdemo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.nio.file.Paths;

@Data
@AllArgsConstructor
public class PublicFileItem {
    String url;
    String name;

    public PublicFileItem(String url) {
        this.url = url;
        if(!StringUtils.isEmpty(url)) this.name = Paths.get(url.replaceAll(":", "")).getFileName().toString();
    }
}
