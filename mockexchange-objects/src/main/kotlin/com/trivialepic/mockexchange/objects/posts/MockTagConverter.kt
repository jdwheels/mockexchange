package com.trivialepic.mockexchange.objects.posts

import org.springframework.util.StringUtils
import java.util.Arrays
import java.util.stream.Collectors
import javax.persistence.AttributeConverter

class MockTagConverter : AttributeConverter<Set<String?>?, String?> {

    override fun convertToDatabaseColumn(attribute: Set<String?>?): String? {
        if (attribute == null) {
            return null
        }
        return if (attribute.isEmpty()) ""
        else attribute.stream()
            .map { tag -> String.format("<%s>", tag) }
            .collect(Collectors.joining())
    }

    override fun convertToEntityAttribute(dbData: String?): Set<String?>? {
        return if (!StringUtils.hasLength(dbData)) emptySet<String>()
        else Arrays.stream(dbData!!.split("><").toTypedArray())
            .map { tag -> tag.replace("([<>])".toRegex(), "") }
            .collect(Collectors.toSet())
    }
}
