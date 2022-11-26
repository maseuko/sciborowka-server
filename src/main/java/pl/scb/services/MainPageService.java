package pl.scb.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.scb.utils.FileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainPageService {
    @Autowired
    private FileManager fileManager;
    @Value("${domain_name}")
    private String domain;

    public boolean updateMainMovie(MultipartFile[] file){
        this.fileManager.setFileLocation("images/main-page/video/");
        List<String> sources;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("data/movie.txt"));
            String fileLoc = bufferedReader.readLine().replace(this.domain,"");
            FileManager.removeFile(fileLoc);
            sources = this.fileManager.uploadFiles(file);
            Files.write(Paths.get("data/movie.txt"), Collections.singleton(sources.get(0)));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean updateAboutImage(MultipartFile[] file){
        this.fileManager.setFileLocation("images/main-page/about/");
        List<String> sources;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("data/about_background.txt"));
            String fileLoc = bufferedReader.readLine().replace(this.domain,"");
            FileManager.removeFile(fileLoc);
            sources = this.fileManager.uploadFiles(file);
            Files.write(Paths.get("data/about_background.txt"), Collections.singleton(sources.get(0)));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean updateAboutText(String about){
        try {
            Files.write(Paths.get("data/about.txt"), Collections.singleton(about));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Map<String, String> getDefaultPageConfig(){
        Map<String, String> config = new HashMap<>();
        try {
            List<String> aboutDesc = Files.readAllLines(Paths.get("data/about.txt"));
            BufferedReader bufferedMovieReader = new BufferedReader(new FileReader("data/movie.txt"));
            BufferedReader bufferedBackgroundReader = new BufferedReader(new FileReader("data/about_background.txt"));
            String ab = "";
            for(String s : aboutDesc){
                ab+=s;
            }

            config.put("aboutDesc", ab);
            config.put("aboutImg", bufferedBackgroundReader.readLine());
            config.put("movieUrl", bufferedMovieReader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return config;
    }
}
