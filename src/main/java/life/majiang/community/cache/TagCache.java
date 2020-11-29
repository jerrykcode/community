package life.majiang.community.cache;

import life.majiang.community.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class TagCache {

    public static List<TagDTO> get() {
        List<TagDTO> list = new ArrayList<>();
        TagDTO program = new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("Java", "js", "css", "html", "C语言", "c++", "Objective-C", "php", "go", "python",
                "rust", "c#", "swift", "Assembly"));
        list.add(program);

        TagDTO framework = new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("Spring", "Spring Boot", "Spring MVC", "Spring security", "Spring cloud", "struts",
                "django", "express", "flask", "ruby-on-rails", "tornado", "laravel", "yii"));
        list.add(framework);

        TagDTO server = new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux", "nginx", "docker", "apache", "ubuntu", "centos",
                "缓存-tomcat", "负载均衡", "unix", "hadoop", "windows-server"));
        list.add(server);

        TagDTO db = new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql", "redis", "mongodb", "oracle", "sql", "no-sql",
                "sqlserver", "sqlite", "graph database"));
        list.add(db);

        TagDTO tools = new TagDTO();
        tools.setCategoryName("开发工具");
        tools.setTags(Arrays.asList("git", "github", "vscode", "vim", "emacs", "intellij-idea",
                "visual studio", "sublime-text", "eclipse", "maven", "svn", "gcc/g++/gdb", "cmake", "gnu make"));
        list.add(tools);

        TagDTO tech = new TagDTO();
        tech.setCategoryName("技术主题");
        tech.setTags(Arrays.asList("后端", "前端", "App", "Android", "ios", "大数据", "AI", "数据结构与算法"));
        list.add(tech);

        TagDTO other = new TagDTO();
        other.setCategoryName("其他");
        other.setTags(Arrays.asList("其他"));
        list.add(other);

        return list;
    }

    public static String filterValid(String tagInput) {
        List<TagDTO> tagDTOList = get();
        Set<String> tagSet = new HashSet<>();
        for (TagDTO tagDTO : tagDTOList)
            for (String tag : tagDTO.getTags())
               tagSet.add(tag);
        String tags[] = StringUtils.split(tagInput, ",");
        for (String tag: tags)
            if (!tagSet.contains(tag))
                return tag;
        return null;
    }
}
