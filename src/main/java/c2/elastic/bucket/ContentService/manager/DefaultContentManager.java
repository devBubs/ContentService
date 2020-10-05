package c2.elastic.bucket.ContentService.manager;

import c2.elastic.bucket.ContentService.dao.ContentDao;
import c2.elastic.bucket.ContentService.model.ContentBO;
import c2.elastic.bucket.ContentService.model.ContentDO;
import c2.elastic.bucket.ContentService.util.ModelConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultContentManager implements ContentManager {

    private final ContentDao contentDao;
    private final ModelConverterUtil modelConverterUtil;

    @Autowired
    public DefaultContentManager(ContentDao contentDao, ModelConverterUtil modelConverterUtil) {
        this.contentDao = contentDao;
        this.modelConverterUtil = modelConverterUtil;
    }

    @Override
    public ContentBO createContent(ContentBO contentBO) {
        ContentDO contentDO = modelConverterUtil.toDO(contentBO);
        contentDO = contentDao.createContent(contentDO);
        return modelConverterUtil.toBO(contentDO);
    }

    @Override
    public ContentBO getContent(long contentId) {
        ContentDO contentDO = contentDao.getContent(contentId);
        return modelConverterUtil.toBO(contentDO);
    }

    @Override
    public ContentBO updateContent(ContentBO contentBO) {
        ContentDO contentDO = modelConverterUtil.toDO(contentBO);
        contentDO = contentDao.updateContent(contentDO);
        return modelConverterUtil.toBO(contentDO);
    }

    @Override
    public List<ContentBO> searchContent() {
        return null;
    }
}
