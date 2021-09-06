package me.googas.io.mocks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import me.googas.adapters.geometry.PointAdapter;
import me.googas.starbox.math.geometry.Point;
import me.googas.starbox.math.geometry.Shape;
import me.googas.starbox.math.geometry.SinglePoint;

public class AdapterTest {

  public static void main(String[] args) {
    Gson gson =
        new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Point.class, new PointAdapter())
            .create();
    List<Shape> shapes = new ArrayList<>();
    for (int x = 0; x < 10; x++) {
      for (int y = 10; y < 20; y++) {
        for (int z = 0; z < 10; z++) {
          shapes.add(new SinglePoint(null, new Point(x, y, z)));
        }
      }
    }
    System.out.println(gson.toJson(shapes));
  }
}
