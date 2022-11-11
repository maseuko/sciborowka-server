package pl.scb.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.scb.models.BlogPost;

public interface BlogPostRepo extends JpaRepository<BlogPost, Long> {
}