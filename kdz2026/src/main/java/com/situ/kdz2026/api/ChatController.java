package com.situ.kdz2026.api;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.situ.kdz2026.model.Course;
import com.situ.kdz2026.service.CourseService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
//@RequestMapping("api/v1/ai")
//public class ChatController {
//    private final ChatClient chatClient;
//
//    public ChatController(ChatClient chatClient) {
//        this.chatClient = chatClient;
//    }
//
//    @GetMapping("/chat")
//    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
//        return chatClient.prompt()
//                .user(message)
//                .call()
//                .content();
//    }
//}
@RestController
@RequestMapping("api/v1/ai")
public class ChatController {

    private final ChatClient chatClient;
    private final CourseService courseService; // 新增

    // 修改构造函数，注入 CourseService
    public ChatController(ChatClient chatClient, CourseService courseService) {
        this.chatClient = chatClient;
        this.courseService = courseService;
    }

    @GetMapping("/chat")
    public String generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        // 判断是否为课程推荐相关问题
        boolean isCourseRecommendationQuery = isCourseRelatedQuery(message);

        if (isCourseRecommendationQuery) {
            // 查询课程数据
            Page<Course> coursePage = courseService.findAll(new Page<>(1, 10), null); // 取前5门课程，或者可加搜索条件
            List<Course> courses = coursePage.getRecords();

            if (courses == null || courses.isEmpty()) {
                return "这个问题太难了，请联系人工客服。";
            }

            // 构造课程推荐信息字符串
            StringBuilder courseListStr = new StringBuilder("我们为您推荐以下课程：\n");
            for (Course course : courses) {
                // 假设 Course 有 name 字段
                courseListStr.append("- ").append(course.getName() != null ? course.getName() : "未命名课程").append("\n");
            }

            // 可选：将课程信息传给 AI，让它组织更自然的话术
            // 这里简单拼接后直接由系统返回，你也可以将 courseListStr 传给 AI 让它润色
            return chatClient.prompt()
                    .user("关于课程推荐，我们有以下课程信息：" + courseListStr.toString() + " 请你用友好、专业的语气向用户推荐这些课程。")
                    .call()
                    .content();
        } else {
            return chatClient.prompt()
                    .user(message)
                    .call()
                    .content();
        }
    }
    // 简单关键词匹配，可根据需要优化为 NLP 模型或更复杂的逻辑
    private boolean isCourseRelatedQuery(String message) {
        if (message == null) return false;
        String lowerMsg = message.toLowerCase();
        return lowerMsg.contains("推荐") &&
                (lowerMsg.contains("课程") || lowerMsg.contains("课") || lowerMsg.contains("学什么") || lowerMsg.contains("有什么课"));
    }
}