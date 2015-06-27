/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import guru.bubl.module.model.User;
import guru.bubl.module.model.graph.FriendlyResourcePojo;
import guru.bubl.module.model.graph.Identification;

import java.util.Set;

public interface RelatedIdentificationOperator {
    RelatedIdentificationOperator relateResourceToIdentification(
            FriendlyResourcePojo relatedResource,
            Identification identification
    );

    RelatedIdentificationOperator removeRelatedResourceToIdentification(
            FriendlyResourcePojo relatedResource,
            Identification identification
    );

    Set<FriendlyResourcePojo> getResourcesRelatedToIdentificationForUser(
            Identification identification,
            User user
    );
}
