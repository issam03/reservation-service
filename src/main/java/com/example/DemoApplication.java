package com.example;

import java.util.Collection;
import java.util.stream.Stream;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableDiscoveryClient
@SpringBootApplication
public class DemoApplication
{
	@Bean
	CommandLineRunner CommandLineRunner(ReservationRepository reservationRepository) {
		return strings -> {
			Stream.of("Issam", "bazze", "Toto", "Alex", "Eric")
				.forEach(s -> reservationRepository.save(new Reservation(s)));
		};
	}
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}
}

@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long>
{
	@RestResource(path = "by-name")
	Collection<Reservation> findByReservationName(@Param("rn") String rn);
}

@RefreshScope
@RestController
class MessageRestController
{
	@Value("${message}")
	private String msg;

	@RequestMapping("/message")
	String message() {
		return this.msg;
	}
}

@Entity(name = "reservation")
class Reservation
{
	@Id @GeneratedValue
	private long id;
	private String reservationName;

	public Reservation() {

	}

	public Reservation(String reservationName)
	{
		this.reservationName = reservationName;
	}

	public long getId()
	{
		return id;
	}

	public String getReservationName()
	{
		return reservationName;
	}

	@Override
	public String toString()
	{
		return "Reservation{" + "id=" + id + ", reservationName='"
			+ reservationName + '\'' + '}';
	}
}
