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
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Autowired
    ReplyService replyService;

    @CrossOrigin
    @GetMapping("/categories")
    public List<Category> getCategories(){
        return categoriesService.findAll();
    }
    
    @CrossOrigin
    @PostMapping("/categories")
    public Category postCategory(@RequestBody CategoryForm categoryForm){
        Category category = new Category();
        category.setDescription(categoryForm.description());
        category.setTitle(categoryForm.title());
        category.setSlug(categoryForm.title());
        categoriesService.save(category);
        return category;
    }

    @CrossOrigin
    @GetMapping("/categories/{categoryName}")
    public List<Topic> getCategory(@PathVariable String categoryName){
        return topicService.findTopicsByCategory(categoryName);
    }
    @CrossOrigin
    @GetMapping("/categories/{categoryName}/topics")
    public List<Topic> getCategoryTopics(@PathVariable String categoryName){
        return topicService.findTopicsByCategory(categoryName);
    }

    @CrossOrigin
    @GetMapping("/categories/{categoryName}/{topicName}")
    public void getCategoryTopic(@PathVariable String categoryName, @PathVariable String topicName){

    }

    @CrossOrigin
    @GetMapping("/topics/{topicId}")
    public Map<String, Object> getTopic(@PathVariable int topicId, HttpServletRequest req){
        String authorizationHeader = req.getHeader("Authorization");
        User user = userService.getUserByAuth(authorizationHeader);
        Map<String, Object> resposta = new HashMap<>();
        Topic topic = topicService.findByTopicId(topicId);
        List<Reply> replies = replyService.findByTopicId(topicId);
        topic.setViews(topic.getViews()+1);
        topicService.save(topic); // Es guarda amb una view mes.
        resposta.put("views", topic.getViews());
        resposta.put("_id", topic.getId());
        resposta.put("id", topic.getId());
        resposta.put("title", topic.getTitle());
        resposta.put("content", topic.getContent());
        resposta.put("category", topic.getCategory());
        resposta.put("user", user);
        resposta.put("replies", replies);
        resposta.put("numberOfReplies", replies.size());
        resposta.put("createdAt", topic.getCreatedAt());
        resposta.put("updatedAt", topic.getUpdatedAt());

        return resposta;
    }

    @CrossOrigin
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
//    @CrossOrigin
//    @DeleteMapping("/topics/{topicId}/replies")

    @CrossOrigin
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
