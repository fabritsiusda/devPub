package main.api.responses;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class BlogInfo {

    @Value("${info.title}")
    private String title;
    @Value("${info.subTitle}")
    private String subTitle;
    @Value("${info.email}")
    private String email;
    @Value("${info.copyright}")
    private String copyright;
    @Value("${info.from}")
    private String copyrightFrom;

}
