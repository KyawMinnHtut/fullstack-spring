package com.jdc.leaves.controller;

import java.time.LocalDate;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jdc.leaves.model.dto.input.LeaveForm;
import com.jdc.leaves.model.service.ClassService;
import com.jdc.leaves.model.service.LeaveService;

@Controller
@RequestMapping("leaves")
public class LeaveController {
	
	@Autowired
	private LeaveService leaveService;
	@Autowired
	private ClassService classService;

	@GetMapping
	public String index(
			@RequestParam Optional<String> from, 
			@RequestParam Optional<String> to,
			ModelMap model) {
		var result = leaveService.search(Optional.empty(), from, to);
		model.put("list", result);
		return "leaves";
	}

	@GetMapping("edit")
	public String edit(@RequestParam int classId, ModelMap model) {
		model.put("classInfo", classService.findInfoById(classId));
		return "leaves-edit";
	}

	@PostMapping
	public String save(@Valid @ModelAttribute LeaveForm form, BindingResult result, ModelMap model) {
		
		if(result.hasErrors()) {
			model.put("classInfo", classService.findInfoById(form.getClassId()));
			return "leaves-edit";
		}
		
		// Save Form
		leaveService.save(form);
		
		// Redirect to Details View
		return "redirect:/leaves";
	}
	
	@ModelAttribute("form")
	LeaveForm form(@RequestParam(required = false) Integer classId, @RequestParam(required = false) Integer studentId) {
		if(null != classId && null != studentId) {
			var form = new LeaveForm(classId, studentId);
			form.setApplyDate(LocalDate.now());
			return form;
		}
		
		return new LeaveForm();
	}

}