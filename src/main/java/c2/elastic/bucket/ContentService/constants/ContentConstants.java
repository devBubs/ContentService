package c2.elastic.bucket.ContentService.constants;

public class ContentConstants {
    public static final String BASE_URL = "/api/v1/content";
    public static final String CREATE_CONTENT_URL = "/";
    public static final String GET_CONTENT_URL = "/{contentId}";
    public static final String SEARCH_CONTENT_URL = "/search";
    public static final String UPDATE_CONTENT_URL = "/{contentId}";
    public static final String PATH_CONTENT_ID = "contentId";

    public static final String CONTENT_TITLE = "contentTitle";
    public static final String CONTENT_RELEASE_YEAR = "releaseYear";
    public static final String CONTENT_TAGS = "tags";
    public static final String GENRE_LABEL = "genre";
    public static final String GENRE_TYPE = "type";
    public static final String PLATFORM_LABEL = "platform";
    public static final String PLATFORM_NAME = "name";
    public static final String CONTENT_GENRE_RELATIONSHIP = "BELONGS_TO";
    public static final String CONTENT_PLATFORM_RELATIONSHIP = "WATCH_ON";
    public static final String CONTENT_PLATFORM_URL = "url";
}
