package c2.elastic.bucket.ContentService.dao;

import c2.elastic.bucket.ContentService.constants.ContentConstants;
import c2.elastic.bucket.ContentService.model.ContentDO;
import c2.elastic.bucket.ContentService.model.ContentType;
import org.apache.commons.lang3.StringUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Transaction;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.tinkerpop.gremlin.process.traversal.P.within;

public class DefaultContentDao implements ContentDao {
    private final GraphTraversalSource traversalSource;

    @Autowired
    public DefaultContentDao(GraphTraversalSource traversalSource){
        this.traversalSource = traversalSource;
    }

    @Override
    public ContentDO createContent(ContentDO content) {
        try(Transaction tx = traversalSource.tx()){
            Vertex contentVertex = traversalSource.addV(content.getContentType().name())
                    .property(ContentConstants.CONTENT_TITLE, content.getContentTitle())
                    .property(ContentConstants.CONTENT_RELEASE_YEAR, content.getContentReleaseYear())
                    .property(ContentConstants.CONTENT_TAGS, StringUtils.join(content.getTags(), ","))
                    .next();
            addContentGenreRelationship(contentVertex, content.getGenres());
            addContentPlatformRelationship(contentVertex, content.getPlatformToUrlMap());
            tx.commit();
            content.setContentId(contentVertex.id().toString());
        }
        return content;
    }


    @Override
    public ContentDO getContent(String contentId) {
        Vertex contentVertex = traversalSource.V().has(ContentConstants.CONTENT_ID, contentId).next();
        List<String> genres = traversalSource.V(contentVertex).out(ContentConstants.CONTENT_GENRE_RELATIONSHIP).toStream()
                .map(vertex -> (String) vertex.value(ContentConstants.GENRE_TYPE))
                .collect(Collectors.toList());
        Map<String, String> platformToUrlMap = traversalSource.V(contentVertex).outE(ContentConstants.CONTENT_PLATFORM_RELATIONSHIP)
                .toStream()
                .collect(Collectors.toMap(edge -> edge.inVertex().value(ContentConstants.PLATFORM_NAME), edge -> edge.value("url")));
        String movieTags = contentVertex.value(ContentConstants.CONTENT_TAGS);
        List<String> movieTagsList = Arrays.asList(movieTags.split(",", 0));
        return ContentDO.builder()
                .contentId(contentVertex.value(ContentConstants.CONTENT_ID))
                .contentTitle(contentVertex.value(ContentConstants.CONTENT_TITLE))
                .contentReleaseYear(contentVertex.value(ContentConstants.CONTENT_RELEASE_YEAR))
                .contentType(ContentType.valueOf(contentVertex.label()))
                .genres(genres)
                .tags(movieTagsList)
                .platformToUrlMap(platformToUrlMap)
                .build();
    }

    @Override
    public ContentDO updateContent(ContentDO contentDO) {
        return null;
    }

    private void addContentGenreRelationship(Vertex contentVertex, List<String> genres){
        GraphTraversal<Vertex, Vertex> genreVertices = traversalSource.V().hasLabel(ContentConstants.GENRE_LABEL).has(ContentConstants.GENRE_TYPE, within(genres));
        while (genreVertices.hasNext()) {
            Vertex genreVertex = genreVertices.next();
            contentVertex.addEdge(ContentConstants.CONTENT_GENRE_RELATIONSHIP, genreVertex);
        }
    }

    private void addContentPlatformRelationship(Vertex contentVertex, Map<String, String> platformToUrlMap) {
        Set<String> platforms = platformToUrlMap.keySet();
        GraphTraversal<Vertex, Vertex> platformVertices = traversalSource.V().hasLabel(ContentConstants.PLATFORM_LABEL).has(ContentConstants.PLATFORM_NAME, within(platforms));
        while (platformVertices.hasNext()) {
            Vertex platformVertex = platformVertices.next();
            String platformName = platformVertex.value(ContentConstants.PLATFORM_NAME);
            contentVertex.addEdge(ContentConstants.CONTENT_PLATFORM_RELATIONSHIP, platformVertex).property("url", platformToUrlMap.get(platformName));
        }

    }
}
