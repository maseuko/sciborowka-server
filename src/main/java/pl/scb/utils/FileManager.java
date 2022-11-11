package pl.scb.utils;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Component
@NoArgsConstructor
public class FileManager {
    private String fileLocation;
    private final static String[] imageTypes = {"png","jpg","jpeg","webp"};
    private final static String[] videoTypes = {"mp3","mp4"};
    public FileManager(String fileLocation) {
        this.fileLocation = fileLocation;
    }
    @Value("domain")
    private String domain;

    private void checkForDirectoryExistence(){
        File directory = new File(this.fileLocation);
        if(!directory.exists()){
            try {
                Files.createDirectories(Path.of(this.fileLocation));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public static boolean doesFileExist(String location){
        File file = new File(location);
        return file.exists() && file.isFile();
    }
    public static boolean doesDirectoryExist(String directoryLocation){
        File file = new File(directoryLocation);
        return file.exists() && file.isDirectory();
    }
    public static ResponseEntity<Object> getFile(String fileLocation){
        File file = new File(fileLocation);
        String[] extension = file.getName().split("\\.");
        String contentType = "";

        for(String imType : FileManager.imageTypes){
            if(extension[extension.length-1].equals(imType)){
                contentType = "image/"+extension[extension.length-1];
            }
        }

        for(String vidType: FileManager.videoTypes){
            if(extension[extension.length-1].equals(vidType)){
                contentType = "video/"+extension[extension.length-1];
            }
        }

        try {
            InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
            return ResponseEntity.ok().contentType(MediaType.valueOf(contentType)).contentLength(file.length()).body(resource);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public List<String> uploadFiles(MultipartFile[] files) throws IOException{
        this.checkForDirectoryExistence();
        List<String> sources = new ArrayList<>();
        for(MultipartFile file: files){
            String fileName = UUID.randomUUID()+file.getOriginalFilename();
            file.transferTo(Path.of(this.fileLocation+fileName));
            sources.add(domain+this.fileLocation+fileName);
        }
        return sources;
    }
    public static boolean removeFile(String fileLocation){
        if(FileManager.doesFileExist(fileLocation)){
            File file = new File(fileLocation);
            return file.delete();
        }
        return false;
    }

    public static boolean removeDirectory(String directoryLocation){
        if(FileManager.doesDirectoryExist(directoryLocation)){
            return FileSystemUtils.deleteRecursively(new File(directoryLocation));
        }
        return true;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }
}
