package pl.scb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.scb.models.Images;
import pl.scb.models.ResponseMessage;
import pl.scb.records.BlogRecord;
import pl.scb.services.MainPageService;
import pl.scb.services.SciborowkaClientService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/sciborowka-client")
public class SciborowkaClientController {
    @Autowired
    private SciborowkaClientService sciborowkaClientService;
    @Autowired
    private MainPageService mainPageService;

    @GetMapping("get-posts")
    public ResponseEntity<Object> getPosts(@RequestParam("page") int page, @RequestParam("limit") int limit){
        List<BlogRecord> blogRecords = sciborowkaClientService.getPosts(page, limit);
        if(blogRecords != null && !blogRecords.isEmpty()){
            return new ResponseMessage(HttpStatus.OK).sendMessage("Blogs fetched succesfully.", blogRecords);
        }
        return new ResponseMessage(HttpStatus.NOT_FOUND).sendMessage("No posts found.");
    }

    @GetMapping("get-post/{id}")
    public ResponseEntity<Object> getPost(@PathVariable("id") long id){
        BlogRecord record = this.sciborowkaClientService.getPost(id);
        if(record != null){
            return new ResponseMessage(HttpStatus.OK).sendMessage("Blog Post fetched succesfully.", record);
        }
        return new ResponseMessage(HttpStatus.NOT_FOUND).sendMessage("Blog not found.");
    }
    @GetMapping("images")
    public ResponseEntity<Object> getByCategory(@RequestParam("category") String name){
        List<Images> images = this.sciborowkaClientService.getImagesByCategoryName(name);
        if(images==null || images.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND).sendMessage("Category with this name doesn't exist.");
        }
        return new ResponseMessage(HttpStatus.OK).sendMessage("Images fetched.", images);
    }

    @GetMapping("categories")
    public ResponseEntity<Object> getCategories(){
        return new ResponseMessage(HttpStatus.OK).sendMessage("Categories fetched.", this.sciborowkaClientService.getCategories());
    }

    @GetMapping("main-page-config")
    public ResponseEntity<Object> getMainPageConfig(){
        return new ResponseMessage(HttpStatus.OK).sendMessage("Config fetched.",this.mainPageService.getDefaultPageConfig());
    }


}
