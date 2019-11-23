package com.iktpreobuka.projekat_za_kraj.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.annotation.JsonView;
import com.iktpreobuka.projekat_za_kraj.security.Views;

@Secured({"ROLE_ADMIN"})
@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping(path = "/project/download")
public class DownloadControler {

/*	@Autowired 
	private FileHandler fileHandler;
*/
	
	private final Logger logger= (Logger) LoggerFactory.getLogger(this.getClass());

	@RequestMapping(method = RequestMethod.GET) 
	public String index() { 
		return "download"; 
		}
	
/*	@RequestMapping(method = RequestMethod.GET, value = "/uploadStatus") 
	public String uploadStatus() { 
		return "uploadStatus"; 
		}
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method = RequestMethod.POST, value = "/upload") 
	public String singleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		String result = null; 
		try { 
			result = fileHandler.singleFileUpload(file, redirectAttributes); 
			logger.debug("Upload finished.");
			} 
		catch (IOException e) { 
			e.printStackTrace(); 
			} 
		return result;
		}
*/
	
	@Secured({"ROLE_ADMIN"})
	@JsonView(Views.Admin.class)
	@RequestMapping(method=RequestMethod.GET, value="/download")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile() {
		logger.debug("################## Download started.");

		File file = new File("D:\\Java kurs\\backend\\Projects\\workspaceIKTPreobuka\\projekat_za_kraj\\logs\\project-logging.log");
		logger.debug("File created.");

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=project-logging.log");
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = null;
		try {
			resource = new ByteArrayResource(Files.readAllBytes(path));
		} catch (IOException e) {
			e.getMessage();
		}

		logger.debug("------------------ Download finished.");
        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
	}

}
