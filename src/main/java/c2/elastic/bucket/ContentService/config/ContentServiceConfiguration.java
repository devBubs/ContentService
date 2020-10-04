package c2.elastic.bucket.ContentService.config;

import com.steelbridgelabs.oss.neo4j.structure.Neo4JGraph;
import com.steelbridgelabs.oss.neo4j.structure.providers.Neo4JNativeElementIdProvider;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContentServiceConfiguration {
    @Bean
    public GraphTraversalSource getGraphTraversalSource(Graph graph){
        return graph.traversal();
    }

    @Bean
    public Graph getGraph(){
        Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo"));
        Neo4JNativeElementIdProvider provider = new Neo4JNativeElementIdProvider();
        return new Neo4JGraph(driver, provider, provider);
    }
}
