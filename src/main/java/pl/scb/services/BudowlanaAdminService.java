package pl.scb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOError;

@Service
public class BudowlanaAdminService {
    @Autowired
    private ImageService imageService;

    public boolean uploadImage(String categoryName, MultipartFile[] files){
        return this.imageService.uploadImage(0,categoryName,"budowlanka", files);
    }

    public boolean removeImage(long id){
        return this.imageService.removeImage(id,"budowlanka");
    }

    public boolean removeCategory(String name) throws IOError {
        return this.imageService.removeCategory(name, "budowlanka");
    }


}
