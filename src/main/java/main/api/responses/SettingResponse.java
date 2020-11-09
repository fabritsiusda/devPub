package main.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SettingResponse {

    @JsonProperty(value = "MULTIUSER_MODE")
    private boolean multiuserMode;
    @JsonProperty(value = "POST_PREMODERATION")
    private boolean postPremoderation;
    @JsonProperty(value = "STATISTICS_IS_PUBLIC")
    private boolean publicStatistic;

}
