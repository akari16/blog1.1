package com.lewyon.controller;

import com.lewyon.dto.FirstPageBlog;
import com.lewyon.pojo.Type;
import com.lewyon.service.BlogService;
import com.lewyon.service.TypeService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TypeShowController {

	@Autowired
	private TypeService typeService;

	@Autowired
	private BlogService blogService;

	@GetMapping("/types/{id}")
	public String types(@RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum, @PathVariable Long id,
			Model model) {
		List<Type> types = typeService.getAllType();
		// -1表示从首页导航点进来的
		if (id == -1) {
			id = types.get(0).getId();
		}
		model.addAttribute("types", types);
		List<FirstPageBlog> blogs = blogService.getByTypeId(id);
		PageMethod.startPage(pageNum, 100);
		PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(blogs);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("activeTypeId", id);
		return "types";
	}

}
