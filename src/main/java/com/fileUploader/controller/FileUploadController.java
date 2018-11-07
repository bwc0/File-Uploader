package com.fileUploader.controller;

import com.fileUploader.exception.StorageFileNotFoundException;
import com.fileUploader.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.stream.Collectors;

@Controller
public class FileUploadController {

    private StorageService storageService;

    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String listUploadedFiles(Model model) {
        model.addAttribute("files", storageService.loadAll()
                .map(path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));

        return "uploadForm";
    }

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\" " + file.getFilename() + " \"").body(file);
    }

    @PostMapping("/")
    public String fileUploadHandler(@RequestParam("file") MultipartFile file,
                                    RedirectAttributes redirectAttributes) {

        storageService.store(file);
        redirectAttributes.addFlashAttribute("message", "You successfully uploaded "
                + file.getOriginalFilename() + "!");

        return "redirect:/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> storageFileNotFoundHandler(StorageFileNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}
