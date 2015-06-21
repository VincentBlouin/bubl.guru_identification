/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import guru.bubl.module.model.FriendlyResource;
import guru.bubl.module.model.graph.Identification;

import java.util.Set;

public interface RelatedIdentificationOperator {
    RelatedIdentificationOperator relateResourceToIdentification(
            FriendlyResource relatedResource,
            Identification identification
    );

    Set<FriendlyResource> getResourcesRelatedToIdentification(
            Identification identification
    );
}
