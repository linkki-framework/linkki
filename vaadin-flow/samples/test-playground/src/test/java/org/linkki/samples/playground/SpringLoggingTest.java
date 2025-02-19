package org.linkki.samples.playground;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SpringLoggingTest {

    private static final Logger logger = LoggerFactory.getLogger(SpringLoggingTest.class);

    @Test
    void testLoggingToConsole() {

        final StringBuilder consoleOutput = new StringBuilder();
        System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
            @Override
            public void write(int b) {
                consoleOutput.append((char)b);
            }
        }));

        logger.info("This is a {} message", "test");

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertThat(consoleOutput.toString()).contains("This is a test message");
    }

}
