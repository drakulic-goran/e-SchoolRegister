package com.iktpreobuka.projekat_za_kraj.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface FileHandler {

	public String singleFileUpload(MultipartFile file, RedirectAttributes redirectAttributes) throws IOException;

}
