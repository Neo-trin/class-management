package com.myclass.controller.student;

import com.myclass.entity.backstage.StudentInfo;
import com.myclass.service.backstage.StudentInfoService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * 学生主控制器
 * @author joe
 * @Date 2019/8/5
 */
@RestController
public class StudentMainController {

    private Logger logger = Logger.getLogger(StudentMainController.class);

    /**
     * 服务对象
     */
    @Resource
    StudentInfoService studentInfoService;

    @GetMapping("login/student.html")
    public ModelAndView login(ModelAndView modelAndView, String backUri, HttpServletRequest request) {
        modelAndView.setViewName("student/login");
        request.getSession().setAttribute("backUri", backUri);
        return modelAndView;
    }

    @GetMapping("index/student.html")
    public ModelAndView index(ModelAndView modelAndView) {
        modelAndView.setViewName("student/index");
        return modelAndView;
    }

    @PostMapping("login/student.do")
    public ModelAndView login(String loginName, String pwd, HttpServletRequest request, ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/index/student.html");
        StudentInfo studentInfo = null;
        try {
            studentInfo = studentInfoService.login(loginName, pwd);
            if (studentInfo == null) {
                modelAndView.setViewName("student/login");
                modelAndView.addObject("error", "用户名或密码错误!");
                return modelAndView;
            }
            String backUri = (String) request.getSession().getAttribute("backUri");
            if (backUri != null) {
                modelAndView.setViewName("redirect:" + backUri);
            }
        } catch (Exception e) {
            logger.error("login.do error:", e);
            modelAndView.setViewName("student/login");
            modelAndView.addObject("error", e.getMessage());
        }
        request.getSession().setAttribute("student", studentInfo);
        return modelAndView;
    }

    @GetMapping("logout")
    public ModelAndView logout(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("redirect:login/student.html");
        StudentInfo studentInfo = (StudentInfo) request.getSession().getAttribute("student");
        if (studentInfo == null) {
            modelAndView.setViewName("redirect:login/teacher.html");
        }
        request.getSession().invalidate();
        return modelAndView;
    }
}
