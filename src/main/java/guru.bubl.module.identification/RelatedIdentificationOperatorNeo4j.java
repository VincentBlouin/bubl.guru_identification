/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import guru.bubl.module.model.User;
import guru.bubl.module.model.graph.FriendlyResourcePojo;
import guru.bubl.module.model.graph.Identification;
import guru.bubl.module.model.json.FriendlyResourceJson;
import guru.bubl.module.neo4j_graph_manipulator.graph.Neo4jFriendlyResource;
import org.codehaus.jettison.json.JSONArray;
import org.neo4j.rest.graphdb.query.QueryEngine;
import org.neo4j.rest.graphdb.util.QueryResult;

import javax.inject.Inject;
import java.net.URI;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static guru.bubl.module.neo4j_graph_manipulator.graph.Neo4jRestApiUtils.map;

public class RelatedIdentificationOperatorNeo4j implements RelatedIdentificationOperator {
    enum props {
        identification_uri,
        related_uris
    }

    @Inject
    protected QueryEngine queryEngine;

    @Override
    public RelatedIdentificationOperator relateResourceToIdentification(FriendlyResourcePojo relatedResource, Identification identification) {
        Set<FriendlyResourcePojo> relatedResources = getResourcesRelatedToIdentificationForUsername(
                identification,
                relatedResource.getOwnerUsername()
        );
        relatedResources.add(
                relatedResource
        );
        setRelatedResourcesForIdentification(
                relatedResources,
                identification,
                relatedResource.getOwnerUsername()
        );
        return this;
    }

    @Override
    public RelatedIdentificationOperator removeRelatedResourceToIdentification(FriendlyResourcePojo relatedResource, Identification identification) {
        Set<FriendlyResourcePojo> relatedResources = getResourcesRelatedToIdentificationForUsername(
                identification,
                relatedResource.getOwnerUsername()
        );
        relatedResources.remove(
                relatedResource
        );
        setRelatedResourcesForIdentification(
                relatedResources,
                identification,
                relatedResource.getOwnerUsername()
        );
        return this;
    }

    @Override
    public Set<FriendlyResourcePojo> getResourcesRelatedToIdentificationForUser(Identification identification, User user) {
        return getResourcesRelatedToIdentificationForUsername(
                identification,
                user.username()
        );
    }

    private void setRelatedResourcesForIdentification(Set<FriendlyResourcePojo> relatedResources, Identification identification, String ownerUserName){
        String query = "MERGE (node {" +
                props.identification_uri + ":{" + props.identification_uri + "}, " +
                Neo4jFriendlyResource.props.owner + ": {" + Neo4jFriendlyResource.props.owner + "}" +
                "}) " +
                "SET node." + props.related_uris + "= { " + props.related_uris + "} ";
        queryEngine.query(
                query,
                map(
                        props.identification_uri.name(), identification.getExternalResourceUri().toString(),
                        Neo4jFriendlyResource.props.owner.name(), ownerUserName,
                        props.related_uris.name(), FriendlyResourceJson.multipleToJson(relatedResources)
                )
        );
    }

    private Set<FriendlyResourcePojo> getResourcesRelatedToIdentificationForUsername(Identification identification, String username) {
        String query = buildQueryPrefix(identification, username) +
                "return node." + props.related_uris + " as related_uris";
        QueryResult<Map<String, Object>> result = queryEngine.query(
                query,
                map()
        );
        if (!result.iterator().hasNext()) {
            return new HashSet<>();
        }
        Object relatedUrisValue = result.iterator().next().get("related_uris");
        if (relatedUrisValue == null) {
            return new HashSet<>();
        }
        return FriendlyResourceJson.fromJsonToSet(
                relatedUrisValue.toString()
        );
    }

    private String buildQueryPrefix(Identification identification, String username) {
        return "START node=node:node_auto_index('" +
                props.identification_uri + ":\"" + identification.getExternalResourceUri() + "\" AND " +
                Neo4jFriendlyResource.props.owner + ":" + username + "') ";
    }
}
