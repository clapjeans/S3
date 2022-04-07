package kopo.poly.controller;

import kopo.poly.dto.OcrDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IImgService;
import kopo.poly.service.IUserService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class MyPageController {

    //유저 서비스 활용
    @Resource(name = "ImgService")
    private IImgService imgService;

    //유저 서비스 활용
    @Resource(name = "UserService")
    private IUserService userService;

    //마이페이지 들어가기
    @GetMapping(value = "myPage")
    public String myPage(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".myPage start!");

        //세션에 담긴 아이디 암호화
        String user_id = EncryptUtil.encAES128CBC(CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")));

        log.info("user_id : " + user_id);


        //사용자 이미지 저장 경로 가져오기
        OcrDTO fDTO = new OcrDTO();
        fDTO.setUser_id(user_id);

        fDTO = imgService.getPath(fDTO);

        if (fDTO == null) {
            fDTO = new OcrDTO();

        }

        model.addAttribute("fDTO", fDTO);

        log.info(this.getClass().getName() + ".myPage end!");

        return "/mypage/mypage";
    }


    //마이페이지 화면 출력
    @GetMapping(value = "mypageEdit")
    public String mypageEdit(HttpSession session, ModelMap model,HttpServletRequest request) throws Exception {

        log.info(this.getClass().getName() + ".mypageEdit start!");

        String msg = "";


        //세션에 담긴 아이디 암호화
        String user_id = EncryptUtil.encAES128CBC(CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")));

        log.info("user_id : " + user_id);


        try {

            //유저 정보를 가져오기위해 아이디 값을 담아 보냄
            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setUser_id(user_id);

            //값 담아올 DTO 생성
            UserInfoDTO rDTO = userService.getUserInfo(pDTO);

            if (rDTO == null) {
                rDTO = new UserInfoDTO();

            }
            //이부분 수정하기 왜 Null 오는지 확인하기
            log.info("rDTO :"+rDTO);

            //사용자 이미지 저장 경로 가져오기
            OcrDTO fDTO = new OcrDTO();
            fDTO.setUser_id(user_id);

            fDTO = imgService.getPath(fDTO);

            if (fDTO == null) {
                fDTO = new OcrDTO();

            }
            log.info("fDTO :"+fDTO.getSave_file_path());

            //사용자 이미지 경로 보내기
            model.addAttribute("fDTO", fDTO);

            // 조회된 리스트 결과값 넣어주기
            model.addAttribute("rDTO", rDTO);

        } catch (Exception e) {
            msg = "실패하였습니다. : " + e.getMessage();
            log.info(e.toString());
            e.printStackTrace();

            //실패하면 마이페이지 못들어감!
            model.addAttribute("msg", msg);
            model.addAttribute("url", "/index");
            return "/redirect";

        }

        log.info(this.getClass().getName() + ".userPage end!");

        return "/mypage/mypageEdit";
    }

    //유저정보 변경
    @PostMapping(value = "UpdateMyPage")
    public String UpdateMyPage(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".mypageEdit end");


        // 유저정보 변경 결과에 대한 메시지 전달할 변수
        String msg = "";
        String url = "";

        //회원정보 변경을 담을 변수
        UserInfoDTO pDTO = null;


        try {

            String user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")); //세션에있는 아이디 담아오기
            String user_name = CmmUtil.nvl(request.getParameter("user_name")); //이름
            String email = CmmUtil.nvl(request.getParameter("user_email")); //이메일
            String addr1 = CmmUtil.nvl(request.getParameter("addr1")); //주소
            String addr2 = CmmUtil.nvl(request.getParameter("addr2")); //상세주소


            log.info(user_name);
            log.info(email);
            log.info(addr1);
            log.info(addr2);
            log.info(user_id);


            //유저 정보를 담기위함
            pDTO = new UserInfoDTO();
            pDTO.setUser_name(user_name);
            pDTO.setEmail(EncryptUtil.encAES128CBC(email)); // 이메일 AES-128-CBC 암호화
            pDTO.setUser_id(EncryptUtil.encAES128CBC(user_id)); //아이디 AES-128-CBC 암호화
            pDTO.setAddr1(addr1); //주소
            pDTO.setAddr2(addr2); //상세주소

            int res = userService.UpdateUserPage(pDTO);


            if (res == 1) {
                // 변경후 세션에 이름 새로답기
                session.setAttribute("SS_USER_NAME", user_name);
                msg = "회원정보가 변경되었습니다..";
                url = "/index";
            } else {
                msg = "오류로 인해  회원정보변경이 실패 하였습니다.";
                url = "/userPage";
            }

        } catch (Exception e) {
            // 저장이 실패되면 유저 보여줄 메시지
            msg = "오류로 인해 회원정보변경이 실패 하였습니다.";
            url = "/userPage";
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            log.info(this.getClass().getName() + "UpdateMyPage End!!");


            // 회원가입 여부 결과 메시지 전달하기
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);

            pDTO = null;
        }



        return "/redirect";
    }

    //관리자 페이지
    @GetMapping(value = "adminPage")
    public String adminPage(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".adminPage start");

        String user_type = CmmUtil.nvl((String) session.getAttribute("SS_USER_TYPE"));
        log.info("user_type : " +user_type );

        if (user_type.equals("")) {
            model.addAttribute("msg", "관리자만 접근가능합니다.");
            model.addAttribute("url", "/index");
            return "/redirect";
        }

        //관리자 아이디 제외 회원 정보가져오기
        UserInfoDTO pDTO = new UserInfoDTO();
        pDTO.setUser_id(EncryptUtil.encAES128CBC((String)session.getAttribute("SS_USER_ID"))); //아이디 AES-128-CBC 암호화

        List<UserInfoDTO> rList= userService.getUserList(pDTO);

        if(rList==null) {
            rList = new ArrayList<>();
        }


        for(UserInfoDTO rDTO : rList) {
            log.info("UserInfoDTO:" +rDTO.getUser_id());
            log.info("UserInfoDTO:" +rDTO.getUser_name());
        }
        log.info(this.getClass().getName() + ".adminPage end");

        model.addAttribute("rList",rList);
        return "/mypage/adminpage";
    }
}
