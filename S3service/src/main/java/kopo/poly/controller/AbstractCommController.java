package kopo.poly.controller;

import kopo.poly.util.CmmUtil;
import org.springframework.ui.ModelMap;

import javax.servlet.http.HttpSession;

public abstract class AbstractCommController {


//    protected String loginCheck(HttpSession session, ModelMap model, String url) {
//
//        String user_id = CmmUtil.nvl((String) session.getAttribute("SS_USER_ID"));
//
//        if (user_id.equals("")) {
//            model.addAttribute("msg", "로그인이 필요합니다.");
//            model.addAttribute("url", "/loginPage");
//            return "/redirect";
//
//        } else {
//
//            return url;
//        }
//
//    }
}
