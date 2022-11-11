package pl.scb.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.scb.models.ImageCategory;
import pl.scb.models.Images;
import pl.scb.models.PageOwner;
import pl.scb.repo.ImageCategoryRepo;
import pl.scb.repo.ImagesRepo;
import pl.scb.repo.PageOwnerRepo;

import java.util.Optional;

@Component
public class OwnerManager {
    @Autowired
    private PageOwnerRepo pageOwnerRepo;
    @Autowired
    private ImagesRepo imagesRepo;
    @Autowired
    private ImageCategoryRepo imageCategoryRepo;

    public boolean isImageOwner(Images img, String name){
        Optional<PageOwner> owner = this.pageOwnerRepo.findByName(name);
        Optional<ImageCategory> category = this.imageCategoryRepo.findById(img.getCategoryId());
        if(owner.isEmpty()) return false;
        if(category.isEmpty()) return false;
        return owner.get().getOwnerId() == category.get().getOwnerId();
    }

    public boolean isCategoryOwner(ImageCategory category ,String name){
        Optional<PageOwner> owner = this.pageOwnerRepo.findByName(name);
        if(owner.isEmpty()) return false;
        return owner.get().getOwnerId() == category.getOwnerId();
    }
}
