package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.CategoryForm;
import com.esliceu.forum.forms.ReplyForm;
import com.esliceu.forum.forms.TopicForm;
import com.esliceu.forum.models.Category;
import com.esliceu.forum.models.Reply;
import com.esliceu.forum.models.Topic;
import com.esliceu.forum.models.User;
import com.esliceu.forum.repos.CategoriesRepo;
import com.esliceu.forum.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
public class CategoryController {
    @Autowired
    CategoriesService categoriesService;

    @Autowired
    TopicService topicService;

    @Autowired
    PermissionService permissionService;

    @Autowired
    UserService userService;

    @Autowired
    ReplyService replyService;

    
    @GetMapping("/categories")
    public List<Category> getCategories(){
        return categoriesService.findAll();
    }
    
    
    @PostMapping("/categories")
    public Category postCategory(@RequestBody CategoryForm categoryForm, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        if (user == null) return null;

        Category category = new Category();
        category.setDescription(categoryForm.description());
        category.setTitle(categoryForm.title());
        category.setSlug(categoryForm.title());
        categoriesService.save(category);
        return category;
    }
    
    @GetMapping("/categories/{categoryName}")
    public Category getCategory(@PathVariable String categoryName){
        return categoriesService.findByCategoryName(categoryName);
    }
    
    @PutMapping("/categories/{categoryName}")
    public Category putCategory(@PathVariable String categoryName, @RequestBody CategoryForm categoryForm, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Category category = categoriesService.findByCategoryName(categoryName);
        category.setTitle(categoryForm.title());
        category.setDescription(categoryForm.description());
        category.setModerators(new int[0]);
        if (user != null){
            categoriesService.save(category);
            return category;
        }
        return null;
    }
    
    @DeleteMapping("/categories/{categoryName}")
    public boolean deleteCategory(@PathVariable String categoryName, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        if (user != null){
            return categoriesService.deleteByTitle(categoryName);
        }
        return false;
    }

    
    @GetMapping("/categories/{categoryName}/topics")
    public List<Topic> getCategoryTopics(@PathVariable String categoryName){
        return topicService.findTopicsByCategory(categoryName);
    }

    
    @GetMapping("/categories/{categoryName}/{topicName}")
    public void getCategoryTopic(@PathVariable String categoryName, @PathVariable String topicName){

    }

    
    @GetMapping("/topics/{topicId}")
    public Map<String, Object> getTopic(@PathVariable int topicId, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Map<String, Object> resposta = new HashMap<>();
        Topic topic = topicService.findByTopicId(topicId);
        List<Reply> replies = replyService.findByTopicId(topicId);
        for (Reply reply : replies) {
            reply.set_id(reply.getId());
        }
        topic.setViews(topic.getViews()+1);
        topicService.save(topic); // Es guarda amb una view mes.
        resposta.put("views", topic.getViews());
        resposta.put("_id", topic.getId());
        resposta.put("id", topic.getId());
        resposta.put("title", topic.getTitle());
        resposta.put("content", topic.getContent());
        resposta.put("category", topic.getCategory());
        topic.getUser().setPermissions(permissionService.findByUserId(topic.getUser().getId()).getRoot());
        resposta.put("user", topic.getUser());
        resposta.put("replies", replies);
        resposta.put("numberOfReplies", replies.size());
        resposta.put("createdAt", topic.getCreatedAt());
        resposta.put("updatedAt", topic.getUpdatedAt());

        return resposta;
    }

    
    @PostMapping("/topics/{topicId}/replies")
    public Reply postReply(@PathVariable int topicId, @RequestBody ReplyForm replyForm, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Topic topic = topicService.findByTopicId(topicId);
        Reply reply = new Reply();
        reply.setContent(replyForm.content());
        reply.setTopic(topic);
        reply.setUser(user);
        reply.setCreatedAt(LocalDateTime.now());
        reply.setUpdatedAt(LocalDateTime.now());
        topic.setNumberOfReplies(topic.getNumberOfReplies()+1);
        topicService.save(topic);
        replyService.save(reply);
        reply = replyService.findByLatest();
        reply.setId(reply.get_id());
        return reply;
    }

    
    @DeleteMapping("/topics/{topicId}/replies/{replyId}")
    public boolean deleteTopic(@PathVariable int topicId, @PathVariable int replyId, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        if (user != null){
            return replyService.deleteById(replyId);
        }
        return false;
    }
    
    @PutMapping("/topics/{topicId}/replies/{replyId}")
    public Reply putReply(@PathVariable int topicId, @PathVariable int replyId, HttpServletRequest req, @RequestBody ReplyForm replyForm){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Reply reply = replyService.findById(replyId);
        reply.setContent(replyForm.content());
        if (user == reply.getUser()){
            replyService.save(reply);
            return reply;
        }
        return null;
    }

    
    @PutMapping("/topics/{topicId}")
    public Topic putTopic(@PathVariable int topicId, HttpServletRequest req, @RequestBody TopicForm topicForm){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Topic topic = topicService.findByTopicId(topicId);
        topic.setContent(topicForm.content());
        topic.setTitle(topicForm.title());
        topic.setCategory(categoriesService.findByCategoryName(topicForm.category()));
        if (user != null){
            topicService.save(topic);
            return topic;
        }
        return null;
    }

    
    @DeleteMapping("/topics/{topicId}")
    public boolean deleteTopic(@PathVariable int topicId, HttpServletRequest req) {
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        if (user != null){
            return topicService.deleteById(topicId);
        }
        return false;
    }
        
    @PostMapping("/topics")
    public Topic postTopic(@RequestBody TopicForm topicForm, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Category category = categoriesService.findByCategoryName(topicForm.category());

        Topic topic = new Topic();
        topic.setCategory(category);
        topic.setContent(topic.getContent());
        topic.setTitle(topicForm.title());
        topic.setCreatedAt(LocalDateTime.now());
        topic.setUser(user);
        topic.setNumberOfReplies(0);

        topicService.save(topic);
        topic = topicService.findByLatest();
        topic.set_id(topic.getId());
        return topic;
    }

}
