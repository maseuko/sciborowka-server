package pl.scb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.scb.models.ImageCategory;
import pl.scb.models.Images;
import pl.scb.models.PageOwner;
import pl.scb.repo.ImageCategoryRepo;
import pl.scb.repo.ImagesRepo;
import pl.scb.repo.PageOwnerRepo;
import pl.scb.utils.FileManager;
import pl.scb.utils.OwnerManager;

import java.io.IOError;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ImageService {
    @Autowired
    private OwnerManager ownerManager;
    @Autowired
    private ImageCategoryRepo imageCategoryRepo;
    @Autowired
    private ImagesRepo imagesRepo;
    @Autowired
    private PageOwnerRepo pageOwnerRepo;
    @Autowired
    private FileManager fileManager;
    @Value("${domain_name}")
    private String domain;

    public boolean uploadImage(long id, String categoryName, String owner,MultipartFile[] files) {
        ImageCategory category = null;
        Optional<PageOwner> pageOwner = pageOwnerRepo.findByName(owner);
        if (pageOwner.isEmpty()) return false;

        Optional<ImageCategory> categoryOptional = this.imageCategoryRepo.findByNameAndOwnerId(categoryName, pageOwner.get().getOwnerId());
        if (categoryOptional.isEmpty() && !categoryName.isEmpty()) {
            category = this.imageCategoryRepo.save(new ImageCategory(categoryName, pageOwner.get().getOwnerId()));
        } else if(!categoryName.isEmpty()) {
            category = categoryOptional.get();
        }

        if (id != 0) {
            this.fileManager.setFileLocation("images/" + owner + "/" + id + "/");
        } else {
            this.fileManager.setFileLocation("images/categories/" + category.getCategoryId() + "/");
        }

        List<String> sources;
        try {
            sources = this.fileManager.uploadFiles(files);
        } catch (IOException e) {
            return true;
        }
        for (String src : sources) {
            Images img = new Images();
            img.setPostId(id);
            img.setUrl(src);
            img.setCategoryId(category!=null ? category.getCategoryId():0);
            this.imagesRepo.save(img);
        }
        return false;
    }
    public boolean removeCategory(String name, String owner) throws IOError {
        Optional<ImageCategory> currentCategory = this.imageCategoryRepo.findByName(name);
        String directoryToDelete;

        if(currentCategory.isPresent()){
            if(!this.ownerManager.isCategoryOwner(currentCategory.get(),owner)) return false;
            directoryToDelete = "images/categories/"+currentCategory.get().getCategoryId();
            if(!FileManager.removeDirectory(directoryToDelete)){
                return false;
            };
            List<Images> im = this.imagesRepo.findAll().stream().filter(image -> {
                return image.getCategoryId() == currentCategory.get().getCategoryId();
            }).toList();
            for(Images image : im){
                this.imagesRepo.deleteById(image.getId());
            }
            this.imageCategoryRepo.delete(currentCategory.get());
            return true;
        }
        throw new IOError(new Throwable("There is no category with such ID"));
    }

    public boolean removeImage(long id, String owner){
        Optional<Images> image = imagesRepo.findById(id);
        if(image.isPresent()){
            if(!this.ownerManager.isImageOwner(image.get(),owner)) return false;
            String filePath = image.get().getUrl().replace(this.domain,"");
            if(FileManager.removeFile(filePath)){
                imagesRepo.deleteAllById(Collections.singleton(id));
                return true;
            }
            return false;
        }
        return false;
    }

    public List<ImageCategory> getCategories(String ownerName){
        Optional<PageOwner> pageOwner = pageOwnerRepo.findByName(ownerName);
        if(pageOwner.isEmpty()) return new ArrayList<>();
        PageOwner owner = pageOwner.get();
        Optional<List<ImageCategory>> categories =  this.imageCategoryRepo.findByOwnerId(owner.getOwnerId());
        return categories.orElseGet(ArrayList::new);
    }

    public List<Images> getImagesByCategoryName(String name, String owner){
        Optional<PageOwner> pageOwner = pageOwnerRepo.findByName(owner);
        if(pageOwner.isEmpty()) return new ArrayList<>();

        Optional<ImageCategory> category = this.imageCategoryRepo.findByNameAndOwnerId(name,pageOwner.get().getOwnerId());
        if(category.isEmpty()) return null;
        return this.imagesRepo.findByCategoryId(category.get().getCategoryId());
    }

}
