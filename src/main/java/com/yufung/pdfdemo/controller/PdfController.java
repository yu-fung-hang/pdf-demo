package com.yufung.pdfdemo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("/pdf")
public class PdfController {
    @GetMapping("/download")
    public void downloadCheck(HttpServletRequest request, HttpServletResponse response) {
        try {
            File file = new File("src/main/resources/pdf/2020-Scrum-Guide-US.pdf");
            BufferedInputStream bis = null;
            OutputStream os = null;
            FileInputStream fileInputStream = null;
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=scrum.pdf");
            try {
                fileInputStream = new FileInputStream(file);
                byte[] buff = new byte[1024];
                bis = new BufferedInputStream(fileInputStream);
                os = response.getOutputStream();

                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    i = bis.read(buff);
                    os.flush();
                }
                os.flush();
                os.close();
//                return SimpleResult.ok("导出成功",os);
            } catch (IOException e) {
                e.printStackTrace();
//                return SimpleResult.fail("导出失败",null);
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                        fileInputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
//                        return SimpleResult.fail("导出失败",null);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/preview")
    public void previewCheck( HttpServletRequest request, HttpServletResponse response) throws
            IOException {
        FileInputStream is = new FileInputStream("src/main/resources/pdf/2020-Scrum-Guide-US.pdf");
        // 清空response
        response.reset();
        //2、设置文件下载方式
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/pdf");
        OutputStream outputStream = response.getOutputStream();
        int count = 0;
        byte[] buffer = new byte[1024 * 1024];
        while ((count = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0, count);
        }
        outputStream.flush();
    }
}