package kopo.poly.persistance.mapper;

import kopo.poly.dto.UserInfoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IUserMapper {
    // 유저 회원가입
    int insertUser(UserInfoDTO pDTO) throws Exception;

    // 유저 회원가입 여부, 이메일
    UserInfoDTO  getUserExistForAJAX(UserInfoDTO pDTO) throws Exception;

    // 유저 로그인
    UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO) throws Exception;

    // 유저 비밀번호 변경
    int updateUserPw(UserInfoDTO pDTO) throws Exception;

    // 유저 정보 삭제
    int deleteUserInfo(UserInfoDTO pDTO) throws Exception;

    //사용자 마이페이지 정보가져오기
    UserInfoDTO getUserInfo(UserInfoDTO pDTO);

    //사용자 정보 업데이트
    int UpdateUserPage(UserInfoDTO pDTO);

    //사용자 Id 찾기
    UserInfoDTO findUserId(UserInfoDTO pDTO);

    //사용자 정보가져오기
    List<UserInfoDTO> getUserList(UserInfoDTO pDTO);
}
