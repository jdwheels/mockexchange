package com.trivialepic.mockexchange.populator

import com.trivialepic.mockexchange.objects.comments.MockComment
import com.trivialepic.mockexchange.objects.posts.MockPost
import com.trivialepic.mockexchange.objects.posts.MockPostType
import com.trivialepic.mockexchange.objects.posts.MockVote
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemStreamReader
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.test.context.ContextConfiguration
import java.time.LocalDateTime

@SpringBootTest
class PopulatorReaderTests {

    @Configuration
    @EnableBatchProcessing
    class ReaderTestConfig {

        @Bean
        fun postsReader(@Value("classpath:test/Posts.xml") file: Resource): ItemStreamReader<MockPost> {
            return BatchStepConfig.itemReader(file, "posts", BatchStepConfig.itemMarshaller(MockPost::class.java))
        }

        @Bean
        fun commentsReader(@Value("classpath:test/Comments.xml") file: Resource): ItemStreamReader<MockComment> {
            return BatchStepConfig.itemReader(file, "comments", BatchStepConfig.itemMarshaller(MockComment::class.java))
        }

        @Bean
        fun votesReader(@Value("classpath:test/Votes.xml") votesFile: Resource): ItemStreamReader<MockVote> {
            return BatchStepConfig.itemReader(votesFile, "votes", BatchStepConfig.itemMarshaller(MockVote::class.java))
        }
    }

    @Autowired
    private lateinit var postsReader: ItemStreamReader<MockPost>

    @Test
    fun postsReaderTests() {
        val items = read(postsReader)

        assertEquals(37, items.size)

        with(items[0]) {
            assertEquals(1, id)
            assertEquals(MockPostType.QUESTION, postType)
            assertEquals(13, acceptedAnswerId)
            assertNull(parentId)
            assertEquals(LocalDateTime.of(2010, 9, 1, 19, 34, 48), creationDate)
            assertEquals(100, score)
            assertEquals(59408, viewCount)
            assertEquals(
                "<p>A coworker of mine believes that <em>any</em> use of in-code comments (ie, not javadoc style method or class comments) is a <a href=\"http://en.wikipedia.org/wiki/Code_smell\">code smell</a>.  What do you think?</p>\n",
                body
            )
            assertNull(ownerDisplayName)
            assertEquals(6, ownerUserId)
            assertEquals(226, lastEditorUserId)
            assertEquals(LocalDateTime.of(2011, 11, 25, 22, 32, 41, 300_000_000), lastEditDate)
            assertEquals(LocalDateTime.of(2012, 11, 27, 19, 29, 27, 740_000_000), lastActivityDate)
            assertEquals("\"Comments are a code smell\"", title)
            assertEquals("<comments><anti-patterns>", allTags)
            assertEquals(34, answerCount)
            assertEquals(76, favoriteCount)
            assertEquals(LocalDateTime.of(2012, 11, 27, 20, 11, 51, 580_000_000), closedDate)
            assertEquals(LocalDateTime.of(2011, 1, 31, 9, 4, 54, 130_000_000), communityOwnedDate)
            assertEquals("CC BY-SA 2.5", contentLicense)
        }

        with(items[22]) {
            assertEquals(8823, id)
            assertEquals(MockPostType.ANSWER, postType)
            assertNull(acceptedAnswerId)
            assertEquals(1, parentId)
            assertEquals(LocalDateTime.of(2010, 10, 1, 16, 46, 34, 193_000_000), creationDate)
            assertEquals(0, score)
            assertNull(viewCount)
            assertEquals(
                "<p>Code comments giving, where applicable, units of function arguments and returns, structure fields, even local variables can be very handy. Remember the Mars Orbiter!</p>\n",
                body
            )
            assertEquals("dmuir", ownerDisplayName)
            assertNull(ownerUserId)
            assertNull(lastEditorUserId)
            assertNull(lastEditDate)
            assertEquals(LocalDateTime.of(2010, 10, 1, 16, 46, 34, 193_000_000), lastActivityDate)
            assertNull(title)
            assertNull(allTags)
            assertNull(answerCount)
            assertNull(favoriteCount)
            assertNull(closedDate)
            assertEquals(LocalDateTime.of(2011, 1, 31, 9, 4, 54, 130_000_000), communityOwnedDate)
            assertEquals("CC BY-SA 2.5", contentLicense)
        }
    }

    @Autowired
    private lateinit var commentsReader: ItemStreamReader<MockComment>



    @Test
    fun commentsReaderTest() {
        val items = read(commentsReader)

        assertEquals(106, items.size)

        with(items[0]) {
            assertEquals(3, id)
            assertEquals(3, postId)
            assertEquals(2, score)
            assertEquals("Good naming convention and well structured code will help you decrease the comments need. Don`t forget that each line of comments you add it's a new line to maintain!!", text)
            assertEquals(LocalDateTime.of(2010, 9, 1, 19, 47, 32, 873_000_000), creationDate)
            assertEquals(28, userId)
            assertEquals("CC BY-SA 2.5", contentLicense)
        }
    }

    @Autowired
    private lateinit var votesReader: ItemStreamReader<MockVote>

    @Test
    fun votesReaderTest() {
        val items = read(votesReader)

        with(items[0]) {
            assertEquals(69, id)
            assertEquals(1, postId)
            assertEquals(2, voteTypeId)
            assertEquals(LocalDateTime.of(2010, 9, 1, 0, 0), creationDate)
        }

        with(items[1]) {
            assertEquals(190, id)
            assertEquals(1, postId)
            assertEquals(5, voteTypeId)
            assertEquals(11, userId)
            assertEquals(LocalDateTime.of(2010, 9, 1, 0, 0), creationDate)
        }
    }

    private fun <T> read(reader: ItemStreamReader<T>): List<T> {
        val items = ArrayList<T>()
        val ctx = ExecutionContext()
        reader.open(ctx)
        var item = reader.read()
        while (item != null) {
            items.add(item)
            item = reader.read()
        }
        commentsReader.close()
        return items
    }
}
