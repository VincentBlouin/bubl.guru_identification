/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import com.google.inject.Injector;
import guru.bubl.module.model.User;
import guru.bubl.module.model.graph.AdaptableGraphComponentTest;
import guru.bubl.module.model.graph.FriendlyResourcePojo;
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

    User someUser;

    @Before
    public void beforeIdentificationTest() {
        identificationInjector = injector.createChildInjector(
                new IdentificationModuleNeo4j()
        );
        relatedIdentificationOperator = identificationInjector.getInstance(
                RelatedIdentificationOperator.class
        );
        someUser = User.withEmailAndUsername("a", "b");
    }

    @Test
    public void can_get_related_identification() {
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).isEmpty()
        );
        FriendlyResourcePojo aVertexRepresentingATshirt = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        someUser
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                aVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).contains(
                        aVertexRepresentingATshirt
                )
        );
    }

    @Test
    public void can_have_multiple_related_resources_for_one_identification() {
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).isEmpty()
        );
        FriendlyResourcePojo aVertexRepresentingATshirt = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        someUser
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                aVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        FriendlyResourcePojo anotherVertexRepresentingATshirt = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        someUser
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                anotherVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).contains(
                        aVertexRepresentingATshirt
                )
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).contains(
                        anotherVertexRepresentingATshirt
                )
        );
    }

    @Test
    public void can_remove_related_identification() {
        FriendlyResourcePojo aVertexRepresentingATshirt = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        someUser
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                aVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        assertFalse(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).isEmpty()
        );
        relatedIdentificationOperator.removeRelatedResourceToIdentification(
                aVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).isEmpty()
        );
    }

    @Test
    public void cannot_get_related_identifications_of_another_user() {
        FriendlyResourcePojo user1Resource = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        someUser
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                user1Resource,
                modelTestScenarios.tShirt()
        );
        assertFalse(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).isEmpty()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        User.withEmailAndUsername("c", "d")
                ).isEmpty()
        );
    }

    @Test
    public void adding_related_identification_doesnt_overwrite_other_user_related_identifications_for_same_identification() {
        FriendlyResourcePojo user1Resource = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        someUser
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                user1Resource,
                modelTestScenarios.tShirt()
        );
        User otherUser = User.withEmailAndUsername("c", "d");
        FriendlyResourcePojo otherUserResource = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        otherUser
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                otherUserResource,
                modelTestScenarios.tShirt()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).contains(user1Resource)
        );
        assertFalse(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).contains(otherUserResource)
        );
    }
    @Test
    public void removing_related_identification_doesnt_erase_other_user_related_identifications_for_same_identification() {
        User someUser = User.withEmailAndUsername("a", "b");
        FriendlyResourcePojo user1Resource = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        someUser
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                user1Resource,
                modelTestScenarios.tShirt()
        );
        User otherUser = User.withEmailAndUsername("c", "d");
        FriendlyResourcePojo otherUserResource = new FriendlyResourcePojo(
                testScenarios.createAVertex(
                        otherUser
                ).uri()
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                otherUserResource,
                modelTestScenarios.tShirt()
        );
        relatedIdentificationOperator.removeRelatedResourceToIdentification(
                otherUserResource,
                modelTestScenarios.tShirt()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        someUser
                ).contains(user1Resource)
        );
    }
}
