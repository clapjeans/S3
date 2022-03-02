package kopo.poly.service.impl;

import kopo.poly.dto.MailDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.persistance.mapper.IUserMapper;
import kopo.poly.service.IMailService;
import kopo.poly.service.IUserService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;


@Slf4j
@Service("UserService")
public class UserService implements IUserService {


    private final IUserMapper userMapper;
    private final IMailService MailService;

    public UserService(IUserMapper userMapper, IMailService mailService) {
        this.userMapper = userMapper;
        MailService = mailService;
    }

    //회원가입
    @Override
    public int insertUser(UserInfoDTO pDTO) throws Exception {


        log.info(this.getClass().getName() + ".insertUser start");

        int res =userMapper.insertUser(pDTO);  //메퍼가 성공하면 1값이 돌아옴

        if(res ==1){
            res = 1; //서비스에 res 1 값을 돌려줌

            MailDTO mDTO = new MailDTO();

            mDTO.setToMail(EncryptUtil.decAES128CBC(CmmUtil.nvl(pDTO.getEmail())));
            mDTO.setTitle("회원가입을 축하드립니다");
            mDTO.setContents(CmmUtil.nvl(pDTO.getUser_name())+"님의 회원가입을 진심으로 축하드립니다");

            MailService.doSendMail(mDTO);
        } else {
            res = 0; //실패하면 res 0 값을 돌려줌
        }

        log.info(this.getClass().getName() + ".insertUser end");

        return res;
    }

    //이메일 유효성 체크
    @Override
    public int getUserExistForAJAX(UserInfoDTO pDTO) throws Exception {

        //이메일 존재 N :1, 이메일 존재 Y:2 기타에러 :0
        int res=0;

        log.info(this.getClass().getName() + ".getUserExistForAJAX start");

        UserInfoDTO rDTO = userMapper.getUserExistForAJAX(pDTO);

        //Mapper 에서 값이 정상적으로 못 넘어 오는 경우를 대비하기 위해사용
        if(rDTO ==null) {
            rDTO = new UserInfoDTO();
        }
        log.info(rDTO.getExists_yn());

        if(CmmUtil.nvl(rDTO.getExists_yn()).equals("Y")){
            res=2; //아이디 있으면 2값을 돌려줌
        }else if(CmmUtil.nvl(rDTO.getExists_yn()).equals("N")){
            res=1; //아이디 없으면  1 로돌려줌
        }

        log.info(this.getClass().getName() + ".getUserExistForAJAX end");

        return res;
    }


    //로그인
    @Override
    public UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".getUserLoginCheck start");

        UserInfoDTO rDTO= userMapper.getUserLoginCheck(pDTO);

        log.info(this.getClass().getName() + ".getUserLoginCheck end");

        return rDTO;
    }


    //비밀번호 변경
    @Override
    public int updateUserPw(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".updateUserPw start");

        int res = userMapper.updateUserPw(pDTO);

        log.info(this.getClass().getName() + ".updateUserPw end");

        return res;
    }

    //회원탈퇴
    @Override
    public int deleteUser(UserInfoDTO pDTO) throws Exception {

        log.info(this.getClass().getName() + ".deleteUser start");


        int res = userMapper.deleteUserInfo(pDTO);

        log.info(this.getClass().getName() + ".deleteUser end");

        return res;
    }

    @Override
    public UserInfoDTO getUserInfo(UserInfoDTO pDTO) {


        log.info(this.getClass().getName() + ".getUserInfo start");


        UserInfoDTO rDTO = userMapper.getUserInfo(pDTO);

        log.info(this.getClass().getName() + ".getUserInfo end");

        return rDTO;
    }

    @Override
    public int UpdateUserPage(UserInfoDTO pDTO) {
        log.info(this.getClass().getName() + ".UpdateUserPage start");


        int res = userMapper.UpdateUserPage(pDTO);

        log.info(this.getClass().getName() + ".UpdateUserPage end");

        return res;
    }

    @Override
    public UserInfoDTO findUserId(UserInfoDTO pDTO) {
        log.info(this.getClass().getName() + ".getUserLoginCheck start");

        UserInfoDTO rDTO= userMapper.findUserId(pDTO);

        log.info(this.getClass().getName() + ".getUserLoginCheck end");

        return rDTO;
    }

    @Override
    public List<UserInfoDTO> getUserList(UserInfoDTO pDTO) {

        log.info(this.getClass().getName() + ".getUserList start");

        List<UserInfoDTO> rList= userMapper.getUserList(pDTO);

        log.info(this.getClass().getName() + ".getUserList end");

        return rList;
    }


}
