package ch.zhaw.textanalysis.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class DemoApplicationTests {

    @MockBean
    private TextClassificationController textClassificationController;

    @Test
    void contextLoads() {
        // Test to ensure the application context loads successfully
    }
}
