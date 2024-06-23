package com.example.debricked.rule_engine.commons;


import org.springframework.web.multipart.MultipartFile;

import java.io.*;

public class CustomMultipartFile implements MultipartFile {

    private final byte[] fileContent;

    private String fileName;

    private String contentType;

    private File file;

    private String destPath = "tmpdir";

    private FileOutputStream fileOutputStream;

    public CustomMultipartFile(byte[] fileData, String name) {
        this.fileContent = fileData;
        this.fileName = name;
        file = new File(destPath + fileName);

    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException {
        fileOutputStream = new FileOutputStream(dest);
        fileOutputStream.write(fileContent);
    }

    public void clearOutStreams() throws IOException {
        if (null != fileOutputStream) {
            fileOutputStream.flush();
            fileOutputStream.close();
            file.deleteOnExit();
        }
    }

    @Override
    public String getName() {
        return this.fileName;
    }

    @Override
    public String getOriginalFilename() {
        return this.fileName;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        if(this.fileContent==null || this.fileContent.length==0){
            return true;
        }
        return false;
    }

    @Override
    public long getSize() {
        return this.fileContent.length;
    }

    @Override
    public byte[] getBytes() throws IOException {
        return fileContent;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(fileContent);
    }
}