/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import com.google.inject.Injector;
import guru.bubl.module.model.User;
import guru.bubl.module.model.graph.AdaptableGraphComponentTest;
import guru.bubl.module.model.graph.FriendlyResourcePojo;
import guru.bubl.module.model.graph.vertex.VertexOperator;
import guru.bubl.module.model.test.scenarios.TestScenarios;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RelatedIdentificationOperatorTest extends AdaptableGraphComponentTest {

    protected static Injector identificationInjector;

    @Inject
    protected TestScenarios testScenarios;

    RelatedIdentificationOperator relatedIdentificationOperator;

    @Before
    public void beforeIdentificationTest() {
        identificationInjector = injector.createChildInjector(
                new IdentificationModuleNeo4j()
        );
        relatedIdentificationOperator = identificationInjector.getInstance(
                RelatedIdentificationOperator.class
        );
    }

    @Test
    public void can_get_related_identification() {
        User user = User.withEmailAndUsername("a", "b");
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        user
                ).isEmpty()
        );
        FriendlyResourcePojo aVertexRepresentingATshirt = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        user
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                aVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        user
                ).contains(
                        aVertexRepresentingATshirt
                )
        );
    }

    @Test
    public void can_remove_related_identification() {
        User user = User.withEmailAndUsername("a", "b");
        FriendlyResourcePojo aVertexRepresentingATshirt = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        user
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                aVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        assertFalse(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        user
                ).isEmpty()
        );
        relatedIdentificationOperator.removeRelatedResourceToIdentification(
                aVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        user
                ).isEmpty()
        );
    }

    @Test
    public void cannot_get_related_identifications_of_another_user() {
        User user1 = User.withEmailAndUsername("a", "b");
        FriendlyResourcePojo user1Resource = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        user1
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                user1Resource,
                modelTestScenarios.tShirt()
        );
        assertFalse(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        user1
                ).isEmpty()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        User.withEmailAndUsername("c", "d")
                ).isEmpty()
        );
    }
}
