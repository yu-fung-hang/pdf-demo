package com.yufung.pdfdemo.service;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class PdfService {
    public void download(HttpServletResponse response) {
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

    public void preview(HttpServletResponse response) throws IOException {
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

    public void getFromRemote() throws IOException {
        URL url = new URL("https://connect.creditsafe.com/v1/companies/CA-X-CA08358159");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(5*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("Authorization", "eyJhbGciOiJSUzI1NiIsImtpZCI6ImNZOGpqZXByakZOZDdVS1FrQ08zcVVycDZMNCIsInR5cCI6IkpXVCIsIng1dCI6ImNZOGpqZXByakZOZDdVS1FrQ08zcVVycDZMNCJ9.eyJuYmYiOjE3MjQ4NjIxODMsImV4cCI6MTcyNDg2NTc4MywiaXNzIjoiaHR0cHM6Ly9teWxvZ2luLmNyZWRpdHNhZmUuY29tIiwiYXVkIjpbImh0dHBzOi8vbXlsb2dpbi5jcmVkaXRzYWZlLmNvbS9yZXNvdXJjZXMiLCJjb25uZWN0X2FwaSIsInVib19hcGlfZ2F0ZXdheSJdLCJjbGllbnRfaWQiOiJjb25uZWN0LmFwaS5jbGllbnQiLCJzdWIiOiIxMDIwMTgzNjMiLCJhdXRoX3RpbWUiOjE3MjQ4NjIxODMsImlkcCI6ImxvY2FsIiwidXNlcm5hbWUiOiJIVCIsImVtYWlsIjoiY3JlZGl0c2FmZV9wcm9kdWN0aW9uQG90dC5jYSIsImN1c3RvbWVySWQiOiIxMDk5MTk0NzYiLCJjb3VudHJ5IjoiQzAiLCJ1c2VyUm9sZSI6IkN1c3RvbWVyIiwid2ViUm9sZUlkIjoiMCIsInNiX2NvdW50cnkiOiJDQSIsInNjb3BlIjpbImNvbm5lY3RfYXBpIiwidWJvX2FwaV9nYXRld2F5Il0sImFtciI6WyJwd2QiXX0.c0POoFexu4RZbIc9fFOJudwLRP8k1pkgyRKyvF3j1ZJ2SdCBc1-I4kM9M8e_hjQ_JlUpY5alN03aHdNaVlAlL81BUjPr0y6Hb4ZZlYEE-9qfgPtHUobVE-Gpr7HySIpVa2uivtkFZOqqjqRromgzCESDpjRFyZd1_8bOJ4dIa_04fbSm3Ju4H9iHUK-LQdXBp-8r0Df42DZoHf12S7w0NKNvR_c1s9YHN9CqVNK-alNcGaHCakMUh0L21990CadWSkemETH_-d4ehcWg4jmjiQYCLW2qkZ7BN2IA2W_u_EGgRDd7G0jlcE7FYcrUoECdSfDUM6App3vN5B6xy39C0Q");
        conn.setRequestProperty("Accept", "application/pdf");
        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);
        //文件保存位置
        File saveDir = new File("src/main/resources/pdf/");
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+"credit-report.pdf");
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }
    }

    private byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
