/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import guru.bubl.module.model.FriendlyResource;
import guru.bubl.module.model.User;
import guru.bubl.module.model.graph.FriendlyResourcePojo;
import guru.bubl.module.model.graph.Identification;
import guru.bubl.module.model.json.ImageJson;
import guru.bubl.module.neo4j_graph_manipulator.graph.Neo4jFriendlyResource;
import guru.bubl.module.neo4j_graph_manipulator.graph.Neo4jIdentificationFactory;
import guru.bubl.module.neo4j_graph_manipulator.graph.graph.Neo4jIdentification;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
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
        related_uris
    }

    @Inject
    protected QueryEngine queryEngine;

    @Override
    public RelatedIdentificationOperator relateResourceToIdentification(FriendlyResource relatedResource, Identification identification) {
        getResourcesRelatedToIdentificationForUsername(
                identification,
                relatedResource.getOwnerUsername()
        );
        String query = "";
        queryEngine.query(
                "",
                map()
        );
        return this;
    }

    @Override
    public Set<FriendlyResource> getResourcesRelatedToIdentificationForUser(Identification identification, User user) {
        return getResourcesRelatedToIdentificationForUsername(
                identification,
                user.username()
        );
    }

    private Set<FriendlyResource> getResourcesRelatedToIdentificationForUsername(Identification identification, String username) {
        String query = "START node=node:node_auto_index('" +
                Neo4jFriendlyResource.props.uri + ":" + identification.uri() + " AND " +
                Neo4jFriendlyResource.props.owner + ":" + username + "') " +
                "return node.related_uris as related_uris";
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
        Set<FriendlyResource> relatedIdentifications = new HashSet<>();
        try {
            JSONArray uris = new JSONArray(
                    relatedUrisValue.toString()
            );
            for (int i = 0; i < uris.length(); i++) {
                relatedIdentifications.add(
                        new FriendlyResourcePojo(
                                URI.create(
                                        uris.getString(i)
                                )
                        )
                );
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return relatedIdentifications;
    }
}
