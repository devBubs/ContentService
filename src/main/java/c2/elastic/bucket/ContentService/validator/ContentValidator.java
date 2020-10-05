package c2.elastic.bucket.ContentService.validator;

import c2.elastic.bucket.ContentService.exception.ContentServiceInvalidInputException;
import c2.elastic.bucket.ContentService.model.ContentDTO;
import c2.elastic.bucket.ContentService.model.ContentType;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static c2.elastic.bucket.ContentService.constants.MessageConstants.CONTENT_GENRES_INVALID;
import static c2.elastic.bucket.ContentService.constants.MessageConstants.CONTENT_GENRES_NULL;
import static c2.elastic.bucket.ContentService.constants.MessageConstants.CONTENT_PLATFORM_URL_MAP_INVALID;
import static c2.elastic.bucket.ContentService.constants.MessageConstants.CONTENT_PLATFORM_URL_MAP_NULL;
import static c2.elastic.bucket.ContentService.constants.MessageConstants.CONTENT_RELEASE_YEAR_INVALID;
import static c2.elastic.bucket.ContentService.constants.MessageConstants.CONTENT_TAGS_INVALID;
import static c2.elastic.bucket.ContentService.constants.MessageConstants.CONTENT_TAGS_NULL;
import static c2.elastic.bucket.ContentService.constants.MessageConstants.CONTENT_TITLE_EMPTY;
import static c2.elastic.bucket.ContentService.constants.MessageConstants.CONTENT_TYPE_INVALID;

@Component
public class ContentValidator {
    public void validateOnCreate(ContentDTO contentDTO) {
        try {
            Preconditions.checkArgument(StringUtils.isNotBlank(contentDTO.getContentTitle()),
                    CONTENT_TITLE_EMPTY);
            Preconditions.checkArgument(Objects.nonNull(contentDTO.getGenres()),
                    CONTENT_GENRES_NULL);
            Preconditions.checkArgument(contentDTO.getGenres().stream().noneMatch(StringUtils::isBlank),
                    CONTENT_GENRES_INVALID);
            Preconditions.checkArgument(contentDTO.getContentReleaseYear() >= 1900 && contentDTO.getContentReleaseYear() <= 2100,
                    CONTENT_RELEASE_YEAR_INVALID);
            Preconditions.checkArgument(EnumUtils.isValidEnum(ContentType.class, contentDTO.getContentType()),
                    CONTENT_TYPE_INVALID);
            Preconditions.checkArgument(Objects.nonNull(contentDTO.getPlatformToUrlMap()),
                    CONTENT_PLATFORM_URL_MAP_NULL);
            Preconditions.checkArgument(contentDTO.getPlatformToUrlMap().values().stream().noneMatch(StringUtils::isBlank),
                    CONTENT_PLATFORM_URL_MAP_INVALID);
            Preconditions.checkArgument(Objects.nonNull(contentDTO.getTags()),
                    CONTENT_TAGS_NULL);
            Preconditions.checkArgument(contentDTO.getTags().stream().noneMatch(StringUtils::isBlank),
                    CONTENT_TAGS_INVALID);
        } catch (IllegalArgumentException e) {
            throw new ContentServiceInvalidInputException(e.getMessage(), e);
        }
    }
}
