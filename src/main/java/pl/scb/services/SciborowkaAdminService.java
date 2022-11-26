package pl.scb.services;

import org.springframework.beans.InvalidPropertyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.scb.models.*;
import pl.scb.repo.*;
import pl.scb.utils.FileManager;

import java.io.IOError;
import java.util.List;
import java.util.Optional;

@Service
public class SciborowkaAdminService {
    @Autowired
    private BlogPostRepo blogPostRepo;
    @Autowired
    private ImagesRepo imagesRepo;
    @Autowired
    private AdminRepo adminRepo;
    @Autowired
    private ImageService imageService;
    @Value("${jwt.secret}")
    private String secret;

    public BlogPost addBlog(BlogPost blogPost) throws InvalidPropertyException {
        if(blogPost.getDescription() == null){
            throw new InvalidPropertyException(this.getClass(),"Description","Invalid description parameter.");
        }
        if(blogPost.getTitle() == null){
            throw new InvalidPropertyException(this.getClass(),"Title","Invalid title parameter.");
        }
        return this.blogPostRepo.save(blogPost);
    }

    public boolean uploadImage(long id, String categoryName, MultipartFile[] files){
        return this.imageService.uploadImage(id,categoryName,"sciborowka",files);
    }

    public BlogPost updateBlog(BlogPost blog) throws IOError {
        Optional<BlogPost> currentBlogPost = this.blogPostRepo.findById(blog.getId());
        if(currentBlogPost.isPresent()){
            if(blog.getDescription().isEmpty()){blog.setDescription(currentBlogPost.get().getDescription());}
            if(blog.getTitle().isEmpty()){blog.setTitle(currentBlogPost.get().getTitle());}
            return this.blogPostRepo.save(blog);
        }

        throw new IOError(new Throwable("Blog not found."));
    }

    public boolean removeBlog(long id) throws IOError{
        Optional<BlogPost> currentBlogPost = this.blogPostRepo.findById(id);
        String directoryToDelete = "images/sciborowka/"+id;
        if(currentBlogPost.isPresent()){
            if(!FileManager.removeDirectory(directoryToDelete)){
                return false;
            };
            List<Images> im = this.imagesRepo.findAll().stream().filter(image -> {
                return image.getPostId() == currentBlogPost.get().getId();
            }).toList();
            for(Images image : im){
                this.imagesRepo.deleteById(image.getId());
            }
            this.blogPostRepo.delete(currentBlogPost.get());
            return true;
        }
        throw new IOError(new Throwable("There is no post with such ID"));
    }

    public boolean removeImage(long id){
        return this.imageService.removeImage(id, "sciborowka");
    }

    public boolean removeCategory(String name) throws IOError {
        return this.imageService.removeCategory(name,"sciborowka");
    }

    public boolean registerAdmin(AdminModel adminModel){
        adminModel.setPassword(BCrypt.hashpw(adminModel.getPassword(), this.secret));
        this.adminRepo.save(adminModel);
        return true;
    }

//    public boolean updateMainVideo(){
//        return this.imageService.uploadImage();
//    }
}
