/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import com.google.inject.Injector;
import guru.bubl.module.model.User;
import guru.bubl.module.model.graph.AdaptableGraphComponentTest;
import guru.bubl.module.model.graph.vertex.VertexOperator;
import guru.bubl.module.model.test.scenarios.TestScenarios;
import org.junit.Before;
import org.junit.Test;

import javax.inject.Inject;

import static org.junit.Assert.assertTrue;

public class RelatedIdentificationOperatorTest extends AdaptableGraphComponentTest{

    protected static Injector identificationInjector;

    @Inject
    protected TestScenarios testScenarios;

    RelatedIdentificationOperator relatedIdentificationOperator;

    @Before
    public void beforeIdentificationTest(){
        identificationInjector = injector.createChildInjector(
                new IdentificationModuleNeo4j()
        );
        relatedIdentificationOperator = identificationInjector.getInstance(
                RelatedIdentificationOperator.class
        );
    }

    @Test
    public void can_get_related_identification(){
        User user = User.withEmailAndUsername("a", "b");
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        user
                ).isEmpty()
        );
        VertexOperator aVertexRepresentingATshirt = testScenarios.createAVertex(
                user
        );
        relatedIdentificationOperator.relateResourceToIdentification(
                aVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentificationForUser(
                        modelTestScenarios.tShirt(),
                        user
                ).contains(aVertexRepresentingATshirt)
        );
    }
}
