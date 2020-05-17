package br.com.weliton.AWSS3WithSpring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "api/file")
public class FileRestController {
	@Autowired
	private FileService fileService;
	
	@PostMapping
	public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
		return (ResponseEntity<String>) fileService.uploadFile(file);
	}
	@GetMapping("{fileName}")
	public ResponseEntity<FileSystemResource> downloadImagem(@PathVariable String fileName){
		return (ResponseEntity<FileSystemResource>) fileService.getFile(fileName); 
	}
	@DeleteMapping("{fileName}")
	public ResponseEntity<Boolean> deletarArquivos(@PathVariable String fileName){
		return (ResponseEntity<Boolean>) fileService.deleteFile(fileName); 
	}

}
