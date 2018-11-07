package com.qj.schedule.web;

import java.util.Map;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.qj.schedule.util.StatusUtil;

@Controller
@RequestMapping("/index")
public class IndexController extends AdminController{
	
    @RequestMapping(value= {"","/","/*"},method=RequestMethod.GET)
    public ModelAndView index(){
    	return new ModelAndView("redirect:/view/index"); 
    }

}