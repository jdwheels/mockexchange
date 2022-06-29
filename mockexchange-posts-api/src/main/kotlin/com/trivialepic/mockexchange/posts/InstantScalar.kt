//package com.trivialepic.mockexchange.posts
//
//import graphql.language.StringValue
//import graphql.schema.Coercing
//import graphql.schema.CoercingSerializeException
//import java.time.format.DateTimeFormatter
//import graphql.schema.CoercingParseValueException
//import graphql.schema.CoercingParseLiteralException
//import java.time.Instant
//import java.time.ZoneId
//
//class InstantScalar : Coercing<Instant?, String> {
//    @Throws(CoercingSerializeException::class)
//    override fun serialize(dataFetcherResult: Any): String? {
//        return if (dataFetcherResult is Instant) {
//            dataFetcherResult.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ISO_INSTANT)
//        } else {
//            throw CoercingSerializeException("Not a valid DateTime")
//        }
//    }
//
//    @Throws(CoercingParseValueException::class)
//    override fun parseValue(input: Any): Instant {
//        return Instant.parse(input.toString())
//    }
//
//    @Throws(CoercingParseLiteralException::class)
//    override fun parseLiteral(input: Any): Instant {
//        if (input is StringValue) {
//            return Instant.parse(input.value)
//        }
//        throw CoercingParseLiteralException("Value is not a valid ISO date time")
//    }
//}
