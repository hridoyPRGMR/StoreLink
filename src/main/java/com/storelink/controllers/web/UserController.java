package com.storelink.controllers.web;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.storelink.apiresponse.ApiResponse;
import com.storelink.dto.UserDto;
import com.storelink.model.Permission;
import com.storelink.model.User;
import com.storelink.services.PermissionService;
import com.storelink.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/cms")
public class UserController extends BaseController {

	private UserService userServ;
	private PermissionService permissionService;

	public UserController(UserService userServ,PermissionService permissionService) {
		this.userServ=userServ;
		this.permissionService=permissionService;
	}

	@GetMapping("/login")
	public String showLoginPage(HttpServletRequest request,Model model) {
		
		String error = (String) request.getSession().getAttribute("error");
        if (error != null) {
            model.addAttribute("error", error);
            request.getSession().removeAttribute("error"); 
        }
		
		return "login";
	}
	
	@PostMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		
		 return "redirect:/cms/login?logout";
	}
	
	@PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE_USER')")
	@GetMapping("/user/create")
	public String createUserPage(Model model) {
		
		UserDto user=new UserDto();
		model.addAttribute("user",user);
		
		return getPageContent(model,"user/create");
	}
	
	@PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE_USER')")
	@PostMapping("/user/create")
	public String createUser(@Valid @ModelAttribute("user") UserDto req,
			BindingResult res,
			Model model
			) {
		
		if(res.hasErrors()){
			return getPageContent(model, "user/create");
		}
		
		try{
			userServ.saveUser(req, "ROLE_ADMIN");
			model.addAttribute("successMessage", "User created Successfully.");
		}
		catch(RuntimeException e){
			log.error("Failed to create user. Error: {}",e.getMessage(),e);
			model.addAttribute("error", "Failed to create User.");
			System.out.println(e.getMessage());
		}
		
		return getPageContent(model,"user/create");
	}

	@PreAuthorize("hasRole('ADMIN') and hasAuthority('USERS')")
	@GetMapping("/user/users")
	public String getUsers(Model model,
			@RequestParam(value = "search",required = false,defaultValue = "") String searchTerm,
			@RequestParam(value = "role",required = false,defaultValue = "ROLE_ADMIN") String role,
			@RequestParam(value = "page",defaultValue = "0") int page,
			@RequestParam(value = "size",defaultValue = "10") int size,
			HttpServletRequest request
		){

			Page<User> userPage = userServ.getUsers(searchTerm,role,page,size);
			List<Permission> permissions = permissionService.getAllPermission();

			System.out.println(userPage.getNumberOfElements());

			model.addAttribute("users", userPage.getContent());
			model.addAttribute("permissions",permissions);

			model.addAttribute("currentPage", page);
			model.addAttribute("totalPages", userPage.getTotalPages());
			model.addAttribute("role", role);
			model.addAttribute("search", searchTerm);
			model.addAttribute("currentPath", request.getRequestURI());

		return getPageContent(model, "/user/user-list");
	}	
	
	//ajax request to get user pre assign permissions
	@GetMapping("/user/user-permissions/{userId}")
	public ResponseEntity<?> getUserPermissions(@PathVariable("userId") Long userId){
		
		Set<Permission> permissions = userServ.getUserPermissions(userId);
		
		return ResponseEntity.ok(new ApiResponse(true,"User Permissions fetched successfully.", permissions));
	}

	@PreAuthorize("hasRole('ADMIN') and hasAuthority('ASSIGN_PERMISSION')")
	@PostMapping("/user/assign-permission")
	public String assignPermission(@RequestParam("userId") String userId,
		@RequestParam("permissionsIds[]") List<Long> permissionIds,
		RedirectAttributes redirect
	){

		try{
			userServ.assignPermission(Long.parseLong(userId), permissionIds);
			redirect.addFlashAttribute("successMessage","Permission assigned Successfully.");
		}catch(Exception e){
			log.error("Failed to assign permission. Error: {}",e.getMessage(),e);
			redirect.addFlashAttribute("error",e.getMessage());
		}

		return "redirect:/cms/user/users";
	}

}
