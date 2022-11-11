package pl.scb.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.scb.utils.FileManager;

@RestController
@CrossOrigin
public class MainController {
    @GetMapping("/images/{parent_dir}/{post_dir}/{file}")
    public ResponseEntity<Object> getMultimedia(@PathVariable("parent_dir") String parentDirectory, @PathVariable("post_dir") String postDirectory, @PathVariable("file") String fileName){
        String location = "images/"+ parentDirectory+"/"+postDirectory+"/"+fileName;
        if(FileManager.doesFileExist(location)){
            return FileManager.getFile(location);
        }
        return null;
    }
}