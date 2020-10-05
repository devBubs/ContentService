package c2.elastic.bucket.ContentService.dao;

import c2.elastic.bucket.ContentService.exception.ContentServiceInvalidInputException;
import c2.elastic.bucket.ContentService.model.ContentDO;
import c2.elastic.bucket.ContentService.model.ContentType;
import c2.elastic.bucket.ContentService.util.Neo4jUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.neo4j.driver.v1.exceptions.ClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static c2.elastic.bucket.ContentService.constants.ContentConstants.CONTENT_GENRE_RELATIONSHIP;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.CONTENT_PLATFORM_RELATIONSHIP;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.CONTENT_PLATFORM_URL;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.CONTENT_RELEASE_YEAR;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.CONTENT_TAGS;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.CONTENT_TITLE;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.GENRE_LABEL;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.GENRE_TYPE;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.PLATFORM_LABEL;
import static c2.elastic.bucket.ContentService.constants.ContentConstants.PLATFORM_NAME;
import static c2.elastic.bucket.ContentService.constants.Neo4jConstants.CONTENT_TAGS_DELIMITER;
import static org.apache.tinkerpop.gremlin.process.traversal.P.within;

@Component
@Slf4j
public class DefaultContentDao implements ContentDao {
    private final GraphTraversalSource traversalSource;
    private final Neo4jUtil neo4jUtil;

    @Autowired
    public DefaultContentDao(GraphTraversalSource traversalSource, Neo4jUtil neo4jUtil) {
        this.traversalSource = traversalSource;
        this.neo4jUtil = neo4jUtil;
    }

    @Override
    public ContentDO createContent(ContentDO content) {
        try (Transaction tx = traversalSource.tx()) {
            Vertex contentVertex = traversalSource.addV(content.getContentType().name())
                    .property(CONTENT_TITLE, content.getContentTitle())
                    .property(CONTENT_RELEASE_YEAR, content.getContentReleaseYear())
                    .property(CONTENT_TAGS, StringUtils.join(content.getTags(), CONTENT_TAGS_DELIMITER))
                    .next();
            addContentGenreRelationship(contentVertex, content.getGenres());
            addContentPlatformRelationship(contentVertex, content.getPlatformToUrlMap());
            tx.commit();
            Long contentId = (Long) contentVertex.id();
            content.setContentId(contentId);
        } catch (ClientException e) {
            neo4jUtil.handleClientException(e);
        }
        return content;
    }


    @Override
    public ContentDO getContent(long contentId) {
        try {
            Vertex contentVertex = traversalSource.V().has(T.id, contentId).next();
            List<String> genres = traversalSource.V(contentVertex).out(CONTENT_GENRE_RELATIONSHIP).toStream()
                    .map(vertex -> (String) vertex.value(GENRE_TYPE))
                    .collect(Collectors.toList());
            Map<String, String> platformToUrlMap = traversalSource.V(contentVertex).outE(CONTENT_PLATFORM_RELATIONSHIP)
                    .toStream()
                    .collect(Collectors.toMap(edge -> edge.inVertex().value(PLATFORM_NAME), edge -> edge.value(CONTENT_PLATFORM_URL)));
            String contentTags = contentVertex.value(CONTENT_TAGS);
            List<String> contentTagsList = Stream.of(contentTags.split(CONTENT_TAGS_DELIMITER))
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toList());
            return ContentDO.builder()
                    .contentId(contentId)
                    .contentTitle(contentVertex.value(CONTENT_TITLE))
                    .contentReleaseYear(contentVertex.value(CONTENT_RELEASE_YEAR))
                    .contentType(ContentType.valueOf(contentVertex.label()))
                    .genres(genres)
                    .tags(contentTagsList)
                    .platformToUrlMap(platformToUrlMap)
                    .build();
        } catch (NoSuchElementException e) {
            String msg = String.format("Content not found for id: %s", contentId);
            throw new ContentServiceInvalidInputException(msg, e);
        }
    }

    @Override
    public ContentDO updateContent(ContentDO contentDO) {
        return null;
    }

    private void addContentGenreRelationship(Vertex contentVertex, List<String> genres) {
        GraphTraversal<Vertex, Vertex> genreVertices = traversalSource.V().hasLabel(GENRE_LABEL).has(GENRE_TYPE, within(genres));
        while (genreVertices.hasNext()) {
            Vertex genreVertex = genreVertices.next();
            contentVertex.addEdge(CONTENT_GENRE_RELATIONSHIP, genreVertex);
        }
    }

    private void addContentPlatformRelationship(Vertex contentVertex, Map<String, String> platformToUrlMap) {
        Set<String> platforms = platformToUrlMap.keySet();
        GraphTraversal<Vertex, Vertex> platformVertices = traversalSource.V().hasLabel(PLATFORM_LABEL).has(PLATFORM_NAME, within(platforms));
        while (platformVertices.hasNext()) {
            Vertex platformVertex = platformVertices.next();
            String platformName = platformVertex.value(PLATFORM_NAME);
            contentVertex.addEdge(CONTENT_PLATFORM_RELATIONSHIP, platformVertex).property(CONTENT_PLATFORM_URL, platformToUrlMap.get(platformName));
        }
    }
}
