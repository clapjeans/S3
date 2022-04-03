package kopo.poly.controller;


import kopo.poly.dto.MailDTO;
import kopo.poly.dto.UserInfoDTO;
import kopo.poly.service.IImgService;
import kopo.poly.service.IMailService;
import kopo.poly.service.IUserService;
import kopo.poly.util.CmmUtil;
import kopo.poly.util.EncryptUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


@Slf4j
@Controller
public class UserController extends AbstractCommController {

    //유저 서비스 활용
    @Resource(name = "ImgService")
    private IImgService imgService;

    //유저 서비스 활용
    @Resource(name = "UserService")
    private IUserService userService;

    //메일서비스 발송을 위한 쿼리 넣어야하는지 솔직히 잘모르겠음 
    @Resource(name = "MailService")
    private IMailService mailService;

    //비밀번호찾기 페이지
    @GetMapping(value = "index")
    public String index() throws Exception {

        log.info(this.getClass().getName() + ".findPwPage start");

        log.info(this.getClass().getName() + ".findPwPage end");

        return "/index";
    }


    //비밀번호찾기 페이지
    @GetMapping(value = "findPwPage")
    public String findPwPage() throws Exception {

        log.info(this.getClass().getName() + ".findPwPage start");

        log.info(this.getClass().getName() + ".findPwPage end");

        return "/login/findPw";
    }

    //아이디 찾기 페이지
    @GetMapping(value = "findIdPage")
    public String findIdPage() throws Exception {

        log.info(this.getClass().getName() + ".findPwPage start");

        log.info(this.getClass().getName() + ".findPwPage end");

        return "/login/findId";
    }

    //로그인 페이지들어가기
    @GetMapping(value = "loginPage")
    public String loginPage() throws Exception {

        log.info(this.getClass().getName() + ".loginPage start!");

        log.info(this.getClass().getName() + ".loginPage end!");

        return "/login/login";
    }

    //회원등록 페이지 
    @GetMapping(value = "register")
    public String register() throws Exception {

        log.info(this.getClass().getName() + ".register start!");

        log.info(this.getClass().getName() + ".register end!");

        return "/login/creatUser";
    }


    //회원로그아웃하기
    @GetMapping(value = "logout")
    public String logout(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".logout start");


        // session 비움 전체비우는 함수
        session.invalidate();

        // 결과 메세지 전달하기
        model.addAttribute("msg", "로그아웃 성공");
        model.addAttribute("url", "/index");

        log.info(this.getClass().getName() + ".logout end");

