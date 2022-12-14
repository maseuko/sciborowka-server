package pl.scb.controller;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.scb.models.BlogPost;
import pl.scb.models.ResponseMessage;
import pl.scb.models.AdminModel;
import pl.scb.services.MainPageService;
import pl.scb.services.SciborowkaAdminService;

import java.io.IOError;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/sciborowka-admin")
public class SciborowkaAdminController {
    @Autowired
    private SciborowkaAdminService sciborowkaAdminService;
    @Autowired
    private MainPageService mainPageService;

    @PostMapping("/add-post")
    public ResponseEntity<Object> addPost(@RequestBody BlogPost blogPost){
        BlogPost bp;
        try{
            bp = this.sciborowkaAdminService.addBlog(blogPost);
        }catch(InvalidPropertyException e){
            return new ResponseMessage(HttpStatus.BAD_REQUEST).sendMessage(e.getMessage());
        }
        return new ResponseMessage(HttpStatus.CREATED).sendMessage("Post added successfully.", bp);
    }

    @PostMapping("/upload-images")
    public ResponseEntity<Object> uploadCategoryImage(@RequestParam("category") String categoryName, @RequestAttribute("files") MultipartFile[] files){
        if(this.sciborowkaAdminService.uploadImage(0, categoryName, files)){
            return new ResponseMessage(HttpStatus.BAD_REQUEST).sendMessage("Images couldn't be uploaded.");
        }
        return new ResponseMessage(HttpStatus.CREATED).sendMessage("Images uploaded successfully.");
    }

    @PostMapping("/upload-images/{post_id}")
    public ResponseEntity<Object> uploadImages(@PathVariable("post_id") long id, @RequestAttribute("files") MultipartFile[] files){
        if(this.sciborowkaAdminService.uploadImage(id, "blog", files)){
            return new ResponseMessage(HttpStatus.BAD_REQUEST).sendMessage("Images couldn't be uploaded.");
        }
        return new ResponseMessage(HttpStatus.CREATED).sendMessage("Images uploaded successfully.");
    }

    @PostMapping("/modify-post")
    public ResponseEntity<Object> modifyPost(@RequestBody BlogPost blogPost){
        BlogPost bp;
        try{
            bp = this.sciborowkaAdminService.updateBlog(blogPost);
        }catch (IOError error){
            return new ResponseMessage(HttpStatus.NOT_FOUND).sendMessage("Couldn't find the post with such an id.");
        }
        return new ResponseMessage(HttpStatus.OK).sendMessage("Blog updated.", bp);
    }

    @DeleteMapping("/remove-post/{id}")
    public ResponseEntity<Object> removePost(@PathVariable("id") long id){
        try{
            if(this.sciborowkaAdminService.removeBlog(id)){
                return new ResponseMessage(HttpStatus.ACCEPTED).sendMessage("Post deleted.");
            }else {
                return new ResponseMessage(HttpStatus.ACCEPTED).sendMessage("Post couldn't be deleted.");
            }
        }catch (IOError e){
            return new ResponseMessage(HttpStatus.NOT_FOUND).sendMessage("Post not found.");
        }
    }

    @DeleteMapping("/remove-image/{id}")
    public ResponseEntity<Object> removeImage(@PathVariable("id") long id){
        if(this.sciborowkaAdminService.removeImage(id)){
            return new ResponseMessage(HttpStatus.ACCEPTED).sendMessage("File removed.");
        }
        return new ResponseMessage(HttpStatus.EXPECTATION_FAILED).sendMessage("File couldn't be removed.");
    }
    @DeleteMapping("/remove-category/{name}")
    public ResponseEntity<Object> removeCategory(@PathVariable("name") String name){
        if(this.sciborowkaAdminService.removeCategory(name)){
            return new ResponseMessage(HttpStatus.ACCEPTED).sendMessage("Category removed.");
        }
        return new ResponseMessage(HttpStatus.EXPECTATION_FAILED).sendMessage("Category couldn't be removed.");
    }
    @PostMapping("/update-about-image")
    private ResponseEntity<Object> updateAboutImage(@RequestAttribute("files") MultipartFile[] files){
        if(this.mainPageService.updateAboutImage(files)){
            return new ResponseMessage(HttpStatus.OK).sendMessage("Main page updated.");
        }else{
            return new ResponseMessage(HttpStatus.EXPECTATION_FAILED).sendMessage("Movie couldn't be updated.");
        }
    }
    @PostMapping("/update-about-description")
    private ResponseEntity<Object> updateAboutDescription(@RequestBody Map<String, String> desc){
        System.out.println(desc.toString());
        if(this.mainPageService.updateAboutText(desc.get("description"))){
            return new ResponseMessage(HttpStatus.OK).sendMessage("Main page updated.");
        }else{
            return new ResponseMessage(HttpStatus.EXPECTATION_FAILED).sendMessage("Movie couldn't be updated.");
        }
    }

    @PostMapping("/update-main-movie")
    private ResponseEntity<Object> updateMovie(@RequestAttribute("files") MultipartFile[] files){
        if(this.mainPageService.updateMainMovie(files)){
            return new ResponseMessage(HttpStatus.OK).sendMessage("Main page updated.");
        }else{
            return new ResponseMessage(HttpStatus.EXPECTATION_FAILED).sendMessage("Movie couldn't be updated.");
        }
    }
}