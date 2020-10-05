package c2.elastic.bucket.ContentService.manager;

import c2.elastic.bucket.ContentService.model.ContentBO;

import java.util.List;

public interface ContentManager {
    ContentBO createContent(ContentBO contentBO);
    ContentBO getContent(long contentId);
    ContentBO updateContent(ContentBO contentBO);
    List<ContentBO> searchContent();
}
