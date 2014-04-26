package thecarlhall.hazelcast.web;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class HazelcastServlet extends HttpServlet {

  private String[] servers = null;
  private HazelcastInstance client = null;

  @Override
  public void init(ServletConfig config) throws ServletException {
    String serverList = config.getInitParameter("servers");

    if (serverList != null) {
      servers = serverList.split(",");

      ClientConfig cfg = new ClientConfig();
      client = HazelcastClient.newHazelcastClient(cfg);
    } else {
      Config cfg = new Config();
      client = Hazelcast.newHazelcastInstance(cfg);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    PrintWriter writer = resp.getWriter();
    HttpSession session = req.getSession();

    int sessionCount = 0;
    Enumeration<String> attrNames = session.getAttributeNames();
    while (attrNames.hasMoreElements()) {
      String attrName = attrNames.nextElement();
      Object attrValue = session.getAttribute(attrName);
      writer.println(String.format("%s = %s", attrName, attrValue));
      sessionCount++;
    }

    Map<Object, Object> entries = client.getMap("test");
    for (Map.Entry<Object, Object> entry : entries.entrySet()) {
      writer.println(String.format("%s = %s", entry.getKey(), entry.getValue()));
    }

    writer.println(String.format("Total (session): %d", sessionCount));
    writer.println(String.format("Total (cashe): %d", entries.size()));
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String type = req.getParameter("type");
    String name = req.getParameter("name");
    String value = req.getParameter("value");

    if ("session".equals(type)) {
      req.getSession().setAttribute(name, value);

    } else {
      Map<String, String> testMap = client.getMap("test");
      testMap.put(name, value);
    }
  }
}
