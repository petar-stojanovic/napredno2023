package src.labs.lab2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

enum TYPE {
    POINT,
    CIRCLE
}

enum DIRECTION {
    UP,
    DOWN,
    LEFT,
    RIGHT
}

class ObjectCanNotBeMovedException extends Exception {
    public ObjectCanNotBeMovedException(int x, int y) {
        super(String.format("Point (%d,%d) is out of bounds", x, y));
    }
}

class MovableObjectNotFittableException extends Exception {

    public MovableObjectNotFittableException(String message) {
        super(message);
    }
}

interface Movable {
    void moveUp() throws ObjectCanNotBeMovedException;

    void moveDown() throws ObjectCanNotBeMovedException;

    void moveLeft() throws ObjectCanNotBeMovedException;

    void moveRight() throws ObjectCanNotBeMovedException;

    int getCurrentXPosition();

    int getCurrentYPosition();

    TYPE getType();

    String getExceptionMessage();

    boolean canBeFitted(int xMax, int yMax);
}

class MovingPoint implements Movable {

    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;

    public MovingPoint(int x, int y, int xSpeed, int ySpeed) {
        this.x = x;
        this.y = y;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getxSpeed() {
        return xSpeed;
    }

    public void setxSpeed(int xSpeed) {
        this.xSpeed = xSpeed;
    }

    public int getySpeed() {
        return ySpeed;
    }

    public void setySpeed(int ySpeed) {
        this.ySpeed = ySpeed;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        if (y + getySpeed() > MovablesCollection.Y_MAX) {
            throw new ObjectCanNotBeMovedException(x, y + getySpeed());
        }
        setY(getY() + getySpeed());
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        if (y - getySpeed() < 0) {
            throw new ObjectCanNotBeMovedException(x, y - getySpeed());
        }

        setY(getY() - getySpeed());
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        if (x - getxSpeed() < 0) {
            throw new ObjectCanNotBeMovedException(x - getxSpeed(), y);
        }
        setX(getX() - getxSpeed());
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        if (x + getxSpeed() > MovablesCollection.X_MAX) {
            throw new ObjectCanNotBeMovedException(x + getxSpeed(), y);
        }

        setX(getX() + getxSpeed());
    }

    @Override
    public int getCurrentXPosition() {
        return getX();
    }

    @Override
    public int getCurrentYPosition() {
        return getY();
    }

    @Override
    public TYPE getType() {
        return TYPE.POINT;
    }

    @Override
    public String getExceptionMessage() {
        return String.format("Point (%d,%d) is out of bounds", x, y);
    }

    @Override
    public boolean canBeFitted(int xMax, int yMax) {
        return x >= 0 && x < xMax && y >= 0 && y < yMax;
    }

    @Override
    public String toString() {
        return String.format("Movable point with coordinates (%d,%d)", x, y);
    }
}

class MovingCircle implements Movable {
    private int radius;
    private MovingPoint center;

    public MovingCircle(int radius, MovingPoint center) {
        this.radius = radius;
        this.center = center;
    }

    @Override
    public void moveUp() throws ObjectCanNotBeMovedException {
        this.center.moveUp();
    }

    @Override
    public void moveDown() throws ObjectCanNotBeMovedException {
        this.center.moveDown();
    }

    @Override
    public void moveLeft() throws ObjectCanNotBeMovedException {
        this.center.moveLeft();
    }

    @Override
    public void moveRight() throws ObjectCanNotBeMovedException {
        this.center.moveRight();

    }

    @Override
    public int getCurrentXPosition() {
        return 0;
    }

    @Override
    public int getCurrentYPosition() {
        return 0;
    }

    @Override
    public TYPE getType() {
        return TYPE.CIRCLE;
    }

    @Override
    public String getExceptionMessage() {
        return String.format("Movable circle with center (%d,%d) and radius %d can not be fitted into the collection", center.getX(),
                          center.getY(), radius);
    }

