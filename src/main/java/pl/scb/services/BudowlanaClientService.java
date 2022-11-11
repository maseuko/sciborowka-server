package pl.scb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.scb.models.ImageCategory;
import pl.scb.models.Images;

import java.util.List;

@Service
public class BudowlanaClientService {
    @Autowired
    private ImageService imageService;
    public List<ImageCategory> getCategories(){
        return this.imageService.getCategories("budowlanka");
    }
    public List<Images> getImagesByCategoryName(String name){
        return this.imageService.getImagesByCategoryName(name, "budowlanka");
    }
}
