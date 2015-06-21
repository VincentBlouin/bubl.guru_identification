/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import guru.bubl.module.model.FriendlyResource;
import guru.bubl.module.model.graph.Identification;

import java.util.HashSet;
import java.util.Set;

public class RelatedIdentificationOperatorES implements RelatedIdentificationOperator {
    @Override
    public RelatedIdentificationOperator relateResourceToIdentification(FriendlyResource relatedResource, Identification identification) {
        return this;
    }

    @Override
    public Set<FriendlyResource> getResourcesRelatedToIdentification(Identification identification) {
        return new HashSet<>();
    }
}
