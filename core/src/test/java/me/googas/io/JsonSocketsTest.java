package me.googas.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import lombok.NonNull;
import me.googas.io.mocks.Person;
import me.googas.net.api.exception.MessengerListenFailException;
import me.googas.net.api.messages.Message;
import me.googas.net.api.messages.Request;
import me.googas.net.api.messages.RequestBuilder;
import me.googas.net.sockets.json.ParamName;
import me.googas.net.sockets.json.Receptor;
import me.googas.net.sockets.json.adapters.MessageDeserializer;
import me.googas.net.sockets.json.client.JsonClient;
import me.googas.net.sockets.json.reflect.ReflectJsonReceptor;
import me.googas.net.sockets.json.server.JsonClientThread;
import me.googas.net.sockets.json.server.JsonSocketServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonSocketsTest {

  @NonNull private static final Logger logger = LoggerFactory.getLogger(StarboxFileTest.class);
  @NonNull private static TestingMocks mocks = new TestingMocks();
  private static JsonSocketServer server;
  private static JsonClient client;

  @BeforeAll
  static void prepareMocks()
      throws IOException, InterruptedException, MessengerListenFailException {
    int port = 3000;
    Gson gson =
        new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Message.class, new MessageDeserializer())
            .create();
    long timeout = 1000;
    JsonSocketsTest.mocks =
        TestingFiles.Contexts.JSON
            .read(TestingFiles.Resources.MOCKS, TestingMocks.class)
            .handle(
                (e) -> {
                  JsonSocketsTest.logger.error(e, () -> "Could not load mocking resources");
                })
            .provide()
            .orElseThrow(() -> new NullPointerException("Mocks could not be loaded"));
    JsonSocketsTest.server =
        new JsonSocketServer(
            3000,
            e -> JsonSocketsTest.logger.error(e, () -> "Caught error in server"),
            null,
            gson,
            timeout);
    JsonSocketsTest.client =
        new JsonClient(
            new Socket("localhost", port),
            e -> JsonSocketsTest.logger.error(e, () -> "Caught error in client"),
            gson,
            timeout);
    JsonSocketsTest.server
        .getReceptors()
        .addAll(ReflectJsonReceptor.getReceptors(new TestingReceptors()));
    JsonSocketsTest.client.addReceptors(new TestingReceptors());
    JsonSocketsTest.server.start();
    JsonSocketsTest.client.start();
    // We should wait a little until the client has been fully connected
    long wait = 0;
    while (JsonSocketsTest.server.getClients().isEmpty()) {
      Thread.sleep(1);
      wait++;
      if (wait > timeout) throw new MessengerListenFailException("Messenger failed to connect");
    }
  }

  @AfterAll
  static void close() throws IOException {
    JsonSocketsTest.client.close();
    JsonSocketsTest.server.close();
    Assertions.assertTrue(JsonSocketsTest.client.isClosed());
  }

  @Test
  @Order(0)
  void clientRequests() throws MessengerListenFailException {
    int id = 0;
    NullPointerException exception = new NullPointerException("Did not return the existing person");
    RequestBuilder<Person> builder = Request.builder(Person.class, "person").put("id", id);
    // Sync request
    Person person = builder.send(JsonSocketsTest.client).orElseThrow(() -> exception);
    Assertions.assertEquals(id, person.getId());
    // Async Request
    JsonSocketsTest.client.sendRequest(
        builder.build(),
        optional -> {
          Person asyncPerson = optional.orElseThrow(() -> exception);
          Assertions.assertEquals(id, person.getId());
        });
    // Async Request with a failed result: incorrect type of parameter
    JsonSocketsTest.client.sendRequest(
        builder.put("id", "foo").build(),
        optional -> {
          Person asyncPerson = optional.orElseThrow(() -> exception);
          Assertions.assertEquals(id, person.getId());
        },
        expected -> {
          JsonSocketsTest.logger.info(expected, () -> "Expected exception");
        });
  }

  @Test
  @Order(1)
  void serverRequests() {
    // Sync request
    Map<JsonClientThread, Optional<Integer>> pings =
        JsonSocketsTest.server.sendRequest(
            Request.builder(int.class, "ping").put("init", System.currentTimeMillis()).build());
    Assertions.assertEquals(1, pings.size());
    pings.forEach(
        (client, optionalPing) -> {
          optionalPing.ifPresent(
              ping -> {
                JsonSocketsTest.logger.info(
                    () -> String.format("Sync: Ping from server to %s is: %dms", client, ping));
              });
        });
    // Async request
    JsonSocketsTest.server.sendRequest(
        Request.builder(int.class, "ping").put("init", System.currentTimeMillis()).build(),
        (client, optionalPing) -> {
          optionalPing.ifPresent(
              ping -> {
                JsonSocketsTest.logger.info(
                    () -> String.format("Async: Ping from server to %s is: %dms", client, ping));
              });
        });
  }

  public static class TestingReceptors {

    @Receptor("person")
    public Person person(@ParamName("id") int id) {
      return JsonSocketsTest.mocks.getPerson(id).orElse(null);
    }

    @Receptor("ping")
    public int ping(@ParamName("init") long init) {
      return (int) (System.currentTimeMillis() - init);
    }
  }
}
