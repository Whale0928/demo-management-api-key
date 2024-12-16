package app.demo.management.key.client;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {

    @Query("SELECT c FROM Client c WHERE c.email = :email")
    Optional<Client> findByEmail(String email);

    @Query("SELECT c FROM Client c WHERE c.apiKey =:apiKey")
    Optional<Client> findByApiKey(String apiKey);
}
