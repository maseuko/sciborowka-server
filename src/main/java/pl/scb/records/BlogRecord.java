package pl.scb.records;

import pl.scb.models.BlogPost;
import pl.scb.models.Images;

import java.util.List;

public record BlogRecord(BlogPost blogPost, List<Images> images) {
}
