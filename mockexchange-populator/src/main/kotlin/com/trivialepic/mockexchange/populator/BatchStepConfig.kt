package com.trivialepic.mockexchange.populator

import com.thoughtworks.xstream.converters.enums.EnumToStringConverter
import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemStreamReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder
import org.springframework.batch.item.validator.BeanValidatingItemProcessor
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.core.io.Resource
import org.springframework.oxm.xstream.XStreamMarshaller
import java.nio.charset.StandardCharsets
import java.util.*
import javax.persistence.EntityManagerFactory

class BatchStepConfig<T>(
    private val entityManagerFactory: EntityManagerFactory,
    private val stepBuilderFactory: StepBuilderFactory,
) {
    lateinit var name: String
    lateinit var clazz: Class<T>
    lateinit var file: Resource

    fun step(): Step {
        return stepBuilderFactory.get(name).chunk<T, T>(100)
            .reader(itemReader())
            .processor(itemProcessor())
            .writer(itemDbWriterImpl())
            .build()
    }

    private fun marshaller(): XStreamMarshaller {
        return itemMarshaller(clazz)
    }

    private fun itemReader(): ItemReader<T> {
        return itemReader(file, name, marshaller())
    }

    private fun itemProcessor(): ItemProcessor<T, T> {
        return BeanValidatingItemProcessor<T>().apply { afterPropertiesSet() }
    }

    private fun itemDbWriterImpl(): ItemWriter<T> {
        return itemDbWriter()
    }

    companion object {

        fun <T> itemMarshaller(clazz: Class<T>): XStreamMarshaller {
            val marshaller = XStreamMarshaller()
            marshaller.setAutodetectAnnotations(true)
            marshaller.setAliases(Collections.singletonMap("row", clazz))
            marshaller.xStream.allowTypes(arrayOf<Class<*>>(clazz))
            val map = HashMap<String, MockPostType>().apply {
                MockPostType.values().forEach {
                    this[it.ordinal.toString()] = it
//                this[it.ordinal] = it
                }
            }
            marshaller.xStream.registerConverter(EnumToStringConverter(MockPostType::class.java, map))
            return marshaller
        }

        fun <T> itemReader(resource: Resource, name: String, marshaller: XStreamMarshaller): ItemStreamReader<T> {
            return StaxEventItemReaderBuilder<T>()
                .resource(resource)
                .name(name)
                .encoding(StandardCharsets.UTF_8.name())
                .addFragmentRootElements("row")
                .unmarshaller(marshaller)
                .build()
        }
    }

    private fun itemDbWriter(): ItemWriter<T> {
        return JpaItemWriterBuilder<T>()
            .entityManagerFactory(entityManagerFactory)
            .build()
    }
}
