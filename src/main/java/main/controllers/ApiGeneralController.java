package main.controllers;

import main.api.responses.BlogInfo;
import main.api.responses.SettingResponse;
import main.api.responses.TagsResponse;
import main.services.ApiService;
import main.services.SettingsService;
import main.services.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiGeneralController {

    private final ApiService apiService;
    private final SettingsService settingsService;
    private final TagService tagService;

    public ApiGeneralController(ApiService apiService, SettingsService settingsService, TagService tagService) {
        this.apiService = apiService;
        this.settingsService = settingsService;
        this.tagService = tagService;
    }

    @GetMapping("/init")
    public ResponseEntity<BlogInfo> getInfo(){
        return apiService.getBlogInfo();
    }

    @GetMapping("/settings")
    public ResponseEntity<SettingResponse> getGlobalSettings(){
        return settingsService.getSettings();
    }

    @GetMapping("/tag")
    public ResponseEntity<TagsResponse> getTags(@PathVariable(required = false, name = "query") String query){
        return tagService.getTagByQuery(query);
    }

}
