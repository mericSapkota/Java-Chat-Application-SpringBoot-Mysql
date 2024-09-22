package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.user.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
	
	@Autowired
	private UserService us;
	
	@Autowired
	private UserRepository uRepo;
	
	@GetMapping("/admin")
	  public String admin(HttpServletRequest req, Model m) {
		  HttpSession session = req.getSession();
		  System.out.println("Admin controller"+session.getAttribute("email"));
		  Optional<User> us = uRepo.findByEmail((String)session.getAttribute("email"));
			
			User user = us.get();
			String pp = user.getImg();
			  if(pp!=null) {
				  m.addAttribute("pp","data:image/jpeg;base64,"+pp); 	
			  }
			  else {
				  m.addAttribute("pp","/img/user_icon.png"); 
			  }
			session.setAttribute("id", user.getId());
			session.setAttribute("fullname", user.getFullName());
			session.setAttribute("username", user.getUserName());
			
			List<User> userList = uRepo.findAll();
			m.addAttribute("userList",userList);
			return findPaginated(1, "userName", "asc", m);
	  }

	@GetMapping("/page/{pageNo}")
		public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
				@RequestParam("sortField") String sortField,
				@RequestParam("sortDir") String sortDir,
				Model model) {
			int pageSize = 2;
			
			Page<User> page = us.findPaginated(pageNo, pageSize, sortField, sortDir);
			List<User> listEmployees = page.getContent();
			
			model.addAttribute("currentPage", pageNo);
			model.addAttribute("totalPages", page.getTotalPages());
			model.addAttribute("totalItems", page.getTotalElements());
			
			model.addAttribute("sortField", sortField);
			model.addAttribute("sortDir", sortDir);
			model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
			
			model.addAttribute("listEmployees", listEmployees);
			return "admin";
	}
	
	@PostMapping("/deleteUser")
		public String delete(@ModelAttribute User u, RedirectAttributes r ) {
		
		try {
			System.out.println("id :"+u.getId());
		uRepo.deleteById(u.getId());
		r.addAttribute("success","Successfully deleted");
		
		return "redirect:/admin";
		}
		catch(Exception e) {
			e.printStackTrace();
			r.addAttribute("failed","Failed Deleting");
			return "redirect:/admin";
		}
	}
	
}
