/*
 * Copyright Vincent Blouin under the GPL License version 3
 */

package guru.bubl.module.identification;

import com.google.inject.AbstractModule;

public class IdentificationModuleNeo4j extends AbstractModule {

    @Override
    protected void configure() {
        bind(
                RelatedIdentificationOperator.class
        ).to(
                RelatedIdentificationOperatorNeo4j.class
        );
    }
}
