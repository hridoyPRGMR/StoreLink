package com.storelink.controllers.web;

import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.storelink.dto.PermissionDto;
import com.storelink.model.Permission;
import com.storelink.services.PermissionService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/cms/permission")
public class PermissionController extends BaseController {

	private final PermissionService perServ;

	public PermissionController(PermissionService perServ) {
		super();
		this.perServ = perServ;
	}

	@PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE_PERMISSION')")
	@GetMapping("/create")
	public String showCreate(Model model, HttpServletRequest request) {
		Permission permission = new Permission();
		model.addAttribute("permissionForm", permission);
		return getPageContent(model, "permission/create");
	}

	@PreAuthorize("hasRole('ADMIN') and hasAuthority('CREATE_PERMISSION')")
	@PostMapping("/create")
	public String savePermission(@Valid @ModelAttribute("permissionForm") PermissionDto req,
			BindingResult bindingResult, Model model) {

		if (bindingResult.hasErrors()) {
			return getPageContent(model, "permission/create");
		}

		if (perServ.findByPermission(req.getPermission()) != null) {
			model.addAttribute("error", "Permission already exist.");
			return getPageContent(model, "permission/create");
		}

		try {
			Permission permission = perServ.convertToEntity(req);
			perServ.save(permission);
			model.addAttribute("successMessage", "Permission Saved Successfully");
		} catch (RuntimeException e) {
			log.error("Failed to create permission. Error: {}",e.getMessage(),e);
			model.addAttribute("error", "Failed to save permission.");
		}

		return getPageContent(model, "permission/create");
	}

	@PreAuthorize("hasRole('ADMIN') and hasAuthority('PERMISSIONS')")
	@GetMapping("/permissions")
	public String getPermissions(Model model) {

		model.addAttribute("permissions", perServ.getAllPermission());

		return getPageContent(model, "permission/permission-list");
	}

	@PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE_PERMISSION')")
	@GetMapping("/edit/{id}")
	public String updatePermissionForm(@PathVariable Long id, Model model) {
	    
		Optional<Permission> permission = perServ.findById(id);
	    
		if (!permission.isPresent()) {
	        model.addAttribute("error", "Permission not found");
	        return getPageContent(model, "permission/permission-list");
	    }

	    PermissionDto dto = perServ.convertToDtO(permission.get());
	    model.addAttribute("id", id);
	    model.addAttribute("permission", dto); 
	    
	    return getPageContent(model, "permission/update");
	}

	@PreAuthorize("hasRole('ADMIN') and hasAuthority('UPDATE_PERMISSION')")
	@PostMapping("/edit/{id}")
	public String updatePermission(@PathVariable Long id, @Valid @ModelAttribute("permission") PermissionDto req, BindingResult res,
	        RedirectAttributes redirectAttributes,Model model) {

	    if (res.hasErrors()) {
	    	return getPageContent(model,"/permission/update");
	    }

	    try {
	        Permission permission = perServ.convertToEntity(req);
	        permission.setId(id);
	        perServ.save(permission);

	        redirectAttributes.addFlashAttribute("successMessage", "Permission updated successfully");
	    } catch (RuntimeException e) {
	    	log.error("Failed to update permission. Error: {}",e.getMessage(),e);
	        redirectAttributes.addFlashAttribute("error", "Failed to update permission.");
	    }

	    return "redirect:/cms/permission/permissions";
	}
	
	@PreAuthorize("hasRole('ADMIN') and hasAuthority('DELETE_PERMISSION')")
	@GetMapping("/delete/{id}")
	public String deletePermission(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		perServ.deleteById(id);
		redirectAttributes.addFlashAttribute("successMessage","Permission deleted successfully");
		return "redirect:/cms/permission/permissions";
	}

}
