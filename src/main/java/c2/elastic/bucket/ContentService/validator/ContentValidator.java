package c2.elastic.bucket.ContentService.validator;

import c2.elastic.bucket.ContentService.exception.ContentServiceInvalidInputException;
import c2.elastic.bucket.ContentService.model.ContentDTO;
import com.google.common.base.Preconditions;

public class ContentValidator {
    public void validateOnCreate(ContentDTO contentDTO) {
        try {
            Preconditions.checkNotNull(contentDTO.getContentTitle(),"Title cannot be empty");
            Preconditions.checkNotNull(contentDTO.getGenres(),"Genres cannot be empty");
            Preconditions.checkArgument(contentDTO.getContentReleaseYear()>=1900 && contentDTO.getContentReleaseYear()<=2100, "Invalid release year");
        } catch (IllegalArgumentException e){
            throw new ContentServiceInvalidInputException(e.getMessage(), e);
        }
    }
}
