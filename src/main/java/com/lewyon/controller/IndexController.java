package com.lewyon.controller;

import com.lewyon.dto.DetailedBlog;
import com.lewyon.dto.FirstPageBlog;
import com.lewyon.dto.RecommendBlog;
import com.lewyon.dto.ShowBlog;
import com.lewyon.exception.NotFountException;
import com.lewyon.pojo.Comment;
import com.lewyon.pojo.Tag;
import com.lewyon.pojo.Type;
import com.lewyon.service.BlogService;
import com.lewyon.service.CommentService;
import com.lewyon.service.TagService;
import com.lewyon.service.TypeService;
import com.lewyon.util.MarkdownUtils;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class IndexController {

	@Autowired
	private BlogService blogService;

	@Autowired
	private TagService tagService;

	@Autowired
	private TypeService typeService;

	@Autowired
	private CommentService commentService;

	@GetMapping("/")
	public String index(Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum) {
		PageMethod.startPage(pageNum, 10);
		List<FirstPageBlog> allFirstPageBlog = blogService.getAllFirstPageBlog();
		/*
		 * System.out.println("num:"+allFirstPageBlog.size()); for (FirstPageBlog
		 * firstPageBlog : allFirstPageBlog) { System.out.println(firstPageBlog); }
		 */
		List<Type> allType = typeService.getAllType();
		/*
		 * System.out.println("num:" + allType.size()); for (Type type : allType) {
		 * System.out.println(type); }
		 */
		List<Tag> allTag = tagService.getAllTag();
		List<RecommendBlog> recommendedBlog = blogService.getRecommendedBlog();
		PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(allFirstPageBlog);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("tags", allTag);
		model.addAttribute("types", allType);
		model.addAttribute("recommendedBlogs", recommendedBlog);
		return "index";
	}

	@GetMapping("/search")
	public String search(Model model, @RequestParam(defaultValue = "1", value = "pageNum") Integer pageNum,
			@RequestParam String query) {
		PageMethod.startPage(pageNum, 100);
		List<FirstPageBlog> searchBlog = blogService.getSearchBlog(query);
		/*
		 * for (FirstPageBlog firstPageBlog : searchBlog) {
		 * System.out.println(firstPageBlog); }
		 */
		PageInfo<FirstPageBlog> pageInfo = new PageInfo<>(searchBlog);
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("query", query);
		return "search";
	}

	@GetMapping("/blog/{id}")
	public String blog(@PathVariable Long id, Model model) {
		ShowBlog showBlog = blogService.getBlogById(id);
		System.out.println(showBlog);
		List<Comment> comments = commentService.listCommentByBlogId(id);
		model.addAttribute("comments", comments);
		if (showBlog == null) {
			throw new NotFountException("该博客不存在");
		}
		String content = showBlog.getContent();
		showBlog.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
		model.addAttribute("blog", showBlog);
		return "blog";
	}

	@ResponseBody
	@PostMapping("/upload") // 等价于 @RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String uplaod(HttpServletRequest req, @RequestParam("editormd-image-file") MultipartFile file, Model m,
			MultipartFile attach) {// 1.
		JSONObject jsonObject = new JSONObject(); // 接受上传的文件

		// file
		try {
			// 2.根据时间戳创建新的文件名，这样即便是第二次上传相同名称的文件，也不会把第一次的文件覆盖了
			String fileName = file.getOriginalFilename();
			// 3.通过req.getServletContext().getRealPath("") 获取当前项目的真实路径，然后拼接前面的文件名
			String destFileName = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\upload\\"
					+ fileName;
			// 4.第一次运行的时候，这个文件所在的目录往往是不存在的，这里需要创建一下目录（创建到了webapp下uploaded文件夹下）
			File destFile = new File(destFileName);
			destFile.getParentFile().mkdirs();
			// 5.把浏览器上传的文件复制到希望的位置
			file.transferTo(destFile);
			// 6.把文件名放在model里，以便后续显示用
			m.addAttribute("fileName", destFileName);
			jsonObject.put("success", 1);
			jsonObject.put("message", "上传成功");
			jsonObject.put("url", "/upload/" + fileName);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println(e);
			jsonObject.put("fail", 0);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println(e);
			jsonObject.put("fail", 0);
		}
		System.out.println(jsonObject);
		System.out.println(jsonObject.toString());
		return jsonObject.toString();
	}

	@GetMapping("/link")
	public String link(Model model) {
		List<Tag> allTag = tagService.getAllTag();
		List<RecommendBlog> recommendedBlog = blogService.getRecommendedBlog();
		List<Type> allType = typeService.getAllType();
		model.addAttribute("tags", allTag);
		model.addAttribute("types", allType);
		model.addAttribute("recommendedBlogs", recommendedBlog);
		return "link";
	}

	@GetMapping("/production")
	public String production(Model model) {
		List<Tag> allTag = tagService.getAllTag();
		List<RecommendBlog> recommendedBlog = blogService.getRecommendedBlog();
		List<Type> allType = typeService.getAllType();
		model.addAttribute("tags", allTag);
		model.addAttribute("types", allType);
		model.addAttribute("recommendedBlogs", recommendedBlog);
		return "production";
	}

}
