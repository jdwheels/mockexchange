package com.trivialepic.mockexchange.posts

import com.trivialepic.mockexchange.objects.users.MockUser
import org.springframework.data.repository.reactive.ReactiveCrudRepository


interface UserRepository : ReactiveCrudRepository<MockUser, Long> {

}
