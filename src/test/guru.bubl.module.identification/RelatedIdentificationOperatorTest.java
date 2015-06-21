/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import com.google.inject.Injector;
import guru.bubl.module.model.graph.AdaptableGraphComponentTest;
import guru.bubl.module.model.graph.GraphFactory;
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
                new IdentificationModule()
        );
        relatedIdentificationOperator = identificationInjector.getInstance(
                RelatedIdentificationOperator.class
        );
    }

    @Test
    public void can_get_related_identification(){
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentification(
                        modelTestScenarios.tShirt()
                ).isEmpty()
        );
        VertexOperator aVertexRepresentingATshirt = testScenarios.createAVertex();
        relatedIdentificationOperator.relateResourceToIdentification(
                aVertexRepresentingATshirt,
                modelTestScenarios.tShirt()
        );
        assertTrue(
                relatedIdentificationOperator.getResourcesRelatedToIdentification(
                        modelTestScenarios.tShirt()
                ).contains(aVertexRepresentingATshirt)
        );
    }
}
