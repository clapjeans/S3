package kopo.poly.controller;

import kopo.poly.util.S3Util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public  abstract class AbstractImageController  {

    private final String bucketName ="thumimg" ;


    protected String thumnailUpload(String saveFilePath,String saveFileName,String ext){

        String fileurl=null;
//        S3Util s3 = new S3Util();

        //원본사진경로
        File oFile = new File(saveFilePath,saveFileName);


        double ratio =5; // 이미지 축소 비율

        //썸네일저장
        File tFile = new File(saveFilePath,"s_"+saveFileName);


        try {
            BufferedImage oImage = ImageIO.read(oFile); // 원본이미지
            int tWidth = (int) (oImage.getWidth() / ratio); // 생성할 썸네일이미지의 너비
            int tHeight = (int) (oImage.getHeight() / ratio); // 생성할 썸네일이미지의 높이

            BufferedImage tImage = new BufferedImage(tWidth, tHeight, BufferedImage.TYPE_3BYTE_BGR); // 썸네일이미지
            Graphics2D graphic = tImage.createGraphics();
            Image image = oImage.getScaledInstance(tWidth, tHeight, Image.SCALE_SMOOTH);
            graphic.drawImage(image, 0, 0, tWidth, tHeight, null);
            graphic.dispose(); // 리소스를 모두 해제

            ImageIO.write(tImage, ext, tFile);

//            s3.fileUpload(bucketName,tFile);
            //파일 url 가져오기
//            fileurl = s3.getFileURL(bucketName, "s_"+saveFileName);

        } catch (IOException e) {
            e.printStackTrace();
        }

       return fileurl;
    }
}
