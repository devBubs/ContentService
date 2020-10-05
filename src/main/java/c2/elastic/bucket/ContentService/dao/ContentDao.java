package c2.elastic.bucket.ContentService.dao;

import c2.elastic.bucket.ContentService.model.ContentDO;

public interface ContentDao {
    ContentDO createContent(ContentDO contentDO);
    ContentDO updateContent(ContentDO contentDO);
    ContentDO getContent(long contentId);
}
