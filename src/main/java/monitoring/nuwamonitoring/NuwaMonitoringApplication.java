package monitoring.nuwamonitoring;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class NuwaMonitoringApplication {

    public static void main(String[] args) {
        SpringApplication.run(NuwaMonitoringApplication.class, args);
    }
}
