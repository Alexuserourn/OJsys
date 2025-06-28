package service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import problem.Problem;
import problem.ProblemDAO;
import service.ProblemService;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 问题服务实现类
 */
@Service
public class ProblemServiceImpl implements ProblemService {
    private final ProblemDAO problemDAO;
    
    @Autowired
    public ProblemServiceImpl(ProblemDAO problemDAO) {
        this.problemDAO = problemDAO;
    }
    
    @Override
    public List<Problem> selectAll() {
        try {
            return problemDAO.selectAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    @Override
    public Problem selectOne(int id) {
        try {
            return problemDAO.selectOne(id);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    @Override
    public boolean insert(Problem problem) {
        try {
            if (problem == null) {
                System.err.println("插入题目失败: 题目对象为空");
                return false;
            }
            
            if (problem.getTitle() == null || problem.getTitle().trim().isEmpty()) {
                System.err.println("插入题目失败: 题目标题为空");
                return false;
            }
            
            problemDAO.insert(problem);
            return true;
        } catch (SQLException e) {
            System.err.println("插入题目失败: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            System.err.println("插入题目时发生未知错误: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean delete(int id) {
        try {
            problemDAO.delete(id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean update(Problem problem) {
        try {
            problemDAO.update(problem);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
} 