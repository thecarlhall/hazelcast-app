package thecarlhall.hazelcast.web;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HazelcastServlet extends HttpServlet {

  private HazelcastInstance client = null;

  @Override
  public void init() throws ServletException {
    client = Hazelcast.newHazelcastInstance();
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    HttpSession session = req.getSession();

    writer.println("Cache contents");
    Map<Object, Object> entries = client.getMap("test");
    for (Map.Entry<Object, Object> entry : entries.entrySet()) {
      writer.println(String.format("%s = %s", entry.getKey(), entry.getValue()));
    }

    writer.println(String.format("Total (cache): %d", entries.size()));
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String name = req.getParameter("name");
    String value = req.getParameter("value");

    Map<String, String> testMap = client.getMap("test");
    testMap.put(name, value);
  }
}
