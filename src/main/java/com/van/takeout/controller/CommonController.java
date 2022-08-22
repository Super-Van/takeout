package com.van.takeout.controller;

import com.van.takeout.util.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${uploadPath}")
    private String uploadPath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        assert filename != null;
        String suffix = filename.substring(filename.lastIndexOf("."));
        String uploadName = UUID.randomUUID().toString() + suffix;
        file.transferTo(new File(uploadPath + uploadName));
        return R.success(uploadName);
    }

    /**
     * @param filename
     * @return 不用ResponseEntity，可用HttpServletResponse参数代替
     * @throws IOException
     */
    @GetMapping("/download")
    public ResponseEntity<byte[]> download(@RequestParam("name") String filename) throws IOException {
        FileInputStream fis = new FileInputStream(uploadPath + filename);
        byte[] bytes = new byte[fis.available()];
        fis.read(bytes);
        return new ResponseEntity<>(bytes, new HttpHeaders(), HttpStatus.OK);
    }
}
