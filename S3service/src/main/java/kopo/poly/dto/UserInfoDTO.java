package kopo.poly.dto;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class UserInfoDTO {

    private String user_id;  //사용자 아이디
    private String user_name; //사용자이름
    private String password; //사용자비밀번호
    private String email; //사용자이메일
    private String addr1; //사용자 주소
    private String addr2; //사용자 상세주소
    private String reg_id;
    private String reg_dt;
    private String chg_id;
    private String chg_dt;


    // 회원 가입시, 중복가입을 방지하기 위해 사용할 변수
    // DB조회 해서 회원이 존재하면 Y값을 반환함
    // DB테이블에 본재하지 않는 가상의 칼럼(ALIAS)
    private String exists_yn;
    private String save_thumfile_path;// 썸네일  파일경로 저장 조인으로 가져와야하는값 가상의컬럼
 

}
