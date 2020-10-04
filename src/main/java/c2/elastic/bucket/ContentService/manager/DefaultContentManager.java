package c2.elastic.bucket.ContentService.manager;

import c2.elastic.bucket.ContentService.dao.ContentDao;
import c2.elastic.bucket.ContentService.model.ContentBO;
import c2.elastic.bucket.ContentService.model.ContentDO;
import c2.elastic.bucket.ContentService.util.ContentUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DefaultContentManager implements ContentManager {

    private final ContentDao contentDao;
    private final ContentUtil contentUtil;

    @Autowired
    public DefaultContentManager(ContentDao contentDao, ContentUtil contentUtil){
        this.contentDao = contentDao;
        this.contentUtil = contentUtil;
    }

    @Override
    public ContentBO createContent(ContentBO contentBO) {
        ContentDO contentDO = contentUtil.toDO(contentBO);
        contentDO = contentDao.createContent(contentDO);
        return contentUtil.toBO(contentDO);
    }

    @Override
    public ContentBO getContent(String contentId) {
        ContentDO contentDO = contentDao.getContent(contentId);
        return contentUtil.toBO(contentDO);
    }

    @Override
    public ContentBO updateContent(ContentBO contentBO) {
        ContentDO contentDO = contentUtil.toDO(contentBO);
        contentDO = contentDao.updateContent(contentDO);
        return contentUtil.toBO(contentDO);
    }

    @Override
    public List<ContentBO> searchContent() {
        return null;
    }
}
