package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.scb.models.BlogPost;

import org.springframework.data.domain.PageRequest;
import java.util.List;

public interface BlogPostRepo extends JpaRepository<BlogPost, Long> {
    @Query(value = "SELECT * FROM sciborowka.blog_post ORDER BY id DESC", nativeQuery = true)
    List<BlogPost> findAllReverse(PageRequest pageable);
}