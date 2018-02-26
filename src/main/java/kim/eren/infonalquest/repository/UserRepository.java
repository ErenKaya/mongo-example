package kim.eren.infonalquest.repository;

import kim.eren.infonalquest.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User,String>{
}
