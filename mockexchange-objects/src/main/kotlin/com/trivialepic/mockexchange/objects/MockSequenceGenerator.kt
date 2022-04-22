package com.trivialepic.mockexchange.objects

import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.enhanced.SequenceStyleGenerator
import java.io.Serializable

class MockSequenceGenerator : SequenceStyleGenerator() {
    override fun generate(session: SharedSessionContractImplementor, obj: Any?): Serializable {
        if (obj != null && obj is MockEntity<*>) {
            return obj.id ?: super.generate(session, obj)
        }
        return super.generate(session, obj)
    }

}
