package kopo.poly.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class S3Util {

    private String accessKey = "AKIAUTN25HIAFNGBFQ54";
    private String secretKey = "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";
    final private String bucketName = "rootimg";
    private String thumbuketName = "thumimg";

    private static AmazonS3 conn;

    public S3Util(AmazonS3 conn) {

        this.conn = conn;
//        log.info(accessKey);
//        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
//        conn = AmazonS3ClientBuilder.standard().withCredentials(new AWSStaticCredentialsProvider(credentials))
//                .withRegion(Regions.AP_NORTHEAST_2).build();

    }


    // 파일 삭제
    public int fileDelete(String bucketName, String fileName) {
        conn.deleteObject(bucketName, fileName);
        int res =1;
        return res;
    }

    // 파일 URL
    public String getFileURL(String bucketName, String fileName) {

        return conn.getUrl(bucketName, fileName).toString();

    }

    //파일업로드
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return upload(uploadFile, dirName);
    }


    //파일업로드
    private String upload(File uploadFile, String dirName) {
        String fileName = dirName + "/" + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName,bucketName);
        removeNewFile(uploadFile);

        return uploadImageUrl;
    }

    //s3에 파일 업로드
    private String putS3(File uploadFile, String fileName,String bucketName) {
        conn.putObject(new PutObjectRequest(bucketName, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return conn.getUrl(bucketName, fileName).toString();
    }

    //로컬파일삭제
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    //멀티파일 파일로 convert null확인하기위해 Optinal을 사용
    private Optional<File> convert(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        convFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return Optional.of(convFile);
    }

    public String thumnailurl(MultipartFile multipartFile, String dirName,String ext) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));


        return thumupload(uploadFile, dirName,ext);
    }

    //썸네일 파일업로드
    private String thumupload(File uploadFile, String dirName, String ext) {

        double ratio = 5; // 이미지 축소 비율
        String fileName = dirName + "/" + "s_" + uploadFile.getName();

        try {
            BufferedImage oImage = ImageIO.read(uploadFile); // 원본이미지
            int tWidth = (int) (oImage.getWidth() / ratio); // 생성할 썸네일이미지의 너비
            int tHeight = (int) (oImage.getHeight() / ratio); // 생성할 썸네일이미지의 높이

            BufferedImage tImage = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR); // 썸네일이미지
            Graphics2D graphic = tImage.createGraphics();
            Image image = oImage.getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH);
            graphic.drawImage(image, 0, 0, tWidth, tHeight, null);
            graphic.dispose(); // 리소스를 모두 해제

            ImageIO.write(tImage, ext, uploadFile);

        } catch (IOException e) {
            e.printStackTrace();
        }


        String uploadImageUrl = putS3(uploadFile, fileName,thumbuketName);
        removeNewFile(uploadFile);
        return uploadImageUrl;

    }
}

