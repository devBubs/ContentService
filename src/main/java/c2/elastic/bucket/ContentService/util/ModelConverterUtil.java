package c2.elastic.bucket.ContentService.util;

import c2.elastic.bucket.ContentService.model.ContentBO;
import c2.elastic.bucket.ContentService.model.ContentDO;
import c2.elastic.bucket.ContentService.model.ContentDTO;
import c2.elastic.bucket.ContentService.model.ContentType;
import org.springframework.stereotype.Component;

@Component
public class ModelConverterUtil {
    public ContentBO toBO(ContentDO contentDO) {
        return ContentBO.builder()
                .contentId(contentDO.getContentId())
                .contentTitle(contentDO.getContentTitle())
                .contentReleaseYear(contentDO.getContentReleaseYear())
                .contentType(contentDO.getContentType())
                .genres(contentDO.getGenres())
                .tags(contentDO.getTags())
                .platformToUrlMap(contentDO.getPlatformToUrlMap())
                .build();
    }

    public ContentBO toBO(ContentDTO contentDTO) {
        return ContentBO.builder()
                .contentId(contentDTO.getContentId())
                .contentTitle(contentDTO.getContentTitle())
                .contentReleaseYear(contentDTO.getContentReleaseYear())
                .contentType(ContentType.valueOf(contentDTO.getContentType()))
                .genres(contentDTO.getGenres())
                .tags(contentDTO.getTags())
                .platformToUrlMap(contentDTO.getPlatformToUrlMap())
                .build();
    }

    public ContentDTO toDTO(ContentBO contentBO) {
        return ContentDTO.builder()
                .contentId(contentBO.getContentId())
                .contentTitle(contentBO.getContentTitle())
                .contentReleaseYear(contentBO.getContentReleaseYear())
                .contentType(contentBO.getContentType().name())
                .genres(contentBO.getGenres())
                .tags(contentBO.getTags())
                .platformToUrlMap(contentBO.getPlatformToUrlMap())
                .build();
    }

    public ContentDO toDO(ContentBO contentBO) {
        return ContentDO.builder()
                .contentId(contentBO.getContentId())
                .contentTitle(contentBO.getContentTitle())
                .contentReleaseYear(contentBO.getContentReleaseYear())
                .contentType(contentBO.getContentType())
                .genres(contentBO.getGenres())
                .tags(contentBO.getTags())
                .platformToUrlMap(contentBO.getPlatformToUrlMap())
                .build();
    }
}
