package code.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/files")
public class FileController {
  private static final String UPLOAD_DIR = "uploads";

  @GetMapping("/upload")
  public String showUploadForm() {
    return "files/upload-form";
  }

  @PostMapping("/upload")
  public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
    if (file.isEmpty()) {
      model.addAttribute("message", "Please select a file to upload.");
      return "files/upload-form";
    }

    try {
      Path uploadPath = Paths.get(UPLOAD_DIR);
      if (!Files.exists(uploadPath)) {
        Files.createDirectories(uploadPath);
      }

      Path filePath = uploadPath.resolve(file.getOriginalFilename());
      file.transferTo(filePath.toFile());

      model.addAttribute("message", "File uploaded successfully: " + file.getOriginalFilename());
      model.addAttribute("path", filePath.toString());

    } catch (IOException e) {
      model.addAttribute("message", "File upload failed: " + e.getMessage());
    }

    return "files/upload-form";
  }
}
