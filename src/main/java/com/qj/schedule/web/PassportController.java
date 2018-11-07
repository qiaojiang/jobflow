package com.qj.schedule.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qj.schedule.bean.UserBean;
import com.qj.schedule.dao.UserDao;
import com.qj.schedule.util.StatusUtil;

@Controller
@RequestMapping("/passport")
public class PassportController extends AdminController{
	
	@Autowired
	private HttpServletRequest request;
	
	@ResponseBody
    @RequestMapping(value="/login",method=RequestMethod.POST)
    public Map<String,Object> login(){
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		int remember = Integer.parseInt(request.getParameter("remember"));
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:bean.xml");
		UserDao userDao = (UserDao) ac.getBean("userDao");
		UserBean userBean = userDao.getUserByAccount(username, password);
		if(userBean != null){
			HttpSession session = request.getSession();
			session.setAttribute("uid", userBean.getUserId());
			session.setAttribute("nickname", userBean.getNickname());
			session.setAttribute("type", userBean.getType());
			session.setAttribute("group_id", userBean.getGroupId());
		    return output(StatusUtil.CODE_SUCCESS, StatusUtil.MSG_LOGIN_SUCCESS, "/view/index");
		}else{
			return output(StatusUtil.CODE_FAILED, StatusUtil.MSG_LOGIN_FAILED, "/view/login");
		}
    }
    
    @RequestMapping(value="/logout")
    public String logout(){
		HttpSession session = request.getSession();
    	session = null;
        return "redirect:/view/login";
    }
}