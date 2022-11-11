package pl.scb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.scb.models.Images;
import pl.scb.models.ResponseMessage;
import pl.scb.services.BudowlanaClientService;

import java.util.List;

@RestController
@RequestMapping("/budowlanka-client")
public class BudowlanaClientController {
    @Autowired
    private BudowlanaClientService budowlanaClientService;

    @GetMapping("categories")
    public ResponseEntity<Object> getCategories(){
        return new ResponseMessage(HttpStatus.OK).sendMessage("Categories fetched.", this.budowlanaClientService.getCategories());
    }

    @GetMapping("images")
    public ResponseEntity<Object> getByCategory(@RequestParam("category") String name){
        List<Images> images = this.budowlanaClientService.getImagesByCategoryName(name);
        if(images==null || images.isEmpty()){
            return new ResponseMessage(HttpStatus.NOT_FOUND).sendMessage("Category with this name doesn't exist.");
        }
        return new ResponseMessage(HttpStatus.OK).sendMessage("Images fetched.", images);
    }
}
