package c2.elastic.bucket.ContentService.controller;

import c2.elastic.bucket.ContentService.exception.ContentServiceInvalidInputException;
import c2.elastic.bucket.ContentService.manager.ContentManager;
import c2.elastic.bucket.ContentService.model.ContentBO;
import c2.elastic.bucket.ContentService.model.ContentDTO;
import c2.elastic.bucket.ContentService.util.ErrorUtil;
import c2.elastic.bucket.ContentService.util.ModelConverterUtil;
import c2.elastic.bucket.ContentService.validator.ContentValidator;
import c2.elastic.bucket.GenBucket.model.GenericResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static c2.elastic.bucket.ContentService.constants.ContentConstants.BASE_URL;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.CREATE_CONTENT_URL;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.GET_CONTENT_URL;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.PATH_CONTENT_ID;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.SEARCH_CONTENT_URL;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.UPDATE_CONTENT_URL;

@RestController
@Slf4j
@RequestMapping(BASE_URL)
public class ContentController {
    private final ContentManager contentManager;
    private final ModelConverterUtil modelConverterUtil;
    private final ContentValidator contentValidator;
    private final ErrorUtil errorUtil;

    @Autowired
    public ContentController(ContentManager contentManager, ModelConverterUtil modelConverterUtil, ContentValidator contentValidator, ErrorUtil errorUtil) {
        this.contentManager = contentManager;
        this.modelConverterUtil = modelConverterUtil;
        this.contentValidator = contentValidator;
        this.errorUtil = errorUtil;
    }

    @PostMapping(CREATE_CONTENT_URL)
    ResponseEntity<GenericResponse<ContentDTO>> createContent(@RequestBody ContentDTO contentDTO) {
        try {
            log.info("Create movie request received with input {}", contentDTO);
            contentValidator.validateOnCreate(contentDTO);
            ContentBO movieBO = contentManager.createContent(modelConverterUtil.toBO(contentDTO));
            contentDTO = modelConverterUtil.toDTO(movieBO);
            GenericResponse<ContentDTO> response = GenericResponse.<ContentDTO>builder()
                    .data(contentDTO)
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ContentServiceInvalidInputException e) {
            log.warn("Failed to create movie due to invalid input {}", contentDTO, e);
            return errorUtil.getErrorResponse(e);
        } catch (Exception e) {
            String msg = "Failed to createContent due to unexpected error";
            log.error(msg, e);
            return errorUtil.getFaultResponse(msg);
        }
    }

    @GetMapping(GET_CONTENT_URL)
    ResponseEntity<GenericResponse<ContentDTO>> getContent(@PathVariable(PATH_CONTENT_ID) long contentId) {
        try {
            log.info("Fetch movie request received with input {}", contentId);
            ContentBO contentBO = contentManager.getContent(contentId);
            ContentDTO contentDTO = modelConverterUtil.toDTO(contentBO);
            GenericResponse<ContentDTO> response = GenericResponse.<ContentDTO>builder().data(contentDTO).build();
            return ResponseEntity.ok(response);
        } catch (ContentServiceInvalidInputException e) {
            log.warn("Failed to get movie due to invalid input {}", contentId, e);
            return errorUtil.getErrorResponse(e);
        } catch (Exception e) {
            String msg = "Failed to getContent due to unexpected error";
            log.error(msg, e);
            return errorUtil.getFaultResponse(msg);
        }
    }

    @PostMapping(SEARCH_CONTENT_URL)
    ResponseEntity<GenericResponse<List<ContentDTO>>> searchContent() {
        log.info("Search movie request received");
        return errorUtil.getFaultResponse("Not implemented");
    }

    @PutMapping(UPDATE_CONTENT_URL)
    ResponseEntity<GenericResponse<ContentDTO>> updateContent(@RequestBody ContentDTO contentDTO,
                                                              @PathVariable(PATH_CONTENT_ID) String contentId) {
        log.info("Update movie request received");
        return errorUtil.getFaultResponse("Not implemented");
    }

}

