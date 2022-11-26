package pl.scb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pl.scb.models.BlogPost;
import pl.scb.models.ImageCategory;
import pl.scb.records.BlogRecord;
import pl.scb.models.Images;
import pl.scb.repo.BlogPostRepo;
import pl.scb.repo.ImagesRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SciborowkaClientService {
    @Autowired
    private BlogPostRepo blogPostRepo;
    @Autowired
    private ImagesRepo imagesRepo;
    @Autowired
    private ImageService imageService;
    public List<BlogRecord> getPosts(int page, int limit){
        List<BlogRecord> blogPosts = new ArrayList<>();
        this.blogPostRepo.findAllReverse(PageRequest.of(page, limit)).forEach(blog -> {
            List<Images> im = this.imagesRepo.findByPostId(blog.getId());
            blogPosts.add(new BlogRecord(blog, im));
        });
        if(!blogPosts.isEmpty()){
            return blogPosts;
        }
        return null;
    }

    public BlogRecord getPost(long id){
        Optional<BlogPost> blog = this.blogPostRepo.findById(id);
        if(blog.isPresent()){
            List<Images> imgs = this.imagesRepo.findByPostId(blog.get().getId());
            return new BlogRecord(blog.get(), imgs);
        }
        return null;
    }

    public List<Images> getImagesByCategoryName(String name){
        return this.imageService.getImagesByCategoryName(name, "sciborowka");
    }

    public List<ImageCategory> getCategories(){
        return this.imageService.getCategories("sciborowka");
    }
}