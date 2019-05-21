package com.infoshareacademy.jjdd6.servlet;

import com.infoshareacademy.jjdd6.service.FileUploadProcessor;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

@MultipartConfig
@WebServlet("/upload")
public class UploaderServlet extends HttpServlet {
    @Inject
    private FileUploadProcessor fileUploadProcessor;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        File file = fileUploadProcessor.uploadFile(req.getPart("file"));
        resp.sendRedirect("/admin/fileupload.html");

    }
}
