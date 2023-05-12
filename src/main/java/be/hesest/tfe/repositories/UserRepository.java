package be.hesest.tfe.repositories;

import be.hesest.tfe.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByEmailAndPassword(String email, String password);

}