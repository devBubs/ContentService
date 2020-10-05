package c2.elastic.bucket.ContentService.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class ContentDTO {
    private long contentId;
    private String contentTitle;
    private long contentReleaseYear;
    private String contentType;
    private List<String> genres;
    private Map<String, String> platformToUrlMap;
    private List<String> tags;
}
