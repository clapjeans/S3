package kopo.poly.persistance.mapper;


import kopo.poly.dto.OcrDTO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface IImgMapper {
	


	//파일 경로 저장하기
	 int insertFilePath(OcrDTO fDTO) throws Exception;

    // path 가져오기
    OcrDTO getFilePath(OcrDTO fDTO);
}
