package c2.elastic.bucket.ContentService.controller;

import c2.elastic.bucket.ContentService.exception.ContentServiceInvalidInputException;
import c2.elastic.bucket.ContentService.manager.ContentManager;
import c2.elastic.bucket.ContentService.model.ContentBO;
import c2.elastic.bucket.ContentService.model.ContentDTO;
import c2.elastic.bucket.ContentService.util.ContentUtil;
import c2.elastic.bucket.ContentService.util.ErrorUtil;
import c2.elastic.bucket.ContentService.validator.ContentValidator;
import c2.elastic.bucket.GenBucket.model.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URI;
import java.util.List;

@RestController
@Slf4j
@Component
@RequestMapping("/api/v1/content")
public class ContentController {
    private final ContentManager contentManager;
    private final ContentUtil contentUtil;
    private final ContentValidator contentValidator;
    private final ErrorUtil errorUtil;

    @Autowired
    public ContentController(ContentManager contentManager, ContentUtil contentUtil, ContentValidator contentValidator, ErrorUtil errorUtil){
        this.contentManager = contentManager;
        this.contentUtil = contentUtil;
        this.contentValidator = contentValidator;
        this.errorUtil = errorUtil;
    }

    @PostMapping("/")
    ResponseEntity<GenericResponse<ContentDTO>> createMovie(@RequestBody ContentDTO contentDTO) {
        try {
            log.info("Create movie request received with input {}", contentDTO);
            contentValidator.validateOnCreate(contentDTO);
            ContentBO movieBO = contentManager.createContent(contentUtil.toBO(contentDTO));
            contentDTO = contentUtil.toDTO(movieBO);
            URI location = URI.create(String.format("/v1/movies/%s", contentDTO.getContentId()));
            GenericResponse<ContentDTO> response = GenericResponse.<ContentDTO>builder()
                    .data(contentDTO)
                    .build();
            return ResponseEntity.created(location).body(response);
        } catch (ContentServiceInvalidInputException e) {
            log.warn("Failed to create movie due to invalid input {}", contentDTO, e);
            return errorUtil.getErrorResponse(e);
        } catch (Exception e) {
            log.error("Failed to create movie due to unexpected error", e);
            return errorUtil.getFaultResponse(e);
        }
    }

    @GetMapping("/{contentId}")
    ResponseEntity<GenericResponse<ContentDTO>> getMovie(@PathVariable("contentId") String contentId) {
        try {
            log.info("Fetch movie request received with input {}", contentId);
            ContentBO contentBO = contentManager.getContent(contentId);
            ContentDTO contentDTO = contentUtil.toDTO(contentBO);
            GenericResponse<ContentDTO> response = GenericResponse.<ContentDTO>builder().data(contentDTO).build();
            return ResponseEntity.ok(response);
        } catch (ContentServiceInvalidInputException e) {
            log.warn("Failed to get movie due to invalid input {}", contentId, e);
            return errorUtil.getErrorResponse(e);
        } catch (Exception e) {
            log.error("Failed to get movie due to unexpected error", e);
            return errorUtil.getFaultResponse(e);
        }
    }

    @PostMapping("/search")
    ResponseEntity<GenericResponse<List<ContentDTO>>> searchMovie() {
        log.info("Search movie request received");
        return errorUtil.getFaultResponse(new NotImplementedException());
    }

    @PutMapping("/{movieId}")
    ResponseEntity<GenericResponse<ContentDTO>> updateMovie(@RequestBody ContentDTO contentDTO,
                                                          @PathVariable("movieId") String movieId) {
        log.info("Update movie request received");
        return errorUtil.getFaultResponse(new NotImplementedException());
    }

}

