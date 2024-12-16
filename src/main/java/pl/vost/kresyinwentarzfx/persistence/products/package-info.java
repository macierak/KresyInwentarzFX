@GenericGenerator(
        name = "ID_GENERATOR",
        strategy = "org.hibernate.id.enhanced.TableGenerator",
        parameters = {
                @Parameter(name = TableGenerator.INCREMENT_PARAM, value = "1"),
                @Parameter(name = TableGenerator.INITIAL_PARAM, value = "1"),
                @Parameter(name = TableGenerator.CONFIG_PREFER_SEGMENT_PER_ENTITY, value = "true")
        }
)
package pl.vost.kresyinwentarzfx.persistence.products;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.enhanced.TableGenerator;

