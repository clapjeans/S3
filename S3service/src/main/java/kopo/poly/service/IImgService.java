package kopo.poly.service;

import kopo.poly.dto.OcrDTO;

import java.util.Map;

public interface IImgService {

    //이미지 경로저장
    int insertFilePath(OcrDTO fDTO)throws Exception;

    //이미지 경로 가져오기
    OcrDTO getPath(OcrDTO fDTO);
}
