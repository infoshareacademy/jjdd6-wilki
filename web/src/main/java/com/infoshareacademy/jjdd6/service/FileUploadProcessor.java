package com.infoshareacademy.jjdd6.service;

import com.infoshareacademy.jjdd6.properties.WebAppProperties;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@RequestScoped
public class FileUploadProcessor {

    @Inject
    private WebAppProperties webAppProperties;

    public File uploadFile(Part filePart) throws IOException {

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        File file = new File(getUploadImageFilesPath() + fileName);

        Files.deleteIfExists(file.toPath());

        InputStream fileContent = filePart.getInputStream();
        Files.copy(fileContent, file.toPath());
        fileContent.close();
        return file;
    }

    private String getUploadImageFilesPath() {
        return webAppProperties.getSetting("CHART_LOCATION") + "/";
    }
}
