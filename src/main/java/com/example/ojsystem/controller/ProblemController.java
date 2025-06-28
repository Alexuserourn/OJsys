package com.example.ojsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class ProblemController {
    
    @GetMapping("/problem/{id}")
    public String getProblem(@PathVariable Long id, Model model) {
        // 获取题目详情的逻辑
        return "problem_detail";
    }
    
    @PostMapping("/submit")
    public String submitSolution(@RequestParam Long problemId,
                               @RequestParam String code,
                               Model model) {
        // 提交代码的逻辑
        return "problem_detail";
    }
} 