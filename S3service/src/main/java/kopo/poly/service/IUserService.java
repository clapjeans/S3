package kopo.poly.service;

import kopo.poly.dto.UserInfoDTO;

import java.util.List;

public interface IUserService {
    // 유저 회원가입
    int insertUser(UserInfoDTO pDTO) throws Exception;

    // 유저 아이디 유효성확인
    int getUserExistForAJAX(UserInfoDTO pDTO) throws Exception;

    // 유저 로그인
    UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO) throws Exception;

    // 유저 비밀번호 변경
    int updateUserPw(UserInfoDTO pDTO) throws Exception;

    // 유저 삭제
    int deleteUser(UserInfoDTO pDTO) throws Exception;

    //유저 정보 가져오기
    UserInfoDTO getUserInfo(UserInfoDTO pDTO);

    //유저 정보 변경하기
    int UpdateUserPage(UserInfoDTO pDTO);

    //유저 아이디찾기
    UserInfoDTO findUserId(UserInfoDTO pDTO);

    //회원리스트가져오기
    List<UserInfoDTO> getUserList(UserInfoDTO pDTO);
}
