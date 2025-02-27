package com.esliceu.forum.controllers;

import com.esliceu.forum.forms.CategoryForm;
import com.esliceu.forum.forms.ReplyForm;
import com.esliceu.forum.forms.TopicForm;
import com.esliceu.forum.models.Category;
import com.esliceu.forum.models.Reply;
import com.esliceu.forum.models.Topic;
import com.esliceu.forum.models.User;
import com.esliceu.forum.services.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
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
        if (categoriesService.findByCategoryName(categoryForm.title()) != null) {
            return categoriesService.findByCategoryName(categoryForm.title());
        }
        if (user == null || !userService.hasPermissionToCategory(user, categoryForm.title())) return null;

        Category category = new Category();
        category.setDescription(categoryForm.description());
        category.setTitle(categoryForm.title());
        category.setSlug(categoryForm.title());
        categoriesService.save(category);
        return category;
    }

    @PutMapping("/categories/{categoryName}")
    public Category putCategory(@PathVariable String categoryName, @RequestBody CategoryForm categoryForm, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Category category = categoriesService.findByCategoryName(categoryName);
        if (user == null || !userService.hasPermissionToCategory(user, categoryName)) {
            return null;
        }

        category.setTitle(categoryForm.title());
        category.setDescription(categoryForm.description());
        category.setSlug(categoryForm.title());
        categoriesService.save(category);
        return category;
    }

    @DeleteMapping("/categories/{categoryName}")
    public boolean deleteCategory(@PathVariable String categoryName, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        if (user == null || !userService.hasPermissionToCategory(user, categoryName)) {
            return false;
        }
        return categoriesService.deleteByTitle(categoryName);
    }

    @GetMapping("/categories/{categoryName}")
    public Category getCategory(@PathVariable String categoryName) {
        Category category = categoriesService.findByCategoryName(categoryName);
        category.set_id(category.get_id());
        if (category.getModerators() == null){
            category.setModerators(new int[0]);
        }
        return category;
    }


    @GetMapping("/categories/{categoryName}/topics")
    public List<Topic> getCategoryTopics(@PathVariable String categoryName){
        return topicService.findTopicsByCategory(categoryName);
    }

    @GetMapping("/topics/{topicId}")
    public Map<String, Object> getTopic(@PathVariable int topicId){
        Map<String, Object> resposta = new HashMap<>();
        Topic topic = topicService.findByTopicId(topicId);

        topic.setViews(topic.getViews() + 1);
        topicService.save(topic);

        List<Reply> replies = replyService.findByTopicId(topicId);
        for (Reply reply : replies) {
            reply.set_id(reply.getId());
            reply.getUser().set_id(reply.getUser().getId());
        }

        resposta.put("views", topic.getViews());
        resposta.put("_id", topic.getId());
        resposta.put("id", topic.getId());
        resposta.put("title", topic.getTitle());
        resposta.put("content", topic.getContent());
        Category category = topic.getCategory();
        category.set_id(category.getId());
        resposta.put("category", category);
        resposta.put("replies", replies);
        User user = topic.getUser();
        user.set_id(user.getId());
        resposta.put("user", user);
        resposta.put("createdAt", topic.getCreatedAt());
        resposta.put("updatedAt", topic.getUpdatedAt());

        return resposta;
    }

    @PutMapping("/topics/{topicId}")
    public Topic putTopic(@PathVariable int topicId, HttpServletRequest req, @RequestBody TopicForm topicForm){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Topic topic = topicService.findByTopicId(topicId);
        user.set_id(user.getId());

        if (user == null || !userService.hasPermissionToCategory(user, topic.getCategory().getTitle())) {
            return null;
        }

        topic.setContent(topicForm.content());
        topic.setTitle(topicForm.title());
        topic.setCategory(categoriesService.findByCategoryName(topicForm.category()));
        topic.getCategory().set_id(topic.getCategory().getId());
        topicService.save(topic);
        return topic;
    }

    @DeleteMapping("/topics/{topicId}")
    public boolean deleteTopic(@PathVariable int topicId, HttpServletRequest req) {
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);

        if (user == null) {
            return false;
        }

        String topicCategory = topicService.findCategoryByTopicId(topicId);

        if (!userService.hasPermissionToCategory(user, topicCategory)) {
            return false;
        }

        return topicService.deleteById(topicId);
    }

    @PostMapping("/topics")
    public Topic postTopic(@RequestBody TopicForm topicForm, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Category category = categoriesService.findByCategoryName(topicForm.category());
        category.set_id(category.getId());
        if (user == null || !userService.hasPermissionToCategory(user, topicForm.category())) {
            return null;
        }
        user.set_id(user.getId());
        Topic topic = new Topic();
        topic.setCategory(category);
        topic.setContent(topicForm.content());
        topic.setTitle(topicForm.title());
        topic.setCreatedAt(LocalDateTime.now());
        topic.setUser(user);
        topic.setNumberOfReplies(0);

        topicService.save(topic);
        topic = topicService.findByLatest();
        topic.set_id(topic.getId());
        return topic;
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

    @PostMapping("/topics/{topicId}/replies")
    public Reply postReply(@PathVariable int topicId, @RequestBody ReplyForm replyForm, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        user.set_id(user.getId());
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
    public boolean deleteReply(@PathVariable int topicId, @PathVariable int replyId, HttpServletRequest req) {
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Reply reply = replyService.findById(replyId);

        if (reply == null || user == null) {
            return false;
        }

        if (reply.getUser().getId() == user.getId()) {
            return replyService.deleteById(replyId);
        }

        Topic topic = topicService.findByTopicId(topicId);
        if (userService.hasPermissionToCategory(user, topic.getCategory().getTitle())) {
            return replyService.deleteById(replyId);
        }

        return false;
    }



}