        return "/redirect";
    }


    //유저 생성
    @PostMapping(value = "insertUser")
    public String insertUser(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".insertUser start");

        int res = 0;  //수정 제대로 됐는지 확인하기

        // 회원가입 결과에 대한 메시지 전달할 변수
        String msg = "";
        String url = "";

        //웹 회원가입결과에 대한 메시지를 전달할변수
        UserInfoDTO pDTO = null;

        try {

            String user_id = CmmUtil.nvl(request.getParameter("user_id")); //회원아이디
            String user_name = CmmUtil.nvl(request.getParameter("user_name"));  //회원이름
            String password = CmmUtil.nvl(request.getParameter("user_pw")); // 비밀번호
            String email = CmmUtil.nvl(request.getParameter("user_email")); //이메일
            String addr1 = CmmUtil.nvl(request.getParameter("addr1")); //주소
            String addr2 = CmmUtil.nvl(request.getParameter("addr2")); //상세주소

            log.info(user_id);
            log.info(user_name);
            log.info(password);
            log.info(email);
            log.info(addr1);
            log.info(addr2);


            //유저 정보를 담기위함
            pDTO = new UserInfoDTO();
            pDTO.setUser_id(EncryptUtil.encAES128CBC(user_id)); //아이디 AES-128-CBC 암호화
            pDTO.setUser_name(user_name);
            pDTO.setPassword(EncryptUtil.encHashSHA256(password)); // 비밀번호 해시 알고리즘 암호화
            pDTO.setEmail(EncryptUtil.encAES128CBC(email)); // 이메일 AES-128-CBC 암호화
            pDTO.setAddr1(addr1);
            pDTO.setAddr2(addr2);

            res = userService.insertUser(pDTO);


            if (res == 1) {
                msg = "회원가입이 되었습니다.";
                url = "/loginPage";
            } else {
                msg = "오류로 인해  회원가입이 실패 하였습니다.";
                url = "/register";
            }

        } catch (Exception e) {
            // 저장이 실패되면 유저 보여줄 메시지
            msg = "회원가입에 실패하였습니다.";
            log.info(e.toString());
            e.printStackTrace();
        } finally {
            log.info(this.getClass().getName() + "inserUserInfo End!!");

            // 회원가입 여부 결과 메시지 전달하기
            model.addAttribute("msg", msg);
            model.addAttribute("url", url);

            pDTO = null;
        }
        return "/redirect";
    }

    // 유저 ID 유효성 확인 jsp로 바로 res값을 보내줌
    @RequestMapping(value = "getUserExistForAJAX")
    @ResponseBody
    public int getUserExistForAJAX(HttpServletRequest request) throws Exception {


        log.info(this.getClass().getName() + ".getUserExistForAJAX start");

        int res = 0;

        try {
            // 이메일 AES-128-CBC 암호화
            String user_id = EncryptUtil.encAES128CBC(CmmUtil.nvl(request.getParameter("user_id")));
            log.info("user_id : " + user_id);
            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setUser_id(user_id);

            res = userService.getUserExistForAJAX(pDTO);


        } catch (Exception e) {

            log.info(e.toString());
            e.printStackTrace();
        }

        log.info(this.getClass().getName() + ".getUserExistForAJAX end");

        return res;
    }


    //유저 로그인
    @PostMapping(value = "/login")
    public String login(HttpServletRequest request, ModelMap model, HttpSession session) throws Exception {

        log.info(this.getClass().getName() + ".login start");

        String msg = "";
        String url = "";

        try {

            // 아이디 AES-128-CBC 암호화
            String user_id = EncryptUtil.encAES128CBC(CmmUtil.nvl(request.getParameter("user_id")));
            // 비밀번호 해시 알고리즘 암호화
            String user_pw = EncryptUtil.encHashSHA256(CmmUtil.nvl(request.getParameter("user_pw")));

            log.info("user_email : " + user_id);
            log.info("user_pw : " + user_pw);

            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setUser_id(user_id);
            pDTO.setPassword(user_pw);


            UserInfoDTO rDTO = userService.getUserLoginCheck(pDTO);

            log.info("rDTO" + rDTO);

            if (rDTO == null) {
                msg = "아이디와 비밀번호를 다시 확인해주세요";
                url = "/loginPage";
            } else {
                log.info("세션이 여기서 담기는데 담기는가?");
                //세션에 담기 복호화
                String res_user_id = EncryptUtil.decAES128CBC(rDTO.getUser_id());
                String res_user_name = rDTO.getUser_name();

                msg = "환영합니다. " + res_user_name + "님";
                url = "/index";

                //로그인시 이메일 이름저장
                session.setAttribute("SS_USER_ID", res_user_id);
                session.setAttribute("SS_USER_NAME", res_user_name);


                //관리자 정보 관리 관리자 아이디 이메일로할시 세션에 이메일담고
                //res_user_email.equals("admin@naver.com")
                if (res_user_id.equals("admin123")) {
                    session.setAttribute("SS_USER_TYPE", "ADMIN");
                    log.info("ADMIN LOGIN");
                }
            }

        } catch (Exception e) {
            msg = "서버 오류입니다. 다시 시도해주세요.";
            url = "/loginPage";
            log.info(e.toString());
            e.printStackTrace();
        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        log.info(this.getClass().getName() + ".login end");

        return "/redirect";

    }

    //비밀번호변경페이지 화면출력
    @GetMapping(value = "updatePwPage")
    public String updatePwPage(HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".updatePwPage start");


        log.info(this.getClass().getName() + ".updatePwPage end");

        return "/login/changPw";
    }

    // 유저 패스워드 변경로직
    @PostMapping(value = "updateUserPw")
    public String updateUserPw(HttpServletRequest request, HttpSession session, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".updateUserPw start");

        String msg = "";
        String url = "";

        try {

            // 이메일 AES-128-CBC 암호화
            String user_email = EncryptUtil.encAES128CBC(CmmUtil.nvl((String) session.getAttribute("user_email")));
            String user_name = CmmUtil.nvl((String) session.getAttribute("user_name"));
            // 비밀번호 해시 알고리즘 암호화
            String user_pw = EncryptUtil.encHashSHA256(CmmUtil.nvl(request.getParameter("user_pw")));

            log.info("user_email : " + user_email);
            log.info("user_name : " + user_name);
            log.info("user_pw : " + user_pw);

            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setPassword(user_pw);
            pDTO.setUser_name(user_name);
            pDTO.setEmail(user_email);


            int res = userService.updateUserPw(pDTO);

            if (res == 1) {
                msg = "성공적으로 비밀번호를 변경했습니다. 다시 로그인 해주세요";
                url = "/index";
            } else {
                msg = "비밀번호 저장에 실패했습니다.";
                url = "/updatePwPage";
            }

        } catch (Exception e) {
            // 저장 실패 시
            msg = "서버 오류입니다.";
            url = "/updatePwPage";
            log.info(e.toString());
            e.printStackTrace();
        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        log.info(this.getClass().getName() + ".updateUserPw end");

        return "/redirect";
    }

    // 유저 비밀번호 찾기 --> 새비밀번호 전송 (비밀번호를모를때)
    @PostMapping(value = "findPw")
    public String findPw(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".findPw start!");

        String msg = "";
        String url = "";

        try {

            String newPW = String.valueOf((int) (Math.random() * 1000000));

            // 이메일 AES-128-CBC 암호화
            String user_email = EncryptUtil.encAES128CBC(CmmUtil.nvl(request.getParameter("user_email")));
            String user_name = CmmUtil.nvl(request.getParameter("user_name"));
            // 비밀번호 해시 알고리즘 암호화
            String user_pw = EncryptUtil.encHashSHA256(newPW);

            log.info("user_email : " + user_email);
            log.info("user_name : " + user_name);

            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setPassword(user_pw);
            pDTO.setUser_name(user_name);
            pDTO.setEmail(user_email);

            int res = userService.updateUserPw(pDTO);

            if (res == 1) {

                MailDTO rDTO = new MailDTO();
                rDTO.setToMail(EncryptUtil.decAES128CBC(user_email));
                rDTO.setTitle("######의 새비밀번호 전송!!!");
                rDTO.setContents("새 비밀번호 : " + newPW);

                int mailRes = mailService.doSendMail(rDTO);

                if (mailRes == 1) {
                    msg = "새 비밀번호를 이메일로 발송했습니다. 로그인 후 변경해주세요.";
                } else {
                    msg = "변경된 비밀번호 발송에 실패했습니다. ####@naver.com 으로 문의해주세요.";
                }
                url = "/loginPage";

            } else if (res == 0) {
                msg = "정보를 다시 확인해주세요.";
                url = "/findPwPage";
            }

        } catch (Exception e) {
            msg = "서버 오류입니다.";
            url = "/findPwPage";
            log.info(e.toString());
            e.printStackTrace();
        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        log.info(this.getClass().getName() + ".findPw end!");

        return "/redirect";
    }

    // 유저 ID찾기 ---> 이메일 전송
    @PostMapping(value = "findID")
    public String findID(HttpServletRequest request, ModelMap model) throws Exception {

        log.info(this.getClass().getName() + ".findPw start!");

        String msg = "";
        String url = "";

        try {


            // 이메일 AES-128-CBC 암호화
            String user_email = EncryptUtil.encAES128CBC(CmmUtil.nvl(request.getParameter("user_email")));
            String user_name = CmmUtil.nvl(request.getParameter("user_name"));


            log.info("user_email : " + user_email);
            log.info("user_name : " + user_name);

            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setUser_name(user_name);
            pDTO.setEmail(user_email);

            UserInfoDTO uDTO = userService.findUserId(pDTO);
            //가져온 아이디 복호화
            String user_id = EncryptUtil.decAES128CBC(uDTO.getUser_id());

            if (uDTO != null) {

                MailDTO rDTO = new MailDTO();
                rDTO.setToMail(EncryptUtil.decAES128CBC(user_email)); //이메일전송을위해 복호화
                rDTO.setTitle("#####" + user_name + "의 아이디 발송!!!");
                rDTO.setContents("아이디 전송 : " + user_id);

                int mailRes = mailService.doSendMail(rDTO);

                if (mailRes == 1) {
                    msg = "아이디를 이메일로 발송했습니다." + user_name + "님의 ID는" + user_id + "입니다.";
                    ;
                } else {
                    msg = "아이디발송에 실패했습니다. ####@naver.com 으로 문의해주세요.";
                }
                url = "/loginPage";

            } else {
                msg = "정보를 다시 확인해주세요.";
                url = "/findIdPage";
            }

        } catch (Exception e) {
            msg = "서버 오류입니다.";
            url = "/findIdPage";
            log.info(e.toString());
            e.printStackTrace();
        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        log.info(this.getClass().getName() + ".findPw end!");

        return "/redirect";
    }


    //유저 삭제
    @GetMapping(value = "deleteUser")
    public String deleteUser(HttpSession session, ModelMap model) {

        log.info(this.getClass().getName() + ".deleteUser start!");

        String msg = "";
        String url = "";

        try {

            // 이메일 AES-128-CBC 암호화
            String user_id = EncryptUtil.encAES128CBC(CmmUtil.nvl((String) session.getAttribute("SS_USER_ID")));
            log.info("user_email : " + user_id);


            UserInfoDTO pDTO = new UserInfoDTO();
            pDTO.setUser_id(user_id); //유저 아이디 DTO에 담아서 전달


            int res = userService.deleteUser(pDTO);

            if (res == 1) {
                msg = "성공적으로 계정이 삭제 되었습니다.";
                url = "/index";
                // session 비움
                session.invalidate();
            } else {
                msg = "회원탈퇴에 실패했습니다.";
                url = "/index";
            }

        } catch (Exception e) {
            // 유저 정보 삭제 실패 시
            msg = "서버 오류입니다.";
            url = "/index";
            log.info(e.toString());
            e.printStackTrace();
        }

        model.addAttribute("msg", msg);
        model.addAttribute("url", url);

        log.info(this.getClass().getName() + ".deleteUser end!");

        return "/redirect";
    }


}
