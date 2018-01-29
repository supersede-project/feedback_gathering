package ch.fhnw.cere.repository.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringJoiner;

/**
 * Created by Aydinli on 29.01.2018.
 */
@RestController
@RequestMapping(value = "${supersede.base_path.feedback}/{language}/applications/{applicationId}/feedbacks")
public class UploadController {
    private static String UPLOADED_FOLDER = "F://supersede_profilepics//";

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String singleFileUpload(@RequestParam("file") MultipartFile file){
        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            return "successfully uploaded file";

        } catch (IOException e) {
            e.printStackTrace();
            return "did not work";
        }
    }

    @RequestMapping(value = "/uploadMulti", method = RequestMethod.POST)
    public String multiFileUpload(@RequestParam("files") MultipartFile[] files){
        StringJoiner sj = new StringJoiner(" , ");

        for(MultipartFile file: files){
            if (file.isEmpty()) {
                continue; //next pls
            }

            try {
                byte[] bytes = file.getBytes();
                Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
                Files.write(path, bytes);

                sj.add(file.getOriginalFilename());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "successfully uploaded files";
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam("file") MultipartFile file) throws IOException{
        if (!file.isEmpty()) {
            BufferedImage src = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            File destination = new File(UPLOADED_FOLDER + "//test_pic_1.png"); // something like C:/Users/tom/Documents/nameBasedOnSomeId.png
            ImageIO.write(src, "png", destination);
            //Save the id you have used to create the file name in the DB. You can retrieve the image in future with the ID.
            return "Image successfully uploaded";
        }
        return "Image is empty";
    }
}
