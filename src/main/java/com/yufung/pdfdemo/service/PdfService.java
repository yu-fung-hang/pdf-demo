package com.yufung.pdfdemo.service;

import com.yufung.pdfdemo.model.CreditSafeAuthenticateResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class PdfService {
    @Autowired
    RestTemplate restTemplate;

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

    public void saveFromRemote(String companyId) throws IOException {
        if (!StringUtils.hasLength(companyId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "companyId cannot be null");
        }

        String token = authenticate();
        URL url = new URL("https://connect.creditsafe.com/v1/companies/CA-X-CA08358159");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(5*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        conn.setRequestProperty("Authorization", token);
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
        File file = new File(saveDir + File.separator + companyId + ".pdf");
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

    private String authenticate() {
        try {
            MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
            requestMap.add("username", "creditsafe_production@ott.ca");
            requestMap.add("password", "owlliho^ra7FOg2A3jiS");

            String url = "https://connect.creditsafe.com/v1/authenticate";
            CreditSafeAuthenticateResponse apiResponse = restTemplate.postForObject(url, requestMap, CreditSafeAuthenticateResponse.class);
            if (apiResponse == null) {
                return null;
            }

            return apiResponse.getToken();
        } catch (Exception e) {
            return null;
        }
    }
}
