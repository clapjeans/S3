package kopo.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OcrDTO {

    private String save_file_path; // 저장된 이미지 파일의 파일 저장경로
    private String save_file_name; // 저장된 파일 이미지 파일 이름
    private String org_file_name; // 원래 파일명
    private String ext; // 파일 확장자
    private String chg_id; // 최근 수정자
    private String chg_dt; // 최근 수정일
    private String user_id;//저장아이디
    private String save_sfile_name; // 저장된 썸네일 파일 이미지 파일 이름
    private String save_thumfile_path;// 썸네일  파일경로 저장

}
