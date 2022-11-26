package pl.scb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.scb.models.ResponseMessage;
import pl.scb.services.BudowlanaAdminService;

@RestController
@CrossOrigin
@RequestMapping("/budowlanka-admin")
public class BudowlanaAdminController {
    @Autowired
    private BudowlanaAdminService budowlanaAdminService;

    @PostMapping("/upload-images")
    public ResponseEntity<Object> uploadCategoryImage(@RequestParam("category") String categoryName, @RequestParam("files") MultipartFile[] files){
        if(this.budowlanaAdminService.uploadImage(categoryName, files)){
            return new ResponseMessage(HttpStatus.BAD_REQUEST).sendMessage("Images couldn't be uploaded.");
        }
        return new ResponseMessage(HttpStatus.CREATED).sendMessage("Images uploaded successfully.");
    }

    @DeleteMapping("/remove-image/{id}")
    public ResponseEntity<Object> removeImage(@PathVariable("id") long id){
        if(this.budowlanaAdminService.removeImage(id)){
            return new ResponseMessage(HttpStatus.ACCEPTED).sendMessage("File removed.");
        }
        return new ResponseMessage(HttpStatus.EXPECTATION_FAILED).sendMessage("File couldn't be removed.");
    }

    @DeleteMapping("/remove-category/{name}")
    public ResponseEntity<Object> removeCategory(@PathVariable("name") String name){
        if(this.budowlanaAdminService.removeCategory(name)){
            return new ResponseMessage(HttpStatus.ACCEPTED).sendMessage("Category removed.");
        }
        return new ResponseMessage(HttpStatus.EXPECTATION_FAILED).sendMessage("Category couldn't be removed.");
    }
}