    @Override
    public boolean canBeFitted(int xMax, int yMax) {
        return this.center.getX() - radius >= 0
                          && this.center.getX() - radius <= xMax
                          && this.center.getY() - radius >= 0
                          && this.center.getY() - radius <= yMax;
    }

    @Override
    public String toString() {
        return "Movable circle with center coordinates (" + center.getX() + "," + center.getY() + ") and radius " + radius;
    }
}

class MovablesCollection {
    private List<Movable> movableList;
    static int X_MAX;
    static int Y_MAX;

    public MovablesCollection(int x_MAX, int y_MAX) {
        movableList = new ArrayList<>();
        X_MAX = x_MAX;
        Y_MAX = y_MAX;
    }

    public static void setxMax(int xMax) {
        MovablesCollection.X_MAX = xMax;
    }

    public static void setyMax(int yMax) {
        MovablesCollection.Y_MAX = yMax;
    }

    void addMovableObject(Movable m) throws MovableObjectNotFittableException {
        int x = m.getCurrentXPosition();
        int y = m.getCurrentYPosition();

        if (!m.canBeFitted(X_MAX, Y_MAX)) {
            throw new MovableObjectNotFittableException(m.getExceptionMessage());
        }

        this.movableList.add(m);
    }

    void moveObjectsFromTypeWithDirection(TYPE type, DIRECTION direction) {
        movableList.stream()
                          .filter(movable -> movable.getType().equals(type))
                          .forEach(movable -> {
                              try {
                                  switch (direction) {
                                      case UP: {
                                          movable.moveUp();
                                          break;
                                      }
                                      case DOWN: {
                                          movable.moveDown();
                                          break;
                                      }
                                      case LEFT: {
                                          movable.moveLeft();
                                          break;
                                      }
                                      case RIGHT: {
                                          movable.moveRight();
                                          break;
                                      }
                                  }
                              } catch (ObjectCanNotBeMovedException e) {
                                  System.out.println(e.getMessage());
                              }
                          });
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collection of movable objects with size ").append(movableList.size()).append(":\n");
        movableList.forEach(it -> sb.append(it.toString()).append("\n"));

        return sb.toString();
    }
}

public class CirclesTest {

    public static void main(String[] args) {

        System.out.println("===COLLECTION CONSTRUCTOR AND ADD METHOD TEST===");
        MovablesCollection collection = new MovablesCollection(100, 100);
        Scanner sc = new Scanner(System.in);
        int samples = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < samples; i++) {
            String inputLine = sc.nextLine();
            String[] parts = inputLine.split(" ");

            int x = Integer.parseInt(parts[1]);
            int y = Integer.parseInt(parts[2]);
            int xSpeed = Integer.parseInt(parts[3]);
            int ySpeed = Integer.parseInt(parts[4]);

            if (Integer.parseInt(parts[0]) == 0) { //point
                try {
                    collection.addMovableObject(new MovingPoint(x, y, xSpeed, ySpeed));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            } else { //circle
                int radius = Integer.parseInt(parts[5]);
                try {
                    collection.addMovableObject(new MovingCircle(radius, new MovingPoint(x, y, xSpeed, ySpeed)));
                } catch (MovableObjectNotFittableException e) {
                    System.out.println(e.getMessage());
                }
            }

        }
        System.out.println(collection.toString());

        System.out.println("MOVE POINTS TO THE LEFT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.LEFT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES DOWN");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.DOWN);
        System.out.println(collection.toString());

        System.out.println("CHANGE X_MAX AND Y_MAX");
        MovablesCollection.setxMax(90);
        MovablesCollection.setyMax(90);

        System.out.println("MOVE POINTS TO THE RIGHT");
        collection.moveObjectsFromTypeWithDirection(TYPE.POINT, DIRECTION.RIGHT);
        System.out.println(collection.toString());

        System.out.println("MOVE CIRCLES UP");
        collection.moveObjectsFromTypeWithDirection(TYPE.CIRCLE, DIRECTION.UP);
        System.out.println(collection.toString());

    }

}
